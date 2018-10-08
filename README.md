# mandelbrotviewer

<b>mandelbrotviewer</b> is a Java program that plots the Mandelbrot and Julia sets. The GUI was created manually, using Swing components. Inside, the program contains the <b>ComplexNumber</b> object to carry out complex number operations and the <b>MandelbrotSet</b> object to calculate whether points are within the Mandelbrot set. I used the escape-time algorithm to calculate whether points are contained within the set, histogram colouring for proportional colouring of pixels, successive refinement as an improvement to user experience and polar coordinate optimisations for a performance speedup.

## Getting started

To run this program, the JDK is needed. If there is no "saves" file present, one needs to be created at the same level as the src folder of the source code so that fractal saving can work. If this is running as a JAR, created a saves folder in the location of the JAR. Saves are generated with the ".fractaldata" file extension. 
