import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class slideshow extends JFrame {
    private static final long serialVersionUID = 1L;
    JLabel pic;
    Timer tm;
    int x = 0;
    //Images Path In Array
    String[] list = {
            "D:\\IdeaProjects\\ImageViewer\\images\\1.jpg",
            "D:\\IdeaProjects\\ImageViewer\\images\\me.jpg",
            "D:\\IdeaProjects\\ImageViewer\\images\\anto.jpg",
            "D:\\IdeaProjects\\ImageViewer\\images\\peace.jpg"
    };

    public slideshow() {

        pic = new JLabel();
        pic.setBounds(10, 10, 400, 400);

        //Call The Function SetImageSize
        SetImageSize(1);
        //set a timer
        tm = new Timer(1000, new ActionListener() {


            public void actionPerformed(ActionEvent e) {
                SetImageSize(x);
                x += 1;
                if (x >= list.length)
                    x = 0;
            }
        });
        add(pic);
        tm.start();
        setLayout(null);
        setSize(425, 440);
        getContentPane().setBackground(Color.decode("#bdb67b"));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //create a function to resize the image
    public void SetImageSize(int i) {
        ImageIcon icon = new ImageIcon(list[i]);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon newImc = new ImageIcon(newImg);
        pic.setIcon(newImc);
    }

    public static void main(String[] args) {
        new slideshow();
    }
}