package mandelbrot;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GridArgandDiagram extends ArgandDiagram implements MouseListener{

    DimensionFrame dimensionFrame;

    GenericQueue<Point> intersectedBoxes;

    int currentStep;

    boolean start;

    boolean finished;

    public GridArgandDiagram(DimensionFrame dimensionFrame, FractalSet fractalSet, DrawingConditions conditions, FractalColours colours, Dimension size, int step) {

        super(fractalSet, conditions, colours, size);

        this.dimensionFrame = dimensionFrame;
        this.currentStep = step;
        this.intersectedBoxes = new GenericQueue<>();
        this.start = false;
        this.finished = false;

        this.addMouseListener(this);

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (start) {

            drawGridlines(g);

            drawBoxes(g);

            if (currentStep == 2)  {
                finished = true;
                start = false;
            }

        }

    }

    public void drawGridlines(Graphics g) {

        if (start) {
            g.setColor(colours.getInverse());
            for (int x = 0; x < this.getWidth(); x += currentStep - 1) {

                g.drawLine(x, 0, x, this.getHeight());

            }
            for (int y = 0; y < this.getHeight(); y += currentStep - 1) {

                g.drawLine(0, y, this.getWidth(), y);

            }
        }

    }

    public void drawBoxes(Graphics g) {

        for (Point intersected: intersectedBoxes) {

            g.setColor(FractalColours.invertColour(colours.getOuter()));
            g.fillRect(intersected.x + 1, intersected.y + 1, currentStep - 2, currentStep - 2);
            g.setColor(Color.BLACK);
            g.drawRect(intersected.x, intersected.y, currentStep - 1, currentStep - 1);

        }

    }

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
