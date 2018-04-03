package assignment4;
/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */

/**
 * Critter2 or "Sal"
 * @author Sabin Jacob
 * Critter2 is highly energy efficient because it carries around a reserve tank that slowly increases over time.
 */
public class Critter2 extends Critter {
	@Override
	public String toString() { return "S"; }
	final int MAX_RESERVES = 10;
	private int reserves;
	private int dir;
	
	/**
	 * Basic constructor for Critter2, includes randomized direction and an empty energy reserve.
	 */
	public Critter2() {
		dir = Critter.getRandomInt(8);
		this.reserves = (MAX_RESERVES / 2);
	}
	
	/**
	 * This method overwrites the basic doTimeStep method and includes a randomized walking direction as well as reproduction and includes
	 * Critter2's (Sal's) randomly increasing energy reserves.  Sal's reserves will be triggered to refill his current energy amount when it
	 * hits the MAX_RESERVES value, at which point his reserves themselves are emptied.
	 */
	@Override
	public void doTimeStep() {
		walk(dir);
		if (this.reserves < MAX_RESERVES) {
			if ((Critter.getRandomInt(8) + 1) > 4) {
				reserves++;
			}
		} else {
			int e = (this.getEnergy() + (this.reserves));
			this.setEnergy(e);
			this.reserves = 0;
		}
		if (getEnergy() > 120) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
	}
	
	/**
	 * Critter2 (Sal) tries to avoid fights at all cost and is very likely to lose.
	 */
	@Override
	public boolean fight(String opponent) {
		return false;
	}

	/**
	 * This method overrides the combat potential method of the base critter class 
	 * and uses a random value between 0 and the Critter2's energy divided in half.
	 * Sal is an incredibly poor combatant when actually cornered into a fight.
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
	 * This method overrides the runStats method within the Critter superclass and prints out data on the list of Critter2's (sals)
	 * regarding their total population size, the sum of their reserves and the average amount held in reserve by each Sal.
	 * @param sals The List of Critters of type Critter2 (Sal) to be summed and analyzed.
	 */
	public static void runStats(java.util.List<Critter> sals) {
		int total_reserves = 0;
		int average_reserves = 0;
		for (Object obj : sals) {
			Critter2 s = (Critter2) obj;
			total_reserves += s.reserves;
		}
		average_reserves = (total_reserves / sals.size());
		System.out.print("" + sals.size() + " total Sals, ");
		System.out.print("" + total_reserves + " (Total Energy in Reserve), ");
		System.out.print("" + average_reserves + " (Average Energy in Reserve)");
		System.out.println();
	}
}
