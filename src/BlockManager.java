import java.awt.*;

/**
 * Created by chris on 23-Apr-17.
 */
public class BlockManager {

    private final boolean DB = false;

    private Block board[][];
    private int rows;
    private int columns;
    private final Color[] colorArray = {Color.RED, Color.BLUE, Color.YELLOW,
            Color.MAGENTA};

    public BlockManager (int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Block[rows][columns];
        createGameBoard();
    }

    public void newPiece(int columnToStartIn) {
        Block[] newColumn = new Block[3];
        for (int i = 0; i < 3; i++ ) {
            int randNum = ((int)(Math.random()*4))%4;
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

    public void dropPiece(ColumnPiece2D piece, int columnToStartIn) {
        int start = 0;
        for (int check = 0; check < rows; check++) {
            System.out.println("Row: " + check + ", COL: " + columnToStartIn);
            System.out.println(board[check][columnToStartIn + 1].toString());
            if (board[check][columnToStartIn + 1].toString() != "W ") {
                start++;
            } else {
                break;
            }
        }
        System.out.println("Start: " + start);
        int j= 0;
        for (int i = rows - 1 - start; i > rows - 4 - start; i--) {
            board[i][columnToStartIn] = piece.getBlockAt(j, 0);
            j++;
        }
    }

    public void createGameBoard() {
        if (DB) System.out.println("Rows: " + rows + ", Columns: " + columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (DB) System.out.println("I: " + i + ", J:" + j);
                board[i][j] = new Block(Color.WHITE, Color.WHITE);
            }
        }
    }

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

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(board[i][j]);
            }
            System.out.print("\n");
        }
        System.out.println("");
    }

    public static void main(String args[]) {
        BlockManager bm1 = new BlockManager(5,5);
        bm1.print();
//        System.out.print(bm1.toString());
//        System.out.println("");
        bm1.newPiece(3);
        bm1.print();
//        System.out.print(bm1.toString());
//        System.out.println("");
        bm1.newPiece(1);
        bm1.print();
//        System.out.print(bm1.toString());
//        System.out.println("");

        BlockManager bm2 = new BlockManager(10,10);
        bm2.print();
        bm2.newPiece(8);
        bm2.newPiece(7);
        bm2.print();
        bm2.newPiece(7);
        bm2.print();


    }
}
