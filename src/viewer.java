import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


/**
 * ImageViewer is the main class of the image viewer application. It builds and
 * displays the application GUI and initialises all other components.
 * <p>
 * To start the application, create an object of this class.
 *
 * @author Michael Kölling and David J. Barnes.
 * @version 3.1
 */
public class viewer {
    // static fields:
    private static final String VERSION = "Version 3.1";
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

    // fields:
    private JFrame frame;
    private panel imagePanel;
    private JLabel filenameLabel;
    private JLabel statusLabel;
    private JButton smallerButton;
    private JButton largerButton;
    private JButton undoButton;
    private JButton redoButton;
    private JButton rotateButton;
    private JButton rotateButtonTwo;
    private JButton multiple;
    private OFImage currentImage;
    private List<filter> filters;

    /**
     * Create an ImageViewer and display its GUI on screen.
     */
    public viewer() {
        currentImage = null;
        filters = createFilters();
        //       makeFrame(); Made public and called in Driver to stop variable unused warning.
    }

    // ---- implementation of menu functions ----

    /**
     * Open function: open a file chooser to select a new image file,
     * and then display the chosen image.
     */
    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(frame);

        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;  // cancelled
        }
        File selectedFile = fileChooser.getSelectedFile();
        currentImage = fileManager.loadImage(selectedFile);

        if (currentImage == null) {   // image file was not a valid image
            JOptionPane.showMessageDialog(frame, "The file was not in a recognized image file format.", "Image Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        imagePanel.setImage(currentImage);
        setButtonsEnabled(true);
        showFilename(selectedFile.getPath());
        showStatus("File loaded.");
        frame.pack();
    }

    /**
     * Close function: close the current image.
     */
    private void close() {
        currentImage = null;
        imagePanel.clearImage();
        showFilename(null);
        setButtonsEnabled(false);
    }

    /**
     * Save As function: save the current image to a file.
     */
    private void saveAs() {
        if (currentImage != null) {
            int returnVal = fileChooser.showSaveDialog(frame);

            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;  // cancelled
            }
            File selectedFile = fileChooser.getSelectedFile();
            fileManager.saveImage(currentImage, selectedFile);

            showFilename(selectedFile.getPath());
        }
    }

    private void quit() //Quit function: quit the application.
    {
        System.exit(0);
    }

    private void reset() { //if current image isn't null, you get the original and display it

        if (currentImage != null) {
            File selectedFile = fileChooser.getSelectedFile();
            currentImage = fileManager.loadImage(selectedFile);
            imagePanel.setImage(currentImage);
            setButtonsEnabled(true);
            showStatus("File reset.");
        }

    }

    private void applyFilter(filter filter) //Apply a given filter to the current image.
    {
        if (currentImage != null) {
            filter.apply(currentImage);
            frame.repaint();
            showStatus("Applied: " + filter.getName());
        } else {
            showStatus("No image loaded.");
        }
    }

    private void showAbout() //'About' function: show the 'about' box.
    {
        JOptionPane.showMessageDialog(frame, "ImageViewer\n" + VERSION, "About ImageViewer", JOptionPane.INFORMATION_MESSAGE);
    }

    private void makeLarger() //make picture bigger
    {
        if (currentImage != null) {
            // create new image with double size
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(width * 2, height * 2);

            // copy pixel data into new image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(x, y);
                    newImage.setPixel(x * 2, y * 2, col);
                    newImage.setPixel(x * 2 + 1, y * 2, col);
                    newImage.setPixel(x * 2, y * 2 + 1, col);
                    newImage.setPixel(x * 2 + 1, y * 2 + 1, col);
                }
            }

            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }


    private void makeSmaller() //make picture smaller
    {
        if (currentImage != null) {
            // create new image with double size
            int width = currentImage.getWidth() / 2;
            int height = currentImage.getHeight() / 2;
            OFImage newImage = new OFImage(width, height);

            // copy pixel data into new image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    newImage.setPixel(x, y, currentImage.getPixel(x * 2, y * 2));
                }
            }

            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }

    private void rotate180() { // similar to flipping horizontally, change x and y to that it rotates

        if (currentImage != null) {
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(width, height);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    newImage.setPixel(width - 1 - x, y, currentImage.getPixel(x, height - 1 - y));

                }
            }
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }

    private void rotate90() {

        if (currentImage != null) {
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(height, width);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    newImage.setPixel(height - 1 - y, x, currentImage.getPixel(x, y));
                }
            }
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }

    private void multiple() {

    }

