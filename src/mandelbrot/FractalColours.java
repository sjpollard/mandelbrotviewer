package mandelbrot;

import java.awt.*;
import java.io.Serializable;

/**
 * Object that stores the three important colours that are used when creating the fractal images.
 * Outer: the colour that starts at the edge of the screen
 * Edge: the colour that is transitioned into
 * Inner: the colour of the complex numbers within the Mandelbrot set
 */

public class FractalColours implements Serializable {

    /**Colour fields*/
    private Color outer;
    private Color edge;
    private Color inner;
    private Color inverse;

    /**Constructor that passes in input colours*/
    public FractalColours(Color outer, Color edge, Color inner) {

        this.outer = outer;
        this.edge = edge;
        this.inner = inner;
        this.inverse = invertColour(inner);

    }

    /**Inverts the RGB values of the input colour and returns the inverted colour*/
    public static Color invertColour(Color colour) {

        return new Color(255 - colour.getRed(), 255 - colour.getGreen(), 255 - colour.getBlue());

    }
    
    /**If colour palette is turned off, this simply returns the correct scaling between two colours*/
    public Color scaleBetweenColours(double scale) {

        Color output;
        int redDif = edge.getRed() - outer.getRed();
        int greenDif = edge.getGreen() - outer.getGreen();
        int blueDif = edge.getBlue() - outer.getBlue();
        output = new Color((int)(outer.getRed() + redDif*scale), (int)(outer.getGreen() + greenDif*scale), (int)(outer.getBlue() + blueDif*scale));
        return output;

    }

    public Color getOuter() {

        return outer;

    }

    public void setOuter(Color outer) {

        this.outer = outer;

    }

    public Color getEdge() {

        return edge;

    }

    public void setEdge(Color edge) {

        this.edge = edge;

    }

    public Color getInner() {

        return inner;

    }

    public void setInner(Color inner) {

        this.inner = inner;
        invertColour(inner);

    }

    public Color getInverse() {

        return inverse;

    }

    public void setInverse(Color inverse) {

        this.inverse = inverse;

    }


}
