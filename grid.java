package minesweeper;

import java.lang.Math;
/**
 * Grid class contains the constructor and all of the necessary methods to fill and manipulate the grid array itself
 * 
 */
public class grid {
    //declaring static fields
    public static int height;
    public static int width;
    public static String[][][] gridArray;
    public static final int MINECOUNT = 10;
    
    /**
     * Constructor takes two <code>integer</code> parameters and builds the "grid" array with them.
     * @param height
     * @param width 
     */
    //grid class constructor with height and width params - also creates the 3Dimensional array used for the display and data grids
    public grid(int height, int width) {
        this.height = height;
        this.width = width; 
        gridArray = new String[2][this.height][this.width];
    }
    
    /**
     * Getter method for height field
     * @return <code>int</code> height 
     */
    public static int getHeight() { 
        return height;
    }
    
    /**
     * Getter method for width field
     * @return <code>int</code> width
     */
    public static int getWidth() {
        return width;
    }
    
    /**
     * Getter method for the grid array
     * @return 3 dimensional array
     */
    public static String[][][] getGridArray() {
        return gridArray;
    }
    
    /**
     * Getter method for number of mines
     * @return static final integer
     */
    public static int getMineCount() {
        return MINECOUNT;
    }
    
    /**
     * Method to remove null values and fill the grid array with mines/asterisks
     * @param gridArray
     * @return 3 dimensional array
     */
    public static String[][][] readyGrid(String[][][] gridArray){
        removeNull(gridArray);
        randomFillGrid(gridArray, getHeight(), getWidth());
        
        return gridArray;
    }
    
    /**
     * Method fills the grid array with mines 
     * @param gridArray
     * @param height
     * @param width
     * @return 3d grid array
     */
    public static String[][][] randomFillGrid(String[][][] gridArray, int height, int width) {
        int mineLimit = getLimit(); //variable declared to store correct number to multiply randomNumber by
        
        for (int i = 0; i < MINECOUNT; i++) //iterating for number of mines
        {
            int num1 = randomNumber(mineLimit); //selecting array positions for mine
            int num2 = randomNumber(mineLimit);
            if(gridArray[1][num1][num2].equalsIgnoreCase("x")){ // if else block to ensure mine positions arent repeated
                i--;                                            // decrements if mine is a duplicate
            } else {
                gridArray[1][num1][num2] = "x";
            }
        }
        
        return gridArray; //returns grid array with mines
    }
    
    /**
     * Method to return a random number within array bounds
     * @param limit
     * @return integer between 0 and "mineLimit"
     */
    public static int randomNumber(int limit){
        int mineLimit = limit; //receives limit based on width and height of grid object
        int randomNum = (int)(Math.random() * mineLimit);
        return randomNum;
    }
    
    /**
     *  Removes null values from the grid array and replaces them with asterisks to ready grid display -- 
        This method places asterisks on both [0] and [1] so it fills both arrays with asterisks
     * @param gridArray
     * @return 3d array
     */
    public static String[][][] removeNull(String[][][] gridArray) {
        for (int i = 0; i < gridArray.length; i++){
            for (int j = 0; j < gridArray[i].length; j++){
                for (int k = 0; k < gridArray[i][j].length; k++) {
                    if(gridArray[i][j][k] == null){ //if array position contains mine, it skips and leaves the mine in place
                    gridArray[i][j][k] = "*";
                }
                }
            }
        }
        return gridArray;
    }
    
    /**
     * Builds and returns string value of the formatted grid with mine positions included
     * @param gridArray
     * @return 3d array
     */
    public static String displayGridData(String[][][] gridArray){
        String gridHeader = "     ";
        String gridRow = " ";
        for (int i = 0; i < gridArray[1].length; i++){
            if(i < 9){
                gridHeader += (i + 1) + "  ";
            } else {
                gridHeader += (i + 1) + " ";
            }
        }        
        for(int i = 0; i < gridArray[1].length; i++){
            if(i <= 8){
                gridRow += " " + (i + 1) + "  ";
            } else {
                gridRow += (i + 1) + "  ";
            }
            for (int j = 0; j < gridArray[1][i].length; j++){
                gridRow += gridArray[1][i][j] + "  ";
            }
            gridRow += "\n" + " ";
        }
        String gridComplete = gridHeader + "\n" + gridRow;
        return gridComplete;
    }
    
    /**
     * Builds and returns string value of the players game grid -- DOES NOT SHOW MINE POSITIONS
     * @param gridArray
     * @return 3d array
     */
    public static String displayGrid(String[][][] gridArray){
        String gridHeader = "     ";
        String gridRow = " ";
        for (int i = 0; i < gridArray[0].length; i++){
            if(i < 9){
                gridHeader += (i + 1) + "  ";
            } else {
                gridHeader += (i + 1) + " ";
            }
        }        
        for(int i = 0; i < gridArray[0].length; i++){
            if(i <= 8){
                gridRow += " " + (i + 1) + "  ";
            } else {
                gridRow += (i + 1) + "  ";
            }
            for (int j = 0; j < gridArray[0][i].length; j++){
                   gridRow += gridArray[0][i][j] + "  ";
            }
            gridRow += "\n" + " ";
        }
        String gridComplete = gridHeader + "\n" + gridRow;
        return gridComplete;
    } 
    
    /**
     * Checks height and width values provided by user to determine the maximum number
     * array position -- this limit helps keep array in bounds
     * @return integer mineLimit
     */
    public static int getLimit() {
        height = getHeight();
        width = getWidth();
        int mineLimit;
        if(height > width){
            mineLimit = width;
        } else if(height < width){ //if else block to determine correct random number to keep array in bounds
            mineLimit = height;
        } else {
            mineLimit = height;
        }
        return mineLimit;
    }

    
}
