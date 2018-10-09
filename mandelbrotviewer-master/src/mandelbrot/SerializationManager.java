package mandelbrot;

import java.io.*;

/**
 * Static class used within the PopupManager that allows for Serialization and Deserialization of
 * the FractalDataSerializable. This is then saved to a file which cannot be read manually. The file
 * extension is 'fractaldata' so as to not be confused with other more common extensions.
 */

public class SerializationManager {

    /**Deserialization method that uses the ObjectInputStream to extract the saved objects from the file*/
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

    /**Serialization method that uses the ObjectOutputStream to create a serialized file*/
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
