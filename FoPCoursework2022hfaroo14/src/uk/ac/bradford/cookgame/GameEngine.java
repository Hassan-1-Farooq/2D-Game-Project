package uk.ac.bradford.cookgame;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and customers, as well as updating information
 * when a key is pressed (processed by the InputHandler) while the game is
 * running.
 *
 * @author prtrundl
 */
public class GameEngine {

    /**
     * An enumeration type to represent different types of tiles that make up a
     * level. Each type has a corresponding image file that is used to draw the
     * correct tile to the screen for each tile in a level. FLOOR tiles are open
     * for movement for players and customers, WALL tiles should block movement
     * into that tile, FOOD tiles allow the player to pick up food, TABLE and
     * DOOR tiles can be used for both decoration and for implementing advanced
     * features.
     */
    public enum TileType {
        WALL, FLOOR1, FLOOR2, FOOD1, FOOD2, FOOD3, TABLE, DOOR;
    }

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 35;

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 18;

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to place the player and
     * customers, and to randomise movement etc. Passing an integer (e.g. 123)
     * to the constructor called here will give fixed results - the same numbers
     * will be generated every time WHICH CAN BE VERY USEFUL FOR TESTING AND
     * BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional customers and reducing patience
     * etc.
     */
    private int levelNumber = 1;  //current level

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 0;

    /**
     * The current score in this game.
     */
    private int score = 0;

    /**
     * The GUI associated with this GameEngine object. This link allows the
     * engine to pass level and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of tiles that represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created. This is the array that is used to draw images to the
     * screen by the GUI class.
     */
    private TileType[][] level;

    /**
     * An ArrayList of Point objects used to create and track possible locations
     * to place the player and customers when a new level is created.
     */
    private ArrayList<Point> spawnLocations;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including stamina and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player player;

