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

    /**Constructor that passes in input colours*/
    public FractalColours(Color outer, Color edge, Color inner) {

        this.outer = outer;
        this.edge = edge;
        this.inner = inner;

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

    }


}
