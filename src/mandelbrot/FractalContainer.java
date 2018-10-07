package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

    /**Reference to the main Mandelbrot set*/
    MandelbrotSet mandelbrotSet;

    /**Colours that the fractals should be generated with*/
    FractalColours colours;
    Color inverseColour;

    /**Fields used when tracking a complex number*/
    private String fractalTracked;
    private DoublyLinkedQueue<ComplexNumber> queue;
    private double pathLength;
    private ComplexNumber first;
    private ComplexNumber last;

    public DrawingConditions conditions;
    public FractalDiagram mandelbrotDiagram;
    public FractalDiagram juliaDiagram;

    /**Constructor that sets up ready to contain FractalDiagrams*/
    public FractalContainer () {

        super();
        this.setFont(new Font("default", Font.BOLD, 12));

        this.colours = new FractalColours(Color.RED, Color.BLUE, Color.BLACK);
        this.inverseColour = invertColour(colours.getInner());
        this.conditions = new DrawingConditions();
        this.conditions.drawMandelbrot = true;
        this.conditions.drawJulia = true;
        this.queue = new DoublyLinkedQueue<>();

    }

    /**Called within MandelbrotFrame (when the FractalSets have been created) to create the FractalDiagrams*/
    public void setupFractalImages(MandelbrotSet mandelbrotSet) {

        this.mandelbrotSet = mandelbrotSet;
        this.mandelbrotDiagram = new FractalDiagram(mandelbrotSet, new Point(0, 0), conditions);
        this.juliaDiagram = new FractalDiagram(mandelbrotSet.juliaSet, new Point(mandelbrotSet.getIterations()[0].length, 0), conditions);

    }


    /**The paint method that is called whenever something visual changes and decides what to draw based on DrawingConditions*/
    @Override
    public void paintComponent(Graphics graphics) {

        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        first = last = null;
        pathLength = 0;

        if (conditions.readyToCreateImage) {

            if (conditions.drawMandelbrot && conditions.drawJulia) {
                mandelbrotDiagram.createImage(colours);
                juliaDiagram.createImage(colours);
            }
            else if (conditions.drawMandelbrot) {
                mandelbrotDiagram.createImage(colours);
            }
            else if (conditions.drawJulia) {
                juliaDiagram.createImage(colours);
            }

        }
        if (conditions.drawMandelbrot) {
            g.drawImage(mandelbrotDiagram.fractalImg, mandelbrotDiagram.getLocation().x, mandelbrotDiagram.getLocation().y, this);
        }
        if (conditions.drawJulia) {
            g.drawImage(juliaDiagram.fractalImg, juliaDiagram.getLocation().x, juliaDiagram.getLocation().y, this);
        }
        if(!queue.isEmpty()) {

            drawLines(g);

        }
        if(conditions.readyToDrawInfo) {

            drawInfo(g);

        }

    }

    /**Method that decides what information should be drawn where*/
    public void drawInfo(Graphics2D g) {

        int infoPos;
        int totalArea;
        g.setColor(inverseColour);

        if (conditions.drawMandelbrot && conditions.drawJulia) {
            infoPos = this.getWidth()/2 - 150;
            drawCross(g, this.getWidth() / 4, this.getHeight() / 2, 4);
            drawCross(g, (this.getWidth() / 4) * 3, this.getHeight() / 2, 4);
        }
        else {
            infoPos = this.getWidth() - 150;
            drawCross(g, this.getWidth() /2, this.getHeight() / 2, 4);
        }

        if (conditions.drawMandelbrot) {
            totalArea = (mandelbrotSet.getIterations().length * mandelbrotSet.getIterations()[1].length) / (mandelbrotSet.getChunkSize() * mandelbrotSet.getChunkSize());
            drawInfomationStrings("mandelbrot", infoPos, mandelbrotSet.getCentre(), mandelbrotSet.getzStart(), mandelbrotSet.getZoom(), mandelbrotSet.getPower(), mandelbrotSet.getPixelArea(), totalArea, g);
        }

        if (conditions.drawJulia) {
            infoPos = this.getWidth() - 150;
            totalArea = (mandelbrotSet.juliaSet.getIterations().length * mandelbrotSet.juliaSet.getIterations()[1].length) / (mandelbrotSet.juliaSet.getChunkSize() * mandelbrotSet.juliaSet.getChunkSize());
            drawInfomationStrings("julia", infoPos, mandelbrotSet.juliaSet.getCentre(), mandelbrotSet.juliaSet.getC(), mandelbrotSet.juliaSet.getZoom(), mandelbrotSet.juliaSet.getPower(), mandelbrotSet.juliaSet.getPixelArea(), totalArea, g);
        }

    }

    /**Starts the tracking process of a specified pixel onscreen and compiles the data found into a queue*/
    public void track(int x, int y) {

        if (conditions.drawMandelbrot && conditions.drawJulia) {
            if (x < this.getWidth()/2) {
                ComplexNumber point = mandelbrotSet.pixelToComplexNumber(x, y);
                queue = mandelbrotSet.fillTrackingQueue(point);
                fractalTracked = "mandelbrot";
            }
            else {
                ComplexNumber point = mandelbrotSet.juliaSet.pixelToComplexNumber(x - this.getWidth()/2, y);
                queue = mandelbrotSet.juliaSet.fillTrackingQueue(point);
                fractalTracked = "julia";
            }
        }
        else if (conditions.drawMandelbrot) {
            ComplexNumber point = mandelbrotSet.pixelToComplexNumber(x, y);
            queue = mandelbrotSet.fillTrackingQueue(point);
            fractalTracked = "mandelbrot";
        }
        else if (conditions.drawJulia) {
            ComplexNumber point = mandelbrotSet.juliaSet.pixelToComplexNumber(x, y);
            queue = mandelbrotSet.juliaSet.fillTrackingQueue(point);
            fractalTracked = "julia";
        }

    }

    /**Methods that use drawString() to output fractal information to the screen*/
    public void drawInfomationStrings(String choice, int infoPos, ComplexNumber centre, ComplexNumber point, double zoom, int power, int pixelArea, int totalArea, Graphics2D g) {
        double percentage;
        double distance;
        String item;
        g.drawString("Centre: " + centre.toString(3), infoPos, 20);
        if (choice.equals("mandelbrot")) item = "zStart: ";
        else item = "c: ";
        g.drawString(item + point.toString(3), infoPos, 40);
        g.drawString("Zoom: " + zoom, infoPos, 60);
        g.drawString("Power: " + power, infoPos, 80);
        percentage = Math.round(((pixelArea / (double) totalArea) * 100) * 100) / 100.0;
        if (percentage > 100) percentage = 100;
        g.drawString("Pixel percentage: " + percentage + "%", infoPos, 100);
        if (first == null || last == null) distance = Double.NaN;
        else distance = Math.round(first.distanceBetween(last) * 1000) / 1000.0;
        g.drawString("Distance moved: " + distance, infoPos, 120);
        g.drawString("Total path length: " + Math.round(pathLength * 1000) / 1000.0, infoPos, 140);
    }

    /**Iterates through the queue to connect up the locations travelled to by a tracked complex number*/
    public void drawLines(Graphics2D g) {

        if (conditions.drawMandelbrot && conditions.drawJulia && fractalTracked.equals("julia")) g.translate(this.getWidth()/2, 0);
        int[] nextPixel, lastPixel;
        first = queue.remove();
        if (fractalTracked.equals("mandelbrot")) lastPixel = mandelbrotSet.complexNumberToPixel(first);
        else lastPixel = mandelbrotSet.juliaSet.complexNumberToPixel(first);
        g.setColor(inverseColour);
        if (conditions.readyToDrawCoords) g.drawString(first.toString(3), lastPixel[0], lastPixel[1]);
        drawDiagonalCross(g, lastPixel[0], lastPixel[1], 4);
        last = first;
        pathLength = 0;
        while(!queue.isEmpty()){

            ComplexNumber next = queue.remove();
            pathLength += last.distanceBetween(next);
            if (fractalTracked.equals("mandelbrot")) nextPixel = mandelbrotSet.complexNumberToPixel(next);
            else nextPixel = mandelbrotSet.juliaSet.complexNumberToPixel(next);

            g.setColor(inverseColour);
            g.drawLine(lastPixel[0], lastPixel[1], nextPixel[0], nextPixel[1]);
            if (queue.isEmpty()) {
                g.setColor(inverseColour);
                if (conditions.readyToDrawCoords) g.drawString(next.toString(3), nextPixel[0], nextPixel[1]);
                drawDiagonalCross(g, nextPixel[0], nextPixel[1], 4);
            }

            last = next;
            lastPixel = nextPixel;
        }
        if (conditions.drawMandelbrot && conditions.drawJulia && fractalTracked.equals("julia")) g.translate(-this.getWidth()/2, 0);

    }

    /**Draws a straight cross with a specified radius at the location input*/
    public void drawCross(Graphics2D g, int x, int y, int radius) {

        g.drawLine(x - radius, y, x + radius, y);
        g.drawLine(x, y - radius, x, y + radius);

    }

    /**Draws a tilted cross with a specified radius at the location input*/
    public void drawDiagonalCross(Graphics2D g, int x, int y, int radius) {

        g.drawLine(x - radius, y + radius, x + radius, y - radius);
        g.drawLine(x - radius, y - radius, x + radius, y + radius);

    }

    /**Returns a colour with random RGB values*/
    public Color randomColour() {

       return new Color((float)Math.random(), (float)Math.random(), (float)Math.random());

    }

    /**Inverts the RGB values of the input colour and returns the inverted colour*/
    public Color invertColour(Color colour) {

        return new Color(255 - colour.getRed(), 255 - colour.getGreen(), 255 - colour.getBlue());

    }

}
