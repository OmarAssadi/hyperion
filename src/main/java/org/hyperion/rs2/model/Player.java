package org.hyperion.rs2.model;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hyperion.data.Persistable;
import org.hyperion.rs2.action.ActionQueue;
import org.hyperion.rs2.action.impl.AttackAction;
import org.hyperion.rs2.event.impl.DeathEvent;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.ISAACCipher;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.rs2.util.NameUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a player-controller character.
 *
 * @author Graham Edgecombe
 */
public class Player extends Entity implements Persistable {

    /**
     * The <code>IoSession</code>.
     */
    private final IoSession session;

    /*
     * Attributes specific to our session.
     */
    /**
     * The ISAAC cipher for incoming data.
     */
    private final ISAACCipher inCipher;
    /**
     * The ISAAC cipher for outgoing data.
     */
    private final ISAACCipher outCipher;
    /**
     * The action sender.
     */
    private final ActionSender actionSender = new ActionSender(this);
    /**
     * A queue of pending chat messages.
     */
    private final Queue<ChatMessage> chatMessages = new LinkedList<>();
    /**
     * A queue of actions.
     */
    private final ActionQueue actionQueue = new ActionQueue();
    /**
     * The interface state.
     */
    private final InterfaceState interfaceState = new InterfaceState(this);
    /**
     * A queue of packets that are pending.
     */
    private final Queue<Packet> pendingPackets = new LinkedList<>();
    /**
     * The request manager which manages trading and duelling requests.
     */
    private final RequestManager requestManager = new RequestManager(this);
    /**
     * The UID, i.e. number in <code>random.dat</code>.
     */
    private final int uid;
    /**
     * The player's appearance information.
     */
    private final Appearance appearance = new Appearance();
    /**
     * The player's equipment.
     */
    private final Container equipment = new Container(Container.Type.STANDARD, Equipment.SIZE);

    /*
     * Core login details.
     */
    /**
     * The player's skill levels.
     */
    private final Skills skills = new Skills(this);
    /**
     * The player's inventory.
     */
    private final Container inventory = new Container(Container.Type.STANDARD, Inventory.SIZE);
    /**
     * The player's bank.
     */
    private final Container bank = new Container(Container.Type.ALWAYS_STACK, Bank.SIZE);
    /**
     * The player's settings.
     */
    private final Settings settings = new Settings();
    /**
     * The current chat message.
     */
    private ChatMessage currentChatMessage;
    /**
     * Active flag: if the player is not active certain changes (e.g. items)
     * should not send packets as that indicates the player is still loading.
     */
    private boolean active = false;

    /*
     * Attributes.
     */
    /**
     * The name.
     */
    private String name;
    /**
     * The name expressed as a long.
     */
    private long nameLong;
    /**
     * The password.
     */
    private String password;
    /**
     * The rights level.
     */
    private Rights rights = Rights.PLAYER;
    /**
     * The members flag.
     */
    private boolean members = true;
    /**
     * The cached update block.
     */
    private Packet cachedUpdateBlock;

    /*
     * Cached details.
     */

    /**
     * Creates a player based on the details object.
     *
     * @param details The details object.
     */
    public Player(final PlayerDetails details) {
        super();
        this.session = details.getSession();
        this.inCipher = details.getInCipher();
        this.outCipher = details.getOutCipher();
        this.name = details.getName();
        this.nameLong = NameUtils.nameToLong(this.name);
        this.password = details.getPassword();
        this.uid = details.getUID();
        this.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        this.setTeleporting(true);
    }

    /**
     * Gets the request manager.
     *
     * @return The request manager.
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Gets the player's name expressed as a long.
     *
     * @return The player's name expressed as a long.
     */
    public long getNameAsLong() {
        return nameLong;
    }

    /**
     * Gets the player's settings.
     *
     * @return The player's settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Writes a packet to the <code>IoSession</code>. If the player is not
     * yet active, the packets are queued.
     *
     * @param packet The packet.
     */
    public void write(final Packet packet) {
        synchronized (this) {
            if (active) {
                for (final Packet pendingPacket : pendingPackets) {
                    session.write(pendingPacket);
                }
                pendingPackets.clear();
                session.write(packet);
            } else {
                pendingPackets.add(packet);
            }
        }
    }

    /**
     * Gets the player's bank.
     *
     * @return The player's bank.
     */
    public Container getBank() {
        return bank;
    }

    /**
     * Gets the interface state.
     *
     * @return The interface state.
     */
    public InterfaceState getInterfaceState() {
        return interfaceState;
    }

