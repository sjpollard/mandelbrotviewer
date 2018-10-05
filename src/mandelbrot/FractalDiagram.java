package mandelbrot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FractalDiagram {

    BufferedImage fractalImg;

    private FractalSet fractalSet;
    private Point location;
    private DrawingConditions conditions;
    private int[] histogram;

    public FractalDiagram(FractalSet fractalSet, Point location, DrawingConditions conditions) {

        this.fractalImg = new BufferedImage(fractalSet.getIterations()[0].length, fractalSet.getIterations().length, BufferedImage.TYPE_INT_RGB);
        this.fractalSet = fractalSet;
        this.location = location;
        this.conditions = conditions;

    }

    public void createImage(FractalColours colours) {

        fractalImg = new BufferedImage(fractalSet.getIterations()[0].length, fractalSet.getIterations().length, BufferedImage.TYPE_INT_RGB);
        int width = fractalSet.getIterations()[0].length;
        int height = fractalSet.getIterations().length;

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
        conditions.readyToCreateImage = false;

    }

    public int[] fillHistogram(int[][] iterations, int chunkSize, int maxIterations) {

        int[] histogram = new int[maxIterations - 1];
        for (int y = 0; y < iterations.length - chunkSize; y += chunkSize) {
            for (int x = 0; x < iterations[0].length - chunkSize; x += chunkSize) {

                if (iterations[y][x] < maxIterations) histogram[iterations[y][x] - 1] += 1;

            }
        }
        return histogram;

    }

    public double calcScale(int[] histogram, int total, int numIterations) {

        double scale = 0;
        for (int i = 0; i < numIterations; i++) {

            scale += histogram[i];

        }
        return scale/(double)total;

    }

    public void addFilledSquare(int x, int y, int width, Color colour) {

        for (int currentY = y; currentY - y < width && currentY < fractalImg.getHeight(); currentY++) {

            for (int currentX = x; currentX - x < width && currentX < fractalImg.getWidth(); currentX++) {

                fractalImg.setRGB(currentX, currentY, colour.getRGB());

            }

        }

    }

    public Color scalePalette(Color firstColour, double scale) {

        float[] hsbArray = new float[3];
        Color.RGBtoHSB(firstColour.getRed(), firstColour.getGreen(), firstColour.getBlue(), hsbArray);
        hsbArray[0] += scale;
        return new Color(Color.HSBtoRGB(hsbArray[0], hsbArray[1], hsbArray[2]));

    }

    public Color scaleBetweenColours(double scale, Color edgeColour, Color outerColour) {

        Color output;
        int redDif = edgeColour.getRed() - outerColour.getRed();
        int greenDif = edgeColour.getGreen() - outerColour.getGreen();
        int blueDif = edgeColour.getBlue() - outerColour.getBlue();
        output = new Color((int)(outerColour.getRed() + redDif*scale), (int)(outerColour.getGreen() + greenDif*scale), (int)(outerColour.getBlue() + blueDif*scale));
        return output;

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
