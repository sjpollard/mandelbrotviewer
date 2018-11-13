package mandelbrot;

import javax.swing.*;
import java.awt.*;

public class DimensionFrame extends JFrame {

    JPanel content;

    FractalSet fractalSet;

    GridArgandDiagram argandDiagram;

    JButton nextButton;

    boolean finished;

    public DimensionFrame(FractalSet fractalSet, FractalColours colours, DrawingConditions conditions) {

        super();

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
        this.content.setBorder(BorderFactory.createLineBorder(Color.green));

        this.nextButton = new JButton("Next");
        this.nextButton.addActionListener(ae -> updateGrids());

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

    public void updateGrids() {

        if (argandDiagram.step == 0 && !finished) argandDiagram.step = 16;
        argandDiagram.repaint();
        argandDiagram.step--;
        if (argandDiagram.step == 2) finished = true;

    }

}
