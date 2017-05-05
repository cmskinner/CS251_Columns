/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class is the JPanel that creates the grid in the game.
 */
public class BlockPanel extends JPanel{

    private boolean DB = false;

    protected static final int CELL_SIZE = 30;
    protected static final Color DEFAULT_BACKGROUND_COLOUR = Color.BLACK;

    protected final int numRows;
    protected final int numCols;
    protected final int cellSize;
    protected final Color backgroundColour;
    protected final int hiddenRows;

    private int removedTiles = 0;

    protected Block[][] grid;

    /**
     * Constructor that initializes all of the params to the member
     * variables, sets the backgroundcolor, sets the preferred size for the
     * panel.
     * @param numRows
     * @param numCols
     * @param cellSize
     * @param backgroundColour
     * @param hiddenRows
     */
    protected BlockPanel(int numRows, int numCols, int cellSize, Color
                         backgroundColour, int hiddenRows) {
        this.numCols = numCols;
        this.numRows = numRows;
        this.cellSize = cellSize;
        this.backgroundColour = backgroundColour;
        this.hiddenRows = hiddenRows;
        grid = new Block[numRows][numCols];
        setBackground(backgroundColour);
        setPreferredSize(new Dimension(numCols*cellSize,
                (numRows-hiddenRows)*cellSize));
        System.out.println("numROWS: " + numRows + " numCols: " + numCols);
    }

    /**
     * Constructor with a 0 hidden rows.
     * @param numRows
     * @param numCols
     * @param cellSize
     * @param backgroundColor
     */
    public BlockPanel(int numRows, int numCols, int cellSize, Color backgroundColor) {
        this(numRows, numCols, cellSize, backgroundColor, 0);
    }

    /**
     * Constructor with default background colour and cell size.
     * @param numRows
     * @param numCols
     */
    public BlockPanel(int numRows, int numCols) {
        this(numRows, numCols, CELL_SIZE, DEFAULT_BACKGROUND_COLOUR);
    }

    /**
     * Clear method for future implementation of "play again"
     */
    public void clear() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = null;
            }
        }
    }

    /**
     * Places the blocks by looking at the coloumn piece and going row by row
     * and places the object in the grid.
     * @param object2D
     * @param row
     * @param col
     */
    public void placeObject(ColumnPiece2D object2D, int row, int col) {
        ColumnPiece2D.Dimension2D dim = object2D.getDimension();
        for (int i = 0; i < dim.getHeight(); ++i) {
            for (int j = 0; j < dim.getWidth(); ++j) {
                Block b = object2D.getBlockAt(i, j);
                if (b != null) {
                    grid[row + i][col + j] = b;
//                    System.out.println("BlockPanel Placed");
                }
            }
        }
    }


    /**
     * Block Painter.
     * @param g
     * @param b
     * @param row
     * @param col
     */
    protected void paintBlock(Graphics g, Block b, int row, int col) {
        if(row >= hiddenRows) {
            b.paint(g, col*cellSize, (row-hiddenRows)*cellSize, cellSize);
        }
    }

    /**
     * Custom paint component
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = hiddenRows; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                if(grid[i][j] != null) {
                    paintBlock(g, grid[i][j], i, j);
                }
            }
        }
    }
}













