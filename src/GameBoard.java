/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class is the bread and button essentially. It extends what makes the
 * grid and places the blocks and then makes it so the blocks move, can be
 * moved, rotated, and handles all of the scoring and checking to see if
 * there is matches.
 *
 * This was adapted from Brooke Chenoweth's GameBoard from Tetris
 */
public class GameBoard extends BlockPanel implements ActionListener{

    private boolean DB = false;

    /**
     * This class handles the ScoreEvent
     */
    public static class ScoreEvent {
        public final int score;
        public final int lines;
        public final int level;
        public final boolean isGameOver;
        public ScoreEvent(int score, int lines, int level, boolean isGameOver) {
            this.score = score;
            this.lines = lines;
            this.level = level;
            this.isGameOver = isGameOver;
        }
    }

    public static interface ScoreListener {
        void updateScore(ScoreEvent ev);
    }

    /**
     * ShapeEvent
     */
    public static class ShapeEvent {
        private final Object2D next;
        private final Object2D current;

        public ShapeEvent(Object2D next, Object2D current) {
            this.next = next;
            this.current = current;
        }
        Object2D getNextShape() { return next; }
        Object2D getCurrentShape() { return current; }
    }

    public static interface ShapeListener {
        void updateShape(ShapeEvent ev);
    }

    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int HIDDEN = 2;
    private static final int MATCHES_PER_LEVEL = 10;

    private boolean isPlaying = false;
    private int score;
    private int lines;
    private int level;
    private boolean gameOver = false;

    private ColumnPiece2D currentShape;
    private ColumnPiece2D nextShape;
    private int row;
    private int col;

    private int boardRemoval[][];
    private BlockManager bm;

    private final Color[] colorArray = {Color.RED, Color.BLUE, Color.YELLOW,
            Color.MAGENTA};

    private Set<ScoreListener> scoreListeners = new HashSet<ScoreListener>();
//    private Set<ShapeListener> shapeListeners = new HashSet<ShapeListener>();

    private Random rnd = new Random();

    /**
     * Constructor to handle the action listeners and initalize the
     * boardRemoval and the BlockManagers
     * @param numRows
     * @param numCols
     * @param cellSize
     * @param backgroundColour
     * @param hidden
     */
    public GameBoard(int numRows, int numCols, int cellSize, Color
            backgroundColour, int hidden) {
        super(numRows, numCols, cellSize, backgroundColour, hidden);
        bm = new BlockManager(numRows, numCols);
        this.boardRemoval = new int[numRows][numCols];
//        this.rnd = rnd;
//        rnd.setSeed(1);
        System.out.println("Starting the Gameboard...");
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isPlaying) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            moveCurrentDown(false);

                            break;
                        case KeyEvent.VK_LEFT:
                            moveCurrentLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            moveCurrentRight();
                            break;
                        case KeyEvent.VK_UP:
                            rotateCurrent();
                            break;
                        case KeyEvent.VK_S:
                            moveCurrentDown(false);
                            break;
                        case KeyEvent.VK_A:
                            moveCurrentLeft();
                            break;
                        case KeyEvent.VK_D:
                            moveCurrentRight();
                            break;
                        case KeyEvent.VK_W:
                            rotateCurrent();
                            break;
                        case KeyEvent.VK_SPACE:
                            moveCurrentDown(true);
                            requestFocus();
                            break;
                    }
                }
            }
        });
    }

    /**
     * Default GameBoard with default initalization.
     */
    public GameBoard() {
        this(ROWS+HIDDEN, COLS, CELL_SIZE, DEFAULT_BACKGROUND_COLOUR, HIDDEN);
    }

    public void actionPerformed(ActionEvent ev) {
        if(isPlaying) {
            moveCurrentDown(false);
        }
    }

    /**
     * adds the score listener
     * @param listener
     */
    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }

    /**
     * updates the score listener
     */
    private void updateScoreListeners() {
        ScoreEvent event = new ScoreEvent(score, lines, level, gameOver);
        for(ScoreListener listener : scoreListeners) {
            listener.updateScore(event);
        }

    }

    /**
     * This was never needed in the end.
     * @param color
     * @return
     */
    private Block makeBlock(Color color) {
        return new Block(color, Color.WHITE);
    }

    /**
     * Generates the next shape and makes it spawn
     */
    private void chooseNextShape() {

        int shapeWidth = currentShape == null ? 0 : currentShape.getDimension().getWidth();
        row = 0;
        col = (numCols - shapeWidth) / 2;

        currentShape = nextShape == null ? null : nextShape.clone();
        nextShape = bm.newPiece(col);

        if(currentShape != null && isOverlappingBottom()) {
            gameOver = true;
            updateScoreListeners();
        } else {
//            updateShapeListeners();
        }
    }

    /**
     * Are we playing?
     * @param isPlaying
     */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            while(currentShape == null) {
                chooseNextShape();
//                System.out.println("Makes it");
            }
        }
    }


    /**
     * Paint Component for the object.
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(currentShape != null) {
            Object2D.Dimension2D dim = currentShape.getDimension();
            for(int i = 0; i < dim.getHeight(); ++i) {
                for (int j = 0; j < dim.getWidth(); ++j) {
                    Block b = currentShape.getBlockAt(i, j);
                    if(b != null) {
                        paintBlock(g, b, row+i, col+j);
                    }
                }
            }
        }
    }

    //TODO figure out how to get this repaint to work.

    /**
     * Checks to see if the piece is touching the bottom, attempts to
     * repaint, place blocks, and choose the next shape.
     * @return
     */
    private boolean placeIfTouchingBottom() {
        if(isTouchingBottom()) {

            repaint(0);
            revalidate();
            repaint(0);

            placeObject(currentShape, row , col);
//            updateLines();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    repaint(5);
                }
            });

            chooseNextShape();
