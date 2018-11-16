package mandelbrot;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Graphical component that has all of the functionality of the ArgandDiagram, with the exception that
 * it can draw gridlines over the originally generated BufferedImage. This object uses data that is fed
 * in from DimensionFrame and uses that to fill in the boxes that have been counted during the last
 * iteration of box counting.
 * */

public class GridArgandDiagram extends ArgandDiagram implements MouseListener{

    /**The parent of this object*/
    DimensionFrame dimensionFrame;

    /**Queue of points that refer to the upper left pixel of the box*/
    GenericQueue<Point> intersectedBoxes;

    /**Current side length of box to draw*/
    int currentLength;

    /**Boolean meaning start drawing*/
    boolean start;

    /**Boolean meaning finished calculation*/
    boolean finished;

    /**Constructs a GridArgandDiagram ready for iteration*/
    public GridArgandDiagram(DimensionFrame dimensionFrame, FractalSet fractalSet, DrawingConditions conditions, FractalColours colours, Dimension size, int length) {

        super(fractalSet, conditions, colours, size);

        this.dimensionFrame = dimensionFrame;
        this.currentLength = length;
        this.intersectedBoxes = new GenericQueue<>();
        this.start = false;
        this.finished = false;

        this.addMouseListener(this);

    }

    /**Method overridden from ArgandDiagram that draws gridlines*/
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (start) {

            drawGridlines(g);

            drawBoxes(g);

            if (currentLength == 2)  {
                finished = true;
                start = false;
            }

        }

    }

    /**Draws gridlines across the screen for the DimensionFrame to count from*/
    public void drawGridlines(Graphics g) {

        if (start) {
            g.setColor(colours.getInverse());
            for (int x = 0; x < this.getWidth(); x += currentLength - 1) {

                g.drawLine(x, 0, x, this.getHeight());

            }
            for (int y = 0; y < this.getHeight(); y += currentLength - 1) {

                g.drawLine(0, y, this.getWidth(), y);

            }
        }

    }

    /**Uses the queue full of Points to fill in grids where the fractal boundary is present*/
    public void drawBoxes(Graphics g) {

        for (Point intersected: intersectedBoxes) {

            g.setColor(FractalColours.invertColour(colours.getOuter()));
            g.fillRect(intersected.x + 1, intersected.y + 1, currentLength - 2, currentLength - 2);
            g.setColor(Color.BLACK);
            g.drawRect(intersected.x, intersected.y, currentLength - 1, currentLength - 1);

        }

    }

    /**Moves to the next iteration when the screen is clicked*/
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        dimensionFrame.updateGridlines();
        RegressionCalculator dimensionRegression = new RegressionCalculator(dimensionFrame.logOfSideLengths, dimensionFrame.logOfNoBoxes);
        System.out.println(dimensionRegression.calculateGradient());

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
