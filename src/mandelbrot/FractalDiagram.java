package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Drawing object that is dedicated to a FractalSet and creates a BufferedImage based on the data
 * generated within the FractalSet. It is here that the number of iterations taken for each
 * complex number to break from the Mandelbrot set is used to scale the colour of each corresponding
 * pixel onscreen. This object can also scale colour based on the histogram method.
 */

public class FractalDiagram extends JPanel {

    MandelbrotFrame mandelbrotFrame;

    Controller controller;

    /**BufferedImage which contains the pixel raster to be drawn by the graphics object*/
    BufferedImage fractalImg;

    /**Colours that the fractals should be generated with*/
    FractalColours colours;
    Color inverseColour;

    /**Fields used when tracking a complex number*/
    private GenericQueue<ComplexNumber> queue;
    private double pathLength;
    private ComplexNumber first;
    private ComplexNumber last;

    public FractalSet fractalSet;
    private Point location;
    public DrawingConditions conditions;
    private int[] histogram;

    /**Constructs a BufferedImage with the same dimensions as the fractals data and passes in references*/
    public FractalDiagram(MandelbrotFrame mandelbrotFrame, FractalSet fractalSet, DrawingConditions conditions, FractalColours colours) {

        super();

        this.mandelbrotFrame = mandelbrotFrame;
        this.location = new Point();
        this.fractalImg = new BufferedImage(fractalSet.getIterations()[0].length, fractalSet.getIterations().length, BufferedImage.TYPE_INT_RGB);
        this.fractalSet = fractalSet;
        this.conditions = conditions;
        this.colours = colours;
        this.inverseColour = invertColour(colours.getInner());
        this.queue = new GenericQueue<>();
        this.controller = new Controller(mandelbrotFrame, this);
        this.setVisible(true);
        this.repaint();

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

            createImage(colours);

        }

        g.drawImage(fractalImg, this.getLocation().x, this.getLocation().y, this);

