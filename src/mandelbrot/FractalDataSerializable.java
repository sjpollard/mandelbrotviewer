package mandelbrot;

import java.io.Serializable;

public class FractalDataSerializable implements Serializable {

    FractalType type;

    int maxIterations;
    int power;
    int chunkSize;
    double zoom;
    ComplexNumber centre;
    ComplexNumber zStart;
    ComplexNumber c;

    FractalColours colours;

    public FractalDataSerializable() {

    }

    public FractalDataSerializable(int maxIterations, int power, int chunkSize, double zoom, ComplexNumber centre, ComplexNumber zStart, ComplexNumber c) {

        this.maxIterations = maxIterations;
        this.power = power;
        this.chunkSize = chunkSize;
        this.zoom = zoom;
        this.centre = centre;
        this.zStart = zStart;
        this.c = c;

    }

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
