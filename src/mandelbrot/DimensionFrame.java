package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

        int size = (int)((Math.log(initialStep)/Math.log(2)) - 1);
        this.logOfNoBoxes = new double[size];
        this.logOfSideLengths = new double[size];

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
