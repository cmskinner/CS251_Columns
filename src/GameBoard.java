import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
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

    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final int HIDDEN = 2;
    private static final int MATCHES_PER_LEVEL = 10;

    private boolean isPlaying = false;

    private ColumnPiece2D currentShape;
    private ColumnPiece2D nextShape;

    private Set<ScoreListener> scoreListeners = new HashSet<ScoreListener>();

    public GameBoard(int numRows, int numCols, int cellSize, Color
            backgroundColour, int hidden) {
        super(numRows, numCols, cellSize, backgroundColour, hidden);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isPlaying) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_DOWN:

                            break;
                        case KeyEvent.VK_LEFT:

                            break;
                        case KeyEvent.VK_RIGHT:
                            break;
                        case KeyEvent.VK_UP:
                            break;
                        case KeyEvent.VK_SPACE:
                            break;
                    }
                }
            }
        });
    }

    public GameBoard() {
        this(ROWS+HIDDEN, COLS, CELL_SIZE, DEFAULT_BACKGROUND_COLOUR, HIDDEN);
    }

    public void actionPerformed(ActionEvent ev) {
        if(isPlaying) {
//            moveCurrentDown(false);
        }
    }

    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }

    private Block makeBlock(Color color) {
        return new Block(color, Color.WHITE);
    }

    private void chooseNextShape() {

    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            while(currentShape == null) {
                chooseNextShape();
            }
        }
    }
}
