package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Secondary JFrame that allows for the user to be able to find the estimated fractal dimension of a fractal.
 * This object uses the box counting method to find the approximate Minkowski–Bouligand dimension of the boundary.
 * As well as this, it graphically shows the user what is happening on each iteration of the box counting method.
 * The more self similar the fractal, the higher the expected fractal dimension.
 */

public class DimensionFrame extends JFrame {

    /**The fractal set to be measured*/
    private FractalSet fractalSet;

    /**Graphical component to display gridlines*/
    private GridArgandDiagram argandDiagram;

    private Dimension screensize;

    /**The current step of the iteration*/
    private int iteration;

    /**The initial side length of the drawn boxes*/
    private int initialLength;

    /*The total number of boxes counted in each iteration**/
    private int boxes;

    /**Array of log(N) where N is number of boxes*/
    double[] logOfNoBoxes;

    /**Array of log(1/ε) where ε is box side length*/
    double[] logOfSideLengths;

    /**Constucts a DimensionFrame that initially just displays the selected fractal*/
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
        this.initialLength = 512;
        this.boxes = 0;

        setupComponents(colours, conditions);

        int size = (int)((Math.log(initialLength)/Math.log(2)) - 1);
        this.logOfNoBoxes = new double[size];
        this.logOfSideLengths = new double[size];

        this.fractalSet.setDimensions(new Dimension(this.getWidth(), this.getHeight()));
        this.fractalSet.iterate(false);

        this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2,(int)(screenSize.getHeight() - this.getHeight())/2);
        this.setVisible(true);

    }

    /**Sets up the components of this object*/
    public void setupComponents(FractalColours colours, DrawingConditions conditions) {

        if (fractalSet.getType() == FractalType.MANDELBROT) conditions.drawJulia = false;
        if (fractalSet.getType() == FractalType.JULIA) conditions.drawMandelbrot = false;

        conditions.readyToCreateImage = true;
        conditions.readyToDrawCoords = false;
        conditions.readyToDrawInfo = false;

        this.argandDiagram = new GridArgandDiagram(this, fractalSet, conditions, colours, screensize, initialLength);

        this.add(argandDiagram);
        this.pack();
        
    }

    /**Draws the next iteration of gridlines by halving box side length*/
    public void updateGridlines() {

        if (!argandDiagram.finished) {
            iteration++;
            argandDiagram.currentLength /= 2;
            argandDiagram.start = true;
            updateGrids();
            logOfNoBoxes[iteration - 1] = Math.log(boxes);
            logOfSideLengths[iteration - 1] = Math.log(1.0 / argandDiagram.currentLength);
        }
        else if (!argandDiagram.start)  {

            String output = "Box counting dimension: " + new RegressionCalculator(logOfSideLengths, logOfNoBoxes).calculateGradient();
            JOptionPane.showMessageDialog(this, output, "Fractal dimension", JOptionPane.PLAIN_MESSAGE);

        }
        argandDiagram.repaint();

    }

    /**Counts the number of grids that cover the boundary of the fractal and queues them for drawing*/
    public void updateGrids() {
        
        boxes = 0;
        for (int y = 0; y < argandDiagram.getHeight(); y += argandDiagram.currentLength - 1) {

            for (int x = 0; x < argandDiagram.getWidth(); x += argandDiagram.currentLength - 1) {

                boolean found = false;

                for (int boxY = y; boxY <= y + argandDiagram.currentLength - 2 && boxY < argandDiagram.getHeight() && !found; boxY++) {

                    for (int boxX = x; boxX <= x + argandDiagram.currentLength - 2 && boxX < argandDiagram.getWidth() && !found; boxX++) {

                        if (argandDiagram.getColorAtPixel(boxX, boxY).equals(argandDiagram.colours.getInner()))  {

                            ArrayList<Color> neighbours = new ArrayList<>();
                            if (boxY - 1 >= 0) neighbours.add(argandDiagram.getColorAtPixel(boxX, boxY - 1));
                            if (boxX + 1 < argandDiagram.getWidth()) neighbours.add(argandDiagram.getColorAtPixel(boxX + 1, boxY));
                            if (boxY + 1 < argandDiagram.getHeight()) neighbours.add(argandDiagram.getColorAtPixel(boxX, boxY + 1));
                            if (boxX - 1 >= 0)neighbours.add(argandDiagram.getColorAtPixel(boxX - 1, boxY));

                            for (Color color: neighbours) {

                                if (!color.equals(argandDiagram.colours.getInner())) {

                                    argandDiagram.intersectedBoxes.add(new Point(x, y));
                                    boxes++;
                                    found = true;
                                    break;

                                }

                            }

                        }

                    }

                }

            }

        }

    }




}
