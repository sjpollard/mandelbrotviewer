package mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Secondary JFrame that contains information and details about the Mandelbrot/Julia sets.
 * If the user wants to find any extra information about the maths behind the fractal or
 * just want to better understand how the program works, they can open this JFrame.
 * The glossary tab contains an alphabetical list of definitions that relate to this program
 * which can be searched through to find keywords.
 */

public class HelpFrame extends JFrame {

    /**Main JPanel of the frame*/
    private JPanel content;

    /**Tabbed pane that holds the three other tabs*/
    private JTabbedPane tabbedPane;

    /**Components of the instructions tab*/
    private JPanel instructionsTab;
    private JScrollPane instructionsScrollTab;
    private JEditorPane instructionsTextPane;

    /**Components of the theory tab*/
    private JPanel theoryTab;
    private JScrollPane theoryScrollTab;
    private JEditorPane theoryTextPane;

    /**Components of the glossary tab*/
    private JPanel glossaryTab;
    private JPanel searchPanel;
    private JLabel searchLabel;
    private JTextField searchBar;
    private JScrollPane glossaryScrollTab;
    private JEditorPane glossaryTextPane;
    private SortedSet<InfoItem> defaultGlossaryItems;

    private String mandelbrotEquation = "z\u2099\u208A\u2081 = z\u2099\u00B2 + c";
    private String argandDiagram = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Mandelset_hires.png/800px-Mandelset_hires.png";
    private String douadyRabbit = "http://mathworld.wolfram.com/images/eps-gif/DouadysRabbitFractal_1000.gif";

