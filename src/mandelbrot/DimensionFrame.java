package mandelbrot;

import javax.swing.*;
import java.awt.*;

public class DimensionFrame extends JFrame {

    FractalSet fractalSet;

    GridArgandDiagram argandDiagram;

    Dimension screensize;

    int iteration;
    int initialStep;
    int boxes;
    double[] logOfNoBoxes;
    double[] logOfSideLengths;

    public DimensionFrame(FractalSet fractalSet, FractalColours colours, DrawingConditions conditions) {

        super("Box Counting Calculator");

        this.screensize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        this.setPreferredSize(screensize);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.fractalSet = fractalSet;
        this.iteration = 0;
        this.initialStep = 512;
        this.boxes = 0;

        setupComponents(colours, conditions);

        this.logOfNoBoxes = new double[8];
        this.logOfSideLengths = new double[8];

        this.fractalSet.setDimensions(new Dimension(this.getWidth(), this.getHeight()));
        this.fractalSet.iterate(false);

        this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2,(int)(screenSize.getHeight() - this.getHeight())/2);
        this.setVisible(true);

    }

    public void setupComponents(FractalColours colours, DrawingConditions conditions) {

        if (fractalSet.getType() == FractalType.MANDELBROT) conditions.drawJulia = false;
        if (fractalSet.getType() == FractalType.JULIA) conditions.drawMandelbrot = false;

        conditions.readyToCreateImage = true;
        conditions.readyToDrawCoords = false;
        conditions.readyToDrawInfo = false;

        this.argandDiagram = new GridArgandDiagram(this, fractalSet, conditions, colours, screensize, initialStep);

        this.add(argandDiagram);
        this.pack();
        
    }

    public void updateGridlines() {

        if (!argandDiagram.finished) {
            iteration++;
            argandDiagram.currentStep /= 2;
            argandDiagram.start = true;
            updateGrids();
            logOfNoBoxes[iteration - 1] = Math.log(boxes);
            logOfSideLengths[iteration - 1] = Math.log(1.0 / argandDiagram.currentStep);
        }
        argandDiagram.repaint();

    }

    public void updateGrids() {
        
        boxes = 0;
        for (int y = 0; y < argandDiagram.getHeight(); y += argandDiagram.currentStep - 1) {

            for (int x = 0; x < argandDiagram.getWidth(); x += argandDiagram.currentStep - 1) {

                boolean found = false;

                for (int boxY = y; boxY <= y + argandDiagram.currentStep - 1 && boxY < argandDiagram.getHeight() && !found; boxY++) {

                    for (int boxX = x; boxX <= x + argandDiagram.currentStep - 1 && boxX < argandDiagram.getWidth() && !found; boxX++) {

                        if (argandDiagram.getColorAtPixel(boxX, boxY).equals(argandDiagram.colours.getInner()))  {

                            argandDiagram.intersectedBoxes.add(new Point(x, y));
                            boxes++;
                            found = true;

                        }

                    }

                }

            }

        }

    }




}
