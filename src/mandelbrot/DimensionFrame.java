package mandelbrot;

import javax.swing.*;
import java.awt.*;

public class DimensionFrame extends JFrame {

    JPanel content;

    FractalSet fractalSet;

    GridArgandDiagram argandDiagram;

    JButton nextButton;


    public DimensionFrame(FractalSet fractalSet, FractalColours colours, DrawingConditions conditions) {

        super();

        this.setMinimumSize(new Dimension(800, 600));
        this.setResizable(false);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.fractalSet = fractalSet;

        setupComponents(colours, conditions);

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

        System.out.println(getSize());
        System.out.println(content.getSize());
        System.out.println(nextButton.getSize());
        System.out.println(argandDiagram.getSize());
        if (!argandDiagram.finished) {
            argandDiagram.start = true;
            updateGrids();
            argandDiagram.repaint();
            argandDiagram.step--;
        }
        if (argandDiagram.step == 1) argandDiagram.finished = true;

    }

    public void updateGrids() {

        for (int y = 0; y < argandDiagram.getHeight(); y += argandDiagram.step) {

            for (int x = 0; x < argandDiagram.getWidth(); x += argandDiagram.step) {

                boolean found = false;

                for (int boxY = y; boxY <= y + argandDiagram.step && boxY < argandDiagram.getHeight() && !found; boxY++) {

                    for (int boxX = x; boxX <= x + argandDiagram.step && boxX < argandDiagram.getWidth() && !found; boxX++) {

                        if (argandDiagram.getColorAtPixel(boxX, boxY).equals(Color.BLACK))  {

                            argandDiagram.intersectedBoxes.add(new Point(x, y));
                            found = true;

                        }

                    }

                }

            }

        }

    }


}