    /**
     * An array of Customer objects that represents the customers in the current
     * level of the game. Elements in this array should be either an object of
     * the type Customer (meaning that a customer is exists (not fed/cleared
     * yet) and therefore needs to be drawn or moved) or should be null (which
     * means nothing is drawn or processed for movement). null values in this
     * array are skipped during drawing and movement processing. Customers that
     * the player correctly feeds can be replaced with (i.e. assigned) the value
     * null in this array which removes them from the game, using syntax such as
     * customers[i] = null.
     */
    private Customer[] customers;

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. The method builds a 2D array of TileType values
     * that will be used to draw level to the screen and to add a variety of
     * tiles into each level. Tiles can be floors, walls, tables, doors or food
     * sources.
     *
     * @return A 2D array filled with TileType values representing the level in
     * the current game. The size of this array should use the width and height
     * of the game level using the LEVEL_WIDTH and LEVEL_HEIGHT attributes.
     */
    private TileType[][] generateLevel() {
        int randx = rng.nextInt(11);
        int randy = rng.nextInt(17);

        TileType[][] floor = new TileType[LEVEL_WIDTH][LEVEL_HEIGHT];
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                floor[i][j] = TileType.FLOOR1;

                for (int p = 0; p < LEVEL_WIDTH; p++) {
                    floor[p][0] = TileType.WALL;
                    floor[p][17] = TileType.WALL;
                    for (int a = 0; a < LEVEL_HEIGHT; a++) {
                        floor[0][a] = TileType.WALL;
                        floor[34][a] = TileType.WALL;

                        for (int w = 14; w < 20; w++) {
                            floor[w][7] = TileType.FLOOR2;
                            for (int k = 0; k < 10; k++) {

                            }
                        }
                    }
                }

                floor[0][0] = TileType.FLOOR1;
                floor[0][4] = TileType.DOOR;

                floor[randx][randy + 1] = TileType.TABLE;
                floor[randx][randy + 3] = TileType.TABLE;
                floor[randx + 2][randy + 1] = TileType.TABLE;
                floor[randx + 2][randy + 3] = TileType.TABLE;
                floor[randx + 2][0] = TileType.DOOR;
                floor[34][randy + 2] = TileType.DOOR;

                floor[15][10] = TileType.FOOD1;
                floor[16][10] = TileType.FOOD2;
                floor[17][10] = TileType.FOOD3;
                floor[13][7] = TileType.WALL;
                floor[13][8] = TileType.FLOOR2;
                floor[13][9] = TileType.FLOOR2;
                floor[13][10] = TileType.FLOOR2;
                floor[13][11] = TileType.FLOOR2;
                floor[13][12] = TileType.WALL;
                floor[14][12] = TileType.FLOOR2;
                floor[15][12] = TileType.FLOOR2;
                floor[16][12] = TileType.FLOOR2;
                floor[17][12] = TileType.FLOOR2;
                floor[18][12] = TileType.FLOOR2;
                floor[19][12] = TileType.FLOOR2;
                floor[20][12] = TileType.WALL;
                floor[20][11] = TileType.FLOOR2;
                floor[20][10] = TileType.FLOOR2;
                floor[20][9] = TileType.FLOOR2;
                floor[20][8] = TileType.FLOOR2;
                floor[20][7] = TileType.WALL;
                floor[0][0] = TileType.WALL;

                //modfy to return the 2D array that you build in this method
            }
        }

        return floor;
    }

    /**
     * Generates spawn points for the player and customers. The method processes
     * the level array and finds positions that are suitable for spawning, i.e.
     * empty tiles such as floors. Suitable positions should then be added to
     * the ArrayList that can be retrieved as Point objects - Points are a
     * simple d of object that contain an X and a Y co-ordinate stored using the
     * int primitive type.
     *
     * @return An ArrayList containing Point objects representing suitable X and
     * Y co-ordinates in the current level where the player or customers can be
     * added into the game.
     */
    private ArrayList<Point> getSpawns() {
        //YOUR CODE HERE

        return null;    //modify to return the ArrayList
    }

    /**
     * Adds customers in suitable locations in the current level. The first
     * version of this method should picked fixed positions for customers by
     * calling the three argument version of the constructor for the Customer
     * class and using fixed values for the patience, X and Y positions of the
     * Customer to be added. Customer objects created this way should be added
     * into an array of Customer objects that is declared, instantiated and
     * filled inside this method. This array should the be returned by this
     * method. Customer objects in the array returned by this method will then
     * be drawn to the screen using the existing code in the GameGUI class.
     *
     * The second version of this method (described in a later task) should call
     * the four argument constructor for Customer (instead of the three argument
     * constructor) and pass an integer with a value of either 1, 2 or 3 for the
     * last argument. This will generate new types of customer that want
     * different types of food.
     *
     * The third version of this method should use the spawnLocations ArrayList
     * to pick suitable positions to add customers, removing these positions
     * from the spawns ArrayList as they are used (using the remove() method) to
     * avoid multiple customers spawning in the same location and to prevent
     * them from being added on top of walls, tables etc. The method then
     * creates customers by instantiating the Customer class, setting patience,
     * and then setting the X and Y position for the customer using the X and Y
     * values from the Point object that was removed from the spawns ArrayList.
     *
     * @return An array of Customer objects representing the customers for the
     * current level of the game
     */
    private Customer[] addCustomers() {
        //YOUR CODE HERE

        Customer customer1 = new Customer(100, 4, 9, 1);
        Customer customer2 = new Customer(100, 7, 7, 2);
        Customer customer3 = new Customer(100, 6, 14, 3);
        Customer[] customers = {customer1, customer2, customer3};

        return customers;    //modify to return the created array of Customer objects
    }

    /**
     * Creates a Player object in the game. The method instantiates the Player
     * class and assigns values for the energy and position.
     *
     * The first version of this method should use fixed a fixed position for
     * the player to start, by setting fixed X and Y values when calling the
     * constructor in the Player class.
     *
     * The second version of this method should use the spawns ArrayList to
     * select a suitable location to spawn the player and removes the Point from
     * the spawns ArrayList. This will prevent the Player from being added to
     * the game inside a wall, bank or breach for example.
     *
     * @return A Player object representing the player in the game
     */
    private Player createPlayer() {

        Player player = new Player(100, 5, 5);

        return player;
        //modify to return a Player object
    }

    /**
     * Handles the movement of the player when attempting to move in the game.
     * This method is automatically called by the InputHandler class when the
     * user has presses one of the arrow keys on the keyboard. The method should
     * check which direction for movement is required, by checking which
     * character was passed to this method (see parameter description below). If
     * the tile above, below, to the left or to the right is clear then the
     * player object should have its position changed to update its position in
     * the game window. If the target tile is not empty then the player should
     * not be moved, but other effects may happen such as giving a customer
     * food, or picking up food etc. To achieve this, the target tile should be
     * checked to determine the type of tile (food, table, wall etc.) and
     * appropriate methods called or attribute values changed.
     *
     * A second version of this method in a later task will also check if the
     * player's stamina is at zero, and if it is then the player should not be
     * moved.
     *
     * @param direction A char representing the direction that the player should
     * move. U is up, D is down, L is left and R is right.
     */
    public void movePlayer(char direction) {
        //YOUR CODE HERE

        player.changeStamina(-1);
        if (player.getStamina() == 0) {
            player.changeStamina(+1);
        }

        int x = player.getX();
        int y = player.getY();
        boolean clash = false;

        switch (direction) {
            case 'L':
                if (level[player.getX() - 1][player.getY()] == TileType.FOOD1) {
                    clash = true;
                    player.grabFood(1);
                } else if (level[player.getX() - 1][player.getY()] == TileType.FOOD2) {
                    clash = true;
                    player.grabFood(2);
                } else if (level[player.getX() - 1][player.getY()] == TileType.FOOD3) {
                    clash = true;
                    player.grabFood(3);
                } else if (level[player.getX() - 1][player.getY()] == TileType.DOOR) {
                    clash = true;
                } else if (level[player.getX() - 1][player.getY()] == TileType.WALL) {
                    clash = true;
                } else if (level[player.getX() - 1][player.getY()] == TileType.TABLE) {
                    clash = true;
                }
                if (player.hasFood()) {
                    for (Customer myCustomer : customers) {
                        if (myCustomer != null) {
                            if (player.getX() - 1 == myCustomer.getX() && player.getY() == myCustomer.getY()) {
                                clash = true;
                                if (player.getCarriedFoodType() == myCustomer.getFoodWanted()) {
                                    player.giveFood();
                                    myCustomer.feed();
                                    score = score + myCustomer.getPatience();
                                    System.out.println(score);

                                }
                            }
                        }
                    }
                }

                if (clash == false) {
                    player.setPosition(x - 1, y);
                }
                break;

            case 'R':
                if (level[player.getX() + 1][player.getY()] == TileType.FOOD1) {
                    clash = true;
                    player.grabFood(1);
                } else if (level[player.getX() + 1][player.getY()] == TileType.FOOD2) {
                    clash = true;
                    player.grabFood(2);
                } else if (level[player.getX() + 1][player.getY()] == TileType.FOOD3) {
                    clash = true;
                    player.grabFood(3);
                } else if (level[player.getX() + 1][player.getY()] == TileType.DOOR) {
                    clash = true;
                } else if (level[player.getX() + 1][player.getY()] == TileType.WALL) {
                    clash = true;
                } else if (level[player.getX() + 1][player.getY()] == TileType.TABLE) {
                    clash = true;
                }
                if (player.hasFood()) {
                    for (Customer myCustomer : customers) {
                        if (myCustomer != null) {
                            if (player.getX() + 1 == myCustomer.getX() && player.getY() == myCustomer.getY()) {
                                clash = true;
                                if (player.getCarriedFoodType() == myCustomer.getFoodWanted()) {
                                    player.giveFood();
                                    myCustomer.feed();
                                    score = score + myCustomer.getPatience();
                                    System.out.println(score);

                                }
                            }
                        }

                    }

                }
                if (clash == false) {
                    player.setPosition(x + 1, y);
                }
                break;

            case 'U':
                if (level[player.getX()][player.getY() - 1] == TileType.FOOD1) {
                    clash = true;
                    player.grabFood(1);
                } else if (level[player.getX()][player.getY() - 1] == TileType.FOOD2) {
                    clash = true;
                    player.grabFood(2);
                } else if (level[player.getX()][player.getY() - 1] == TileType.FOOD3) {
                    clash = true;
                    player.grabFood(3);
                } else if (level[player.getX()][player.getY() - 1] == TileType.DOOR) {
                    clash = true;
                } else if (level[player.getX()][player.getY() - 1] == TileType.TABLE) {
                    clash = true;
                } else if (level[player.getX()][player.getY() - 1] == TileType.WALL) {
                    clash = true;
                }
                if (player.hasFood()) {
                    for (Customer myCustomer : customers) {
                        if (myCustomer != null) {
                            if (player.getX() == myCustomer.getX() && player.getY() - 1 == myCustomer.getY()) {
                                clash = true;
                                if (player.getCarriedFoodType() == myCustomer.getFoodWanted()) {
                                    player.giveFood();
                                    myCustomer.feed();
                                    score = score + myCustomer.getPatience();
                                    System.out.println(score);
                                }
                            }
                        }
                    }
                }
                if (clash == false) {
                    player.setPosition(x, y - 1);
                }
                break;

            case 'D':
                if (level[player.getX()][player.getY() + 1] == TileType.FOOD1) {
                    clash = true;
                    player.grabFood(1);
                } else if (level[player.getX()][player.getY() + 1] == TileType.FOOD2) {
                    clash = true;
                    player.grabFood(2);
                } else if (level[player.getX()][player.getY() + 1] == TileType.FOOD3) {
                    clash = true;
                    player.grabFood(3);
                } else if (level[player.getX()][player.getY() + 1] == TileType.DOOR) {
                    clash = true;
                } else if (level[player.getX()][player.getY() + 1] == TileType.TABLE) {
                    clash = true;
                }
                if (level[player.getX()][player.getY() + 1] == TileType.WALL) {
                    clash = true;
                }

                if (player.hasFood()) {
                    for (Customer myCustomer : customers) {
                        if (myCustomer != null) {
                            if (player.getX() == myCustomer.getX() && player.getY() + 1 == myCustomer.getY()) {
                                clash = true;
                                if (player.getCarriedFoodType() == myCustomer.getFoodWanted()) {
                                    player.giveFood();
                                    myCustomer.feed();
                                    score = score + myCustomer.getPatience();
                                    System.out.println(score);

                                }
                            }
                        }
                    }
                }
                if (clash == false) {
                    player.setPosition(x, y + 1);
                }

                break;
        }
        if (null != level[x][y]) {
            switch (level[x][y]) {
                case FOOD1:
                    player.grabFood(1);
                    break;
                case FOOD2:
                    player.grabFood(2);
                    break;
                case FOOD3:
                    player.grabFood(3);
                    break;
                default:
                    break;
            }
        }
        if (player.hasFood()) {
            for (Customer myCustomer : customers) {
                if (myCustomer != null) {
                    if (player.getX() == myCustomer.getX() && player.getY() == myCustomer.getY()) {

                        if (player.getCarriedFoodType() == myCustomer.getFoodWanted()) {
                            player.giveFood();
                            myCustomer.feed();
                            score = score + myCustomer.getPatience();
                            System.out.println(score);
                        }
                    }
                }
            }
        }
    }

    public void increaseStamina() {
        player.changeStamina(5);
    }

    /**
     * Attempts to give a customer the food that the player is carrying. This
     * method should only be called (from the movePlayer method) when the player
     * attempts to move into the same tile as a customer and the player is
     * already carrying the right type of food - i.e. the type of food that the
     * customer wants.
     *
     * This method should call a method on the player object to give the food
     * (thus setting the player's attributes properly to reflect that they
     * delivered food and now can pick up more), and increase the score as well
     * as printing the score to the standard output. Finally it should "feed"
     * the customer by calling the correct method on the Customer object
     * indicating that the player has "fed".
     *
     * @param g The Customer object corresponding to the customer in the game
     * that the player just attempted to move into the same tile as.
     */
    private void deliverFood(Customer c) {
        //YOUR CODE HERE

    }

    /**
     * Moves a specific customer in the game. The method updates the X and Y
     * attributes of the Customer object passed to the method, to set its new
     * position.
     *
     * @param c The Customer that needs to be moved
     */
    private void moveCustomer(Customer c) {
        int x = c.getX();
        int y = c.getY();
        if (c.getX() == player.getX() - 3 && c.getY() == player.getY()) {
            return;
        }

        if (level[c.getX() + 1][c.getY()] == TileType.TABLE) {
            return;
        } else if (level[c.getX() - 1][c.getY()] == TileType.TABLE) {
            return;
        } else if (level[c.getX()][c.getY() + 1] == TileType.TABLE) {
            return;
        } else if (level[c.getX()][c.getY() - 1] == TileType.TABLE) {
            return;
        }

        int navigate = rng.nextInt(4);

        switch (navigate) {
            case 3:

                if (level[c.getX() + 1][c.getY()] != TileType.WALL) {
                    c.setPosition(x + 1, y);
                } else if (level[c.getX() + 1][c.getY()] != TileType.FOOD1) {
                    c.setPosition(x + 1, y);
                } else if (level[c.getX() + 1][c.getY()] != TileType.FOOD2) {
                    c.setPosition(x + 1, y);
                } else if (level[c.getX() + 1][c.getY()] != TileType.FOOD3) {
                    c.setPosition(x + 1, y);
                } else if (level[c.getX() + 1][c.getY()] != TileType.TABLE) {
                    c.setPosition(x + 1, y);
                }
                break;

            case 2:
                if (level[c.getX() - 1][c.getY()] != TileType.WALL) {
                    c.setPosition(x - 1, y);
                } else if (level[c.getX() - 1][c.getY()] != TileType.FOOD1) {
                    c.setPosition(x - 1, y);
                } else if (level[c.getX() - 1][c.getY()] != TileType.FOOD2) {
                    c.setPosition(x - 1, y);
                } else if (level[c.getX() - 1][c.getY()] != TileType.FOOD3) {
                    c.setPosition(x - 1, y);
                } else if (level[c.getX() - 1][c.getY()] != TileType.TABLE) {
                    c.setPosition(x - 1, y);
                }
                break;

            case 1:
                if (level[c.getX()][c.getY() + 1] != TileType.WALL) {
                    c.setPosition(x, y + 1);
                } else if (level[c.getX()][c.getY() + 1] != TileType.FOOD1) {
                    c.setPosition(x, y + 1);
                } else if (level[c.getX()][c.getY() + 1] != TileType.FOOD2) {
                    c.setPosition(x, y + 1);
                } else if (level[c.getX()][c.getY() + 1] != TileType.FOOD3) {
                    c.setPosition(x, y + 1);
                } else if (level[c.getX()][c.getY() + 1] != TileType.TABLE) {
                    c.setPosition(x, y + 1);
                }
                break;
            default:
                if (level[c.getX()][c.getY() - 1] != TileType.WALL) {
                    c.setPosition(x, y - 1);
                } else if (level[c.getX()][c.getY() - 1] != TileType.FOOD1) {
                    c.setPosition(x, y - 1);
                } else if (level[c.getX()][c.getY() - 1] != TileType.FOOD2) {
                    c.setPosition(x, y - 1);
                } else if (level[c.getX()][c.getY() - 1] != TileType.FOOD3) {
                    c.setPosition(x, y - 1);
                } else if (level[c.getX()][c.getY() - 1] != TileType.TABLE) {
                    c.setPosition(x, y - 1);
                }
                break;
        }
    }

    /**
     * Moves all customers on the current level. This method iterates over all
     * elements of the customers array (e.g. using a for loop) and checks if
     * each one is null (using an if statement inside that for loop). For every
     * element of the array that is NOT null, this method calls the moveCustomer
     * method and passes it the current array element (i.e. the current customer
     * object being used in the loop).
     */
    private void moveAllCustomers() {
        //YOUR CODE HERE
        for (int i = 0; i < customers.length; i++) {

            if (customers[i] != null) {
                moveCustomer(customers[i]);

            }
        }
    }

    /**
     * Processes the customers array to find any Customer in the array that has
     * been fed (i.e. given food by the player of the type they wanted). Any
     * Customer in the array with a "fed" attribute value of true should be set
     * to null; when drawing or moving customers the null elements in the
     * customers array are skipped, essentially removing them from the game.
     */
    private void cleanFedCustomers() {
        //YOUR CODE HERE

        for (int i = 0; i < customers.length; i++) {
            Customer myCustomer = customers[i];

            if (customers[i] != null) {
                if (myCustomer.beenFed()) {
                    customers[i] = null;

                }
            }
        }
    }

    /**
     * This method is called when the number of "unfed" customers in the level
     * is zero, meaning that the player has fed all customers, "completing" the
     * level. This method is similar to the startGame method and will use SOME
     * identical code.
     *
     * This method should increase the current level number, create a new level
     * by calling the generateLevel method and setting the level attribute using
     * the returned 2D array, add new Customers, and finally place the player in
     * the new level.
     *
     * A second version of this method in a later task should also find suitable
     * positions to add customers and the player using the spawnLocations
     * ArrayList and code in the getSpawns method.
     */
    private void nextLevel() {
        //YOUR CODE HERE

        level = generateLevel();
        spawnLocations = getSpawns();
        customers = addCustomers();
        player = createPlayer();
        gui.updateDisplay(level, player, customers);
        levelNumber++;

    }

    /**
     * The first version of this method should place the player in the game
     * level by setting new fixed X and Y values for the player object in this
     * class.
     *
     * The second version of this method in a later task should place the player
     * in a game level by choosing a position from the spawnLocations ArrayList,
     * removing the spawn position as it is used. The method sets the players
     * position in the level by calling its setPosition method with the x and y
     * values of the Point taken from the spawnLocations ArrayList.
     */
    private void placePlayer() {
        //YOUR CODE HERE
    }

    /**
     * This method should be called each game turn and should check if all
     * NON-NULL Customer objects in the customers array have been fed. If all
     * valid Customer objects have been fed or the array contains only null
     * values then this method should return true, but if there are any customer
     * objects in the array with a "fed" attribute value of false then the
     * method should return false.
     *
     * @return true if all Customer objects in the customers array have been
     * fed, false otherwise
     */
    private boolean allCustomersFed() {
        //YOUR CODE HERE
        int fedCustomer = 0;
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] == null) {
                fedCustomer++;
            }

        }
        if (fedCustomer == customers.length) {
            nextLevel();
        }
        return true;   //modify to return either true or false
    }

    /**
     * This method is automatically called by doTurn, and it should reduce the
     * patience value for all Customer objects in the customers array by a small
     * fixed amount, by using a for loop that iterates over the customers array,
     * checking for non-null elements and calling an appropriate method on any
     * non-null objects in the array.
     */
    private void reduceCustomerPatience() {
        //YOUR CODE HERE
        for (int i = 0; i < customers.length; i++) {
            Customer myCustomer = customers[i];
            if (customers[i] != null) {
                myCustomer.changePatience(-1);
            }
        }
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method clears (removes from the game) "fed" customers every
     * ten turns, moves any customers that have not been fed and cleared every
     * three turns, and increments the turn number. Finally it makes the GUI
     * redraw the game level by passing it the level, player and customers
     * objects for the current level.
     *
     * A second version of this method in a later task will also check if all
     * customers in the current level have been fed, and if they have it will
     * call the nextLevel() method to generate a new, harder level.
     *
     * A third version of this method will also increase the player's stamina
     * slightly to allow them to recover and move again if their stamina runs
     * out.
     */
    public void doTurn() {

        turnNumber++;
        if (turnNumber % 10 == 0) {
            cleanFedCustomers();

        }
        if (turnNumber % 3 == 0) {
            moveAllCustomers();
            reduceCustomerPatience();
            allCustomersFed();
        }
        gui.updateDisplay(level, player, customers);
    }

    /**
     * Starts a game. This method generates a level, finds spawn positions in
     * the level, adds customers and the player and then requests the GUI to
     * update the level on screen using the information on level, player and
     * customers.
     */
    public void startGame() {
        level = generateLevel();
        spawnLocations = getSpawns();
        customers = addCustomers();
        player = createPlayer();
        gui.updateDisplay(level, player, customers);
    }
}
