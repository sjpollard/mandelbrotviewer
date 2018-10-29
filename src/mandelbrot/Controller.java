package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Controller section of the MVC which deals with user mouse inputs to allow them to control
 * the image of the fractal. The class implements listeners to deal with mouse events and
 * then changes the corresponding values in the MandelbrotSet/JuliaSet objects, so that they
 * draw an altered image. The actions possible with this class are: movement/zooming of the
 * image and tracking of numbers in the complex plane.
 * */

public class Controller implements MouseListener, MouseWheelListener, MouseMotionListener {

    /**Reference to the MandelbrotFrame GUI*/
    private MandelbrotFrame mandelbrotFrame;

    /**Reference to the FractalDiagram of this controller*/
    private FractalDiagram fractalDiagram;
    
    /**Reference to the FractalSet of the linked diagram*/
    FractalSet fractalSet;
    
    /**Data used while dragging the fractal image to a new location*/
    private boolean dragging;
    private Point lastPos;

    /**Sets the reference to the GUI component and adds the listeners to this object*/
    public Controller(MandelbrotFrame mandelbrotFrame, FractalDiagram fractalDiagram) {

        this.mandelbrotFrame = mandelbrotFrame;
        this.fractalDiagram = fractalDiagram;
        this.fractalSet = fractalDiagram.getFractalSet();

        this.fractalDiagram.addMouseListener(this);
        this.fractalDiagram.addMouseWheelListener(this);
        this.fractalDiagram.addMouseMotionListener(this);

    }

    /**Transfers the centre of the current image to the pixel that has been clicked by the user*/
    @Override
    public void mouseClicked(MouseEvent me) {

        mandelbrotFrame.addActionToStack();
        if (me.getButton() == MouseEvent.BUTTON1) {

            ComplexNumber newCentre = fractalSet.pixelToComplexNumber(me.getX(), me.getY());
            fractalSet.setCentre(newCentre);
            if (fractalSet.getType() == FractalType.MANDELBROT) mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);

            mandelbrotFrame.iterateAndDraw();
        }

    }

    /**Ensures that the fractals have focus if the mouse is on them, so MenuBar components don't interfere*/
    @Override
    public void mouseEntered(MouseEvent me) {

        fractalDiagram.grabFocus();

    }

    @Override
    public void mouseExited(MouseEvent me) {


    }

    /**Stores the location of the mouse when it is pressed down*/
    @Override
    public void mousePressed(MouseEvent me) {

        lastPos = new Point(me.getX(), me.getY());

    }

    /**Moves the correct FractalDiagram to the pixel where the mouse button is released*/
    @Override
    public void mouseReleased(MouseEvent me) {

        if (SwingUtilities.isLeftMouseButton(me)) {
            dragging = false;
            fractalDiagram.setImgLocation(new Point());
            mandelbrotFrame.iterateAndDraw();
        }

    }

    /**Whenever the mouse wheel scrolls, the zoom variable of the selected fractal is changed by a common ratio*/
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

        mandelbrotFrame.addActionToStack();

        if (mwe.getWheelRotation() < 0) {
            fractalSet.setZoom(fractalSet.getZoom() * (2 * -mwe.getWheelRotation()));
        }
        else {
            fractalSet.setZoom(fractalSet.getZoom() / (2 * mwe.getWheelRotation()));
        }
        mandelbrotFrame.iterateAndDraw();

    }

    /**Updates the location where the FractalDiagram should be placed when the mouse button is released*/
    @Override
    public void mouseDragged(MouseEvent mme) {

        if (SwingUtilities.isLeftMouseButton(mme)) {

            if (!dragging) {
                mandelbrotFrame.addActionToStack();
                dragging = true;
            }

            ComplexNumber newCentre;
            fractalDiagram.translateImgLocation(new Point(mme.getX() - lastPos.x, mme.getY() - lastPos.y));
            newCentre = calculateNewCentre(fractalSet, mme.getX(), mme.getY(), lastPos);
            fractalSet.setCentre(newCentre);
            if (fractalSet.getType() == FractalType.MANDELBROT) mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);

            mandelbrotFrame.draw();
            lastPos = new Point(mme.getX(), mme.getY());

        }
        else if (SwingUtilities.isRightMouseButton(mme)) {

            fractalDiagram.track(mme.getX(), mme.getY());
            mandelbrotFrame.draw();

        }

    }

    /**Calculates the new centre of the FractalSet by considering how many pixels the image has moved by*/
    private ComplexNumber calculateNewCentre(FractalSet set, int newX, int newY, Point lastPos) {

        ComplexNumber newPos = set.pixelToComplexNumber(newX, newY);
        ComplexNumber diff = newPos.subtract(set.pixelToComplexNumber(lastPos.x, lastPos.y));
        return set.getCentre().subtract(diff);

    }

    @Override
    public void mouseMoved(MouseEvent mme) {



    }

}