// ---- support methods ----

    /**
     * Show the file name of the current image in the fils display label.
     * 'null' may be used as a parameter if no file is currently loaded.
     *
     * @param filename The file name to be displayed, or null for 'no file'.
     */
    private void showFilename(String filename) {
        if (filename == null) {
            filenameLabel.setText("No file displayed.");
        } else {
            filenameLabel.setText("File: " + filename);
        }
    }


    /**
     * Show a message in the status bar at the bottom of the screen.
     *
     * @param text The status message.
     */
    private void showStatus(String text) {
        statusLabel.setText(text);
    }


    /**
     * Enable or disable all toolbar buttons.
     *
     * @param status 'true' to enable the buttons, 'false' to disable.
     */
    private void setButtonsEnabled(boolean status) {
        smallerButton.setEnabled(status);
        largerButton.setEnabled(status);
        rotateButton.setEnabled(status);
        rotateButtonTwo.setEnabled(status);
        multiple.setEnabled(true);

    }


    /**
     * Create a list with all the known filters.
     *
     * @return The list of filters.
     */
    private List<filter> createFilters() {
        List<filter> filterList = new ArrayList<filter>();
        filterList.add(new dark("Darker"));
        filterList.add(new light("Lighter"));
        filterList.add(new threshold("Threshold"));
        filterList.add(new invert("Invert"));
        filterList.add(new solarize("Solarize"));
        filterList.add(new smooth("Smooth"));
        filterList.add(new pixel("Pixelize"));
        filterList.add(new mirror("Mirror"));
        filterList.add(new grayScale("Grayscale"));
        filterList.add(new edge("Edge Detection"));
        filterList.add(new fishEye("Fish Eye"));
        filterList.add(new temperature("Temperature"));
        filterList.add(new contrast("Contrast"));

        return filterList;
    }

// ---- Swing stuff to build the frame and all its components and menus ----

    /**
     * Create the Swing frame and its content.
     */
    public void makeFrame() {
        frame = new JFrame("ImageViewer");
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        makeMenuBar(frame);

        // Specify the layout manager with nice spacing
        contentPane.setLayout(new BorderLayout(6, 6));

        // Create the image pane in the center
        imagePanel = new panel();
        imagePanel.setBorder(new EtchedBorder());
        contentPane.add(imagePanel, BorderLayout.CENTER);

        // Create two labels at top and bottom for the file name and status messages
        filenameLabel = new JLabel();
        contentPane.add(filenameLabel, BorderLayout.NORTH);

        statusLabel = new JLabel(VERSION);
        contentPane.add(statusLabel, BorderLayout.SOUTH);

        // Create the toolbar with the buttons
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(0, 1));

        smallerButton = new JButton("Smaller");
        smallerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeSmaller();
            }
        });
        toolbar.add(smallerButton);

        largerButton = new JButton("Larger");
        largerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeLarger();
            }
        });
        toolbar.add(largerButton);

        rotateButton = new JButton("Rotate 180°");
        rotateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotate180();
            }
        });
        toolbar.add(rotateButton);

        rotateButtonTwo = new JButton("Rotate 90°");
        rotateButtonTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotate90();
            }
        });
        toolbar.add(rotateButtonTwo);

        multiple = new JButton("Multiple Images");
        multiple.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                multiple();
            }
        });
        toolbar.add(multiple);


        // Add toolbar into panel with flow layout for spacing
        JPanel flow = new JPanel();
        flow.add(toolbar);

        contentPane.add(flow, BorderLayout.WEST);

        // building is done - arrange the components
        showFilename(null);
        setButtonsEnabled(false);
        frame.pack();

        // place the frame at the center of the screen and show
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - frame.getWidth() / 2, d.height / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
    }

    /**
     * Create the main frame's menu bar.
     *
     * @param frame The frame that the menu bar should be added to.
     */
    private void makeMenuBar(JFrame frame) {
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu menu;
        JMenuItem item;

        // create the File menu
        menu = new JMenu("File");
        menubar.add(menu);

        item = new JMenuItem("Open...");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        menu.add(item);

        item = new JMenuItem("Close");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Save As...");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        menu.add(item);

        menu = new JMenu("Edit");
        menubar.add(menu);

        item = new JMenuItem("Reset");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Undo");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Redo");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(item);
        menu.add(item);

        // create the Filter menu
        menu = new JMenu("Filter");
        menubar.add(menu);

        for (final filter filter : filters) {
            item = new JMenuItem(filter.getName());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    applyFilter(filter);
                }
            });
            menu.add(item);
        }

        menu = new JMenu("Slide Show");
        menubar.add(menu);

        item = new JMenuItem("Start Slide Show");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new slideshow();
            }
        });
        menu.add(item);


        // create the Help menu
        menu = new JMenu("Help");
        menubar.add(menu);

        item = new JMenuItem("About ImageViewer...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        menu.add(item);

    }
}
