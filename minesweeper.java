package minesweeper;

import java.util.Scanner;
/**
 * This program is a copy of the game MineSweeper.
 * @author Tucker Kent
 */
public class minesweeper {
  /**
   * The minesweeper class contains only the main method which includes a loop to replay the game
   * @param args 
   */
  public static void main(String[] args) {
        //Display welcome statement
        System.out.println("Welcome to my version of MineSweeper");
        System.out.println("Programmed by: Tucker Kent");
        System.out.println();
        
        //creating scanner object and declaring choice variable for while loop
        Scanner sc = new Scanner(System.in);
        String choice = "y";
        
        //begin while loop that controls whether game is played or exited after win/loss
        while (choice.equalsIgnoreCase("y")){
            
            game newgame = new game(); //creating new game object
        
            game.start(); //calling start method from game class
            
            //printing dialogue to get new choice var -- new game or quit
            System.out.println("Would you like to play again?");
            System.out.println("Enter Y to continue or enter any other key to exit:\n"); 
            choice = sc.next();
            
            //resetting playCounter
            game.setPlayCounter(0);
        }
        //exit message
        System.out.println("\nThanks for playing!");
        
    }
    
}
