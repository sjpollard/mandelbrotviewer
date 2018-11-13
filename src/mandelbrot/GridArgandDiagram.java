package mandelbrot;

import java.awt.*;

public class GridArgandDiagram extends ArgandDiagram {

    int step;

    public GridArgandDiagram(FractalSet fractalSet, DrawingConditions conditions, FractalColours colours) {

        super(fractalSet, conditions, colours);

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (step < 17 && step > 2) {
            g.setColor(colours.getInverse());
            for (int x = 0; x < this.getWidth(); x += (step - 1)) {

                g.drawLine(x, 0, x, this.getHeight());

            }
            for (int y = 0; y < this.getHeight(); y += (step - 1)) {

                g.drawLine(0, y, this.getWidth(), y);

            }
        }

    }

}