        if(!queue.isEmpty()) {

            drawLines(g);

        }
        if(conditions.readyToDrawInfo) {

            drawInfo(g);

        }

    }

    /**Large scale method that analyses the data found in the FractalSet to assign colour to individual pixels*/
    public void createImage(FractalColours colours) {

        int width = fractalSet.getIterations()[0].length;
        int height = fractalSet.getIterations().length;

        fractalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int total = 0;
        if (conditions.readyToHistogramColour) {
            histogram = fillHistogram(fractalSet.getIterations(), fractalSet.getChunkSize(), fractalSet.getMaxIterations());
            for (int i = 0; i < fractalSet.getMaxIterations() - 1; i++) total += histogram[i];
        }
        for (int y = 0; y < height; y += fractalSet.getChunkSize()) {
            for (int x = 0; x < width; x += fractalSet.getChunkSize()) {

                if (fractalSet.getIterations()[y][x] == fractalSet.getMaxIterations()) {

                    addFilledSquare(x, y, fractalSet.getChunkSize(), colours.getInner());

                }
                else {

                    double scale;
                    if (conditions.readyToHistogramColour) scale = calcScale(histogram, total, fractalSet.getIterations()[y][x]);
                    else scale = fractalSet.getIterations()[y][x]/(double)fractalSet.getMaxIterations();

                    Color pixelColour;
                    if (conditions.readyToColourPalette) pixelColour = scalePalette(colours.getOuter(), scale);
                    else pixelColour = scaleBetweenColours(scale, colours.getEdge(), colours.getOuter());

                    addFilledSquare(x, y, fractalSet.getChunkSize(), pixelColour);

                }
            }
        }
        if (fractalSet.getType() == FractalType.MANDELBROT) conditions.readyToCreateImage = false;

    }

    /**Iterates through the queue to connect up the locations travelled to by a tracked complex number*/
    public void drawLines(Graphics2D g) {

        int[] nextPixel, lastPixel;
        first = queue.remove();
        lastPixel = fractalSet.complexNumberToPixel(first);
        g.setColor(inverseColour);

        if (conditions.readyToDrawCoords) g.drawString(first.toString(3), lastPixel[0], lastPixel[1]);

        drawDiagonalCross(g, lastPixel[0], lastPixel[1], 4);
        last = first;
        pathLength = 0;
        for (ComplexNumber point: queue) {

            pathLength += last.distanceBetween(point);
            nextPixel = fractalSet.complexNumberToPixel(point);

            g.setColor(inverseColour);
            g.drawLine(lastPixel[0], lastPixel[1], nextPixel[0], nextPixel[1]);
            if (queue.isEmpty()) {
                g.setColor(inverseColour);
                if (conditions.readyToDrawCoords) g.drawString(point.toString(3), nextPixel[0], nextPixel[1]);
                drawDiagonalCross(g, nextPixel[0], nextPixel[1], 4);
            }

            last = point;
            lastPixel = nextPixel;
        }

    }

    /**Method that decides what information should be drawn where*/
    public void drawInfo(Graphics2D g) {

        int infoPos;
        int totalArea = (fractalSet.getIterations().length * fractalSet.getIterations()[0].length) / (fractalSet.getChunkSize() * fractalSet.getChunkSize());
        double percentage;
        double distance;
        String item = "";
        ComplexNumber point = new ComplexNumber();

        g.setColor(inverseColour);
        infoPos = this.getWidth() - 150;
        drawCross(g, this.getWidth() / 2, this.getHeight() / 2, 4);
        g.drawString("Centre: " + fractalSet.getCentre().toString(3), infoPos, 20);

        if (fractalSet.getType() == FractalType.MANDELBROT) {
            item = "zStart: ";
            point = fractalSet.getzStart();
        }
        else if (fractalSet.getType() == FractalType.JULIA) {
            item = "c: ";
            point = fractalSet.getC();
        }

        g.drawString(item + point.toString(3), infoPos, 40);
        g.drawString("Zoom: " + fractalSet.getZoom(), infoPos, 60);
        g.drawString("Power: " + fractalSet.getPower(), infoPos, 80);

        percentage = Math.round(((fractalSet.getPixelArea()/ (double) totalArea) * 100) * 100) / 100.0;
        if (percentage > 100) percentage = 100;
        g.drawString("Pixel percentage: " + percentage + "%", infoPos, 100);

        if (first == null || last == null) distance = Double.NaN;
        else distance = Math.round(first.distanceBetween(last) * 1000) / 1000.0;
        g.drawString("Distance moved: " + distance, infoPos, 120);
        g.drawString("Total path length: " + Math.round(pathLength * 1000) / 1000.0, infoPos, 140);


    }

    /**Starts the tracking process of a specified pixel onscreen and compiles the data found into a queue*/
    public void track(int x, int y) {

        ComplexNumber point = fractalSet.pixelToComplexNumber(x, y);
        queue = fractalSet.fillTrackingQueue(point);

    }

    /**Forms a cumulative list of the number of pixels that iterate to be less than or equal to each iteration*/
    public int[] fillHistogram(int[][] iterations, int chunkSize, int maxIterations) {

        int[] histogram = new int[maxIterations - 1];
        for (int y = 0; y < iterations.length - chunkSize; y += chunkSize) {
            for (int x = 0; x < iterations[0].length - chunkSize; x += chunkSize) {

                if (iterations[y][x] < maxIterations) histogram[iterations[y][x] - 1] += 1;

            }
        }
        return histogram;

    }

    /**Based on an input numIterations, looks through the histogram to find the proportional scaling for this pixel*/
    public double calcScale(int[] histogram, int total, int numIterations) {

        double scale = 0;
        for (int i = 0; i < numIterations; i++) {

            scale += histogram[i];

        }
        return scale /(double)total;

    }

    /**Used within createImage() for to add a filled in square to the BufferedImage (to allow for lower resolution)*/
    public void addFilledSquare(int x, int y, int width, Color colour) {

        for (int currentY = y; currentY - y < width && currentY < fractalImg.getHeight(); currentY++) {

            for (int currentX = x; currentX - x < width && currentX < fractalImg.getWidth(); currentX++) {

                fractalImg.setRGB(currentX, currentY, colour.getRGB());

            }

        }

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

    /**Uses HSV colouring to scale from a beginning colour, through the rainbow and back to itself*/
    public Color scalePalette(Color firstColour, double scale) {

        float[] hsbArray = new float[3];
        Color.RGBtoHSB(firstColour.getRed(), firstColour.getGreen(), firstColour.getBlue(), hsbArray);
        hsbArray[0] += scale;
        return new Color(Color.HSBtoRGB(hsbArray[0], hsbArray[1], hsbArray[2]));

    }

    /**If colour palette is turned off, this simply returns the correct scaling between two colours*/
    public Color scaleBetweenColours(double scale, Color edgeColour, Color outerColour) {

        Color output;
        int redDif = edgeColour.getRed() - outerColour.getRed();
        int greenDif = edgeColour.getGreen() - outerColour.getGreen();
        int blueDif = edgeColour.getBlue() - outerColour.getBlue();
        output = new Color((int)(outerColour.getRed() + redDif*scale), (int)(outerColour.getGreen() + greenDif*scale), (int)(outerColour.getBlue() + blueDif*scale));
        return output;

    }

    /**Returns a colour with random RGB values*/
    public Color randomColour() {

        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random());

    }

    /**Inverts the RGB values of the input colour and returns the inverted colour*/
    public Color invertColour(Color colour) {

        return new Color(255 - colour.getRed(), 255 - colour.getGreen(), 255 - colour.getBlue());

    }

    public void setFractalSet(FractalSet fractalSet) {

        this.fractalSet = fractalSet;

    }

    public Point getLocation() {

        return this.location;

    }

    public void setLocation(Point location) {

        this.location = location;

    }

    public void translateLocation(Point change) {

        location.translate(change.x, change.y);

    }

}
