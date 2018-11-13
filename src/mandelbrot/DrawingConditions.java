package mandelbrot;

/**
 * Basic record that contains a set of booleans that are needed to properly execute the paintComponent()
 * method. Each boolean decides whether a different part of the final image should be drawn (as decided
 * by the user). Examples are: drawing fractal information, drawing coordinates of tracked points and
 * using the histogram method of drawing.
 */

public class DrawingConditions {

    /**Booleans that equate to drawing conditions*/
    public boolean drawMandelbrot;
    public boolean drawJulia;
    public boolean readyToCreateImage;
    public boolean readyToDrawInfo;
    public boolean readyToDrawCoords;
    public boolean readyToColourPalette;
    public boolean readyToHistogramColour;

    /**Constructs an empty object*/
    public DrawingConditions() {

    }

    public DrawingConditions clone() {

        DrawingConditions clone = new DrawingConditions();
        clone.drawMandelbrot = this.drawMandelbrot;
        clone.drawJulia = this.drawJulia;
        clone.readyToCreateImage = this.readyToCreateImage;
        clone.readyToDrawInfo = this.readyToDrawInfo;
        clone.readyToDrawCoords = this.readyToDrawCoords;
        clone.readyToColourPalette = this.readyToColourPalette;
        clone.readyToHistogramColour = this.readyToHistogramColour;

        return clone;

    }

}