    /**Constructs a HelpFrame*/
    public HelpFrame() {

        super("Help");

        this.setSize(800, 600);
        this.setResizable(true);
        this.setIconImage(new ImageIcon("src\\images\\icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setupComponents();

        this.setLocation((int)(screenSize.getWidth() - this.getWidth())/2,(int)(screenSize.getHeight() - this.getHeight())/2);
        this.setVisible(true);

    }

    /**Constructs and adds the components to the HelpFrame*/
    private void setupComponents() {

        content = new JPanel(new BorderLayout());
        content.setBackground(Color.white);
        instructionsTab = new JPanel();
        theoryTab = new JPanel();
        theoryTab.setBackground(Color.white);
        glossaryTab = new JPanel();
        glossaryTab.setBackground(Color.white);
        tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.setBackground(Color.white);
        tabbedPane.setFont(MandelbrotFrame.headerFont);

        setupInstructionsTab();
        tabbedPane.add("Instructions", instructionsTab);
        setupGlossaryTab();
        tabbedPane.add("Theory", theoryTab);
        setupTheoryTab();
        tabbedPane.add("Glossary", glossaryTab);

        content.add(tabbedPane, BorderLayout.CENTER);
        this.add(content);
    }

    /**Sets up the instructions tab with appropriate text*/
    private void setupInstructionsTab() {

        instructionsTab.setLayout(new BorderLayout());

        instructionsTextPane = new JEditorPane("text/html", "");
        instructionsTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        instructionsTextPane.setFont(MandelbrotFrame.normalFont);
        instructionsTextPane.setEditable(false);
        instructionsTextPane.setMargin(new Insets(20,50,20,50));
        instructionsTextPane.setText("<font size=5><b>Introduction:</b></font> <br>" +
                "Welcome to the Mandelbrot Viewer program. This is programmed in Java and allows you to render and explore the famous Mandelbrot and Julia sets. " +
                "Along the top of the screen, you will find the tools necessary to utilise this program. The file menu allows for fractal settings to be saved to " +
                "a text file and be loaded later on. Next, the options menu allows you to change specific values for rendering and some other interesting features. " +
                "Finally, the colour menu allows you to customise the image by choosing the colours of sections of the set. " +
                "Along the top menu bar, there is a slider that allows for the user to select the number of times the image is refined before outputting. " +
                "As well as this, there is a spinner that allows you to increase performance where necessary by rendering at an effectively lower resolution. <br><br>" +
                "<font size=5><b>Tips:</b></font> <br>" +
                "\u2022 If you are running this as a Jar file, then you should need to create a folder called \"saves\" where the Jar is located. <br>" +
                "\u2022 When inputting complex numbers, it is important to remember to enter them in rectangular form (x+yi). <br>" +
                "\u2022 The deeper you zoom in, the more you should increase chunk size as this will lead to a smoother experience. <br>" +
                "\u2022 Using histogram colouring is useful for when max iterations is high or you are zoomed deep, as it retains a proportional colour palette. <br>" +
                "\u2022 If you use successive refinement, do not overload the inputs because the thread may struggle to process it all. <br><br>" +
                "<font size=5><b>Controls:</b></font> <br>" +
                "<b>Left mouse click:</b> Moves the focus of the image to the position clicked.<br>" +
                "<b>Left mouse hold:</b> Drags the image towards where the cursor is heading.<br>" +
                "<b>Right mouse hold:</b> Tracks the behaviour of the Complex Number at the cursor by drawing path lines.<br>" +
                "<b>Mouse wheel scroll forward:</b> Zooms into the image by the given scale factor.<br>" +
                "<b>Mouse wheel scroll backward:</b> Zooms out of the image by the given scale factor.");

        instructionsTextPane.setCaretPosition(0);
        instructionsScrollTab = new JScrollPane(instructionsTextPane);
        instructionsScrollTab.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        instructionsTab.add(instructionsScrollTab, BorderLayout.CENTER);

    }

    /**Sets up the theory tab with appropriate text*/
    private void setupTheoryTab() {

        theoryTab.setLayout(new BorderLayout());

        theoryTextPane = new JEditorPane("text/html", "");
        theoryTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        theoryTextPane.setFont(MandelbrotFrame.normalFont);
        theoryTextPane.setEditable(false);
        theoryTextPane.setMargin(new Insets(20,50,20,50));

        theoryTextPane.setText("<font size=5><b>The Mandelbrot set:</b></font> <br>" +
                "The image of the Mandelbrot set is a collection of complex numbers coloured in on an Argand diagram (the complex plane) to varying degrees of colour. In truth, the set is simply the " +
                "somewhat boring black centre of the classical image but with some colour scaling, a more interesting image can be developed. Definitively, the Mandelbrot set is the collection of complex numbers(c) " +
                "that do not diverge when passed into the function " + mandelbrotEquation + " repeatedly. In layman's terms, this means it is the numbers that <i>don't</i> eventually get really big.<br>" +
                "<img src=" + argandDiagram + " width=400 height=293></img><br>" +
                "As can be seen in the image above, the Mandelbrot set sits inside of a circle of radius 2 and it is known that if a complex number's magnitude ever exceeds 2, it won't be part of the Mandelbrot set. " +
                "The method to create this image is called the escape-time algorithm and is fairly efficient as the only complex numbers that are processed until the max iterations are the members of the Mandelbrot set. " +
                "The general rules followed while calculating the numbers in the Mandelbrot set are: <br><br>" +
                "<b>1.</b> For a given image size, convert each pixel to its respective complex number in the complex plane. This complex number is c. <br>" +
                "<b>2.</b> Where z\u2080 = 0, pass each complex number(c) into " + mandelbrotEquation + " and break when the magnitude of z is greater than 2. <br>" +
                "<b>3.</b> Colour the point based on the number of times z was passed through the equation. <br><br>" +
                "<b>Example:</b><br>" +
                "Say that we want to process the complex number -0.5 + 0.5i for 5 iterations, we will simply pass it into " + mandelbrotEquation + " 5 times. <br><br>" +
                "<b>Iteration 1:</b> -0.5 + 0.5i <br>" +
                "<b>Iteration 2:</b> -0.5 + 0.0i <br>" +
                "<b>Iteration 3:</b> -0.25 + 0.5i <br>" +
                "<b>Iteration 4:</b> -0.6875 + 0.25i <br>" +
                "<b>Iteration 5:</b> -0.08984375 + 0.15625i <br><br>" +
                "The magnitude of this complex number is only 0.18024 and so it is in the Mandelbrot set, because it did not increase to a size past 2. <br><br>" +
                "<font size=5><b>The Julia set:</b></font> <br> " +
                "The Julia sets are very similar to the Mandelbrot set and are intrinsically linked. Every point within the Mandelbrot set has a \"filled\" Julia set, which seem to all partially make up the shape of the Mandelbrot set. " +
                "If the point is outside of the Mandelbrot set, then the Julia set appears to be a \"dust\" without any real body. Definitively, the Julia set, is the collection of complex numbers(z\u2080) that do not diverge when " +
                "iterated upon in " + mandelbrotEquation + ". <br>" +
                "<img src= "+ douadyRabbit + "></img><br> " +
                "The image above is an example of the \"Douady rabbit\" Julia set which has been coloured in. The general rules followed while calculating the numbers in a Julia set are: <br><br>" +
                "<b>1.</b> For a given image size, convert each pixel to it's respective complex number in the complex plane. This complex number is z\u2080. <br>" +
                "<b>2.</b> Where c = a chosen complex number, pass each complex number(z\u2080) into " + mandelbrotEquation + " and break when the magnitude of z is greater than 2. <br>" +
                "<b>3.</b> Colour the point based on the number of times z was passed through the equation. <br><br>");
        theoryTextPane.setCaretPosition(0);

        theoryScrollTab = new JScrollPane(theoryTextPane);
        theoryScrollTab.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        theoryTab.add(theoryScrollTab, BorderLayout.CENTER);

    }

    /**Sets up the glossary tab with the appropriate text*/
    private void setupGlossaryTab() {

        glossaryTab.setLayout(new BoxLayout(glossaryTab, BoxLayout.PAGE_AXIS));
        glossaryTab.setBorder(BorderFactory.createLineBorder(Color.gray));

        searchPanel= new JPanel(new FlowLayout());
        searchPanel.setBackground(Color.white);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 33));
        searchLabel = new JLabel("Search: ");
        searchLabel.setFont(MandelbrotFrame.headerFont);
        searchBar = new JTextField(20);
        searchBar.setFont(MandelbrotFrame.normalFont);
        searchPanel.add(searchLabel);
        searchPanel.add(searchBar);

        glossaryTextPane = new JEditorPane("text/html", "");
        glossaryTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        glossaryTextPane.setFont(MandelbrotFrame.normalFont);
        glossaryTextPane.setEditable(false);
        glossaryTextPane.setMargin(new Insets(20,50,20,50));

        defaultGlossaryItems = new TreeSet<>();
        fillTreeSet();

        searchThroughGlossary();

        glossaryScrollTab = new JScrollPane(glossaryTextPane);
        glossaryScrollTab.setBorder(BorderFactory.createLineBorder(Color.white));
        glossaryScrollTab.setBackground(Color.white);
        glossaryScrollTab.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        glossaryTab.add(searchPanel);
        glossaryTab.add(glossaryScrollTab);

        searchBar.addActionListener(ae -> searchThroughGlossary());

    }

