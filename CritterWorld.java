package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */

public class CritterWorld {
	
	/**
	 *  This world is itself just a giant 2 dimensional matrix of critters.
	 */
	
	private static Critter[][] world = new Critter[Params.world_width][Params.world_height];
	
	/**
	 * Makes World accessible as a public class.
	 * @return World a 2 dimensional matrix of critters.
	 */
	public static Critter[][] getWorld() {
		return world;
	}
	
	/**
	 * Prints out the World grid using a double loop, including empty spaces and all types of critters.
	 */
	public static void printWorld() {
        printBorder();
		for (int i = 0; i < Params.world_height; i++) {       		
        		if (i > 0 && i < Params.world_height) {
        			System.out.print('|');
        		}
        		for (int j = 0; j < Params.world_width; j++) {
            		if ((world[j][i] == null)) {
                    System.out.print(' ');
                } else {
                    System.out.print(world[j][i]);
                }
        		}
            if (i > 0 && i < Params.world_height) {
        			System.out.print('|');
            }
            System.out.println();
        }
		printBorder();
		System.out.println();
	}
    
	public static void printBorder() {
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.print("+");
	}
	
    /**
     * Adds a Critter at position (x,y) in the 2 dimensional array
     * @param x The X Coordinate of the Critter to be added.
     * @param y The Y Coordinate of the Critter to be added.
     * @param c The Critter that needs to be added.
     */
    public static void addCritter(int x, int y, Critter c) {
        world[x][y] = c;
    }
    
    /**
     * Places a null to the 2 dimensional array at (x,y), presumably where a critter originally was.
     * @param x The X Coordinate of the Critter
     * @param y The Y Coordinate of the Critter
     */
    public static void removeCritter(int x, int y){
        world[x][y] = null;
    }
    
    /**
     * Empties the current World by generating a new completely empty 2 dimensional array and using it as a replacement.
     */
    public static void clear() {
        world = new Critter[Params.world_width][Params.world_height];
    }
}
