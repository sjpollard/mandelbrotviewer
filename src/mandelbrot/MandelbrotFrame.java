package mandelbrot;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
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
    JPanel content;

    /**Menu bar components*/
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem screenShotItem;
    JMenuItem quitItem;

    /**Options menu components*/
    JMenu optionsMenu;
    JMenu fractalChoiceMenu;
    ButtonGroup buttonGroup;
    JRadioButtonMenuItem bothButton;
    JRadioButtonMenuItem mandelbrotOnlyButton;
    JRadioButtonMenuItem juliaOnlyButton;
    JMenuItem resetItem;
    JMenuItem settingsItem;
    JCheckBoxMenuItem drawInfoItem;
    JCheckBoxMenuItem drawCoordsItem;
    JCheckBoxMenuItem drawSuccessRefineItem;
    SettingsFrame settingsFrame;

    /**Colour menu components*/
    JMenu colourMenu;
    JMenuItem outerItem;
    JMenuItem edgeItem;
    JMenuItem innerItem;
    JCheckBoxMenuItem paletteItem;
    JCheckBoxMenuItem histogramItem;

    /**Help menu components*/
    JMenuItem helpMenu;
    HelpFrame helpFrame;

    /**Value changer components*/
    JLabel maxIterationsLabel;
    JSlider iterSlider;
    JLabel chunkSizeLabel;
    JSpinner chunkSpinner;

    /**Undo/redo components*/
    JButton undoButton;
    JButton redoButton;
    GenericStack<FractalDataSerializable[]> redoStack;
    GenericStack<FractalDataSerializable[]> undoStack;

    /**Graphical component*/
    FractalContainer diagram;

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
        this.diagram.setupFractalImages(mandelbrotSet);
        this.diagram.conditions.readyToCreateImage = true;
        this.diagram.conditions.readyToColourPalette = true;
        this.successiveRefinementOption = 1;
        this.successiveRefiner = new Thread(new SuccessiveRefiner(this));
        this.setVisible(true);
        this.draw();

    }

    /**Groups up the construction and use of components*/
    public void setupComponents() {

        initialiseComponents();
        addComponents();

        this.add(content);
        this.pack();

    }

    /**Constructs all of the components and objects used*/
    public void initialiseComponents() {

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
        bothButton = new JRadioButtonMenuItem("Both on screen", true);
        mandelbrotOnlyButton = new JRadioButtonMenuItem("Mandelbrot set only");
        juliaOnlyButton = new JRadioButtonMenuItem("Julia set only");
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

        diagram = new FractalContainer();

    }

    /**Adds components to MandelbrotFrame and applies listeners*/
    public void addComponents() {

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
        buttonGroup = new ButtonGroup();
        buttonGroup.add(bothButton);
        buttonGroup.add(mandelbrotOnlyButton);
        buttonGroup.add(juliaOnlyButton);
        fractalChoiceMenu.add(bothButton);
        fractalChoiceMenu.add(mandelbrotOnlyButton);
        fractalChoiceMenu.add(juliaOnlyButton);
        setAllMenuItemsColour(fractalChoiceMenu, Color.white);
        optionsMenu.add(resetItem);
        optionsMenu.add(settingsItem);
        optionsMenu.add(drawInfoItem);
        optionsMenu.add(drawCoordsItem);
        optionsMenu.add(drawSuccessRefineItem);
        setAllMenuItemsColour(optionsMenu, Color.white);

        bothButton.addActionListener(e -> changeFractalDrawn("both"));
        mandelbrotOnlyButton.addActionListener(e -> changeFractalDrawn("mandelbrot"));
        juliaOnlyButton.addActionListener(e -> changeFractalDrawn("julia"));
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
        content.add(diagram, BorderLayout.CENTER);

    }

    /**Sets every menu components background to a specific colour*/
    public void setAllMenuItemsColour(JMenu menu, Color colour) {

        for (Component menuItem: menu.getMenuComponents()) menuItem.setBackground(colour);

    }

    /**Creates a MandelbrotSet object with the default properties*/
    public void createSet() {

        if (diagram.conditions.drawMandelbrot && diagram.conditions.drawJulia) {
            mandelbrotSet = new MandelbrotSet(diagram.getWidth() / 2, diagram.getHeight(), 100, 2, true);
        }
        else {
            mandelbrotSet = new MandelbrotSet(diagram.getWidth(), diagram.getHeight(), 100, 2, true);
        }

        diagram.conditions.readyToCreateImage = true;

    }

    /**Iterate using the SuccessiveRefiner runnable to allow for the resolution to build up over time*/
    public void successivelyRefine() {

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
    public void addGeneralMenuListener(JMenu jmenu){

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
    public void changeFractalDrawn(String option) {

        if (option.equals("both")) {
            diagram.conditions.drawMandelbrot = true;
            diagram.conditions.drawJulia = true;
            mandelbrotSet.setIterations(new int[diagram.getHeight()][diagram.getWidth()/2]);
            mandelbrotSet.setLastResults(new ComplexNumber[diagram.getHeight()][diagram.getWidth()/2]);
            mandelbrotSet.setRefined(new boolean[diagram.getHeight()][diagram.getWidth()/2]);
            mandelbrotSet.juliaSet.setIterations(new int[diagram.getHeight()][diagram.getWidth()/2]);
            mandelbrotSet.juliaSet.setLastResults(new ComplexNumber[diagram.getHeight()][diagram.getWidth()/2]);
            mandelbrotSet.juliaSet.setRefined(new boolean[diagram.getHeight()][diagram.getWidth()/2]);
            diagram.mandelbrotDiagram.fractalImg = new BufferedImage(diagram.getWidth()/2, diagram.getHeight(), BufferedImage.TYPE_INT_RGB);
            diagram.juliaDiagram.fractalImg = new BufferedImage(diagram.getWidth()/2, diagram.getHeight(), BufferedImage.TYPE_INT_RGB);
            diagram.mandelbrotDiagram.setLocation(new Point());
            diagram.juliaDiagram.setLocation(new Point(diagram.getWidth()/2, 0));
        }
        else if (option.equals("mandelbrot")) {
            diagram.conditions.drawMandelbrot = true;
            diagram.conditions.drawJulia = false;
            mandelbrotSet.setIterations(new int[diagram.getHeight()][diagram.getWidth()]);
            mandelbrotSet.setLastResults(new ComplexNumber[diagram.getHeight()][diagram.getWidth()]);
            mandelbrotSet.setRefined(new boolean[diagram.getHeight()][diagram.getWidth()]);
            diagram.mandelbrotDiagram.fractalImg = new BufferedImage(diagram.getWidth(), diagram.getHeight(), BufferedImage.TYPE_INT_RGB);
            diagram.mandelbrotDiagram.setLocation(new Point());
        }
        else if(option.equals("julia")) {
            diagram.conditions.drawMandelbrot = false;
            diagram.conditions.drawJulia = true;
            mandelbrotSet.juliaSet.setIterations(new int[diagram.getHeight()][diagram.getWidth()]);
            mandelbrotSet.juliaSet.setLastResults(new ComplexNumber[diagram.getHeight()][diagram.getWidth()]);
            mandelbrotSet.juliaSet.setRefined(new boolean[diagram.getHeight()][diagram.getWidth()]);
            diagram.juliaDiagram.fractalImg = new BufferedImage(diagram.getWidth(), diagram.getHeight(), BufferedImage.TYPE_INT_RGB);
            diagram.juliaDiagram.setLocation(new Point());
        }
        calculateIterations();
        draw();

    }

    /**Changes whether or not to draw information*/
    public void editDrawInfo() {

        diagram.conditions.readyToDrawInfo = drawInfoItem.getState();
        draw();

    }

    /**Changes whether or not to draw coordinates tracked*/
    public void editDrawCoords() {

        diagram.conditions.readyToDrawCoords = drawCoordsItem.getState();
        draw();

    }

    /**Changes whether or not to use HSV colour scaling*/
    public void editUsePalette() {

        diagram.conditions.readyToColourPalette = paletteItem.getState();
        diagram.conditions.readyToCreateImage = true;
        draw();

    }

    /**Changes whether or not to use histogram colouring*/
    public void editUseHistogram() {

        diagram.conditions.readyToHistogramColour = histogramItem.getState();
        diagram.conditions.readyToCreateImage = true;
        draw();
    }

    /**Changes whether or not to successively refine on iteration*/
    public void editDrawSuccessRefine() {

        if (drawSuccessRefineItem.getState()) successiveRefinementOption = 16;
        else successiveRefinementOption = 1;

    }

    /**Activates when slider is moved and changes the max iterations of the fractal*/
    public void sliderEditMaxIterations() {

        int newValue = iterSlider.getValue();
        if (!(newValue == mandelbrotSet.getMaxIterations())) {
            maxIterationsLabel.setText("Max iterations: " + newValue);
            if (newValue == 0) newValue = 1;
            if (newValue > mandelbrotSet.getMaxIterations()) {
                mandelbrotSet.iterateForwards(newValue);
                mandelbrotSet.juliaSet.iterateForwards(newValue);
                diagram.conditions.readyToCreateImage = true;
            }
            else  {
                mandelbrotSet.setMaxIterations(newValue);
                mandelbrotSet.juliaSet.setMaxIterations(newValue);
                calculateIterations();
            }
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
    public void spinnerEditChunkSize() {

        int newValue = (int)chunkSpinner.getValue();
        if (!(mandelbrotSet.getChunkSize() == newValue)) {
            mandelbrotSet.setChunkSize(newValue);
            mandelbrotSet.juliaSet.setChunkSize(newValue);
        }

    }

    /**Adds the action performed to the undo stack*/
    public void addActionToStack() {

        FractalDataSerializable[] oldData = {new FractalDataSerializable(mandelbrotSet, diagram.colours), new FractalDataSerializable(mandelbrotSet.juliaSet, diagram.colours)};
        undoStack.add(oldData);
        if (!redoStack.isEmpty()) redoStack = new GenericStack<>();

    }

    /**Undoes the last action performed*/
    public void undoAction() {

        if (!undoStack.isEmpty()) {

            FractalDataSerializable[] oldData = {new FractalDataSerializable(mandelbrotSet, diagram.colours), new FractalDataSerializable(mandelbrotSet.juliaSet, diagram.colours)};
            redoStack.add(oldData);
            FractalDataSerializable[] fractalData = undoStack.remove();
            mandelbrotSet.setAllValues(fractalData[0]);
            mandelbrotSet.juliaSet.setAllValues(fractalData[1]);
            calculateIterations();
            draw();

        }

    }

    /**Redoes the last action that was undone*/
    public void redoAction() {

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
    public void calculateIterations() {

        if (diagram.conditions.drawMandelbrot) {
            mandelbrotSet.iterate(false);

        }
        if (diagram.conditions.drawJulia) {
            mandelbrotSet.juliaSet.iterate(false);
        }
        diagram.conditions.readyToCreateImage = true;

    }

    /**Shortened method name for repainting graphics component*/
    public void draw() {

        diagram.mandelbrotDiagram.repaint();
        diagram.conditions.readyToCreateImage = true;
        diagram.juliaDiagram.repaint();

    }

}
