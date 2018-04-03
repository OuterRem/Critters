package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */


import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {

	private static String myPackage;
	private	static List<Critter> population = new ArrayList<Critter>();
	private static List<Critter> babies = new ArrayList<Critter>();
	private  int [] location = {0, 0};
	private static Map<Critter, int[]> previousCritters = new HashMap<>();
	private static int timestep = 0;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	protected void setEnergy(int e) {
		this.energy = e;
	}
	private int x_coord;
	private int y_coord;
	
	/**
	 * The walk method takes into account the torus nature of the world and allows critters to walk "off" the map and back into the map from the 
	 * opposite side using a series of conditional statements.  In addition, babies are ignored because they do not choose their own directions.
	 * Because there are only 8 possible directions, 4 cardinal and 4 diagonal, the input cannot exceed 8.
	 * Note that diagonals contain conditions for both cardinal directions that intersect, in case of meeting the CritterWorld's edge.
	 * @param direction An integer signifying the direction of travel, starting from Eastwards as a 0, and rotating clockwise, while including
	 * diagonal and cardinal directions.
	 */
	protected final void walk(int direction) {
		direction %= 8;

		if(!babies.contains(this)){ 
			this.energy -= Params.walk_energy_cost;
		}

		switch(direction) {
		// Directly right, or East.
    		case 0:
    		    if(this.x_coord == (Params.world_width - 1)) {this.x_coord = 0;}
                else {this.x_coord++;}
    		    break;
    		// Diagonally down-right, or South-east.
    		// Contains conditions for both down & right, or South & East as well as the exact CritterWorld Corner.
    		case 1:
    		    if((this.x_coord == (Params.world_width - 1)) && (this.y_coord == 0))
    		    {
                    this.x_coord = 0;
                    this.y_coord = Params.world_height - 1;
                }
                else if(this.y_coord == 0) {this.x_coord++; this.y_coord = Params.world_height - 1;}
                else if(this.x_coord == (Params.world_width - 1)) {this.x_coord = 0; this.y_coord--;}
                else {this.x_coord++; this.y_coord--;}
    		    break;
    		// Directly down, or South.
    		case 2:
    		    if(this.y_coord == 0){this.y_coord = Params.world_height - 1;}
                else{ this.y_coord--;}
    		    break;
    		// Diagonally down-left, or South-west.
    		// Contains conditions for both down & left, or South & West as well as the exact CritterWorld Corner.
    		case 3:
    		    if((this.x_coord == 0) && (this.y_coord == 0))
                {
                    this.x_coord = Params.world_width - 1;
                    this.y_coord = Params.world_height - 1;
                }
                else if(this.x_coord == 0) {this.x_coord = Params.world_width - 1; this.y_coord--; }
                else if(this.y_coord == 0) {this.x_coord--; this.y_coord = Params.world_height - 1;}
                else {this.x_coord--; this.y_coord--;}
    		    break;
    		// Directly left, or West.
    		case 4:
    		    if(this.x_coord == 0) {this.x_coord = Params.world_width - 1;}
                else {this.x_coord--;}
    		    break;
    		// Diagonally up-left, or North-west.
    		// Contains conditions for both up & left, or North & West as well as the exact CritterWorld Corner.
    		case 5:
    		    if((this.x_coord == 0) && (this.y_coord == (Params.world_height - 1)))
                {
                    this.x_coord = Params.world_width - 1;
                    this.y_coord = 0;
                }
                else if(this.x_coord == 0) {this.x_coord = Params.world_width - 1; this.y_coord++;}
                else if(this.y_coord == (Params.world_height - 1)) {this.y_coord = 0; this.x_coord--;}
                else {this.y_coord++; this.x_coord--;}
    		    break;
    		// Directly up, or North.
    		case 6:
    		    if(this.y_coord == (Params.world_height - 1)) {this.y_coord = 0;}
                else {this.y_coord++;}
    		    break;
    		// Diagonally up-right, or North-east.
    		// Contains conditions for both up & right, or North & East as well as the exact CritterWorld Corner.
    		case 7:
    		    if((this.x_coord == (Params.world_width - 1)) && (this.y_coord == (Params.world_height - 1)))
                {
                    this.x_coord = 0;
                    this.y_coord = 0;
                }
                else if(this.y_coord == (Params.world_height - 1)) {this.x_coord++; this.y_coord = 0;}
                else if(this.x_coord == (Params.world_width - 1)) {this.y_coord++; this.x_coord = 0;}
                else {this.x_coord++; this.y_coord++; }
		}
	}
	
	/**
	 * The run method is effectively a double move in the direction given at the additional run energy cost.  Utilizes two walk commands
	 * to cover distance, which each subtract energy from the critter's total energy pool. The method returns energy taken
	 * by the two walk commands initiated, then subtracts the necessary run energy cost.
	 * @param direction The integer value indicating which of the 8 possible directions, 4 cardinal & 4 diagonal, taken during movement.
	 */
	protected final void run(int direction) {
		this.energy += Params.walk_energy_cost;
		this.energy += Params.walk_energy_cost;
		this.energy -= Params.run_energy_cost;
		walk(direction);
		walk(direction);
	}
	
	/**
	 * The reproduce method allows the critters to create children by subtracting the min_reproduce_energy in Params.java.  The baby 
	 * is given half the energy of the parent, while the parent's energy is reduced by nearly half. The baby moves to a space adjacent to 
	 * the location of the parent.
	 * @param baby The receiving parameter determines the Critter subclass based on the parent.
	 * @param dir The direction of travel the Baby will take.
	 */
	protected final void reproduce(Critter baby, int dir) {
	    if (this.getEnergy() >= Params.min_reproduce_energy) {
	        babies.add(baby);
	        baby.energy = this.energy / 2;
	        this.energy = this.energy / 2 + this.energy % 2;
	        baby.x_coord = this.x_coord;
	        baby.y_coord = this.y_coord;
	        baby.walk(dir); 
	        // System.out.println("Baby Born!");
	        // Debugging purposes only.
	    }
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String opponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		String className = myPackage+"."+critter_class_name;
		Class<?> c = null;
		try{
			c = Class.forName(className);
		} 
		catch(ClassNotFoundException ce){
			throw new InvalidCritterException(critter_class_name);
		} 
		try{
		    if(Critter.class.isAssignableFrom(c)){			    
				Critter o = (Critter) c.newInstance();
				o.energy = Params.start_energy;
				o.x_coord = getRandomInt(Params.world_width);
				o.y_coord = getRandomInt(Params.world_height);
				CritterWorld.addCritter(o.x_coord, o.y_coord, o);  // add to matrix
				population.add(o);
			}
		} 
		catch (InstantiationException | IllegalAccessException e){
	         throw new InvalidCritterException(critter_class_name);
		}
	
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new ArrayList<Critter>();
		String className = myPackage + "." + critter_class_name;
        Class<?> c = null;
        try{
            c = Class.forName(className);
        } 
        catch(ClassNotFoundException ce){
            throw new InvalidCritterException(critter_class_name);
        }
        List<Critter> set = population;
        for (Critter current : set) {
            if (c.isInstance(current)) {
                result.add(current);
            }
        }
		return result;
	}
	
	/**
	 * Prints out how many critters of each type there are on the board in total, or for a specific critter type.
	 * @param critters List of critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		Map<String, Integer> count = new HashMap<String, Integer>();
		for (Critter c : critters) {
			Integer prevCount = count.get(c.toString());
			if (prevCount == null) {
				count.put(c.toString(),  1);
			} else {
				count.put(c.toString(), prevCount.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : count.keySet()) {
			System.out.print(prefix + s + ":" + count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every time step.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	public static int getTimeStep() {
		return timestep;
	}
	
	/**
	 * Clears CritterWorld and both the population and baby Lists.
	 */
	public static void clearWorld() {
		Critter.population.clear();
		Critter.babies.clear();
		CritterWorld.clear();
	}
	
	/**
	 * This method advances the entire CritterWorld by one action, including increasing the time step counter, moving Critters via walk or run,
	 * generating Algae, allowing critters to enter into combat, removing dead critters, and turning babies into adults.
	 */
	public static void worldTimeStep() {
		timestep++;
		// System.out.println("timestep = " + timestep);
		// Debugging based on timesteps.
		previousCritters = new HashMap<>();
		for (Critter c : population) {
			previousCritters.put(c, c.location);
			updateLocation(c, c.x_coord, c.y_coord);
			c.doTimeStep();
			if (checkX(c) != -1) {
				if (checkX(c) != c.x_coord || checkY(c) != c.y_coord) {
					updateLocation(c, c.x_coord, c.y_coord);
				}
			}
		}
		doEncounters();
		killCritters();
		genAlgae();
		population.addAll(babies);
		babies.clear();
	}
	
	/**
	 * This method removes dead critters from CritterWorld by checking their energy level.  It is also the method by which Critters have their
	 * rest energy subtracted for a turn, as these two processes go hand in hand.
	 */
	public static void killCritters() {
		List<Critter> deadCritters = new ArrayList<Critter>();
		for (Critter c : population) {
			c.energy -= Params.rest_energy_cost;
			if (c.energy <= 0) {
				deadCritters.add(c);
			}
		}
		population.removeAll(deadCritters);
	}
	
	/**
	 * Determines the X coordinate of Critter c as held by the Location Array.
	 * @param c The Critter to be checked.
	 * @return The X coordinate stored in the Location Array.
	 */
	public static int checkX(Critter c) {
		if (population.contains(c)) {
			return c.location[0];
		}
		System.out.println("Critter does not exist.");
		return -1;
	}
	
	/**
	 * Determines the Y coordinate of Critter c as held by the Location Array.
	 * @param c The Critter to be checked.
	 * @return The Y coordinate stored in the Location Array.
	 */
	public static int checkY(Critter c) {
		if (population.contains(c)) {
			return c.location[1];
		}
		return -1;
	}
	
	/**
	 * Updates the Location array of a specific Critter.
	 * @param c The Critter whose location array is to be updated.
	 * @param x The new X Coordinate.
	 * @param y The new Y Coordinate.
	 */
	public static void updateLocation(Critter c, int x, int y) {
		c.location[0] = c.x_coord;
		c.location[1] = c.y_coord;
	}
	
	/**
	 * This method plays through the encounters of various pairs of critters using helper methods while keeping track
	 * of Critters that have already had encounters via an ArrayList.  A winner is decided, if necessary, and critters  
	 * which attempt to flee are tracked.  Helper methods are listed below this method.
	 */
	public static void doEncounters() {
		List<Critter> encounteredCritters = new ArrayList<Critter>();
		for (int i = 0; i < Params.world_width; i++) {
			for (int j = 0; j < Params.world_height; j++) {
				Critter c1 = null; 
				Critter c2 = null;
				Critter w = null;
				boolean getWinner = false;
				if (checkEncounter(i, j) >= 2) {
					if (getWinner) {
						c1 = w;
						c2 = encounter(i, j, encounteredCritters);
						encounteredCritters.add(c2);
					} else {
						c1 = encounter(i, j, encounteredCritters);
						encounteredCritters.add(c1);
						c2 = encounter(i, j, encounteredCritters);
						encounteredCritters.add(c2);
					}
					int C1X = c1.x_coord;
					int C1Y = c1.y_coord;
					int C2X = c2.x_coord;
					int C2Y = c2.y_coord;
					int c1Potential = getCombatPotential(c1, c2);
					int c2Potential = getCombatPotential(c2, c1);
					escapeEncounter(c1, C1X, C1Y);
					escapeEncounter(c2, C2X, C2Y);
					if((c1.getEnergy() > 0) && (c2.getEnergy() > 0) && (c1.x_coord == c2.x_coord) && (c1.y_coord == c2.y_coord)) {
						if((c1Potential == c2Potential ) || (c1Potential  > c2Potential )){
							w = battle(c1, c2, getWinner, i, j);
						} else {
							w = battle(c2, c1, getWinner, i, j);
						}
					}
				}
			}
		}
	}
	
	/**
	 * This method determines the combat potential of a critter at random, based on their current energy level.
	 * @param c The combatant creature.
	 * @param o The opponent creature.
	 * @return The integer value used as a combat capability estimate, decided at random.
	 */
	protected static int getCombatPotential(Critter c, Critter o) {
		boolean BattleC = c.fight(o.toString());
		if ((BattleC) && (c.energy > 0)) {
			return getRandomInt(c.energy);
		}
		return 0;
	}
	
	/**
	 * This method plays through the steps in which a winner and loser of a battle are decided based on a flag and conditions surrounding
	 * the combat potentials of the critters determined by the previous helper function and enforced by conditional statements made before 
	 * this method is called.
	 * @param winner The winner of an encounter.
	 * @param loser The loser of an encounter.
	 * @param pickWinner
	 * @param x the x coordinate in which the battle takes place.
	 * @param y the y coordinate in which the battle takes place.
	 * @return The winner of the battle.
	 */
	private static Critter battle(Critter winner, Critter loser, boolean pickWinner, int x, int y) {
		winner.energy = winner.energy + ((loser.energy) / 2) + 1;
		loser.energy = 0;
		population.remove(loser);
		CritterWorld.addCritter(x, y, winner);;
		pickWinner = true;
		return winner;
	}
	
	/**
	 * This method determines if a critter is successfully able to escape an encounter based on the direction the critter chooses to
	 * travel, specifically if that zone is occupied or not and updates the critter's location appropriately.
	 * @param c The fleeing critter in question.
	 * @param x the x coordinate which the critter is traveling towards.
	 * @param y the y coordinate which the critter is traveling towards.
	 */
	private static void escapeEncounter(Critter c, int x, int y) {
		if (x != c.x_coord || y != c.y_coord) {
			if ((previousCritters.get(c)[0] != c.location[0]) && (previousCritters.get(c)[1] != c.location[1])) {
				c.x_coord = x;
				c.y_coord = y;
				updateLocation(c, c.x_coord, c.y_coord);
			} else {
				for (int k = 0; k < population.size(); k++) {
					if ((population.get(k).x_coord == checkX(c)) && (population.get(k).y_coord == checkY(c)) && !(population.get(k).equals(c))) {
						c.x_coord = checkX(c);
						c.y_coord = checkY(c);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * This method determines the number of active conflicts taking place across the CritterWorld and returns that value.
	 * @param x the x coordinate of the grid location to be checked for an encounter.
	 * @param y the y coordinate of the grid location to be checked for an encounter.
	 * @return The sum total of conflicts in CritterWorld.
	 */
	private static int checkEncounter(int x, int y){
		int conflicts = 0;
		for(Critter c : population){
			if((c.x_coord == x) && (c.y_coord == y)){
				conflicts++;
			}
		}
		return conflicts;
	}
	
	/**
	 * This method determines if a Critter is in an encounter in a given location in the CritterWorld.
	 * @param x the x coordinate of the grid location in which the encounter is taking place.
	 * @param y the y coordinate of the grid location in which the encounter is taking place.
	 * @param list the list of Critters for which an encounter is taking place.
	 * @return The Critter suffering an encounter with another Critter.
	 */
	private static Critter encounter(int x, int y, List<Critter> list) {
		for (Critter c : population) {
			if ((checkX(c) == x) && (checkY(c) == y) && !(list.contains(c))) {
				return c;
			}
		}	
		return null;
	}
	
	/**
	 * Generates Algae randomly around the map, happens for each worldTimeStep.  This guarantees that a food supply exists for the population.
	 * Requires a Critter exception to function but cannot actually cause an error because it is an automated input rather than a console or
	 * text based input.
	 */
	private static void genAlgae() {
	    for (int i = 0; i < Params.refresh_algae_count; i++) {
	        try {
                makeCritter("Algae");
            } catch (InvalidCritterException e) {
            	
            }
	    }
	}
	
	/**
	 * This method clears the CritterWorld 2 dimensional array and then re-populates it with only living critters, and  
	 * then finally prints the entire array in its most current state.
	 */
	public static void displayWorld() {
		CritterWorld.clear();
		for(Critter c : population){ 
			CritterWorld.addCritter(c.x_coord, c.y_coord, c);
		}
		CritterWorld.printWorld();
	}
}
