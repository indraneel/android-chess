android-chess
==============
Play chess on Android! For two players, or one against the computer. AI
difficulty: laughably easy.

grid
-----
The *Grid* interface details the specification of a generic grid. You can add
types to a certain location, query to see what is there, and print the board
out to whatever output is specified.  

The *Location* class obviously encapsulates a location. It has support for
converting numerical coordinates into proper chess coordinates (file and
rank).  

The *Chessboard* class is an extension of *Board*, which is an implementation
of the Grid. It represents a standard 8x8 chessboard and uses a HashMap to map
a Location to the Piece occupied by it. Besides placing pieces on it, you can
draw the board out to standard output, invert the board, etc. For Android, we
can use this method to draw to the screen, invert the buttons if necessary,
etc.

pieces
------
This package defines all of the standard chess pieces as their own object. All
pieces are subclasses of the *Piece* class, which defines some standard
behavior shared by all the pieces.

Some important methods are *getAttackedLocations()*, which return a list of the
locations attacked or defended by that piece, and *getMoveLocations()*, which
lists the valid locations the piece can move to. There are also some helper
methods for dealing with piece notation for playing the game.

game
----
The game package defines some handy classes for actually playing a game of
chess.

The *Game* class is the most important, as it actually represents a game
that is being played. To interact with the game object, you query the game's
*move()* method, which moves the piece from the source location to the
destination specified. This means that we need to write a new class (perhaps
called *Chess*) that has a Game object, gets input, sanitizes it and then feeds
it into the game. After that, it is up to the GUI to render the board to the
screen.

Other useful things are some exception classes, which allow for robust handling
of error conditions and the like. There is also a *Move* class, which
represents one move in the game and can be used for the move-by-move playback.

Things to Do
-------------
- Make an AI that selects from the possible moves and picks one at random (this
  should only take a slight modification of my original code)
- Find out how we can save games on the phone
- Render the board to the screen; handle touch input
