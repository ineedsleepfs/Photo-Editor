import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUI {

    private JFrame frame;
    private JButton undo;
    private JButton redo;
    private JButton rotate;
    private JButton multiple;
    private Image image;


    public GUI() throws IOException {
        this.makeFrame();
    }

    private void makeFrame() throws IOException {
        frame = new JFrame("Image Viewer GUI");
        Container contentPane = frame.getContentPane();

        undo = new JButton("undo");
        redo = new JButton("redo");
        rotate = new JButton("rotate");
        multiple = new JButton("multiple");

        JPanel p1 = new JPanel(new GridLayout(7, 1));
        p1.add(undo);
        p1.add(redo);
        p1.add(rotate);
        p1.add(multiple);


        JLabel Image1 = new JLabel();


        //image = ImageIO.read(getClass().getResource("/Exemption Project/images"));


        contentPane.add(p1, BorderLayout.WEST);
        //contentPane.add(p2, BorderLayout.CENTER);

        frame.pack();
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
    }
}

