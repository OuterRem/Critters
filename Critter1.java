package assignment4;
/* CRITTERS Critter1.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */

/**
 * Critter1 or "Bob"
 * @author Sabin Jacob
 * Critter1 gains more energy from fights than a regular critter and fights more willingly.
 */
public class Critter1 extends Critter {
	@Override
	public String toString() { return "B"; }
	
	private int dir;
	private int rage;
	final int MAX_RAGE = 150;
	final int STARTING_RAGE = 50;
	final int MIN_RAGE = 25;
	
	/**
	 * Basic constructor for Critter1, includes randomized direction and initial rage counter.
	 */
	public Critter1() {
		dir = Critter.getRandomInt(8);
		rage = STARTING_RAGE;
	}
	
	/**
	 * This method overwrites the basic doTimeStep method and includes a randomized walking direction as well as reproduction and includes
	 * Critter1's (Bob's) scaling rage progression.  Bob's energy will hit zero if his rage reaches it's maximum threshold, however, until
	 * that moment, Bob's combat ability sky rockets.
	 */
	@Override
	public void doTimeStep() {
		walk(dir);
		rage++;
		if (rage < MIN_RAGE) {
			rage = MIN_RAGE;
		}
		if (getEnergy() > 120) {
			Critter1 child = new Critter1();
			child.rage = (this.rage / 2);
			this.rage = (this.rage / 2);
			reproduce(child, Critter.getRandomInt(8));
		}
		if (rage > MAX_RAGE) {
			this.setEnergy(0);
		}
	}
	
	/**
	 * Critter1 (Bob) is always willing to fight, and very likely to win.
	 */
	@Override
	public boolean fight(String opponent) {
		return true;
	}

	/**
	 * This method overrides the combat potential method of the base critter class 
	 * and uses a random value between 0 and the Critter1's energy plus Critter1's current rage level.  As Bob's rage
	 * progresses, his combat ability spikes.
	 * @param c The combatant creature.
	 * @param o The opponent creature.
	 * @return The integer value used as a combat capability estimate, decided at random.
	 */
	protected static int getCombatPotential(Critter1 c, Critter o) {
		boolean BattleC = c.fight(o.toString());
		if ((BattleC) && (c.getEnergy() > 0)) {
			return (getRandomInt(c.getEnergy()) + (c.rage / 10));
		}
		return 0;
	}
	
	/**
	 * This method overrides the runStats method within the Critter superclass and prints out data on the list of Critter1's (bobs)
	 * regarding their total population size, the sum of their rage and the average rage per Bob.
	 * @param bobs The List of Critters of type Critter1 (Bob) to be summed and analyzed.
	 */
	public static void runStats(java.util.List<Critter> bobs) {
		int total_rage = 0;
		int average_rage = 0;
		for (Object obj : bobs) {
			Critter1 b = (Critter1) obj;
			total_rage += b.rage;
		}
		average_rage = (total_rage / bobs.size());
		System.out.print("" + bobs.size() + " total Bobs, ");
		System.out.print("" + total_rage + " (Total Rage), ");
		System.out.print("" + average_rage + " (Average Rage)");
		System.out.println();
	}
}
