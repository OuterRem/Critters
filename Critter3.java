package assignment4;
/* CRITTERS Critter3.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */

/**
 * Critter3 or "Rick"
 * @author Sabin Jacob
 * Critter3 moves at high speeds based on a six sided dice roll without significant energy loss due to movement.
 */
public class Critter3 extends Critter {
	@Override
	public String toString() { return "R"; }
	private int dir;
	private int diceResult;
	private int distanceTravelled;
	final int NUM_SIDES = 6;
	
	/**
	 * Basic constructor for Critter3, includes randomized direction.
	 */
	public Critter3() {
		this.dir = Critter.getRandomInt(8);
		this.diceResult = 0;
		this.distanceTravelled = 0;
	}
	
	/**
	 * This method uses the getRandomInt method in the super class Critter to roll a die of NUM_SIDES sides returns that result.
	 * Note that the roll is not 0 inclusive but instead is corrected by an addition of 1.
	 * @return A randomized number between 1 and 6.
	 */
	public int rollDice( ) {
		return (Critter.getRandomInt(this.NUM_SIDES) + 1);
	}
	
	/**
	 * This method overwrites the basic doTimeStep method and includes a randomized walking direction as well as reproduction and includes
	 * Critter3's (Rick's) dice roll based movement speed. Rick takes reduced movement energy penalties for moving at these speeds.
	 * Note that Rick's direction does not change with each walk command, meaning that it is entirely possible for him to crash into an enemy.
	 * The total distance travelled by every Rick is recorded for statistical use.
	 */
	@Override
	public void doTimeStep() {
		this.diceResult = rollDice();
		for (int i = 0; i < this.diceResult; i++) {
			walk(dir);
			this.distanceTravelled++;
		}
		if (this.diceResult > 0) { // Negative or zero dice rolles are theoretically impossible.
			int e = (this.getEnergy() + (this.diceResult - 1));
			this.setEnergy(e);
			// Extraneous energy used by high speed movement is returned because Rick does not need extra energy to move at randomized speeds.
		}
		if (getEnergy() > 120) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * Critter2 (Rick) tries to avoid fights and is likely to lose but cannot control his rate of travel and is easily capable of crashing into
	 * an enemy.
	 */
	@Override
	public boolean fight(String opponent) {
		return false;
	}

	/**
	 * This method overrides the combat potential method of the base critter class 
	 * and uses a random value between 0 and the Critter2's energy divided in half.
	 * Rick is also a poor combatant when actually cornered into a fight.
	 * @param c The combatant creature.
	 * @param o The opponent creature.
	 * @return The integer value used as a combat capability estimate, decided at random.
	 */
	protected static int getCombatPotential(Critter1 c, Critter o) {
		boolean BattleC = c.fight(o.toString());
		if ((BattleC) && (c.getEnergy() > 0)) {
			return (getRandomInt(c.getEnergy()) / 2);
		}
		return 0;
	}
	
	/**
	 * This method overrides the runStats method within the Critter superclass and prints out data on the list of Critter3's (ricks)
	 * regarding their total population size, the total distance all Ricks have travelled and the average distance travelled by each Rick.
	 * @param ricks The List of Critters of type Critter3 (Rick) to be summed and analyzed.
	 */
	public static void runStats(java.util.List<Critter> ricks) {
		int total_distanceTravelled = 0;
		int average_distanceTravelled = 0;
		for (Object obj : ricks) {
			Critter3 r = (Critter3) obj;
			total_distanceTravelled += r.distanceTravelled;
		}
		average_distanceTravelled = (total_distanceTravelled / ricks.size());
		System.out.print("" + ricks.size() + " total Ricks, ");
		System.out.print("" + total_distanceTravelled + " (Total Distance Travelled by all Ricks), ");
		System.out.print("" + average_distanceTravelled + " (Average Distance Travelled by each Rick)");
		System.out.println();
	}
}

