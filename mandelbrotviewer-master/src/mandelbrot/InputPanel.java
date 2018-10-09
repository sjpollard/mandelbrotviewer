package mandelbrot;

import javax.swing.*;
import java.awt.*;

/**
 * Small GUI component that joins together a JLabel and a JTextField to allow for named input
 * spaces. The point of this is to allow for a list of these objects to be created to keep
 * track of all of them. Used within SettingsFrame to allow for the user to enter specific data.
 */

public class InputPanel extends JPanel {

    /**Custom components*/
    private JLabel titleLable;
    private JTextField inputField;

    /**Constructor that sets up the JPanel with the specified title and default data*/
    public InputPanel(String title, String currentValue) {

        super();

        titleLable = new JLabel(title);
        inputField = new JTextField(currentValue, 20);
        titleLable.setFont(MandelbrotFrame.headerFont);
        inputField.setFont(MandelbrotFrame.normalFont);

        this.setBackground(Color.white);
        this.setLayout(new BorderLayout(50, 0));

        this.add(titleLable, BorderLayout.WEST);
        this.add(inputField, BorderLayout.EAST);

    }

    public String getInputFieldText() {
        return inputField.getText();
    }

    public void setInputFieldText(String inputFieldText) {
        inputField.setText(inputFieldText);
    }


}
