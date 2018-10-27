package mandelbrot;

public class MandelbrotViewer {

    /**The main class that starts the program*/
    public static void main(String[] args) {

        //Uses anti-aliasing for swing text
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        //Constructs a GUI window
        MandelbrotFrame mandelbrotFrame = new MandelbrotFrame();

    }

}