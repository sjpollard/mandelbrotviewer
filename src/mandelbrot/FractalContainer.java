package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * JPanel that sits within the content of MandelbrotFrame and manages the drawing of fractals.
 * This object contains the paintComponent() method and so everything is directed towards here
 * at some point so that it can be drawn. Mainly, this object manages the creation of the
 * BufferedImages within the FractalDiagrams and also draws extra components on top of the
 * existing image. These include fractal information and the route of a tracked complex
 * number.
 */

@SuppressWarnings("serial")
public class FractalContainer extends JPanel {

    private MandelbrotFrame mandelbrotFrame;

    /**Reference to the main Mandelbrot set*/
    MandelbrotSet mandelbrotSet;

    public FractalColours colours;
    public DrawingConditions conditions;
    public FractalDiagram mandelbrotDiagram;
    public FractalDiagram juliaDiagram;

    ArrayList<FractalDiagram> repaintList;

    /**Constructor that sets up ready to contain FractalDiagrams*/
    public FractalContainer (MandelbrotFrame mandelbrotFrame) {

        super();

        this.setFont(new Font("default", Font.BOLD, 12));
        this.setLayout(new GridLayout(1,2));

        this.mandelbrotFrame = mandelbrotFrame;

        this.colours = new FractalColours(Color.RED, Color.BLUE, Color.BLACK);
        this.conditions = new DrawingConditions();
        this.conditions.drawMandelbrot = true;
        this.conditions.drawJulia = true;

        this.setVisible(true);

    }

    public void drawImages() {

        if (conditions.readyToCreateImage) {
            repaintList = new ArrayList<>();
            if (conditions.drawMandelbrot) repaintList.add(mandelbrotDiagram);
            if (conditions.drawJulia) repaintList.add(juliaDiagram);
            mandelbrotDiagram.repaintList = repaintList;
            juliaDiagram.repaintList = repaintList;
        }
        mandelbrotDiagram.repaint();
        juliaDiagram.repaint();

    }

    /**Called within MandelbrotFrame (when the FractalSets have been created) to create the FractalDiagrams*/
    void setupFractalImages(MandelbrotSet mandelbrotSet) {

        this.setPreferredSize(new Dimension(mandelbrotSet.getIterations()[0].length, mandelbrotSet.getIterations().length));

        this.mandelbrotSet = mandelbrotSet;
        this.repaintList = new ArrayList<>();
        this.mandelbrotDiagram = new FractalDiagram(mandelbrotFrame, mandelbrotSet, conditions, colours, repaintList);
        this.juliaDiagram = new FractalDiagram(mandelbrotFrame, mandelbrotSet.juliaSet, conditions, colours, repaintList);

        this.add(mandelbrotDiagram);
        this.add(juliaDiagram);

    }

}
