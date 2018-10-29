package mandelbrot;

import java.awt.*;

/**
 * The backbone of the program that contains and deals with the complex number maths
 * required to generate a fractal image. Implements the FractalSet interface so that
 * it gains the required methods. The JuliaSet object is contained within this as
 * each drawing of the Mandelbrot set contains a corresponding Julia set within.
 * Primarily, this class calculates the number of iterations that every pixel requires
 * to break from the Mandelbrot set.
 */

public class MandelbrotSet implements FractalSet {

    /**Enum that identifies this object when it is passed around as a FractalSet*/
    private FractalType type = FractalType.MANDELBROT;

    /**Fields that contain fractal properties and generated values*/
    private Dimension dimensions;
    private int[][] iterations;
    private ComplexNumber[][] lastResults;
    private boolean[][] refined;
    private int maxIterations;
    private double power;
    private int chunkSize;
    private int pixelArea;
    private double zoom = 150;
    private ComplexNumber centre = new ComplexNumber();
    private ComplexNumber zStart = new ComplexNumber();
    private ComplexNumber c = new ComplexNumber();

    /**Corresponding Julia set*/
    public JuliaSet juliaSet;

    /**Empty constructor*/
    public MandelbrotSet() {

    }

    /**Constructs a MandelbrotSet with the given properties*/
    public MandelbrotSet(int width, int height, int maxIterations, double power, boolean createJulia) {

        this.setDimensions(new Dimension(width, height));
        this.maxIterations = maxIterations;
        this.power = power;
        this.chunkSize = 1;
        if (createJulia) {
            this.juliaSet = new JuliaSet(this);
        }
        this.iterate(false);
    }

    /**Iterates through every pixel on screen, calculating the number of iterations*/
    public void iterate(boolean partOfSuccession) {

        if (!partOfSuccession || chunkSize == 16) pixelArea = 0;

        for (int y = 0; y < iterations.length; y += chunkSize) {
            for (int x = 0; x < iterations[0].length; x += chunkSize) {

                if (partOfSuccession) {
                    if (!refined[y][x]) {
                        refined[y][x] = true;
                        assignIterations(x, y);
                    }
                }
                else assignIterations(x, y);

            }
        }

    }

    /**Decides whether a complex number is able to be skipped/optimised or iterated upon*/
    public void assignIterations(int x, int y) {

        int iterationsCount;
        c = pixelToComplexNumber(x, y);
        if (c.sqrOfMagnitude() <= 4) {
            if (power == 2 && zStart.equals(new ComplexNumber())) {
                if (c.isCardioid() || c.isPeriodTwoBulb()) {
                    iterationsCount = maxIterations;
                    lastResults[y][x] = c;
                }
                else iterationsCount = numIterations(zStart, c, x, y);
            }
            else iterationsCount = numIterations(zStart, c, x, y);
        }
        else  {
            iterationsCount = 1;
            lastResults[y][x] = c;
        }
        if (iterationsCount == maxIterations) pixelArea++;
        iterations[y][x] = iterationsCount;

    }

    /**Converts a pixel onscreen into the corresponding value in the complex plane*/
    public ComplexNumber pixelToComplexNumber(int x, int y) {

        ComplexNumber cn = new ComplexNumber();
        cn.setReal(centre.getReal() + (x - iterations[0].length/2)/zoom);
        cn.setImaginary((centre.getImaginary() - (y - iterations.length/2)/zoom));
        return cn;

    }

    /**Converts a given complex number into the coordinates of the corresponding pixel onscreen*/
    public int[] complexNumberToPixel(ComplexNumber point) {

        int x = (int)((point.getReal() - centre.getReal()) * zoom) + (iterations[0].length/2);
        int y = (int) -((point.getImaginary() - centre.getImaginary()) * zoom) + (iterations.length/2);
        return new int[]{x, y};

    }

    /**Iterates upon the input complex number (with the Mandelbrot set equation) until it breaks, or bails out*/
    public int numIterations(ComplexNumber zStart, ComplexNumber c, int x, int y) {

        int i;

        for (i = 0; i < maxIterations && zStart.sqrOfMagnitude() <= 4; i++) {
            if (power % 1 == 0) {
                zStart = zStart.pow((int) power);
            }
            else zStart = zStart.pow(power);
            zStart = zStart.add(c);
        }
        lastResults[y][x] = zStart;

        return i;

    }

