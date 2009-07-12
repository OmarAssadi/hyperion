package com.grahamedgecombe.rs2.model;

import java.util.Deque;
import java.util.LinkedList;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.util.DirectionUtils;

/**
 * <p>A <code>WalkingQueue</code> stores steps the client needs to walk and
 * allows this queue of steps to be modified.</p>
 * 
 * <p>The class will also process these steps when
 * {@link #processNextPlayerMovement()} is called. This should be called once
 * per server cycle.</p>
 * @author Graham
 *
 */
public class WalkingQueue {
	
	/**
	 * Represents a single point in the queue.
	 * @author Graham
	 *
	 */
	private static class Point {
		/**
		 * The x-coordinate.
		 */
		private int x = 0;
		
		/**
		 * The y-coordinate.
		 */
		private int y = 0;
		
		/**
		 * The direction to walk to this point.
		 */
		private int dir = -1;
	}
	
	/**
	 * The maximum size of the queue. If there are more points than this size,
	 * they are discarded.
	 */
	public static final int MAXIMUM_SIZE = 50;
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * The queue of waypoints.
	 */
	private Deque<Point> waypoints = new LinkedList<Point>();
	
	/**
	 * Creates the <code>WalkingQueue</code> for the specified
	 * <code>Player</code>.
	 * @param player The player whose walking queue this is. 
	 */
	public WalkingQueue(Player player) {
		this.player = player;
	}
	
	/**
	 * Resets the walking queue so it contains no more steps.
	 */
	public void reset() {
		waypoints.clear();
		Point p = new Point();
		p.x = player.getLocation().getLocalX();
		p.y = player.getLocation().getLocalY();
		waypoints.add(p);
	}
	
	/**
	 * Adds a single step to the walking queue, filling in the points to the
	 * previous point in the queue if necessary.
	 * @param x The local x coordinate.
	 * @param y The local y coordinate.
	 */
	public void addStep(int x, int y) {
		/*
		 * The RuneScape client will not send all the points in the queue.
		 * It just sends places where the direction changes.
		 * 
		 * For instance, walking from a route like this:
		 * 
		 * <code>
		 * *****
		 *     *
		 *     *
		 *     *****
		 * </code>
		 * 
		 * Only the places marked with X will be sent:
		 * 
		 * <code>
		 * X***X
		 *     *
		 *     *
		 *     X***X
		 * </code>
		 * 
		 * This code will 'fill in' these points and then add them to the
		 * queue.
		 */
		
		/*
		 * We need to know the previous point to fill in the path.
		 */
		if(waypoints.size() == 0) {
			/*
			 * There is no last point, reset the queue to add the player's
			 * current location.
			 */
			reset();
		}
		
		/*
		 * We retrieve the previous point here.
		 */
		Point last = waypoints.peekLast();
		
		/*
		 * We now work out the difference between the points.
		 */
		int diffX = x - last.x;
		int diffY = y - last.y;
		/*
		 * And calculate the number of steps there is between the points.
		 */
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for(int i = 0; i < max; i++) {
			/*
			 * Keep lowering the differences until they reach 0 - when our
			 * route will be complete.
			 */
			if(diffX < 0) {
				diffX++;
			} else if(diffX > 0) {
				diffX--;
			}
			if(diffY < 0) {
				diffY++;
			} else if(diffY > 0) {
				diffY--;
			}
			
			/*
			 * Add this next step to the queue.
			 */
			addStepInternal(x - diffX, y - diffY);
		}
	}

	/**
	 * Adds a single step to the queue internally without counting gaps.
	 * This method is unsafe if used incorrectly so it is private to protect
	 * the queue.
	 * @param x The x coordinate of the step.
	 * @param y The y coordinate of the step.
	 */
	private void addStepInternal(int x, int y) {
		/*
		 * Check if we are going to violate capacity restrictions.
		 */
		if(waypoints.size() >= MAXIMUM_SIZE) {
			/*
			 * If we are we'll just skip the point. The player won't get a
			 * complete route by large routes are not probable and are more
			 * likely sent by bots to crash servers.
			 */
			return;
		}
		
		/*
		 * We retrieve the previous point (this is to calculate the direction
		 * to move in).
		 */
		Point last = waypoints.peekLast();
		
		/*
		 * Now we work out the difference between these steps.
		 */
		int diffX = x - last.x;
		int diffY = y - last.y;
		
		/*
		 * And calculate the direction between them.
		 */
		int dir = DirectionUtils.direction(diffX, diffY);
		
		/*
		 * We now have the information to add a point to the queue! We create
		 * the actual point object and add it.
		 */
		Point p = new Point();
		p.x = x;
		p.y = y;
		p.dir = dir;
		waypoints.add(p);
	}
	
