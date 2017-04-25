/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

/**
 * This tests the BlockManager.java and it creates 2 different types of Block
 * Managers and does various things with them that the Block Manager class
 * supports to make the game of columns.
 */
public class BlockManagerTest {

    public static void main( String args[]) {
        System.out.println("Creating empty Block Manager with 7 rows and 5 " +
                "columns: ");
        BlockManager bm1 = new BlockManager(7,5);
        bm1.print();

        bm1.newPiece(0);
        bm1.print();

        bm1.newPiece(1);
        bm1.print();

        bm1.newPiece(2);
        bm1.print();

        bm1.newPiece(1);
        bm1.print();


        System.out.println("Moving down what has been detected as removal.");
        /*
        This is all this way so that I can clearly lay it all out in a
        printed statement for these tests. This will all be in a type of loop
         in the final assignment.
         */
        bm1.removeTiles();
        bm1.moveDown();
        bm1.print();
        bm1.checkForMatch();
        bm1.removeTiles();
        bm1.moveDown();
        bm1.print();

        System.out.println("Creating empty Block Manager with 9 rows and 6 " +
                "columns: ");
        BlockManager bm2 = new BlockManager(9, 6);
        bm2.print();

        bm2.newPiece(0);
        bm2.newPiece(1);
        bm2.newPiece(2);
        bm2.newPiece(1);
        bm2.print();

        System.out.println("Moving down what has been detected as removal.");
        /*
        This is all this way so that I can clearly lay it all out in a
        printed statement for these tests. This will all be in a type of loop
         in the final assignment.
         */
        bm2.removeTiles();
        bm2.moveDown();
        bm2.print();
        bm2.checkForMatch();
        bm2.removeTiles();
        bm2.moveDown();
        bm2.print();


        System.out.println("Back to the first Block Manager\n");
        bm1.print();

        bm1.newPiece(2);
        bm1.print();

        bm1.newPiece(1);
        bm1.print();

        bm1.newPiece(3);
        bm1.print();
        bm1.removeTiles();
        bm1.print();
        bm1.moveDown();
        bm1.print();

    }
}
