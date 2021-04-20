# Hyperion

The `develop` and `main` branches contain minor modifications to the code. If you're purely
interested in the original Hyperion, please see the `original` branch, which is a direct
migration of the SVN repository.

---

*The following is taken from: [http://archive.is/te51Q](http://archive.is/te51Q)*

# **Hyperion:** *the open-source Java gameserver suite*

#### **About**

Hyperion is a Java gameserver suite server which aims to provide an excellent, stable, quality base with advanced features such as update server (to stream the cache to the client) and login server (for multi-world support with cross world messaging).

The server has been developed completely from scratch, even the ISAAC cipher code is scratch (as the one used by most is copyrighted Jagex - from a deob).

There are lots of unique ideas: we are spending time making the server good and high quality instead of cramming lots of features in a small amount of time.

Also, for those working on their own servers, there is documentation on the login, update and in-game protocol, and information about the updating and walking procedures.

Twitter

SVN commits as well as our ramblings can be found here: [http://twitter.com/hyperion_server.](http://twitter.com/hyperion_server.)

#### **Credits**

**Developers:**
* Graham
* Blake
* BrettR


**Thanks to:**
* Vastico - item def code + data file
* Lothy - equipment code snippet
* Miss Silabsoft - some more equipment fixes
* Scu11 - found + fixed multiple bugs
* Sub - found npc bug
* Sean - testing, ideas
* William - testing, ideas


#### Features
* ✔️ = Complete
* 🚧 = Under development
* ⭕ = Not yet started

##### 🚧 World server

* ✔️ Login
* ⭕ Updating
  - ✔️ Rendering
  - ✔️ Multiplayer
  - 🚧 NPCs
  - 🚧 Update masks
  - ✔️ Region system

* 🚧 Communication
  - ✔️ Public chat
  - ✔️ Commands
  - ⭕ Chat options
  - ⭕ Friends/ignores/private chat
  - ⭕ Report abuse

* ✔️ Inventory/item support
  - ✔️ Item class
  - ✔️ Container class
  - ✔️ Interface container listener
  - ✔️ Equipment container listener
  - ✔️ Weapon container listener
  - ✔️ Swapping/equipping/etc
  - ✔️ Banking
  - ✔️ Action and action queue system
* 🚧 Clipping and path finding


##### 🚧 Login server

* ✔️ Saving/loading/online checking
* ⭕ World password check
* ⭕ PM and friendslist


##### ✔️ Cache system

##### ✔️ File server

* ✔️ JAGGRAB and HTTP server
* ✔️ Integrated webclient


##### Web interface

#### **Unique features**

**Action system:**

The action system is another first which is going to prove quite unique as it is developed. There is an Action class and ActionQueue class. The ActionQueue will queue and process actions for a player.

Since a lot of actions share code (i.e. woodcutting and mining are extremely similar), there are some abstract classes such as AbstractHarvestingAction which will contain code common to both.

These allow rapid development and easy bug fixing, as well as just looking cool.

**Cached update blocks:**

Say you had 100 players in a region, and then a new one logs in. In a traditional server, each 100 players will reconstruct the new player's appearance update block when they login. This server will cache update blocks for the duration of the cycle, so it only happens once, at the expense of slightly more memory usage.

This should give some improvements when there are a lot of players in an area, especially if the updates are large (like appearance blocks).

**Pooled player updating:**

Player updating is a read only task. This means you can execute it in multiple threads safely. The server creates a thread pool with the same number of cores as the current computer it is running on, and dispatches update tasks to this pool.

To prevent threading issues, the main thread will block until all the updates are complete. Then the main thread may run as usual, processing packets and performing reads and writes safely with little worry of thread-safety.

This can give vast improvements when you have a PC with multiple cores and/or processors. If you look at the screenshot above, which was taken on a dual core PC, the server is using around 80% of the CPU which means it is taking advantage of both cores (otherwise it would be at or below 50%).

There are classes allowing you to do this as well (namely the ParallelTask class), however, there are few remaining cases in which this is possible to do safely.

**Update server:**

The update server uses the ondemand and a HTTP/0.9-like protocol to stream the cache to the client just like a popular MMORPG. This gives a few advantages: when the cache is modified, it can be pushed out to all new clients very easily with minimum bandwidth used up by subsequent updates (the files in the cache are only updated when the client deems it necessary). Also, no client modifications are needed to make it work (assuming you are using the original deob).

#### **Benchmark**

I've done some testing and it seems the server can handle 2000 players without crashing. However, **these are not real players** so not entirely accurate. Also, bear in mind the server in fact has very few features and this will have an impact on performance (in a real server if you had 2000 players they would be performing actions).

For those interested in how the benchmark was setup, the SYI attacker was run on the same PC as the server. The PC is dual core, 2.2ghz. With the way in which the server is setup it would perform better on more cores, as not just the input/output is done in a multi-threaded manner. The throttle filter was disabled. With it enabled, they will connect slowly. Also, there is no limit per IP yet.

While this number of players are online, the server is still usable (logins are slower than normal - as expected - but there isn't much lag).
