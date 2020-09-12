package minesweeper;

import java.util.Scanner;
/**
 * The game class provides the methods that control the game mechanics.
 * The game class includes all methods called by minesweeper class.
 * @author tucke
 */
public class game {
   
    protected static int playCounter;
    
    //no constructor created for game object -- will use default
    
    /**
     * Start method creates grid object and initializes game loop
     */
    public static void start(){
        //creating scanner object and declaring variables
        Scanner sc = new Scanner(System.in);
        int height;
        int width;
        
        //gathering height/width variables
        height = getInt(sc, "Enter number of rows: ");
        width = getInt(sc, "Enter number of columns: ");
                
        //creating new grid object with height and width parameters
        grid g = new grid(height, width);
        
        //calling gameloop method
        gameLoop();
    }
    
    /**
     * Game loop method controls the flow of the game and calls methods required to advance game
     * as well as win and lose game.
     */
    public static void gameLoop(){
        //creating scanner object, declaring variables, and using getter method to retrieve 3d string array
        Scanner sc = new Scanner(System.in);
        String[][][] gridArray = grid.getGridArray();
        gridArray = grid.readyGrid(gridArray);
        int row;
        int col;
        boolean mineHit = false;
        boolean winGame = false;
        
        //while loop to controlthe game loop method
        while (mineHit == false && winGame == false){
            System.out.println();
            System.out.println(grid.displayGrid(gridArray)); //displaying game grid
            System.out.println("Enter a grid location:");
            System.out.println("First enter the row, press enter, then enter the column:");
            row = (checkRange(sc) - 1); // subtracting 1 to keep these -
            col = (checkRange(sc) - 1); // variables in bounds of the array
            boolean b = checkLocation(row, col); //calling method to check if coordinates have a mine
            if (b == true){ //in case of mine hit
                loseGame(); //method to display losing message
                mineHit = true; //boolean to end current while loop
            } else {
                clearBlanks(row, col); //if no mine is hit clearBlanks updates the grid
            }
            if (checkWin(gridArray) == true){ //checking win conditions
                winGame(); //if game is won, prints winning message
                winGame = true; // boolean to end current while loop
            }
            //updating play counter to display how many moves player made during this game
            playCounter ++;
        }
    }
    
    /**
     * Checks the number of mines adjacent to the grid coordinate called by user
     * @param row
     * @param col
     * @return integer representing number of adjacent mines to a cell
     */
    public static int numberOfMines(int row, int col) {
        int mineNum = 0; //mineNum variable initialized to 0 - will store nested methods return value
        int maxRow = (grid.getHeight() - 1);//set these variables so they 
        int maxCol = (grid.getWidth() - 1); //change based on grid size
        if (row == 0 && col == 0){
            mineNum = checkAdj00(row, col);
        } else if (row == 0 && col == maxCol){
            mineNum = checkAdj09(row, col);
        } else if (row == maxRow && col == 0){
            mineNum = checkAdj90(row, col);
        } else if (row == maxRow && col == maxCol){ //if esle block to decide which method to call based
            mineNum = checkAdj99(row, col);         //on position of grid coordinates passed to numberOfMines method
        } else if (row == 0){                       //    --method descriptions with methods themselves--
            mineNum = checkAdjRow0(row, col);
        } else if (row == maxRow){
            mineNum = checkAdjRow9(row, col);
        } else if (col == 0){
            mineNum = checkAdjCol0(row, col);
        } else if (col == maxCol){
            mineNum = checkAdjCol9(row, col);
        } else {
            mineNum = checkAdjCells(row, col);
        }
        return mineNum; //passes mineNum integer to clearBlanks()
    }
    
    /**
     * Checks if the coordinates passed to it contain a mine
     * @param row
     * @param col
     * @return boolean true or false
     */
    public static boolean checkLocation(int row, int col) {
        String[][][] gridArray = grid.getGridArray(); //retrieving gridArray
        if (gridArray[1][row][col].equalsIgnoreCase("x")){ //mine spaces in grid contain "x"
            return true; //grid coordinates contain mine
        } else {
            return false; //grid coords o not contain mine
        }
    }
    
    /**
     * Clears the grid spaces and places number of adjacent mines into grid
     * @param row
     * @param col 
     */
    public static void clearBlanks(int row, int col){
        String[][][] gridArray = grid.getGridArray(); // retrieving gridArray object
        int rowVar = row;
        int colVar = col;                       
        int maxRow = (grid.getHeight() - 1); //variables declared and initialized
        int maxCol = (grid.getWidth() - 1);
        int adjMine; // var to store return value of numberOfMines
        while (checkLocation(rowVar, colVar) == false){ // this loop clears grid coords in negative direction
            adjMine = numberOfMines(rowVar, colVar);
            if (adjMine == 0){
                gridArray[0][rowVar][colVar] = " "; //adjacent mines are rewritten as a blank                        
            } else {
                gridArray[0][rowVar][colVar] = Integer.toString(adjMine); // if adjacent coords contain mines, int is converted to string and stored
            }
            colVar --; // postfix decrement to move the column coordinate
            if (rowVar > 0 && colVar < 0){ // if else block to determine if the coordinates are still on the grid
                    rowVar --;       //decrementing row number
                    colVar = maxCol; //resetting column number to max for new row
                } else if (rowVar == 0 && colVar < 0){
                break; //if row is zero, breaks out of while loop to keep array in bounds
                }
        }
        //resetting row and column values to what they were passed into method as
        rowVar = row; 
        colVar = col;
        while (checkLocation(rowVar, colVar) == false){ // this loop moves in a positive direction - same as previous except
            adjMine = numberOfMines(rowVar, colVar);    // increments rather than decrements
            if (adjMine == 0){
                gridArray[0][rowVar][colVar] = " ";
            } else {
                gridArray[0][rowVar][colVar] = Integer.toString(adjMine);
            }          
            colVar ++;
            if(rowVar < maxRow && colVar > maxCol){
                rowVar++;
                colVar = 0;
            }else if(rowVar == maxRow && colVar > maxCol){ 
                break; // breaks out of loop if row and column are maxed -- to keep array in bounds
            }
        }
    }
    