    /**Moves the iterations forward from current max iterations a given value*/
    public void iterateForwards(int newMaxIterations) {

        this.maxIterations = newMaxIterations;
        for (int y = 0; y < lastResults.length; y += chunkSize) {
            for (int x = 0; x < lastResults[0].length; x += chunkSize) {

                if (!(lastResults[y][x] == null)) {
                    lastResults[y][x] = stepUpIterations(lastResults[y][x], x, y, newMaxIterations - iterations[y][x]);
                }
                else {
                    numIterations(zStart, pixelToComplexNumber(x, y), x, y);
                }

            }
        }

    }

    /**Calculates the number of iterations to be added on to this complex numbers bailout*/
    public ComplexNumber stepUpIterations(ComplexNumber zCurrent, int x, int y, int steps) {

        ComplexNumber c = pixelToComplexNumber(x, y);
        int i;
        for (i = 0; i < steps && zCurrent.sqrOfMagnitude() < 4; i++) {
            if (power % 1 == 0) {
                zCurrent = zCurrent.pow((int)power);
            }
            else zCurrent = zCurrent.pow(power);
            zCurrent = zCurrent.add(c);
        }
        iterations[y][x] += i;
        return zCurrent;

    }

    /**Adds the result after each iteration of a complex number to the tracking queue*/
    public GenericQueue<ComplexNumber> fillTrackingQueue(ComplexNumber point) {

        GenericQueue<ComplexNumber> queue = new GenericQueue<>();
        int i = 1;
        ComplexNumber current = new ComplexNumber();
        while (i <= maxIterations && current.sqrOfMagnitude() <= 4) {
            if (power % 1 == 0) {
                current = current.pow((int)power);
            }
            else current = current.pow(power);
            current = current.add(point);
            queue.add(current);
            i++;
        }
        return queue;

    }

    public FractalType getType() {
        return type;
    }

    /**Re-constructs the array so that it is empty*/
    public void resetRefined() {
       this.refined = new boolean[iterations.length][iterations[0].length];
    }

    public void setAllValues(String[] values) {

        this.maxIterations = Integer.parseInt(values[0]);
        this.power = Double.parseDouble(values[1]);
        this.chunkSize = Integer.parseInt(values[2]);
        this.zoom = Double.parseDouble(values[3]);
        this.centre = new ComplexNumber(values[4]);
        this.zStart = new ComplexNumber(values[5]);

    }

    public void setAllValues(FractalDataSerializable fractalData) {

        this.maxIterations = fractalData.maxIterations;
        this.power = fractalData.power;
        this.chunkSize = fractalData.chunkSize;
        this.zoom = fractalData.zoom;
        this.centre = fractalData.centre;
        this.zStart = fractalData.zStart;
        this.c = fractalData.c;

    }

    public Dimension getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimension dimensions) {

        this.dimensions = dimensions;
        this.iterations = new int[dimensions.height][dimensions.width];
        this.lastResults = new ComplexNumber[dimensions.height][dimensions.width];
        this.refined = new boolean[dimensions.height][dimensions.width];

    }

    public int[][] getIterations() {
        return iterations;
    }

    public void setIterations(int x, int y, int input) {
        this.iterations[y][x] = input;
    }

    public void setIterations(int[][] iterations) {
        this.iterations = iterations;
    }

    public ComplexNumber[][] getLastResults() {
        return lastResults;
    }

    public void setLastResults(ComplexNumber[][] lastResults) {
        this.lastResults = lastResults;
    }

    public boolean[][] getRefined() {
        return refined;
    }

    public void setRefined(boolean[][] refined) {
        this.refined = refined;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getPixelArea() {
        return pixelArea;
    }

    public void setPixelArea(int pixelArea) {
        this.pixelArea = pixelArea;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public ComplexNumber getCentre() {
        return centre;
    }

    public void setCentre(ComplexNumber centre) {
        this.centre = centre;
    }

    public ComplexNumber getzStart() {
        return zStart;
    }

    public void setzStart(ComplexNumber zStart) {
        this.zStart = zStart;
    }

    public ComplexNumber getC() {
        return c;
    }

    public void setC(ComplexNumber c) {
        this.c = c;
    }

}
