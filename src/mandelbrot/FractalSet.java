package mandelbrot;

import java.awt.*;

/**
 * Interface that dictates the methods required for an object to act as a FractalSet.
 * The methods here can be implemented differently, but they need to achieve the same
 * outcome for an image to be drawn at the end of iterating. To be specific, this
 * interface is for fractals using complex numbers.
 */

public interface FractalSet {

    /**Starts the iteration calculations*/
    void iterate(boolean partOfSuccession);

    /**Calls the correct method of calculation*/
    void assignIterations(int x, int y);

    /**Converts pixel coordinate to complex number*/
    ComplexNumber pixelToComplexNumber(int x, int y);

    /**Converts complex number to pixel coordinate*/
    int[] complexNumberToPixel(ComplexNumber point);

    /**Calculates the number of iterations required to bailout*/
    int numIterations(ComplexNumber zStart, ComplexNumber c, int x, int y);

    /**Increments max iterations by a given value*/
    void partiallyIterate(int change);

    /**Alter iterations based on the new bailout*/
    ComplexNumber stepIterationsUp(ComplexNumber zCurrent, int x, int y, int steps);

    /**Fill queue with values after each iteration of a given point*/
    GenericQueue<ComplexNumber> fillTrackingQueue(ComplexNumber point);

    FractalType getType();

    /**Clones the fractal set to separate reference*/
    FractalSet clone();

    void setAllValues(FractalDataSerializable fractalData);

    Dimension getDimensions();

    void setDimensions(Dimension dimensions);

    int[][] getIterations();

    void setIterations(int x, int y, int input);

    void setIterations(int[][] coordinates);

    ComplexNumber[][] getLastResults();

    void setLastResults(ComplexNumber[][] lastResults);

    boolean[][] getRefined();

    void setRefined(boolean[][] refined);

    int getMaxIterations();

    void setMaxIterations(int maxIterations);

    double getPower();

    void setPower(double power);

    int getChunkSize();

    void setChunkSize(int chunkSize);

    int getPixelArea();

    void setPixelArea(int pixelArea);

    double getZoom();

    void setZoom(double zoom);

    ComplexNumber getCentre();

    void setCentre(ComplexNumber centre);

    ComplexNumber getzStart();

    void setzStart(ComplexNumber zStart);

    ComplexNumber getC();

    void setC(ComplexNumber c);

}
