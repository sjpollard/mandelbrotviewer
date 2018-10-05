package mandelbrot;

import java.io.*;

/**
 * Static class used within the PopupManager that allows for Serialization and Deserialization of
 * the FractalDataSerializable. This is then saved to a file which cannot be read by manually, within
 *
 */

public class SerializationManager {

    public static void readFromFile(MandelbrotFrame mandelbrotFrame, String source) {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source)));
            FractalDataSerializable mandelbrotData = (FractalDataSerializable)inputStream.readObject();
            mandelbrotFrame.mandelbrotSet.setAllValues(mandelbrotData);
            mandelbrotFrame.diagram.colours = mandelbrotData.colours;
            mandelbrotFrame.mandelbrotSet.juliaSet.setAllValues((FractalDataSerializable)inputStream.readObject());
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void writeToFile(MandelbrotFrame mandelbrotFrame, String destination) {

        try {

            ObjectOutputStream outputStream =  new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(destination + ".fractaldata")));
            outputStream.writeObject(new FractalDataSerializable(mandelbrotFrame.mandelbrotSet, mandelbrotFrame.diagram.colours));
            outputStream.reset();
            outputStream.writeObject(new FractalDataSerializable(mandelbrotFrame.mandelbrotSet.juliaSet, mandelbrotFrame.diagram.colours));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
