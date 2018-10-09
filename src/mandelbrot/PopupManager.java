package mandelbrot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Static class that contains methods used in the MenuBar and manages the generic popups that the
 * program uses. Within these methods, the JFileChooser and JColorChooser default popups are used.
 * To save and load files, the SerializationManager is used to serialize and deserialize the
 * FractalDataSerializable objects.
 */

public class PopupManager {

    /**Lets the user specify a file location and then loads the values stored within*/
    public static void loadValues(MandelbrotFrame mandelbrotFrame) {

        mandelbrotFrame.draw();
        JFileChooser jfc = new JFileChooser(mandelbrotFrame.filePath);
        jfc.setDialogTitle("Load from file");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showOpenDialog(mandelbrotFrame) == JFileChooser.APPROVE_OPTION) {

            SerializationManager.readFromFile(mandelbrotFrame, jfc.getSelectedFile().toString());
            mandelbrotFrame.adjustSlider(mandelbrotFrame.mandelbrotSet.getMaxIterations());
            mandelbrotFrame.chunkSpinner.setValue(mandelbrotFrame.mandelbrotSet.getChunkSize());
            mandelbrotFrame.diagram.mandelbrotDiagram.setFractalSet(mandelbrotFrame.mandelbrotSet);
            mandelbrotFrame.diagram.juliaDiagram.setFractalSet(mandelbrotFrame.mandelbrotSet.juliaSet);

        }
        mandelbrotFrame.iterateAndDraw();

    }

    /**Lets the user specify a file location and then saves the current values to this location*/
    public static void saveValues(MandelbrotFrame mandelbrotFrame) {

        mandelbrotFrame.draw();
        JFileChooser jfc = new JFileChooser(mandelbrotFrame.filePath);
        jfc.setDialogTitle("Save to file");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showSaveDialog(mandelbrotFrame) == JFileChooser.APPROVE_OPTION) {

            SerializationManager.writeToFile(mandelbrotFrame, jfc.getSelectedFile().toString());

        }
        mandelbrotFrame.draw();

    }

    /**Lets the user specify a file location and then saves a png of the current fractal to this location*/
    public static void saveScreenShot(MandelbrotFrame mandelbrotFrame) {

        mandelbrotFrame.draw();
        JFileChooser jfc = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        jfc.setDialogTitle("Save screenshot");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showSaveDialog(mandelbrotFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                File screenShotPath = new File(jfc.getSelectedFile().getPath() + ".png");
                if (mandelbrotFrame.diagram.conditions.drawMandelbrot) {
                    ImageIO.write(mandelbrotFrame.diagram.mandelbrotDiagram.fractalImg, "png", screenShotPath);
                }
                if (mandelbrotFrame.diagram.conditions.drawJulia) {
                    ImageIO.write(mandelbrotFrame.diagram.juliaDiagram.fractalImg, "png", screenShotPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**Allows the user to select whether or not they want to reset the program to its original values*/
    public static void resetValues(MandelbrotFrame mandelbrotFrame) {

        mandelbrotFrame.draw();
        int reset = JOptionPane.showConfirmDialog(mandelbrotFrame, "Are you sure you want to reset?", "Reset", JOptionPane.YES_NO_OPTION);
        if (reset == JOptionPane.YES_OPTION) {
            mandelbrotFrame.createSet();
            mandelbrotFrame.diagram.mandelbrotSet = mandelbrotFrame.mandelbrotSet;
            mandelbrotFrame.diagram.mandelbrotDiagram.setFractalSet(mandelbrotFrame.mandelbrotSet);
            mandelbrotFrame.diagram.juliaDiagram.setFractalSet(mandelbrotFrame.mandelbrotSet.juliaSet);
            mandelbrotFrame.maxIterationsLabel.setText("Max iterations: 100");
            mandelbrotFrame.adjustSlider(100);
            mandelbrotFrame.chunkSpinner.setValue(1);
            mandelbrotFrame.diagram.colours = new FractalColours(Color.RED, Color.BLUE, Color.BLACK);
            mandelbrotFrame.diagram.inverseColour = Color.WHITE;
        }
        mandelbrotFrame.draw();

    }

    /**Allows the user to select a new colour for a specific part of the fractal image*/
    public static void editColour(MandelbrotFrame mandelbrotFrame, String choice) {

        mandelbrotFrame.draw();
        if(choice.equals("outer"))  {
            Color newColour = JColorChooser.showDialog(mandelbrotFrame, "Pick a colour", mandelbrotFrame.diagram.colours.getOuter());
            if (!(newColour == null)) {
                mandelbrotFrame.diagram.colours.setOuter(newColour);
            }
        }
        else if(choice.equals("edge")) {
            Color newColour = JColorChooser.showDialog(mandelbrotFrame, "Pick a colour", mandelbrotFrame.diagram.colours.getEdge());
            if (!(newColour == null)) {
                mandelbrotFrame.diagram.colours.setEdge(newColour);
            }
        }
        else if (choice.equals("inner")) {
            Color newColour = JColorChooser.showDialog(mandelbrotFrame, "Pick a colour", mandelbrotFrame.diagram.colours.getInner());
            if (!(newColour == null)) {
                mandelbrotFrame.diagram.colours.setInner(newColour);
                mandelbrotFrame.diagram.inverseColour = mandelbrotFrame.diagram.invertColour(newColour);
            }
        }
        mandelbrotFrame.diagram.conditions.readyToCreateImage = true;
        mandelbrotFrame.draw();

    }

    /**Displays an error message to the user if they have entered an invalid input*/
    public static void displayValidationErrorMessage(MandelbrotFrame mandelbrotFrame) {

        JOptionPane.showMessageDialog(mandelbrotFrame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);

    }

}
