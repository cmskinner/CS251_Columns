/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

import java.awt.*;
import java.util.Random;

/**
 * This class essentially takes Block objects and creates a board of it and
 * can do different things which include making new pieces, removing pieces,
 * moving pieces down like gravity, checking for matches, and printing the
 * board.
 */
public class BlockManager {

    /**
     * This is simply for debugging purposes. When set to true, it prints out
     * loads of printing statements for various different variables.
     */
    private final boolean DB = false;


    private int removedTiles = 0;

    private Block board[][];
    private int boardRemoval[][];
    private int rows;
    private int columns;
    private final Color[] colorArray = {Color.RED, Color.BLUE, Color.YELLOW,
            Color.MAGENTA};
    private Random rnd = new Random();

    /**
     * Constructor that initializes all of the member variables that are
     * needed and sets the random seed.
     * @param rows
     * @param columns
     */
    public BlockManager (int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Block[rows][columns];
        this.boardRemoval = new int[rows][columns];
        this.rnd = rnd;
        rnd.setSeed(1);
        createGameBoard();
    }

    /**
     * Creates the new random piece that will be dropped in next. Makes an
     * array of Block objects then creates the ColumnPiece2D object.
     *
     * @param columnToStartIn
     */
    public void newPiece(int columnToStartIn) {
        Block[] newColumn = new Block[3];
        for (int i = 0; i < 3; i++ ) {
            int randNum = (Math.abs(rnd.nextInt())%4);
            if (DB) System.out.println(randNum);
            Block newBlock = new Block(colorArray[randNum], Color.WHITE);
            newColumn[i] = newBlock;
        }

        ColumnPiece2D piece = new ColumnPiece2D(newColumn);
        dropPiece(piece, columnToStartIn);

//        int j = 0;
//        for (int i = rows - 1; i > rows - 4; i--) {
//            board[i][columnToStartIn] = newColumn[j];
//            j++;
//        }

    }

    /**
     * Drops the piece, as the name suggests. Calculates the offset so blocks
     * go above other blocks, simulating the gravity idea. Also deals with
     * multiple matches.
     * @param piece
     * @param columnToStartIn
     */
    public void dropPiece(ColumnPiece2D piece, int columnToStartIn) {
        System.out.println("Dropping Piece in Column " + columnToStartIn + ":" +
                " ");
        int start = 0;
        for (int check = 0; check < rows; check++) {
            if (DB) System.out.println("Row: " + check + ", COL: " +
                    columnToStartIn);
            if (DB) System.out.println(board[check][columnToStartIn + 1]
                    .toString());
            if (board[check][columnToStartIn].toString() == "W ") {
                start++;
            } else {

                break;
            }
        }
//        System.out.println("Start: " + start);
        int j= 0;
        for (int i = start - 1; i > start - 4; i--) {
            board[i][columnToStartIn] = piece.getBlockAt(j, 0);
            j++;
        }


        boolean foundMatch = false;
        int counter = 0;
        do {
            foundMatch = false;
            foundMatch = checkForMatch();
            counter++;
            if (counter == 5) break;
        } while (foundMatch);
    }

    /**
     * The major long and drawn out algorithm to find matches. Finds the
     * matches and then marks them for deletion in the boardRemoval board
     * with a 1.
     *
     * @return true if it found a match, false if not.
     */
    public boolean checkForMatch() {
        boolean foundMatch = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boardRemoval[i][j] = 0;
            }
        }

        int directionRow[] = {-1, 0, 1, 1};
        int directionColumn[] = {1, 1, 1, 0};
        int counter = 0;
        Block currentMatchBlock = null;

        for (int direction = 0; direction < 4; direction++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (DB) System.out.println("ROW: " + i + ", COLUMN: " + j);
                    if ((i + directionRow[direction] >= rows) || (j +
                            directionColumn[direction] >= columns)) {
                        if (DB) System.out.println("1st Continuing.");
                        continue;
                    }
                    if ((i + 2*directionRow[direction] >= rows) || (j +
                            2*directionColumn[direction] >= columns)) {
                        if (DB) System.out.println("2nd Continuing.");
                        continue;
                    }
                    if (currentMatchBlock == null) {
                        currentMatchBlock = board[i][j];
                        if (DB) System.out.println("Current Match Block: " +
                                currentMatchBlock.toString());
                    }

//                    System.out.println(currentMatchBlock.toString());
                    if (currentMatchBlock.toString().equals("W ")) {
//                        System.out.println("Throwing out: " + currentMatchBlock.toString());
                        currentMatchBlock = null;
                        continue;
                    } else {
//                        System.out.println("HERE");
                        if (currentMatchBlock.toString().equals(board[i +
                                directionRow[direction]][j +
                                directionColumn[direction]].toString())) {
                            if (DB) System.out.println("1st Counter " +
                                    "Increase\n");
                            counter++;
                            if (currentMatchBlock.toString().equals(board[i +
                                    2*directionRow[direction]][j +
                                    2*directionColumn[direction]].toString())) {
                                if (DB) System.out.println("2nd Counter " +
                                        "Increase\n");
                                counter++;


                            }
                        }
                        if (counter == 2) {
                            if (DB)System.out.println("Counter is 2");
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
     * Removes the tiles that are marked for removal in the boardRemoval
     * double array.
     */
    public void removeTiles() {
        System.out.println("Removing tiles...");
        for (int rmI = 0; rmI < rows; rmI++) {
            for (int rmJ = 0; rmJ < columns; rmJ++) {
                if (boardRemoval[rmI][rmJ] == 1) {
                    board[rmI][rmJ] = new Block(Color
                            .WHITE, Color.WHITE);
                    removedTiles++;
                }
            }
        }
    }

    /**
     * Checks for white pieces in the board and if there is a white piece, it
     * deletes it and moves it down 5 or less times (since 5 is the maximum
     * one could get in a vertical line.
     */
    public void moveDown() {
        System.out.println("Total tiles removed: " + removedTiles);
        for (int j = 0; j < columns; j++) {
            for (int i = rows - 1; i >= 0; i--) {
                int counter = 0;
                while (board[i][j].toString().equals("W ")) {
                    for (int k = i; k > 0; k--) {
                        board[k][j] = board[k - 1][j];
                    }
                    counter++;
                    if (counter == 5) break;
                }

            }
        }
    }

    /**
     * Creates a blank board with White pieces.
     */
    public void createGameBoard() {
        if (DB) System.out.println("Rows: " + rows + ", Columns: " + columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (DB) System.out.println("I: " + i + ", J:" + j);
                board[i][j] = new Block(Color.WHITE, Color.WHITE);
            }
        }
    }

    /**
     * Simple toString() override.
     * @return string representation of the board
     */
    public String toString() {
        String boardString = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boardString += board[i][j].toString();
            }
            boardString += "\n";
        }
        return boardString;
    }

    /**
     * Print statement for the unit testing, it is essentially toString(),
     * but it easier to call.
     */
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("");
    }

}
