package mandelbrot;

/**
 * Custom runnable that can be used to create a thread that allows for this program to iterate using successive
 * refinement. This class calls for iteration at increasing resolutions and displays after each one, allowing for
 * the user to see a developing image of increasing effective resolution. The Thread.sleep() method is used here
 * to allow for the user to be able to actually see the image when it has been created, otherwise only the final
 * image is displayed.
 */

public class SuccessiveRefiner implements Runnable{

    /**Reference to the GUI*/
    private MandelbrotFrame mandelbrotFrame;

    /**Chunk size at which to stop successive refinement*/
    private int breakPoint;

    /**Constructor that sets the fields to the inputs*/
    public SuccessiveRefiner(MandelbrotFrame mandelbrotFrame) {
        this.mandelbrotFrame = mandelbrotFrame;
        this.breakPoint = mandelbrotFrame.mandelbrotSet.getChunkSize();
    }

    /**Method that is called when a thread is created based on this runnable which starts the successive refinement process*/
    @Override
    public void run() {

        for (int i = mandelbrotFrame.successiveRefinementOption; i >= breakPoint; i /= 2) {
            mandelbrotFrame.chunkSpinner.setValue(i);
            mandelbrotFrame.mandelbrotSet.iterate(true);
            mandelbrotFrame.mandelbrotSet.juliaSet.iterate(true);
            mandelbrotFrame.diagram.conditions.readyToCreateImage = true;
            mandelbrotFrame.draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mandelbrotFrame.mandelbrotSet.resetRefined();
        mandelbrotFrame.mandelbrotSet.juliaSet.resetRefined();

    }

}
