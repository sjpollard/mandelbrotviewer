package mandelbrot;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * The main GUI component that contains every other component. This object manages everything visual
 * (including calling the iteration/draw functions) and also handles the interactions with the swing components.
 * When the user changes any values, an event is caught and dealt with. The SettingsFrame and HelpFrame objects
 * are used here as additional GUI components.
 */

public class MandelbrotFrame extends JFrame {

    /**Default fonts*/
    static final Font headerFont = new Font(Font.DIALOG, Font.BOLD, 14);
    static final Font normalFont = new Font(Font.DIALOG, Font.PLAIN, 14);

    /**Mandelbrot set object that will be drawn*/
    MandelbrotSet mandelbrotSet;

    String filePath;

    /**Main JPanel of the frame*/
    private JPanel content;

    /**Menu bar components*/
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadItem;
    private JMenuItem saveItem;
    private JMenuItem screenShotItem;
    private JMenuItem quitItem;

    /**Options menu components*/
    private JMenu optionsMenu;
    private JMenu fractalChoiceMenu;
    JCheckBoxMenuItem mandelbrotButton;
    JCheckBoxMenuItem juliaButton;
    private JMenuItem resetItem;
    private JMenuItem settingsItem;
    private JCheckBoxMenuItem drawInfoItem;
    private JCheckBoxMenuItem drawCoordsItem;
    private JCheckBoxMenuItem drawSuccessRefineItem;
    private SettingsFrame settingsFrame;

    /**Colour menu components*/
    private JMenu colourMenu;
    private JMenuItem outerItem;
    private JMenuItem edgeItem;
    private JMenuItem innerItem;
    private JCheckBoxMenuItem paletteItem;
    private JCheckBoxMenuItem histogramItem;

    /**Help menu components*/
    private JMenuItem helpMenu;
    private HelpFrame helpFrame;

    /**Value changer components*/
    JLabel maxIterationsLabel;
    private JSlider iterSlider;
    private JLabel chunkSizeLabel;
    JSpinner chunkSpinner;

    /**Undo/redo components*/
    private JButton undoButton;
    private JButton redoButton;
    private GenericStack<FractalDataSerializable[]> redoStack;
    private GenericStack<FractalDataSerializable[]> undoStack;

    /**Graphical component*/
    FractalContainer fractalContainer;

    private Dimension screenSize;
    private Thread successiveRefiner;
    int successiveRefinementOption;

    /**Constructs a default MandelbrotFrame with max screen size*/
    public MandelbrotFrame() {

        super("Mandelbrot Viewer");
        this.filePath = "saves";
        this.screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        this.setPreferredSize(screenSize);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        this.setFont(MandelbrotFrame.headerFont);
        this.setResizable(true);

        this.setupComponents();
        this.createSet();
        this.fractalContainer.setupFractalImages(mandelbrotSet);
        this.fractalContainer.conditions.readyToCreateImage = true;
        this.fractalContainer.conditions.readyToColourPalette = true;
        this.successiveRefinementOption = 1;
        this.successiveRefiner = new Thread(new SuccessiveRefiner(this));
        this.setVisible(true);

    }

    /**Groups up the construction and use of components*/
    private void setupComponents() {

        initialiseComponents();
        addComponents();

        this.add(content);
        this.pack();

    }

    /**Constructs all of the components and objects used*/
    private void initialiseComponents() {

        content = new JPanel(new BorderLayout());
        content.setPreferredSize(screenSize);

        menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);

        fileMenu = new JMenu("File");
        fileMenu.setFont(MandelbrotFrame.headerFont);
        loadItem = new JMenuItem("Load from file");
        saveItem = new JMenuItem("Save to file");
        screenShotItem = new JMenuItem("Save screenshot");
        quitItem = new JMenuItem(("Quit program"));

        optionsMenu = new JMenu("Options");
        optionsMenu.setFont(MandelbrotFrame.headerFont);
        fractalChoiceMenu = new JMenu("Fractal choice");
        fractalChoiceMenu.setOpaque(true);
        fractalChoiceMenu.setBackground(Color.white);
        mandelbrotButton = new JCheckBoxMenuItem("Mandelbrot set", true);
        juliaButton = new JCheckBoxMenuItem("Julia set", true);
        resetItem = new JMenuItem("Reset values");
        settingsItem = new JMenuItem("Edit fractal settings");
        drawInfoItem = new JCheckBoxMenuItem("Draw information", false);
        drawCoordsItem = new JCheckBoxMenuItem("Draw coordinates when tracking", false);
        drawSuccessRefineItem = new JCheckBoxMenuItem("Draw with successive refinement active", false);

