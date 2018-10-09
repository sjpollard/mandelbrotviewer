package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Secondary JFrame that contains text fields to allow for user input to change different properties
 * of the fractal image. These include: max iterations, chunk size, power, centre and zoom. For the
 * Mandelbrot: zStart can be changed and for the Julia: c can be changed.
 */

public class SettingsFrame extends JFrame {

    /**Main JPanel of the frame*/
    JPanel content;

    /**Value changer components*/
    JTabbedPane tabbedPane;
    JPanel generalTab;
    JPanel mandelbrotTab;
    JPanel juliaTab;
    JPanel buttonPanel;

    JButton applyButton;
    JButton closeButton;

    /**ArrayLists of components to display on each tab*/
    ArrayList<InputPanel> generalPanelList;
    ArrayList<InputPanel> mandelbrotPanelList;
    ArrayList<InputPanel> juliaPanelList;

    /**Constructs a SettingsFrame and allows reference to the MandelbrotFrame*/
    public SettingsFrame(MandelbrotFrame mandelbrotFrame) {

        super("Settings");

        this.setResizable(false);
        this.setBackground(Color.white);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.generalPanelList = new ArrayList<>();
        this.mandelbrotPanelList = new ArrayList<>();
        this.juliaPanelList = new ArrayList<>();

        setupComponents(mandelbrotFrame);

        this.add(content);
        this.pack();
        this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2,(int)(screenSize.getHeight() - this.getHeight())/2);
        this.setVisible(true);

    }

    /**Constructs and adds components to this object*/
    public void setupComponents(MandelbrotFrame mandelbrotFrame) {

        content = new JPanel();
        content.setBackground(Color.white);
        generalTab = new JPanel();
        generalTab.setBackground(Color.white);
        mandelbrotTab = new JPanel();
        mandelbrotTab.setBackground(Color.white);
        juliaTab = new JPanel();
        juliaTab.setBackground(Color.white);
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.white);
        tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.setFont(MandelbrotFrame.headerFont);
        tabbedPane.setBackground(Color.white);
        applyButton = new JButton("Apply");
        closeButton = new JButton("Close");

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        generalTab.setLayout(new BoxLayout(generalTab, BoxLayout.Y_AXIS));
        mandelbrotTab.setLayout(new BoxLayout(mandelbrotTab, BoxLayout.Y_AXIS));
        juliaTab.setLayout(new BoxLayout(juliaTab, BoxLayout.Y_AXIS));

        applyButton.addActionListener(ae -> changeSettings(mandelbrotFrame));
        closeButton.addActionListener(ae -> this.dispose());

        setupGeneralTab(mandelbrotFrame);
        setupMandelbrotTab(mandelbrotFrame);
        setupJuliaTab(mandelbrotFrame);

        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        content.add(tabbedPane);
        content.add(buttonPanel);

    }

    /**Sets up the components for the general values*/
    public void setupGeneralTab(MandelbrotFrame mandelbrotFrame){

        generalPanelList.add(new InputPanel("Max iterations:", String.valueOf(mandelbrotFrame.mandelbrotSet.getMaxIterations())));
        generalPanelList.add(new InputPanel("Power:", String.valueOf(mandelbrotFrame.mandelbrotSet.getPower())));
        generalPanelList.add(new InputPanel("Chunk size:", String.valueOf(mandelbrotFrame.mandelbrotSet.getChunkSize())));
        addInputPanels(generalTab, generalPanelList);
        tabbedPane.addTab("General", generalTab);

    }

    /**Sets up the components for Mandelbrot values*/
    public void setupMandelbrotTab(MandelbrotFrame mandelbrotFrame) {

        mandelbrotPanelList.add(new InputPanel("Zoom:", String.valueOf(mandelbrotFrame.mandelbrotSet.getZoom())));
        mandelbrotPanelList.add(new InputPanel("Centre:", String.valueOf(mandelbrotFrame.mandelbrotSet.getCentre())));
        mandelbrotPanelList.add(new InputPanel("zStart:", String.valueOf(mandelbrotFrame.mandelbrotSet.getzStart())));
        addInputPanels(mandelbrotTab, mandelbrotPanelList);
        tabbedPane.addTab("Mandelbrot set", mandelbrotTab);

    }

    /**Sets up the components for Julia values*/
    public void setupJuliaTab(MandelbrotFrame mandelbrotFrame) {

        juliaPanelList.add(new InputPanel("Zoom:", String.valueOf(mandelbrotFrame.mandelbrotSet.juliaSet.getZoom())));
        juliaPanelList.add(new InputPanel("Centre:", String.valueOf(mandelbrotFrame.mandelbrotSet.juliaSet.getCentre())));
        juliaPanelList.add(new InputPanel("c:", String.valueOf(mandelbrotFrame.mandelbrotSet.juliaSet.getC())));
        addInputPanels(juliaTab, juliaPanelList);
        tabbedPane.addTab("Julia set", juliaTab);

    }

    /**Iterates through the ArrayList to add InputPanels to a specific tab*/
    public void addInputPanels(JPanel parent, ArrayList<InputPanel> inputPanelList) {

        parent.add(Box.createVerticalStrut(10));
        for (InputPanel panel: inputPanelList)  {
            parent.add(panel);
            parent.add(Box.createVerticalStrut(10));
        }

    }

    /**Creates an array of values of the data entered*/
    public String[] createSettingsArray(ArrayList<InputPanel> panelList) {

        String[] settingsArray =  new String[6];
        int i = 0;
        for (InputPanel panel: generalPanelList) {
            settingsArray[i] = panel.getInputFieldText();
            i++;
        }
        for (InputPanel panel: panelList) {
            settingsArray[i] = panel.getInputFieldText();
            i++;
        }

        return settingsArray;

    }

    /**Verifies via the RegexManager class, whether all of the inputs can have valid data extrapolated*/
    public boolean entriesValid() {

        for (InputPanel item: generalPanelList) {
            if (!RegexManager.matchesInteger(item.getInputFieldText())) return false;
        }
        if (!RegexManager.matchesDouble(mandelbrotPanelList.get(0).getInputFieldText())) return false;
        if (!RegexManager.matchesComplexNumber(mandelbrotPanelList.get(1).getInputFieldText())) return false;
        if (!RegexManager.matchesComplexNumber(mandelbrotPanelList.get(2).getInputFieldText())) return false;
        if (!RegexManager.matchesDouble(juliaPanelList.get(0).getInputFieldText())) return false;
        if (!RegexManager.matchesComplexNumber(juliaPanelList.get(1).getInputFieldText())) return false;
        if (!RegexManager.matchesComplexNumber(juliaPanelList.get(2).getInputFieldText())) return false;
        return true;

    }

    /**Retrieves and changes the values entered by the user*/
    public void changeSettings(MandelbrotFrame mandelbrotFrame) {

        if (entriesValid()) {
            mandelbrotFrame.addActionToStack();
            mandelbrotFrame.mandelbrotSet.setAllValues(createSettingsArray(mandelbrotPanelList));
            mandelbrotFrame.mandelbrotSet.juliaSet.setAllValues(createSettingsArray(juliaPanelList));
            mandelbrotFrame.adjustSlider(mandelbrotFrame.mandelbrotSet.getMaxIterations());
            mandelbrotFrame.maxIterationsLabel.setText("Max iterations: " + mandelbrotFrame.mandelbrotSet.getMaxIterations());
            mandelbrotFrame.chunkSpinner.setValue(mandelbrotFrame.mandelbrotSet.getChunkSize());
            mandelbrotFrame.iterateAndDraw();
        }
        else PopupManager.displayValidationErrorMessage(mandelbrotFrame);

    }

}
