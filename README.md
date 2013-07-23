OVERVIEW
==================

This project is an android mobile game. In this game, blocks are placed into a window by the player, to balance in a pile, in real time.
The pile balances on a fulcrum - until it falls.

OBJECTIVE
==================

The tower will grow as one start placing blocks on it, until it cannot balance, and falls.

The player needs to make the tower as high as he or she can before the tower falls. The player's score is the height of the tower he/she builds.

FEATURES
==================

* A multiplayer mode, in which a player can compete with a second player. In this case, the player who fails to survive (i.e. whose last placed block leads to the falling of the pile) loses.
* A menu of blocks from which user can choose a block from a given set of blocks of different shapes and dimensions.

IMPLEMENTATION
========================

####Platform - Android
####Languages - Java, XML
####Modules - JBox2D

* JBox2D (physics engine) is used for physics simulation
* Multiplayer Mode is implemented over bluetooth api of android sdk

IDEAS YET TO IMPLEMENT
==========================

* Advanced parameters for score calculation to be determined
* Levels for varying difficulties (ex. changing fulcrum placement, adding obstacles etc.)