    /**
     * Checks if there is a cached update block for this cycle.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean hasCachedUpdateBlock() {
        return cachedUpdateBlock != null;
    }

    /**
     * Gets the cached update block.
     *
     * @return The cached update block.
     */
    public Packet getCachedUpdateBlock() {
        return cachedUpdateBlock;
    }

    /**
     * Sets the cached update block for this cycle.
     *
     * @param cachedUpdateBlock The cached update block.
     */
    public void setCachedUpdateBlock(final Packet cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    /**
     * Resets the cached update block.
     */
    public void resetCachedUpdateBlock() {
        cachedUpdateBlock = null;
    }

    /**
     * Gets the current chat message.
     *
     * @return The current chat message.
     */
    public ChatMessage getCurrentChatMessage() {
        return currentChatMessage;
    }

    /**
     * Sets the current chat message.
     *
     * @param currentChatMessage The current chat message to set.
     */
    public void setCurrentChatMessage(final ChatMessage currentChatMessage) {
        this.currentChatMessage = currentChatMessage;
    }

    /**
     * Gets the queue of pending chat messages.
     *
     * @return The queue of pending chat messages.
     */
    public Queue<ChatMessage> getChatMessageQueue() {
        return chatMessages;
    }

    /**
     * Gets the player's appearance.
     *
     * @return The player's appearance.
     */
    public Appearance getAppearance() {
        return appearance;
    }

    /**
     * Gets the player's equipment.
     *
     * @return The player's equipment.
     */
    public Container getEquipment() {
        return equipment;
    }

    /**
     * Gets the player's skills.
     *
     * @return The player's skills.
     */
    public Skills getSkills() {
        return skills;
    }

    /**
     * Gets the action sender.
     *
     * @return The action sender.
     */
    public ActionSender getActionSender() {
        return actionSender;
    }

    /**
     * Gets the incoming ISAAC cipher.
     *
     * @return The incoming ISAAC cipher.
     */
    public ISAACCipher getInCipher() {
        return inCipher;
    }

    /**
     * Gets the outgoing ISAAC cipher.
     *
     * @return The outgoing ISAAC cipher.
     */
    public ISAACCipher getOutCipher() {
        return outCipher;
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's password.
     *
     * @return The player's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the player's password.
     *
     * @param pass The password.
     */
    public void setPassword(final String pass) {
        this.password = pass;
    }

    /**
     * Gets the player's UID.
     *
     * @return The player's UID.
     */
    public int getUID() {
        return uid;
    }

    /**
     * Gets the <code>IoSession</code>.
     *
     * @return The player's <code>IoSession</code>.
     */
    public IoSession getSession() {
        return session;
    }

    /**
     * Gets the rights.
     *
     * @return The player's rights.
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * Sets the rights.
     *
     * @param rights The rights level to set.
     */
    public void setRights(final Rights rights) {
        this.rights = rights;
    }

    /**
     * Checks if this player has a member's account.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isMembers() {
        return members;
    }

    /**
     * Sets the members flag.
     *
     * @param members The members flag.
     */
    public void setMembers(final boolean members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return Player.class.getName() + " [name=" + name + " rights=" + rights + " members=" + members + " index=" + this.getIndex() + "]";
    }

    /**
     * Gets the active flag.
     *
     * @return The active flag.
     */
    public boolean isActive() {
        synchronized (this) {
            return active;
        }
    }

    /**
     * Sets the active flag.
     *
     * @param active The active flag.
     */
    public void setActive(final boolean active) {
        synchronized (this) {
            this.active = active;
        }
    }

    /**
     * Gets the inventory.
     *
     * @return The inventory.
     */
    public Container getInventory() {
        return inventory;
    }

    /**
     * Updates the players' options when in a PvP area.
     */
    public void updatePlayerAttackOptions(final boolean enable) {
        if (enable) {
            actionSender.sendInteractionOption("Attack", 1, true);
            //actionSender.sendOverlay(381);
        } else {

        }
    }

    public void inflictDamage(final Hit inc) {
        this.inflictDamage(inc, null);
    }

    /**
     * Manages updateflags and HP modification when a hit occurs.
     *
     * @param source The Entity dealing the blow.
     */
    public void inflictDamage(final Hit inc, final Entity source) {
        if (getUpdateFlags().get(UpdateFlag.HIT)) {
            if (!getUpdateFlags().get(UpdateFlag.HIT_2)) {
                getDamage().setHit2(inc);
                getUpdateFlags().flag(UpdateFlag.HIT_2);
            }
        } else {
            getDamage().setHit1(inc);
            getUpdateFlags().flag(UpdateFlag.HIT);
        }
        skills.detractLevel(Skills.HITPOINTS, inc.getDamage());
        if ((source != null)) {
            this.setInCombat(true);
            this.setAggressorState(false);
            if (this.isAutoRetaliating()) {
                this.face(source.getLocation());
                this.getActionQueue().addAction(new AttackAction(this, source));
            }
        }
        if (skills.getLevel(Skills.HITPOINTS) <= 0) {
            if (!this.isDead()) {
                World.getWorld().submit(new DeathEvent(this));
            }
            this.setDead(true);
        }
    }

    /**
     * Gets the action queue.
     *
     * @return The action queue.
     */
    public ActionQueue getActionQueue() {
        return actionQueue;
    }

    @Override
    public void serialize(final IoBuffer buf) {
        IoBufferUtils.putRS2String(buf, NameUtils.formatName(name));
        IoBufferUtils.putRS2String(buf, password);
        buf.put((byte) rights.toInteger());
        buf.put((byte) (members ? 1 : 0));
        buf.putShort((short) getLocation().getX());
        buf.putShort((short) getLocation().getY());
        buf.put((byte) getLocation().getZ());
        final int[] look = appearance.getLook();
        for (int i = 0; i < 13; i++) {
            buf.put((byte) look[i]);
        }
        for (int i = 0; i < Equipment.SIZE; i++) {
            final Item item = equipment.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            buf.put((byte) skills.getLevel(i));
            buf.putDouble(skills.getExperience(i));
        }
        for (int i = 0; i < Inventory.SIZE; i++) {
            final Item item = inventory.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
        for (int i = 0; i < Bank.SIZE; i++) {
            final Item item = bank.get(i);
            if (item == null) {
                buf.putShort((short) 65535);
            } else {
                buf.putShort((short) item.getId());
                buf.putInt(item.getCount());
            }
        }
    }

    @Override
    public void deserialize(final IoBuffer buf) {
        this.name = IoBufferUtils.getRS2String(buf);
        this.nameLong = NameUtils.nameToLong(this.name);
        this.password = IoBufferUtils.getRS2String(buf);
        this.rights = Player.Rights.getRights(buf.getUnsigned());
        this.members = buf.getUnsigned() == 1;
        setLocation(Location.create(buf.getUnsignedShort(), buf.getUnsignedShort(), buf.getUnsigned()));
        final int[] look = new int[13];
        for (int i = 0; i < 13; i++) {
            look[i] = buf.getUnsigned();
        }
        appearance.setLook(look);
        for (int i = 0; i < Equipment.SIZE; i++) {
            final int id = buf.getUnsignedShort();
            if (id != 65535) {
                final int amt = buf.getInt();
                final Item item = new Item(id, amt);
                equipment.set(i, item);
            }
        }
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            skills.setSkill(i, buf.getUnsigned(), buf.getDouble());
        }
        for (int i = 0; i < Inventory.SIZE; i++) {
            final int id = buf.getUnsignedShort();
            if (id != 65535) {
                final int amt = buf.getInt();
                final Item item = new Item(id, amt);
                inventory.set(i, item);
            }
        }
        if (buf.hasRemaining()) { // backwards compat
            for (int i = 0; i < Bank.SIZE; i++) {
                final int id = buf.getUnsignedShort();
                if (id != 65535) {
                    final int amt = buf.getInt();
                    final Item item = new Item(id, amt);
                    bank.set(i, item);
                }
            }
        }
    }

    @Override
    public void removeFromRegion(final Region region) {
        region.removePlayer(this);
    }

    @Override
    public void addToRegion(final Region region) {
        region.addPlayer(this);
    }

    @Override
    public void inflictDamage(final int damage, final HitType type) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getClientIndex() {
        return this.getIndex() + 32768;
    }

    /**
     * Represents the rights of a player.
     *
     * @author Graham Edgecombe
     */
    public enum Rights {

        /**
         * A standard account.
         */
        PLAYER(0),

        /**
         * A player-moderator account.
         */
        MODERATOR(1),

        /**
         * An administrator account.
         */
        ADMINISTRATOR(2);

        /**
         * The integer representing this rights level.
         */
        private final int value;

        /**
         * Creates a rights level.
         *
         * @param value The integer representing this rights level.
         */
        Rights(final int value) {
            this.value = value;
        }

        /**
         * Gets rights by a specific integer.
         *
         * @param value The integer returned by {@link #toInteger()}.
         * @return The rights level.
         */
        public static Rights getRights(final int value) {
            if (value == 1) {
                return MODERATOR;
            } else if (value == 2) {
                return ADMINISTRATOR;
            } else {
                return PLAYER;
            }
        }

        /**
         * Gets an integer representing this rights level.
         *
         * @return An integer representing this rights level.
         */
        public int toInteger() {
            return value;
        }
    }

}
