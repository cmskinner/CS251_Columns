/**
 * Created by chris on 23-Apr-17.
 */

public class ColumnPiece2D implements Object2D, Cloneable{

    private Block[] column;

    public ColumnPiece2D( Block[] column) {
        this.column = column;
    }

    public void rotate() {
        Block holdingValue = column[0];
        for (int i = 0; i < column.length; i++ ) {
            if (i == column.length - 1) {
                column[column.length - 1] = holdingValue;
            }else  {
                column[i - 1] = column[i];
            }
        }
    }

    public String toString() {
        Dimension2D dimension = getDimension();
        String s = "";
        for ( int row = 0; row < dimension.getHeight(); row++ ) {
            s += column[row] == null ? " " : "*";
            s += "\n";
        }
        return s;
    }

    public Dimension2D getDimension() {
        if (column != null ) {
            return new Dimension2D ( column.length, column.length > 0 ?
                    1 : 0);
        } else {
            return new Dimension2D ( 0, 0);
        }
    }

    public Block getBlockAt(int row, int col) {
        return column[row];
    }

    public ColumnPiece2D clone() {
        try {
            return (ColumnPiece2D) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