    /**
     * Checks if the player wins the current game
     * @param gridArray
     * @return boolean value
     */
    public static boolean checkWin(String[][][] gridArray){
       gridArray = grid.getGridArray(); //retrieving grid object
       int mineCount = grid.getMineCount(); //retrieving total number of mines
       int astCount = 0; //declared astCount to compare to minecount 
       for(int i = 0; i < gridArray[0].length; i++){
           for(int j = 0; j < gridArray[0][i].length; j++){ //nested for loops to check how many asterisks the grid contains
                if (gridArray[0][i][j].equals("*")){
                    astCount += 1;
                }
           }
       }
       if (mineCount == astCount){
           return true; // if player has won
       } else {
           return false; // if player has not yet won
       }
    }
    
    /**
     * If user wins the game - Displays win message and number of moves
     */
    public static void winGame(){
        String[][][] gridArray = grid.getGridArray();
        System.out.println(grid.displayGridData(gridArray));
        System.out.println("Great job! You found all " + grid.getMineCount() + " mines!");
        System.out.println("You won in " + getPlayCounter() + " moves!\n");
    }
    
    /**
     * If user hits a mine -- Displays losing message
     */
    public static void loseGame(){
        String[][][] gridArray = grid.getGridArray();
        System.out.println(grid.displayGridData(gridArray));
        System.out.println("You hit a mine! Game Over!\n");
    }
    
    /**
     * Checks for blanks if the grid coords are 0,0 -- top left corner
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdj00(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if  coords are 0,maxColumn - top right corner
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdj09(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if  coords are maxRow, 0 -- bottom left corner
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdj90(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if  coords are maxRow,maxCol -- bottom right corner
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdj99(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if  coords are in row 0 -- top row
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdjRow0(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if coords are in Max row -- bottom row
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdjRow9(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;       
    }
    
    /**
     * Checking for blanks if coords are in column zero - left side
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdjCol0(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;        
    }
    
    /**
     * Checking for blanks if coords are in maxCol -- right side
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdjCol9(int row, int col) {
        int mineNum = 0;
        boolean b;
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Checking for blanks if in a central grid coord -- checks all around the space
     * @param row
     * @param col
     * @return integer representing number of adjacent mines
     */
    public static int checkAdjCells(int row, int col){
        int mineNum = 0;
        boolean b;
        b = checkLocation(row - 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row - 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row, col + 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col - 1);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col);
        if (b == true){
            mineNum += 1;
        }
        b = checkLocation(row + 1, col + 1);
        if (b == true){
            mineNum += 1;
        }
        return mineNum;
    }
    
    /**
     * Returns the number of plays incremented by gameLoop()
     * @return integer
     */
    public static int getPlayCounter(){
        return playCounter;
    }
    
    /**
     * Setter method to alter playcounter for accuracy in the game loop
     * @param newCount 
     */
    public static void setPlayCounter(int newCount){
        playCounter = newCount;
    }
    
    /**
     * Validates user entry to only include integers -- overloaded method
     * @param sc
     * @param prompt
     * @return 
     */
    public static int getInt(Scanner sc, String prompt){
        int input = 0;
        boolean isValid = false;
        while (isValid == false){
            System.out.println(prompt);
            if (sc.hasNextInt()){
                input = sc.nextInt();
                isValid = true;
            } else {
                System.out.println("Incorrect entry. Please enter a whole number.");
            }
            sc.nextLine();
        } 
        return input;
    }
    
     /**
     * Validates user entry to only include integers -- overloaded method
     * @param sc
     * @return 
     */
    public static int getInt(Scanner sc){
        int input = 0;
        boolean isValid = false;
        while (isValid == false){
            if (sc.hasNextInt()){
                input = sc.nextInt();
                isValid = true;
            } else {
                System.out.println("Incorrect entry. Please enter a whole number.");
            }
            sc.nextLine();
        } 
        return input;
    }
    
    /**
     * Checks the range of the user entry to ensure it is greater than zero and less than limit established by grid.getLimit() -- overloaded method()
     * @param sc
     * @param prompt
     * @return 
     */
    public static int checkRange(Scanner sc, String prompt){
        int input = getInt(sc, prompt);
        int maxInput = grid.getLimit();
        boolean isValid = false;
        while (isValid == false){
            if (input > 0 && input <= maxInput){
                isValid = true;
            } else {
                System.out.println("Incorrect entry. Number must be between 1 and " + maxInput);
                input = getInt(sc, prompt);
            }
        }
        return input;
    }
    
    /**
     * Checks the range of the user entry to ensure it is greater than zero and less than limit established by grid.getLimit() -- overloaded method()
     * @param sc
     * @return 
     */
    public static int checkRange(Scanner sc){
        int input = getInt(sc);
        int maxInput = grid.getLimit();
        boolean isValid = false;
        while (isValid == false){
            if (input > 0 && input <= maxInput){
                isValid = true;
            } else {
                System.out.println("Incorrect entry. Number must be between 1 and " + maxInput);
                input = getInt(sc);
            }
        }
        return input;
    }
}
   