    /**Adds the glossary items currently searched for to the glossary text pane*/
    private void addGlossaryItems(GenericQueue<InfoItem> currentGlossaryItems) {

        StringBuilder contentText = new StringBuilder();
        for (InfoItem item: currentGlossaryItems) contentText.append(item.getBoldTitle()).append(" ").append(item.getExplanation()).append("<br><br>");
        glossaryTextPane.setText("<font size=5><b>Definitions:</b></font><br>" + contentText.toString());
        glossaryTextPane.setCaretPosition(0);

    }

    /**Searches through the glossary items to find those containing the string parameter*/
    private void searchThroughGlossary() {

        String searchParameter = searchBar.getText().toLowerCase();
        GenericQueue<InfoItem> searchedItems = new GenericQueue<>();

        glossaryTextPane.setText("");

        for (InfoItem glossaryItem: defaultGlossaryItems) {
            if (glossaryItem.getTitle().toLowerCase().contains(searchParameter) || glossaryItem.getExplanation().toLowerCase().contains(searchParameter)) {
                searchedItems.add(glossaryItem);
            }
        }

        addGlossaryItems(searchedItems);

    }

    /**Constructs and adds the default glossary items to a SortedSet*/
    private void fillTreeSet() {

        defaultGlossaryItems.add(new InfoItem("Complex number", "An extension of the real numbers that includes a real and imaginary part."));
        defaultGlossaryItems.add(new InfoItem("Argand diagram", "A graphical method of displaying complex numbers where the x and y axes represent the real and imaginary number lines respectively."));
        defaultGlossaryItems.add(new InfoItem("Magnitude", "The absolute size of the complex number, which is calculated using Pythagoras' theorem with the real and imaginary values."));
        defaultGlossaryItems.add(new InfoItem("Argument", "The angle that the complex number makes with the positive x-axis, which is calculated using the tangent function with the real and imaginary values."));
        defaultGlossaryItems.add(new InfoItem("i", "The unit of imaginary numbers that is equal to the impossible value of \u221A-1"));
        defaultGlossaryItems.add(new InfoItem("Rectangular form", "A method of displaying complex numbers as a sum of the real(x) and imaginary(y) parts: (x + yi)."));
        defaultGlossaryItems.add(new InfoItem("Polar form", "A method of displaying complex numbers with trigonometry using their magnitude(r) and argument(\u03B8): r(cos(\u03B8)+isin(\u03B8))"));
        defaultGlossaryItems.add(new InfoItem("Fractal", "An image that is recursive and infinitely deep, usually with striking details and self-similarity."));
        defaultGlossaryItems.add(new InfoItem("Max iterations", "The maximum number of times that a complex number passes through the Mandelbrot equation and in effect a measure of the image detail."));
        defaultGlossaryItems.add(new InfoItem("Mandelbrot set", "The set of complex numbers(c) which do not diverge when iterated by " + mandelbrotEquation + " and z always starts at 0. It is mainly famous for generating an intricate self-similar fractal with \"baroque\" style patterns. As complex numbers behave chaotically with the Mandelbrot set equation, it is also linked to Chaos theory."));
        defaultGlossaryItems.add(new InfoItem("Julia set","The Julia set of a complex number(c) is the set complex numbers(z) which do not diverge when iterated by f(z) = z\u00B2 + c."));
        defaultGlossaryItems.add(new InfoItem("Multibrot set", "A version of the Mandelbrot set where the power that z is raised to is greater than 2."));
        defaultGlossaryItems.add(new InfoItem("Successive refinement","The system where the effective resolution of rendering increases over time to allow for an image to be developed faster for the user."));
        defaultGlossaryItems.add(new InfoItem("Chunk size", "The size of each \"pixel\" in the fractal image which means that the larger the chunk size, the faster the image can be developed."));
        defaultGlossaryItems.add(new InfoItem("Real numbers", "The range of numbers that includes all integers, decimals and irrational values."));
        defaultGlossaryItems.add(new InfoItem("Imaginary numbers", "The range of numbers that do not exist within the reals and are multiples of i."));
        defaultGlossaryItems.add(new InfoItem("Iterate", "Repeating a process a given number of times (e.g. repeatedly passing a complex number through the Mandelbrot equation)."));
        defaultGlossaryItems.add(new InfoItem("Benoit Mandelbrot", "A Polish mathematician (20 November 1924 – 14 October 2010) who coined the word \"fractal\" and generated the first digital image of the Mandelbrot set with an IBM computer. He popularised fractals and the \"art of roughness\", especially in reference to real life objects like coastlines."));
        defaultGlossaryItems.add(new InfoItem("Gaston Julia", "A French mathematician (3 February 1893 – 19 March 1978) who devised the formula for Julia sets and was widely forgotten about until Mandelbrot mentioned him in his own works. His studies were interrupted when he was conscripted into the army and he later lost his nose in an attack."));
        defaultGlossaryItems.add(new InfoItem("Escape-time algorithm", "A method of calculating when complex numbers are in the Mandelbrot set where if the number does not iterate to a magnitude greater than 2, it is included."));
        defaultGlossaryItems.add(new InfoItem("Pixel percentage", "The percentage of pixels in the image that are included within the set."));
        defaultGlossaryItems.add(new InfoItem("Distance moved", "The distance from where the complex number starts and where it ends after being iterated upon."));
        defaultGlossaryItems.add(new InfoItem("Total path length", "The length of the path generated when tracking a complex number as it iterates."));
        defaultGlossaryItems.add(new InfoItem("Misiurewicz point", "A section of the Mandelbrot set for which the point is preperiodic (i.e. it becomes periodic after iterating but it isn't periodic itself). An example is the point at -0.101+0.956i."));
        defaultGlossaryItems.add(new InfoItem("Periodic point", "A periodic point of a function is a value that is returned to after a certain number of iterations."));
        defaultGlossaryItems.add(new InfoItem("Minibrots", "This is the name for the areas of the set that repeat the famous image of the Mandelbrot set and hence is where the self-similarity lies. There are infinitely many of these along the x-axis."));
        defaultGlossaryItems.add(new InfoItem("Cardioid", "A shape formed by polar equations that resembles a heart and is the main body of the Mandelbrot set."));
        defaultGlossaryItems.add(new InfoItem("Histogram colouring", "A colouring system that uses a histogram to evenly distribute colour across the screen, independent of max iterations and zoom depth."));
        defaultGlossaryItems.add(new InfoItem("Chaos theory", "The study of events where minute changes to the starting conditions can massively alter the end result."));
        defaultGlossaryItems.add(new InfoItem("Adrien Douady", "A French mathematician (25 September 1935 – 2 November 2006) who helped discover that the Mandelbrot set is connected and the Douady rabbit is named after."));
        defaultGlossaryItems.add(new InfoItem("Douady rabbit", "A range of Julia sets that somewhat resemble rabbits ears, named after Adrien Douady. An example is the Julia set of -0.127 + 0.76i."));
        defaultGlossaryItems.add(new InfoItem("Seahorse valley", "This is a section of the Mandelbrot set which resembles seahorses. An example can be seen around -0.74 + 0.183i."));

    }

}
