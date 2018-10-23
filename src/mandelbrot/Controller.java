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
    MandelbrotFrame mandelbrotFrame;

    /**Data used while dragging the fractal image to a new location*/
    boolean dragging;
    private int lastX;
    private int lastY;

    /**Sets the reference to the GUI component and adds the listeners to this object*/
    public Controller(MandelbrotFrame mandelbrotFrame) {

        this.mandelbrotFrame = mandelbrotFrame;
        //this.mandelbrotFrame.diagram.addMouseListener(this);
        //this.mandelbrotFrame.diagram.addMouseWheelListener(this);
        //this.mandelbrotFrame.diagram.addMouseMotionListener(this);
        this.mandelbrotFrame.diagram.mandelbrotDiagram.addMouseListener(this);
        this.mandelbrotFrame.diagram.mandelbrotDiagram.addMouseWheelListener(this);
        this.mandelbrotFrame.diagram.mandelbrotDiagram.addMouseMotionListener(this);
        this.mandelbrotFrame.diagram.juliaDiagram.addMouseListener(this);
        this.mandelbrotFrame.diagram.juliaDiagram.addMouseWheelListener(this);
        this.mandelbrotFrame.diagram.juliaDiagram.addMouseMotionListener(this);

    }

    /**Transfers the centre of the current image to the pixel that has been clicked by the user*/
    @Override
    public void mouseClicked(MouseEvent me) {

        mandelbrotFrame.addActionToStack();
        if (me.getButton() == MouseEvent.BUTTON1) {

            if (mandelbrotFrame.diagram.conditions.drawMandelbrot && mandelbrotFrame.diagram.conditions.drawJulia) {
                if (me.getX() < mandelbrotFrame.diagram.getWidth() / 2) {
                    ComplexNumber newCentre = mandelbrotFrame.mandelbrotSet.pixelToComplexNumber(me.getX(), me.getY());
                    mandelbrotFrame.mandelbrotSet.setCentre(newCentre);
                    mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);
                } else {
                    ComplexNumber newCentre = mandelbrotFrame.mandelbrotSet.juliaSet.pixelToComplexNumber(me.getX() - mandelbrotFrame.diagram.getWidth() / 2, me.getY());
                    mandelbrotFrame.mandelbrotSet.juliaSet.setCentre(newCentre);
                }
            }
            else if (mandelbrotFrame.diagram.conditions.drawMandelbrot) {
                ComplexNumber newCentre = mandelbrotFrame.mandelbrotSet.pixelToComplexNumber(me.getX(), me.getY());
                mandelbrotFrame.mandelbrotSet.setCentre(newCentre);
                mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);
            }
            else if (mandelbrotFrame.diagram.conditions.drawJulia) {
                ComplexNumber newCentre = mandelbrotFrame.mandelbrotSet.juliaSet.pixelToComplexNumber(me.getX(), me.getY());
                mandelbrotFrame.mandelbrotSet.juliaSet.setCentre(newCentre);
            }
            mandelbrotFrame.iterateAndDraw();
        }

    }

    /**Ensures that the fractals have focus if the mouse is on them, so MenuBar components don't interfere*/
    @Override
    public void mouseEntered(MouseEvent me) {


    }

    @Override
    public void mouseExited(MouseEvent me) {


    }

    /**Stores the location of the mouse when it is pressed down*/
    @Override
    public void mousePressed(MouseEvent me) {

        lastX = me.getX();
        lastY = me.getY();

    }

    /**Moves the correct FractalDiagram to the pixel where the mouse button is released*/
    @Override
    public void mouseReleased(MouseEvent me) {

        if (SwingUtilities.isLeftMouseButton(me)) {
            dragging = false;
            if (mandelbrotFrame.diagram.conditions.drawMandelbrot && mandelbrotFrame.diagram.conditions.drawJulia) {
                mandelbrotFrame.diagram.mandelbrotDiagram.setLocation(new Point());
                mandelbrotFrame.diagram.juliaDiagram.setLocation(new Point(mandelbrotFrame.diagram.getWidth()/2, 0));
            }
            else if (mandelbrotFrame.diagram.conditions.drawMandelbrot) {
                mandelbrotFrame.diagram.mandelbrotDiagram.setLocation(new Point());
            }
            else if (mandelbrotFrame.diagram.conditions.drawJulia) {
                mandelbrotFrame.diagram.juliaDiagram.setLocation(new Point());
            }
            mandelbrotFrame.iterateAndDraw();
        }

    }

    /**Whenever the mouse wheel scrolls, the zoom variable of the selected fractal is changed by a common ratio*/
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

        mandelbrotFrame.addActionToStack();
        if (mandelbrotFrame.diagram.conditions.drawMandelbrot && mandelbrotFrame.diagram.conditions.drawJulia) {
            if (mwe.getX() < mandelbrotFrame.getWidth() / 2) {
                if (mwe.getWheelRotation() < 0) {
                    mandelbrotFrame.mandelbrotSet.setZoom(mandelbrotFrame.mandelbrotSet.getZoom() * (2 * -mwe.getWheelRotation()));
                } else {
                    mandelbrotFrame.mandelbrotSet.setZoom(mandelbrotFrame.mandelbrotSet.getZoom() / (2 * mwe.getWheelRotation()));
                }
            } else {
                if (mwe.getWheelRotation() < 0) {
                    mandelbrotFrame.mandelbrotSet.juliaSet.setZoom(mandelbrotFrame.mandelbrotSet.juliaSet.getZoom() * (2 * -mwe.getWheelRotation()));
                } else {
                    mandelbrotFrame.mandelbrotSet.juliaSet.setZoom(mandelbrotFrame.mandelbrotSet.juliaSet.getZoom() / (2 * mwe.getWheelRotation()));
                }
            }
        }
        else if (mandelbrotFrame.diagram.conditions.drawMandelbrot) {
            if (mwe.getWheelRotation() < 0) {
                mandelbrotFrame.mandelbrotSet.setZoom(mandelbrotFrame.mandelbrotSet.getZoom() * (2 * -mwe.getWheelRotation()));
            }
            else {
                mandelbrotFrame.mandelbrotSet.setZoom(mandelbrotFrame.mandelbrotSet.getZoom() / (2 * mwe.getWheelRotation()));
            }
        }
        else if (mandelbrotFrame.diagram.conditions.drawJulia) {
            if (mwe.getWheelRotation() < 0) {
                mandelbrotFrame.mandelbrotSet.juliaSet.setZoom(mandelbrotFrame.mandelbrotSet.juliaSet.getZoom() * (2 * -mwe.getWheelRotation()));
            }
            else {
                mandelbrotFrame.mandelbrotSet.juliaSet.setZoom(mandelbrotFrame.mandelbrotSet.juliaSet.getZoom() / (2 * mwe.getWheelRotation()));
            }
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
            if (mandelbrotFrame.diagram.conditions.drawMandelbrot && mandelbrotFrame.diagram.conditions.drawJulia) {
                if (lastX < mandelbrotFrame.diagram.getWidth() / 2) {
                    mandelbrotFrame.diagram.mandelbrotDiagram.translateLocation(new Point(mme.getX() - lastX, mme.getY() - lastY));
                    newCentre = calculateNewCentre(mandelbrotFrame.mandelbrotSet, mme.getX(), mme.getY(), lastX, lastY);
                    mandelbrotFrame.mandelbrotSet.setCentre(newCentre);
                    mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);
                } else {
                    mandelbrotFrame.diagram.juliaDiagram.translateLocation(new Point(mme.getX() - lastX, mme.getY() - lastY));
                    newCentre = calculateNewCentre(mandelbrotFrame.mandelbrotSet.juliaSet, mme.getX() - mandelbrotFrame.diagram.getWidth() / 2, mme.getY(), lastX - mandelbrotFrame.diagram.getWidth() / 2, lastY);
                    mandelbrotFrame.mandelbrotSet.juliaSet.setCentre(newCentre);
                }
            }
            else if (mandelbrotFrame.diagram.conditions.drawMandelbrot) {
                mandelbrotFrame.diagram.mandelbrotDiagram.translateLocation(new Point(mme.getX() - lastX, mme.getY() - lastY));
                newCentre = calculateNewCentre(mandelbrotFrame.mandelbrotSet, mme.getX(), mme.getY(), lastX, lastY);
                mandelbrotFrame.mandelbrotSet.setCentre(newCentre);
                mandelbrotFrame.mandelbrotSet.juliaSet.setC(newCentre);
            }
            else if (mandelbrotFrame.diagram.conditions.drawJulia) {
                mandelbrotFrame.diagram.juliaDiagram.translateLocation(new Point(mme.getX() - lastX, mme.getY() - lastY));
                newCentre = calculateNewCentre(mandelbrotFrame.mandelbrotSet.juliaSet, mme.getX(), mme.getY(), lastX, lastY);
                mandelbrotFrame.mandelbrotSet.juliaSet.setCentre(newCentre);
            }
            mandelbrotFrame.draw();
            lastX = mme.getX();
            lastY = mme.getY();
        }
        else if (SwingUtilities.isRightMouseButton(mme)) {

            //mandelbrotFrame.diagram.track(mme.getX(), mme.getY());
            mandelbrotFrame.draw();

        }

    }

    /**Calculates the new centre of the FractalSet by considering how many pixels the image has moved by*/
    public ComplexNumber calculateNewCentre(FractalSet set, int newX, int newY, int oldX, int oldY) {

        ComplexNumber newPos = set.pixelToComplexNumber(newX, newY);
        ComplexNumber diff = newPos.subtract(set.pixelToComplexNumber(oldX, oldY));
        return set.getCentre().subtract(diff);

    }

    @Override
    public void mouseMoved(MouseEvent mme) {



    }

}