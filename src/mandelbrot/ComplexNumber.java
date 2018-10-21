package mandelbrot;

import java.io.Serializable;

/**
 * General purpose data type/structure which allows for a double implementation
 * of complex numbers in vector form. This class is capable of addition,
 * subtraction, multiplication and exponentiation. The implementation of this
 * object is centered around use in iteration of the Mandelbrot equation, so
 * it has magnitude checking and capability of cardioid/bulb optimization.
 */

public class ComplexNumber implements Serializable {

    /**Fields that contain the real and imaginary parts of a given complex number*/
    private double real;
    private double imaginary;

    /**Constructs a complex number at the origin*/
    public ComplexNumber() {
        this.real = 0;
        this.imaginary = 0;
    }

    /**Constructs a complex number based on the input values*/
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**Constructs a complex number via a string (x, yi or x+yi forms)*/
    public ComplexNumber(String complexNumber) {
        double real = 0;
        double imaginary = 0;
        if (RegexManager.matchesSignedDouble(complexNumber))  {
            real = Double.parseDouble(complexNumber);
        }
        else if (RegexManager.matchesImaginaryNumber(complexNumber)) {
            imaginary = Double.parseDouble(complexNumber.substring(0, complexNumber.length() - 1));
        }
        else {
            String[] characters = complexNumber.split("");
            int index = 0;
            for (String ch: characters) {
                if (index != 0 && (ch.equals("+") || ch.equals("-"))) break;
                index++;
            }
            real = Double.parseDouble(complexNumber.substring(0, index));
            imaginary = Double.parseDouble(complexNumber.substring(index, complexNumber.length() - 1));
        }
        this.real = real;
        this.imaginary = imaginary;
    }

    /**Adds two complex numbers together vectorially*/
    public ComplexNumber add(ComplexNumber augend) {
        ComplexNumber sum = new ComplexNumber();
        sum.real = this.real + augend.real;
        sum.imaginary = this.imaginary + augend.imaginary;
        return sum;
    }

    /**Subtracts two complex numbers vectorially*/
    public ComplexNumber subtract(ComplexNumber subtrahend) {
        ComplexNumber result = new ComplexNumber();
        result.real = this.real - subtrahend.real;
        result.imaginary = this.imaginary - subtrahend.imaginary;
        return result;
    }

    /**Multiplies two complex numbers vectorially*/
    public ComplexNumber multiply(ComplexNumber cn) {
        ComplexNumber product = new ComplexNumber();
        product.real = this.real * cn.real - this.imaginary * cn.imaginary;
        product.imaginary = this.real * cn.imaginary + cn.real * this.imaginary;
        return product;
    }

    /**Squares a complex number*/
    public ComplexNumber square() {
        ComplexNumber square = new ComplexNumber();
        square.real = this.real * this.real - this.imaginary * this.imaginary;
        square.imaginary = this.real * this.imaginary * 2;
        return square;
    }

    /**Raises a complex number to the power of an integer exponent*/
    public ComplexNumber pow(int power) {
        ComplexNumber result = this;
        for (int i = 1; i < power; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    /**Returns the magnitude of a complex number squared*/
    public double sqrOfMagnitude() {

        double sqrOfMagnitude;
        sqrOfMagnitude = this.real * this.real + this.imaginary * this.imaginary;
        return sqrOfMagnitude;

    }

    /**Calculates the perpendicular distance between two complex numbers*/
    public double distanceBetween(ComplexNumber point) {

        double distanceSquared = Math.pow(this.real - point.real, 2) + Math.pow(this.imaginary - point.imaginary, 2);
        return Math.sqrt(distanceSquared);

    }

    /**Checks whether the complex number sits within the main cardioid of the Mandelbrot set*/
    public boolean isCardioid () {

        double p = Math.sqrt(Math.pow(this.getReal() - 0.25, 2) + Math.pow(this.getImaginary(), 2));
        return this.getReal() < p - (2 * Math.pow(p, 2)) + 0.25;
    }

    /**Checks whether the complex number sits within the period two bulb of the Mandelbrot set*/
    public boolean isPeriodTwoBulb () {

        return Math.pow(this.getReal() + 1, 2) + Math.pow(this.getImaginary(), 2) < 1.0 / 16;
    }

    /**Returns a complex number with strictly positive components*/
    public ComplexNumber abs() {

        return new ComplexNumber(Math.abs(this.real), Math.abs(this.imaginary));

    }

    /**Checks whether two complex numbers are mathematically equivalent*/
    public boolean equals(ComplexNumber cn) {
        return cn.getReal() == this.getReal() && cn.getImaginary() == this.getImaginary();
    }

    /**Outputs the complex number as a string*/
    @Override
    public String toString() {

        String operation = "";
        if (imaginary >= 0) operation = "+";
        return (real + operation + imaginary+ "i");

    }

    /**Outputs the complex number to a given decimal place as a string*/
    public String toString(int dp) {

        String operation = "";
        if ((Math.round(imaginary * (int)Math.pow(10, dp))/Math.pow(10, dp)) >= 0) operation = "+";
        return ((Math.round(real * (int)Math.pow(10, dp))/Math.pow(10, dp)) + operation + (Math.round(imaginary * (int)Math.pow(10, dp))/Math.pow(10, dp)) + "i");

    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

}
