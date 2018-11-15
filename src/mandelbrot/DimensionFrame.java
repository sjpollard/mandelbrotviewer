package mandelbrot;

import javax.swing.*;
import java.awt.*;

public class DimensionFrame extends JFrame {

    JPanel content;

    FractalSet fractalSet;

    GridArgandDiagram argandDiagram;

    JButton nextButton;

    int boxes;
    double[] logOfNoBoxes;
    double[] logOfSideLengths;

    public DimensionFrame(FractalSet fractalSet, FractalColours colours, DrawingConditions conditions) {

        super();

        this.setSize(800, 600);
        this.setResizable(false);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.fractalSet = fractalSet;
        this.boxes = 0;

        setupComponents(colours, conditions);

        this.logOfNoBoxes = new double[argandDiagram.step - 1];
        this.logOfSideLengths = new double[argandDiagram.step - 1];

        this.fractalSet.setDimensions(new Dimension(this.getWidth(), this.getHeight()));
        this.fractalSet.iterate(false);

        this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2,(int)(screenSize.getHeight() - this.getHeight())/2);
        this.setVisible(true);

    }

    public void setupComponents(FractalColours colours, DrawingConditions conditions) {

        this.content = new JPanel();
        this.content.setLayout(new BorderLayout());

        this.nextButton = new JButton("Next");
        this.nextButton.addActionListener(ae -> updateGridlines());

        if (fractalSet.getType() == FractalType.MANDELBROT) conditions.drawJulia = false;
        if (fractalSet.getType() == FractalType.JULIA) conditions.drawMandelbrot = false;

        conditions.readyToCreateImage = true;
        conditions.readyToDrawCoords = false;
        conditions.readyToDrawInfo = false;

        this.argandDiagram = new GridArgandDiagram(fractalSet, conditions, colours);
        this.content.add(argandDiagram, BorderLayout.NORTH);
        this.content.add(nextButton, BorderLayout.CENTER);
        this.add(content);
        this.pack();
        
    }

    public void updateGridlines() {

        if (!argandDiagram.finished) {
            argandDiagram.start = true;
            updateGrids();
            logOfNoBoxes[16 - argandDiagram.step] = Math.log(boxes);
            logOfSideLengths[16 - argandDiagram.step] = Math.log(1.0 / argandDiagram.step);
            argandDiagram.repaint();
            argandDiagram.step--;
        }
        if (argandDiagram.step == 1)  {
            argandDiagram.finished = true;
            argandDiagram.start = false;
            argandDiagram.repaint();
            RegressionCalculator dimensionRegression = new RegressionCalculator(logOfSideLengths, logOfNoBoxes);
            System.out.println(dimensionRegression.calculateGradient());
        }

    }

    public void updateGrids() {

        boxes = 0;
        for (int y = 0; y < argandDiagram.getHeight(); y += argandDiagram.step - 1) {

            for (int x = 0; x < argandDiagram.getWidth(); x += argandDiagram.step - 1) {

                boolean found = false;

                for (int boxY = y; boxY <= y + argandDiagram.step && boxY < argandDiagram.getHeight() && !found; boxY++) {

                    for (int boxX = x; boxX <= x + argandDiagram.step && boxX < argandDiagram.getWidth() && !found; boxX++) {

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
