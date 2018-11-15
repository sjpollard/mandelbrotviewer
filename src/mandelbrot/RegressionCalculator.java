package mandelbrot;

public class RegressionCalculator {

    double xMean;
    double yMean;
    double sumOfXDeviationSqr;
    double sumOfYDeviationSqr;
    double[] xValues;
    double[] yValues;
    double[] xDeviation;
    double[] yDeviation;

    public RegressionCalculator(double[] xValues, double[] yValues) {

        this.xValues = xValues;
        this.yValues = yValues;
        this.xDeviation = new double[xValues.length];
        this.yDeviation = new double[yValues.length];

        this.xMean = calculateMean(xValues);
        this.yMean = calculateMean(yValues);

        for (int i = 0; i < this.xValues.length; i++) {
            this.xDeviation[i] = xValues[i] - this.xMean;
            this.yDeviation[i] = yValues[i] - this.yMean;
        }

    }

    public double calculateGradient() {

        double correlationCoefficient = calculateCorrelation();
        double standardDevRatio = calculateStandardDev(yValues.length, sumOfYDeviationSqr) / calculateStandardDev(xValues.length, sumOfXDeviationSqr);

        return correlationCoefficient * standardDevRatio;

    }

    public double calculateCorrelation() {

        double sumOfProduct = 0;
        double rootOfProduct = 0;

        for (int i = 0; i < xValues.length; i++) {
            sumOfProduct += xDeviation[i] * yDeviation[i];
            sumOfXDeviationSqr += xDeviation[i] * xDeviation[i];
            sumOfYDeviationSqr += yDeviation[i] * yDeviation[i];
        }

        rootOfProduct = Math.sqrt(sumOfXDeviationSqr * sumOfYDeviationSqr);

        return sumOfProduct / rootOfProduct;

    }

    public double calculateMean(double[] values) {

        double sum = 0;
        for (double value: values)  {
            sum += value;
        }
        return sum/values.length;

    }

    public double calculateStandardDev(double length, double sumOfDeviationSqr) {

        return Math.sqrt(sumOfDeviationSqr/(length - 1));

    }

}
