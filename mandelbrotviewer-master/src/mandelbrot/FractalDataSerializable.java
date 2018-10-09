package mandelbrot;

import java.io.Serializable;

/**
 * Reasonably large record that contains information to be Serialized or simply moved through
 * the program together. This class is used when saving and loading files as it is crucial to
 * the Serialization process. As well as this, it is used in the stack operations to allow for
 * undo/redo of user inputs.
 */

public class FractalDataSerializable implements Serializable {

    FractalType type;

    /**Data needed to define a FractalSet*/
    int maxIterations;
    int power;
    int chunkSize;
    double zoom;
    ComplexNumber centre;
    ComplexNumber zStart;
    ComplexNumber c;

    /**Colours of the fractal*/
    FractalColours colours;

    /**Empty constructor*/
    public FractalDataSerializable() {

    }

    /**Takes all of the important values from the input fractal set and stores them for later access*/
    public FractalDataSerializable(FractalSet fractalSet, FractalColours colours) {

        this.type = fractalSet.getType();

        this.maxIterations = fractalSet.getMaxIterations();
        this.power = fractalSet.getPower();
        this.chunkSize = fractalSet.getChunkSize();
        this.zoom = fractalSet.getZoom();
        this.centre = fractalSet.getCentre();
        this.zStart = fractalSet.getzStart();
        this.c = fractalSet.getC();

        this.colours = colours;

    }

}
