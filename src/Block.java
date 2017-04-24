/**
 * Christopher Skinner
 * 2017 - April - 24
 * Class: 251
 * Section: 001
 */

import java.awt.Color;
import java.awt.Graphics;

/**
 * Pulled this code essentially from Brooke's Tetris game.
 *
 * The class is used to build the pieces in the Columns game.
 */
public class Block {

    private Color fillColor;
    private Color lineColor;

    public Block(Color fillColor, Color lineColor) {
        this.fillColor = fillColor;
        this.lineColor = lineColor;
    }

    public String toString() {
        String fillColorString;
        String lineColorString;

        if (fillColor == Color.RED) {
            fillColorString = "R ";
        } else if (fillColor == Color.BLUE) {
            fillColorString = "B ";
        } else if (fillColor == Color.YELLOW) {
            fillColorString = "Y ";
        } else if (fillColor == Color.MAGENTA) {
            fillColorString = "M ";
        } else {
            fillColorString = "W ";
        }

        return fillColorString;
    }

    public void paint(Graphics graphic, int x, int y, int cellSize ) {
        graphic.setColor(fillColor);
        graphic.fillRoundRect(x, y, cellSize, cellSize, cellSize/4,
                cellSize/4);
        graphic.setColor(lineColor);
        graphic.drawRoundRect(x, y, cellSize, cellSize, cellSize/4,
                cellSize/4);
    }
}
