/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

public interface Object2D {

    public static class Dimension2D {
        protected int height;
        protected int width;

        public Dimension2D( int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String toString() {
            return "[" + height + "," + width + "]";
        }
    }

    void rotate();

    Dimension2D getDimension();

    String toString();

    Block getBlockAt(int row, int col);
}
