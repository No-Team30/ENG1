# ENG1
# NOTE THE JAR FILE IS OUT OF DATE AND DOES NOT INCLUDE TELEPORTATION, BUT IS IN THE BASE CODE 

A Java game as described by ENG1 module CompSci York Uni.

Skins sourced from: https://github.com/czyzby/gdx-skins

Graphics Workflow:
    Design graphics in inkscrape
    (One tile is 64px, 64px)
    
    Export as a tileset

    Import into Tiled
    Build map from the tileset



Game Container 
    This is the entry point for the game
    Is a container for the “Screen” instance

Screens:
    Game
        This is where the actual game will be run
        TODO Pause Screen?
    Main Menu
        The screen initially loaded on startup
    Settings
        Settings for the game




Game Classes:
    Movement Class
        Handles the movement for various instances
    Input handler:
        Moving character

    Auber Class:
    Inherits from movement
    Infiltrator
        Inherits from movement

    Systems
        ?
