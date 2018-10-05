package mandelbrot;

import java.awt.*;
import java.io.Serializable;

public class FractalColours implements Serializable {

    private Color outer;
    private Color edge;
    private Color inner;

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
