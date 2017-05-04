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
 * Created by chris on 01-May-17.
 */
public class GameBoard extends BlockPanel implements ActionListener{

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

    private BlockManager bm;

    private final Color[] colorArray = {Color.RED, Color.BLUE, Color.YELLOW,
            Color.MAGENTA};

    private Set<ScoreListener> scoreListeners = new HashSet<ScoreListener>();
//    private Set<ShapeListener> shapeListeners = new HashSet<ShapeListener>();

    private Random rnd = new Random();

    public GameBoard(int numRows, int numCols, int cellSize, Color
            backgroundColour, int hidden) {
        super(numRows, numCols, cellSize, backgroundColour, hidden);
        bm = new BlockManager(numRows, numCols);
//        this.rnd = rnd;
//        rnd.setSeed(1);
        System.out.println("Starting the Gameboard...");
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isPlaying) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            System.out.println("DOWN");
                            moveCurrentDown(false);
                            break;
                        case KeyEvent.VK_LEFT:
                            System.out.println("LEFT");
                            moveCurrentLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            System.out.println("RIGHT");
                            moveCurrentRight();
                            break;
                        case KeyEvent.VK_UP:
                            System.out.println("UP");
                            rotateCurrent();
                            break;
                        case KeyEvent.VK_S:
                            System.out.println("DOWN");
                            moveCurrentDown(false);
                            break;
                        case KeyEvent.VK_A:
                            System.out.println("LEFT");
                            moveCurrentLeft();
                            break;
                        case KeyEvent.VK_D:
                            System.out.println("RIGHT");
                            moveCurrentRight();
                            break;
                        case KeyEvent.VK_W:
                            System.out.println("UP");
                            rotateCurrent();
                            break;
                        case KeyEvent.VK_SPACE:
                            System.out.println("SPACE");
                            moveCurrentDown(true);
                            break;
                    }
                }
            }
        });
        System.out.println("End of constructor");
    }

    public GameBoard() {
        this(ROWS+HIDDEN, COLS, CELL_SIZE, DEFAULT_BACKGROUND_COLOUR, HIDDEN);
    }

    public void actionPerformed(ActionEvent ev) {
        if(isPlaying) {
            moveCurrentDown(false);
        }
    }

    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }

    private void updateScoreListeners() {
        ScoreEvent event = new ScoreEvent(score, lines, level, gameOver);
        for(ScoreListener listener : scoreListeners) {
            listener.updateScore(event);
        }
    }

//    public void addShapeListener(ShapeListener listener) {
//        shapeListeners.add(listener);
//    }
//
//    private void updateShapeListeners() {
//        ShapeEvent event = new ShapeEvent(nextShape, currentShape);
//        for(ShapeListener listener : shapeListeners) {
//            listener.updateShape(event);
//        }
//    }

    private Block makeBlock(Color color) {
        return new Block(color, Color.WHITE);
    }

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

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            while(currentShape == null) {
                chooseNextShape();
                System.out.println("Makes it");
            }
        }
    }



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

    private boolean placeIfTouchingBottom() {
        if(isTouchingBottom()) {
            placeObject(currentShape, row , col);
            if (bm.checkForMatch()) {
                bm.removeTiles();
            }
//            updateLines();
            chooseNextShape();
            System.out.println("placed and choosen next shape");
            return true;
        }
        return false;
    }

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

    private void moveCurrentDown(boolean dropPiece) {
        boolean placed = false;
        do {
            row++;
            placed = placeIfTouchingBottom();
        } while(dropPiece && !placed);
        repaint();
    }

    private void moveCurrentLeft() {
        if(!isTouchingLeft()) {
            col--;
            placeIfTouchingBottom();
            repaint();
        }
    }

    private void moveCurrentRight() {
        if(!isTouchingRight()) {
            col++;
            placeIfTouchingBottom();
            repaint();
        }
    }

    private void rotateCurrent() {
        currentShape.rotate();
        adjustPosition();
        placeIfTouchingBottom();
        repaint();
    }

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