	/**
	 * Processes the next player's movement.
	 */
	public void processNextPlayerMovement() {
		/*
		 * Store our current location for checking for a region change
		 * later.
		 */
		Location currentLocation = player.getLocation();
		
		/*
		 * Store the teleporting flag.
		 */
		boolean teleporting = player.hasTeleportTarget();
		
		/*
		 * The points which we are walking to.
		 */
		Point walkPoint = null, runPoint = null;
		
		/*
		 * Checks if the player is teleporting i.e. not walking.
		 */
		if(teleporting) {
			/*
			 * Reset the walking queue as it will no longer apply after the
			 * teleport.
			 */
			reset();
			
			/*
			 * Set the 'teleporting' flag which indicates the player is
			 * teleporting.
			 */
			player.setTeleporting(true);
			
			/*
			 * Sets the player's new location to be their target.
			 */
			player.setLocation(player.getTeleportTarget());
			
			/*
			 * Resets the teleport target.
			 */
			player.resetTeleportTarget();
		} else {
			/*
			 * If the player isn't teleporting, they are walking (or standing
			 * still). We get the next direction of movement here.
			 */
			walkPoint = getNextPoint();
			
			/*
			 * Technically we should check for running here.
			 */
			if(false) {
				runPoint = getNextPoint();
			}
			
			/*
			 * Now set the sprites.
			 */
			int walkDir = walkPoint == null ? -1 : walkPoint.dir;
			int runDir = runPoint == null ? -1 : runPoint.dir;
			player.getSprites().setSprites(walkDir, runDir);
		}
		
		/*
		 * Check for a map region change, and if the map region has
		 * changed, set the appropriate flag so the new map region packet
		 * is sent.
		 */
		int regionDiffX = currentLocation.getRegionX() - player.getLastKnownRegion().getRegionX();
		int regionDiffY = currentLocation.getRegionY() - player.getLastKnownRegion().getRegionY();
		if(regionDiffX <= -4 || regionDiffY <= -4 || regionDiffX >= 4 || regionDiffY >= 4) {
			/*
			 * Set the map region changing flag so the new map region packet is
			 * sent upon update.
			 */
			player.setMapRegionChanging(true);
			
			/*
			 * If we were not already teleporting we need to put some points back onto the queue.
			 */
			if(!teleporting) {
				/*
				 * We are no longer walking throughout the map region change.
				 */
				player.getSprites().setSprites(-1, -1);
				
				/*
				 * We should switch our location back.
				 */
				player.setLocation(currentLocation);
				
				/*
				 * We should add the running and walking points back in reverse
				 * order if necessary.
				 */
				if(runPoint != null) {
					waypoints.addFirst(runPoint);
				}
				if(walkPoint != null) {
					waypoints.addFirst(walkPoint);
				}
			}
		}
		
	}
	
	/**
	 * Gets the next point of movement.
	 * @return The next point.
	 */
	private Point getNextPoint() {
		/*
		 * Take the next point from the queue.
		 */
		Point p = waypoints.poll();
		/*
		 * Checks if there are no more points.
		 */
		if(p == null) {
			/*
			 * Return <code>null</code> indicating no movement happened.
			 */
			return null;
		} else {
			/*
			 * Set the player's new location.
			 */
			int diffX = Constants.DIRECTION_DELTA_X[p.dir];
			int diffY = Constants.DIRECTION_DELTA_Y[p.dir];
			player.setLocation(player.getLocation().transform(diffX, diffY, 0));
			/*
			 * And return the direction.
			 */
			return p;
		}
	}

}
