package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Sabin Jacob
 * skj238
 * 15455
 * Slip days used: <0>
 * Spring 2018
 */

import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;


/*
 * Usage: java <assignment4>. Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    // private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.  Builds parser
     * @param args Arguments can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        run();
        
        /* Write your code above */
        System.out.flush();

    }
    
    private static void run() {
        boolean flag = false;
        while (!flag){
            System.out.print("critters>");
            String command = kb.nextLine(); // get full line
            String[] tokens = command.split("\\s+");   // split into array of tokens, delete whitespace
            //System.out.println(Arrays.toString(tokens));    // debugging
            
            boolean invalidCommand = false;
            if (tokens.length == 0) {
                invalidCommand = true;
            } else {
            		try {
            			switch(tokens[0]) {
            				case "quit":
            					tokensGreater(tokens, 1);
            					flag = true;
            					break;
            				case "show":
            					tokensGreater(tokens, 1);
            					Critter.displayWorld();
            					break;
            				case "step":
            					tokensGreater(tokens, 2);
            					processStep(tokens);
            					break;
            				case "seed":
            					tokensEqual(tokens, 2);
            					Critter.setSeed(getInt(tokens[1]));
            					break;
            				case "make":
            					tokensGreater(tokens, 3);
            					processMake(tokens);
            					break;
            				case "stats":
            					tokensEqual(tokens, 2);
            					processStats(tokens);              
            					break;
            				default:
            					invalidCommand = true;
            					break;
            			}
            		} catch (Exception e) { // If there's a parse error, display 'error processing'
            			
            			System.out.println("error processing: " + command);
            		}
            }
      
        if (invalidCommand) {
            System.out.println("invalid command: " + command);
            invalidCommand = false;
        }

        } // end of while loop
        
        return;
    }
    
    public static void tokensGreater(String[] tokens, int length) throws Exception {
      if (tokens.length > length) { 
    	  	throw new Exception("Invalid command length"); 
      }
    }
    
    public static void tokensEqual(String[] tokens, int length) throws Exception {
        if (tokens.length != length) { 
      	  	throw new Exception("Invalid command length"); 
        }
      }
    
    /**
     * Given a token, returns the parsed integer of the token. 
     * @param token is the string to be parsed to an integer
     * @return integer version of token
     * @throws NumberFormatException if the parse cannot be completed.
     */
    private static int getInt(String token) {
        return Integer.parseInt(token);
    }
    
    /**
     * Takes the input tokens from Main and processes the command for make.
     * Call only when the command is indeed make and has the proper number of tokens.
     * @param tokens a string array split from a single array taken from the console input or a text file input.
     * @throws InvalidCritterException
     */
    public static void processMake(String[] tokens) throws Exception {
        String class_name = tokens[1];
        int count = 1;
        if (tokens.length == 3) {
            count = getInt(tokens[2]);
            if (count < 0) { throw new Exception("Can only make a positive number of Critters"); }
        }
        for (int i = 0; i < count; i++) {
            Critter.makeCritter(class_name);
        }
    }
    
    /**
     * Processes the step command.  Can handle a number of steps equal to the parsed integer given by the input, or just processes a single step.
     * @param tokens Taken from the console input or a text file input.
     * @throws Exception if the latter input is incorrectly formatted
     */
    public static void processStep(String[] tokens) throws Exception {  
    		int steps = 1;
        if (tokens.length == 2) {
        		steps = Integer.parseInt(tokens[1]);
            if (steps < 0) { 
            		throw new Exception("Steps must be positive."); 
            }
        }
        if (steps > 0) {
            for (int i = 0; i < steps; i++) {
                Critter.worldTimeStep();
            }
        }
    }
    
    /**
     * Helper function for processing the stats command.
     * @param tokens Taken from the console input or a text file input.
     * @throws Exception if the latter input is incorrectly formatted
     */
    public static void processStats(String[] tokens) throws Exception {
        List<Critter> list = Critter.getInstances(tokens[1]);
        String className = myPackage+"."+tokens[1];
        Class<?> c = null;
        try{
            c = Class.forName(className);
        }
        catch(ClassNotFoundException ce){
            throw new InvalidCritterException(tokens[1]);
        }
        Method m = c.getMethod("runStats", List.class);
        m.invoke(null, list);
    }


}