        colourMenu = new JMenu("Colours");
        colourMenu.setFont(MandelbrotFrame.headerFont);
        outerItem = new JMenuItem("Edit outer colour");
        edgeItem = new JMenuItem("Edit edge colour");
        innerItem = new JMenuItem("Edit inner colour");
        paletteItem = new JCheckBoxMenuItem("Use colour palette", true);
        histogramItem = new JCheckBoxMenuItem("Use histogram colouring", false);

        helpMenu = new JMenuItem("Help");
        helpMenu.setFont(MandelbrotFrame.headerFont);
        helpMenu.setBackground(Color.white);
        helpMenu.setMaximumSize(new Dimension(45,43));

        maxIterationsLabel = new JLabel("Max iterations: 100");
        maxIterationsLabel.setFont(MandelbrotFrame.headerFont);
        iterSlider = new JSlider(0, 100, 100);
        chunkSizeLabel = new JLabel("Chunk size: ");
        chunkSizeLabel.setFont(MandelbrotFrame.headerFont);

        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        undoStack = new GenericStack<>();
        redoStack = new GenericStack<>();

        fractalContainer = new FractalContainer(this);

    }

    /**Adds components to MandelbrotFrame and applies listeners*/
    private void addComponents() {

        addGeneralMenuListener(fileMenu);
        addGeneralMenuListener(optionsMenu);
        addGeneralMenuListener(colourMenu);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(screenShotItem);
        fileMenu.add(quitItem);
        setAllMenuItemsColour(fileMenu, Color.white);

        loadItem.addActionListener(ae ->  PopupManager.loadValues(this));
        saveItem.addActionListener(ae -> PopupManager.saveValues(this));
        screenShotItem.addActionListener(ae -> PopupManager.saveScreenShot(this));
        quitItem.addActionListener(ae -> System.exit(0));

        optionsMenu.add(fractalChoiceMenu);
        fractalChoiceMenu.add(mandelbrotButton);
        fractalChoiceMenu.add(juliaButton);
        setAllMenuItemsColour(fractalChoiceMenu, Color.white);

        optionsMenu.add(resetItem);
        optionsMenu.add(settingsItem);
        optionsMenu.add(drawInfoItem);
        optionsMenu.add(drawCoordsItem);
        optionsMenu.add(drawSuccessRefineItem);
        setAllMenuItemsColour(optionsMenu, Color.white);

        mandelbrotButton.addActionListener(this::changeFractalConditions);
        juliaButton.addActionListener(this::changeFractalConditions);
        resetItem.addActionListener(ae -> PopupManager.resetValues(this));
        settingsItem.addActionListener(ae -> {
            if (settingsFrame != null) settingsFrame.dispose();
            settingsFrame = new SettingsFrame(this);
        });
        drawInfoItem.addActionListener(ae -> editDrawInfo());
        drawCoordsItem.addActionListener(ae -> editDrawCoords());
        drawSuccessRefineItem.addActionListener(ae -> editDrawSuccessRefine());

        colourMenu.add(outerItem);
        colourMenu.add(edgeItem);
        colourMenu.add(innerItem);
        colourMenu.add(paletteItem);
        colourMenu.add(histogramItem);
        setAllMenuItemsColour(colourMenu, Color.white);

        outerItem.addActionListener(ae -> PopupManager.editColour(this,"outer"));
        edgeItem.addActionListener(ae -> PopupManager.editColour(this, "edge"));
        innerItem.addActionListener(ae -> PopupManager.editColour(this, "inner"));
        paletteItem.addActionListener(ae -> editUsePalette());
        histogramItem.addActionListener(ae -> editUseHistogram());

        helpMenu.addActionListener(ae -> {
            if (helpFrame != null) helpFrame.dispose();
            helpFrame = new HelpFrame();
        });

        iterSlider.setMinorTickSpacing(1);
        iterSlider.setMajorTickSpacing(10);
        iterSlider.setPaintTicks(true);
        iterSlider.setPaintLabels(true);
        iterSlider.setBackground(Color.white);
        iterSlider.setMaximumSize(new Dimension(1200, 45));
        iterSlider.setFocusable(false);
        iterSlider.addChangeListener(ce -> sliderEditMaxIterations());

        chunkSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 16, 1));
        chunkSpinner.setMaximumSize(new Dimension(50, 23));
        chunkSpinner.addChangeListener(ce -> spinnerEditChunkSize());

        undoButton.addActionListener(ae -> undoAction());
        redoButton.addActionListener(ae -> redoAction());

        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(colourMenu);
        menuBar.add(helpMenu);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(maxIterationsLabel);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(iterSlider);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(chunkSizeLabel);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(chunkSpinner);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(undoButton);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(redoButton);

        content.add(menuBar, BorderLayout.NORTH);
        content.add(fractalContainer, BorderLayout.CENTER);

    }

    /**Sets every menu components background to a specific colour*/
    private void setAllMenuItemsColour(JMenu menu, Color colour) {

        for (Component menuItem: menu.getMenuComponents()) menuItem.setBackground(colour);

    }

    /**Creates a MandelbrotSet object with the default properties*/
    public void createSet() {

        if (fractalContainer.conditions.drawMandelbrot && fractalContainer.conditions.drawJulia) {
            mandelbrotSet = new MandelbrotSet(fractalContainer.getWidth() / 2, fractalContainer.getHeight(), 100, 2, true);
        }
        else {
            mandelbrotSet = new MandelbrotSet(fractalContainer.getWidth(), fractalContainer.getHeight(), 100, 2, true);
        }

        fractalContainer.conditions.readyToCreateImage = true;

    }

    /**Iterate using the SuccessiveRefiner runnable to allow for the resolution to build up over time*/
    private void successivelyRefine() {

        try {
            successiveRefiner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        successiveRefiner = new Thread(new SuccessiveRefiner(this));
        successiveRefiner.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**Ensuring that opening a menu doesn't unload the image*/
    private void addGeneralMenuListener(JMenu jmenu){

        jmenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {

            }

            @Override
            public void menuDeselected(MenuEvent e) {
                draw();
            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }

        });

    }

    /**Sets everything ready to re-iterate and display a different fractal*/
    public void changeFractalConditions(ActionEvent ae) {

        if (!mandelbrotButton.getState() && !juliaButton.getState()) {
            ((JCheckBoxMenuItem)ae.getSource()).setState(true);
        }
        fractalContainer.conditions.drawMandelbrot = mandelbrotButton.getState();
        fractalContainer.conditions.drawJulia = juliaButton.getState();

        changeFractalDrawn();

    }

    public void changeFractalDrawn() {

        int width = fractalContainer.getWidth();
        int height = fractalContainer.getHeight();

        if (fractalContainer.conditions.drawMandelbrot && fractalContainer.conditions.drawJulia) {
            mandelbrotSet.setDimensions(new Dimension(width/2, height));
            mandelbrotSet.juliaSet.setDimensions(new Dimension(width/2, height));
            fractalContainer.mandelbrotDiagram.fractalImg = new BufferedImage(width/2, height, BufferedImage.TYPE_INT_RGB);
            fractalContainer.juliaDiagram.fractalImg = new BufferedImage(width/2, height, BufferedImage.TYPE_INT_RGB);
            fractalContainer.mandelbrotDiagram.setLocation(0,0);
            fractalContainer.mandelbrotDiagram.setSize(width/2, height);
            fractalContainer.juliaDiagram.setLocation(width/2, 0);
            fractalContainer.juliaDiagram.setSize(width/2, height);
            fractalContainer.add(fractalContainer.mandelbrotDiagram);
            fractalContainer.add(fractalContainer.juliaDiagram);
        }
        else if (fractalContainer.conditions.drawMandelbrot) {
            mandelbrotSet.setDimensions(new Dimension(width, height));
            fractalContainer.mandelbrotDiagram.fractalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            fractalContainer.mandelbrotDiagram.setLocation(0,0);
            fractalContainer.mandelbrotDiagram.setSize(width, height);
            fractalContainer.remove(fractalContainer.juliaDiagram);
            fractalContainer.add(fractalContainer.mandelbrotDiagram);
        }
        else if(fractalContainer.conditions.drawJulia) {
            mandelbrotSet.juliaSet.setDimensions(new Dimension(width, height));
            fractalContainer.juliaDiagram.fractalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            fractalContainer.juliaDiagram.setLocation(0,0);
            fractalContainer.juliaDiagram.setSize(width, height);
            fractalContainer.remove(fractalContainer.mandelbrotDiagram);
            fractalContainer.add(fractalContainer.juliaDiagram);
        }
        calculateIterations();
        draw();

    }

    /**Changes whether or not to draw information*/
    private void editDrawInfo() {

        fractalContainer.conditions.readyToDrawInfo = drawInfoItem.getState();
        draw();

    }

    /**Changes whether or not to draw coordinates tracked*/
    private void editDrawCoords() {

        fractalContainer.conditions.readyToDrawCoords = drawCoordsItem.getState();
        draw();

    }

    /**Changes whether or not to use HSV colour scaling*/
    private void editUsePalette() {

        fractalContainer.conditions.readyToColourPalette = paletteItem.getState();
        fractalContainer.conditions.readyToCreateImage = true;
        draw();

    }

    /**Changes whether or not to use histogram colouring*/
    private void editUseHistogram() {

        fractalContainer.conditions.readyToHistogramColour = histogramItem.getState();
        fractalContainer.conditions.readyToCreateImage = true;
        draw();
    }

    /**Changes whether or not to successively refine on iteration*/
    private void editDrawSuccessRefine() {

        if (drawSuccessRefineItem.getState()) successiveRefinementOption = 16;
        else successiveRefinementOption = 1;

    }

    /**Activates when slider is moved and changes the max iterations of the fractal*/
    private void sliderEditMaxIterations() {

        int newValue = iterSlider.getValue();
        if (newValue != mandelbrotSet.getMaxIterations()) {
            maxIterationsLabel.setText("Max iterations: " + newValue);
            if (newValue == 0) newValue = 1;
            int change = newValue - mandelbrotSet.getMaxIterations();
            mandelbrotSet.partiallyIterate(change);
            mandelbrotSet.juliaSet.partiallyIterate(change);
            fractalContainer.conditions.readyToCreateImage = true;
            /*else {
                mandelbrotSet.setMaxIterations(newValue);
                mandelbrotSet.juliaSet.setMaxIterations(newValue);
                calculateIterations();
            }*/
        }
        draw();

    }

    /**Generates the correct ticks and separations for the slider*/
    public void adjustSlider(int newMaxIterations) {

        iterSlider.setMaximum(newMaxIterations);
        iterSlider.setValue(newMaxIterations);
        int largeSpacing = (int)Math.ceil(newMaxIterations/10.0);
        iterSlider.setMinorTickSpacing((int)Math.ceil(largeSpacing/10.0));
        iterSlider.setMajorTickSpacing(largeSpacing);
        iterSlider.setLabelTable(iterSlider.createStandardLabels(largeSpacing));

    }

    /**Activates when spinner is used and changes the chunk size of the fractal*/
    private void spinnerEditChunkSize() {

        int newValue = (int)chunkSpinner.getValue();
        if (mandelbrotSet.getChunkSize() != newValue) {
            mandelbrotSet.setChunkSize(newValue);
            mandelbrotSet.juliaSet.setChunkSize(newValue);
        }

    }

    /**Adds the action performed to the undo stack*/
    public void addActionToStack() {

        FractalDataSerializable[] oldData = {new FractalDataSerializable(mandelbrotSet, fractalContainer.colours), new FractalDataSerializable(mandelbrotSet.juliaSet, fractalContainer.colours)};
        undoStack.add(oldData);
        if (!redoStack.isEmpty()) redoStack = new GenericStack<>();

    }

    /**Undoes the last action performed*/
    private void undoAction() {

        if (!undoStack.isEmpty()) {

            FractalDataSerializable[] oldData = {new FractalDataSerializable(mandelbrotSet, fractalContainer.colours), new FractalDataSerializable(mandelbrotSet.juliaSet, fractalContainer.colours)};
            redoStack.add(oldData);
            FractalDataSerializable[] fractalData = undoStack.remove();
            mandelbrotSet.setAllValues(fractalData[0]);
            mandelbrotSet.juliaSet.setAllValues(fractalData[1]);
            calculateIterations();
            draw();

        }

    }

    /**Redoes the last action that was undone*/
    private void redoAction() {

        if (!redoStack.isEmpty()) {

            FractalDataSerializable[] fractalData = redoStack.remove();
            mandelbrotSet.setAllValues(fractalData[0]);
            mandelbrotSet.juliaSet.setAllValues(fractalData[1]);
            undoStack.add(fractalData);
            calculateIterations();
            draw();

        }

    }

    /**Either successively refines or iterates normally*/
    public void iterateAndDraw() {

        if (this.successiveRefinementOption > 1) {
            this.successivelyRefine();
        }
        else {
            this.calculateIterations();
            this.draw();
        }

    }

    /**Iterates normally*/
    private void calculateIterations() {

        if (fractalContainer.conditions.drawMandelbrot) {
            mandelbrotSet.iterate(false);
        }
        if (fractalContainer.conditions.drawJulia) {
            mandelbrotSet.juliaSet.iterate(false);
        }
        fractalContainer.conditions.readyToCreateImage = true;

    }

    /**Shortened method name for repainting graphics component*/
    public void draw() {

        fractalContainer.drawImages();

    }

}