//            System.out.println("placed and got next shape");
            return true;
        }
        return false;
    }


    /**
     * Is there an overlap?
     * @return
     */
    private boolean isOverlappingBottom() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = dim.getHeight()-1; i >= 0; --i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i,j) != null) {
                    if(row + i + 1 >= numRows ||
                            grid[row + i][col + j] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This essentially is checking to see if there is matches with blocks
     * ever tick (this is how I got the board to repaint as much as it does).
     * It was a bit of a hack around.
     * @param dropPiece
     */
    private void moveCurrentDown(boolean dropPiece) {
        boolean placed = false;
        do {
            row++;
            placed = placeIfTouchingBottom();
            moveDown();
            checkForMatch();
            turnTilesWhite();
            removeTiles();
            moveDown();
        } while(dropPiece && !placed);
        repaint();
    }

    /**
     * Move the current piece left
     */
    private void moveCurrentLeft() {
        if(!isTouchingLeft()) {
            col--;
            placeIfTouchingBottom();
            repaint();
        }
    }

    /**
     * move the current piece right
     */
    private void moveCurrentRight() {
        if(!isTouchingRight()) {
            col++;
            placeIfTouchingBottom();
            repaint();
        }
    }

    /**
     * Rotate the current blocks within the piece using the ColumnPiece2D
     * rotate method.
     */
    private void rotateCurrent() {
        currentShape.rotate();
        adjustPosition();
        placeIfTouchingBottom();
        repaint();
    }

    /**
     * Changes the way the block appears.
     */
    private void adjustPosition() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        if(col + dim.getWidth() > numCols) {
            col = numCols-dim.getWidth();
        }
        if(row + dim.getHeight() > numRows) {
            row = numRows - dim.getHeight();
        }
        while(isOverlappingBottom() && row > 0) {
            row--;
        }
    }

    /**
     * Is it touching the bottom? A simple checker used for other methods.
     * @return true or false
     */
    private boolean isTouchingBottom() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = dim.getHeight()-1; i >= 0; --i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i,j) != null) {
                    if(row + i + 1 >= numRows ||
                            grid[row + i + 1][col + j] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Is it touching the left? Used to make sure the player cannot go off
     * the board
     * @return true or false
     */
    private boolean isTouchingLeft() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = 0; i < dim.getHeight(); ++i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i, j) != null) {
                    if(col + j <= 0 ||
                            grid[row + i][col + j - 1] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Is it touching the right? Used to make sure the player cannot go off
     * the board
     * @return true or false
     *
     */
    private boolean isTouchingRight() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = 0; i < dim.getHeight(); ++i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i, j) != null) {
                    if(col + j + 1 >= numCols ||
                            grid[row + i][col + j + 1] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    TODO not identifying 3 in a row correctly, will sometimes grab an extra
    that is not in the 3 in a row, and sometimes doesn't identify the 3 in
    the row.

    Not identifying in the 3 in a row: bottom left corner.
     */

    /**
     * Checks for the 3+ in a row match. The magic algorithm
     * @return true or false
     */
    public boolean checkForMatch() {
        boolean foundMatch = false;
        for (int i = 0; i < ROWS + HIDDEN; i++) {
            for (int j = 0; j < COLS; j++) {
                boardRemoval[i][j] = 0;
            }
        }

        int directionRow[] = {-1, 0, 1, 1};
        int directionColumn[] = {1, 1, 1, 0};
        int counter = 0;
        Block currentMatchBlock = null;

        for (int direction = 0; direction < 4; direction++) {
//            System.out.println("Direction: " + direction);
            for (int i = HIDDEN; i < ROWS + HIDDEN; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (DB) System.out.println("ROW: " + i + ", COLUMN: " + j);
                    if ((i + directionRow[direction] >= ROWS + HIDDEN) || (j +
                            directionColumn[direction] >= COLS)) {
                        if (DB) System.out.println("1st Continuing.");
                        continue;
                    }
                    if ((i + 2*directionRow[direction] >= ROWS + HIDDEN) || (j +
                            2*directionColumn[direction] >= COLS)) {
                        if (DB) System.out.println("2nd Continuing.");
                        continue;
                    }
//                    System.out.println("Is current Match Block null? " +
//                            (currentMatchBlock == null));
                    if (currentMatchBlock == null) {
//                        System.out.println("HERE");
                        if (grid[i][j] == null) {
                            continue;
                        } else {
                            currentMatchBlock = grid[i][j];
                        }
//                        System.out.println(grid[i][j].toString());
//                        currentMatchBlock = grid[i][j];
//                        System.out.println("Current Match Block: " +
//                                currentMatchBlock.toString());
                    }

//                    System.out.println(currentMatchBlock.toString());
                    if (currentMatchBlock == null) {
//                        System.out.println("Throwing out: " + currentMatchBlock.toString());
                        currentMatchBlock = null;
                        continue;
                    } else {
//                        System.out.println("HERE");
                        if (grid[i + directionRow[direction]][j +
                                directionColumn[direction]] == null) {
                            continue;
                        }
                        if (grid[i + 2*directionRow[direction]][j +
                                2*directionColumn[direction]] == null) {
                            continue;
                        }
                        if (currentMatchBlock.toString().equals(grid[i +
                                directionRow[direction]][j +
                                directionColumn[direction]].toString())) {

                            counter++;
                            if (currentMatchBlock.toString().equals(grid[i +
                                    2*directionRow[direction]][j +
                                    2*directionColumn[direction]].toString())) {
                                if (DB) System.out.println("2nd Counter " +
                                        "Increase\n");
                                counter++;


                            }
                        }
                        if (counter == 2) {
                            System.out.println("Counter = 2");
                            foundMatch = true;
                            boardRemoval[i][j] = 1;
                            boardRemoval[i + directionRow[direction]][j +
                                    directionColumn[direction]] = 1;
                            boardRemoval[i + 2*directionRow[direction]][j +
                                    2*directionColumn[direction]] = 1;
//                            for (int rmI = 0; rmI < rows; rmI++) {
//                                for (int rmJ = 0; rmJ < columns; rmJ++) {
//                                    if (boardRemoval[rmI][rmJ] == 1) {
//                                        board[rmI][rmJ] = new Block(Color
//                                                .WHITE, Color.WHITE);
//                                        removedTiles++;
//                                    }
//                                }
//                            }
                        }
                        counter = 0;
                        currentMatchBlock = null;
                    }
                }
            }
        }

        return foundMatch;
    }

    /**
     * Turns the tiles that need to be removed white.
     */
    public void turnTilesWhite() {
//        System.out.println("Removing tiles...");
        for (int rmI = 0; rmI < ROWS + HIDDEN; rmI++) {
            for (int rmJ = 0; rmJ < COLS; rmJ++) {
                if (boardRemoval[rmI][rmJ] == 1) {
                    grid[rmI][rmJ] = new Block(Color.WHITE, Color.WHITE);
                }
            }
        }
    }

    /**
     * Removes the blocks that have been indicated as a match
     */
    public void removeTiles() {
//        System.out.println("Removing tiles...");
        for (int rmI = 0; rmI < ROWS + HIDDEN; rmI++) {
            for (int rmJ = 0; rmJ < COLS; rmJ++) {
                if (boardRemoval[rmI][rmJ] == 1) {
                    grid[rmI][rmJ] = null;
                    updateScoreListeners();
                    score++;
                    boardRemoval[rmI][rmJ] = 0;
                    level = (int)(score /10);
                }
            }
        }
    }

    /**
     * Moves everything down
     */
    public void moveDown() {
//        System.out.println("Total tiles removed: " + score);
        for (int j = 0; j < COLS; j++) {
            for (int i = ROWS + HIDDEN - 1; i >= 0; i--) {
                int counter = 0;
//                System.out.println("ROWS: " + i + " COLS: " + j);

                while (grid[i][j] == null) {
                    for (int k = i; k > 0; k--) {
                        grid[k][j] = grid[k - 1][j];
//                        System.out.println("GG");
                    }
                    counter++;
                    if (counter == 5) break;
                }

            }
        }
    }


    /**
     * Unit testing.
     * @param args
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        GameBoard panel = new GameBoard();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.requestFocus();

    }

}
