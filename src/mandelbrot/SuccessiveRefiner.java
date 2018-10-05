package mandelbrot;

public class SuccessiveRefiner implements Runnable{

    private MandelbrotFrame mandelbrotFrame;
    private int breakPoint;

    public SuccessiveRefiner(MandelbrotFrame GUI) {
        this.mandelbrotFrame = GUI;
        this.breakPoint = GUI.mandelbrotSet.getChunkSize();
    }

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
