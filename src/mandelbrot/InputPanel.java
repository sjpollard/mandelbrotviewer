package mandelbrot;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {

    private JLabel titleLable;
    private JTextField inputField;

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
