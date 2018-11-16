package mandelbrot;

/**
 * Mathematical object that is used to calculate the Pearson Correlation Coefficient and then to find the
 * gradient of the linear regression line that lies along the data points entered. The object functions via
 * two arrays that contain the x and y values respectively.
 */

public class RegressionCalculator {

    /**The respective sums of the deviation squared of the variable*/
    private double sumOfXDeviationSqr;
    private double sumOfYDeviationSqr;

    /**Lists of corresponding input data*/
    private double[] xValues;
    private double[] yValues;

    /**Lists of the deviation for each point*/
    private double[] xDeviation;
    private double[] yDeviation;

    /**Constructs a RegressionCalculator and sets up for calculation of gradient*/
    public RegressionCalculator(double[] xValues, double[] yValues) {

        this.xValues = xValues;
        this.yValues = yValues;
        this.xDeviation = new double[xValues.length];
        this.yDeviation = new double[yValues.length];

        double xMean = calculateMean(xValues);
        double yMean = calculateMean(yValues);

        for (int i = 0; i < this.xValues.length; i++) {
            this.xDeviation[i] = xValues[i] - xMean;
            this.yDeviation[i] = yValues[i] - yMean;
        }

    }

    /**Uses m = r(sy/sx) to calculate the gradient of the linear regression line*/
    public double calculateGradient() {

        double correlationCoefficient = calculateCorrelation();
        double standardDevRatio = calculateStandardDev(yValues.length, sumOfYDeviationSqr) / calculateStandardDev(xValues.length, sumOfXDeviationSqr);

        return correlationCoefficient * standardDevRatio;

    }

    /**Calculates the PMCC for the data entered to find the correlation between x and y*/
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

    /**Calculates the mean of the data entered*/
    private double calculateMean(double[] values) {

        double sum = 0;
        for (double value: values)  {
            sum += value;
        }
        return sum/values.length;

    }

    /**Calculates the population standard deviation of the specified data*/
    private double calculateStandardDev(double length, double sumOfDeviationSqr) {

        return Math.sqrt(sumOfDeviationSqr/(length - 1));

    }

}
