package assign11;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A GUI frame for the Image Processor application.
 * 
 * This class provides a user interface that allows users to:
 * - Open image files from their system
 * - Apply various image filters (e.g., blur, flip, grayscale)
 * - Save the filtered image to a file
 * 
 * The frame uses menu options and event handling to manage user actions,
 * and displays images using an ImagePanel.
 * 
 * @author Mervyn Vera
 * @version April 16, 2026
 */
public class ImageProcessorFrame extends JFrame implements ActionListener, ChangeListener {
    private Image sourceImage;
    private Image filteredImage;

    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;

    private JRadioButtonMenuItem swapRedGreenItem;
    private JRadioButtonMenuItem flipItem;
    private JRadioButtonMenuItem blurItem;
    private JRadioButtonMenuItem grayscaleItem;
    private JRadioButtonMenuItem invertItem;
    private JRadioButtonMenuItem brightnessItem;

    private JSlider brightnessSlider;
    private JPanel sliderPanel;
    private JLabel brightnessLabel;
    private Image brightnessBaseImage;

    private JRadioButtonMenuItem cropItem;
    private ImagePanel imagePanel;
    private Rectangle cropRectangle;
    private boolean cropReady;

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the main application frame and initializes the GUI components,
     * including the menu bar and an empty starting panel.
     */
    public ImageProcessorFrame() {
        super("Image Processor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);

        this.imagePanel = new ImagePanel(null, this);

        this.brightnessSlider = new JSlider(-200, 200, 0);
        this.brightnessSlider.setMajorTickSpacing(50);
        this.brightnessSlider.setMinorTickSpacing(10);
        this.brightnessSlider.setPaintTicks(true);
        this.brightnessSlider.setPaintLabels(true);
        this.brightnessSlider.setEnabled(false);
        this.brightnessSlider.addChangeListener(this);

        this.brightnessLabel = new JLabel(
                "Brightness: drag left to darken, right to brighten."
        );

        this.sliderPanel = new JPanel(new BorderLayout());
        this.sliderPanel.add(this.brightnessLabel, BorderLayout.NORTH);
        this.sliderPanel.add(this.brightnessSlider, BorderLayout.CENTER);
        this.sliderPanel.setVisible(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(this.imagePanel, BorderLayout.CENTER);
        mainPanel.add(this.sliderPanel, BorderLayout.SOUTH);
        this.setContentPane(mainPanel);

        this.setJMenuBar(createMenuBar());

        this.imagePanel.setLayout(new BorderLayout());
    }

    /**
     * Creates and returns the menu bar for the application.
     *
     * The menu bar includes:
     * - File options (open, save, exit)
     * - Filter options for applying image transformations
     *
     * @return the constructed JMenuBar
     */
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        this.openItem = new JMenuItem("Open Image...");
        this.saveItem = new JMenuItem("Save Filtered Image as JPG...");
        this.exitItem = new JMenuItem("Exit");

        this.openItem.setActionCommand("OPEN");
        this.saveItem.setActionCommand("SAVE");
        this.exitItem.setActionCommand("EXIT");

        this.openItem.addActionListener(this);
        this.saveItem.addActionListener(this);
        this.exitItem.addActionListener(this);

        this.saveItem.setEnabled(false);

        fileMenu.add(this.openItem);
        fileMenu.add(this.saveItem);
        fileMenu.addSeparator();
        fileMenu.add(this.exitItem);

        JMenu filterMenu = new JMenu("Filters");
        ButtonGroup group = new ButtonGroup();

        this.swapRedGreenItem = new JRadioButtonMenuItem("Swap Red and Green");
        this.flipItem = new JRadioButtonMenuItem("Flip");
        this.blurItem = new JRadioButtonMenuItem("Blur");
        this.grayscaleItem = new JRadioButtonMenuItem("Grayscale");
        this.invertItem = new JRadioButtonMenuItem("Invert Colors");
        this.brightnessItem = new JRadioButtonMenuItem("Brightness");
        this.cropItem = new JRadioButtonMenuItem("Crop");

        this.swapRedGreenItem.setActionCommand("SWAP_RG");
        this.flipItem.setActionCommand("FLIP");
        this.blurItem.setActionCommand("BLUR");
        this.grayscaleItem.setActionCommand("GRAY");
        this.invertItem.setActionCommand("INVERT");
        this.brightnessItem.setActionCommand("BRIGHTNESS");
        this.cropItem.setActionCommand("CROP");

        this.swapRedGreenItem.setToolTipText("Swap each pixel's red and green values.");
        this.flipItem.setToolTipText("Flips the image upside-down.");
        this.blurItem.setToolTipText("Blurs the image.");
        this.grayscaleItem.setToolTipText("Convert the image to grayscale.");
        this.invertItem.setToolTipText("Invert the image colors.");
        this.brightnessItem.setToolTipText("Changes the brightness of the image.");
        this.cropItem.setToolTipText("Draw a rectangle on the image, then crop to it.");

        this.swapRedGreenItem.addActionListener(this);
        this.flipItem.addActionListener(this);
        this.blurItem.addActionListener(this);
        this.grayscaleItem.addActionListener(this);
        this.invertItem.addActionListener(this);
        this.brightnessItem.addActionListener(this);
        this.cropItem.addActionListener(this);

        this.swapRedGreenItem.setEnabled(false);
        this.flipItem.setEnabled(false);
        this.blurItem.setEnabled(false);
        this.grayscaleItem.setEnabled(false);
        this.invertItem.setEnabled(false);
        this.brightnessItem.setEnabled(false);
        this.cropItem.setEnabled(false);

        group.add(this.swapRedGreenItem);
        group.add(this.flipItem);
        group.add(this.blurItem);
        group.add(this.grayscaleItem);
        group.add(this.invertItem);
        group.add(this.brightnessItem);
        group.add(this.cropItem);

        filterMenu.add(this.swapRedGreenItem);
        filterMenu.add(this.flipItem);
        filterMenu.add(this.blurItem);
        filterMenu.add(this.grayscaleItem);
        filterMenu.add(this.invertItem);
        filterMenu.add(this.brightnessItem);
        filterMenu.add(this.cropItem);

        bar.add(fileMenu);
        bar.add(filterMenu);

        return bar;
    }

    /**
     * Handles user actions from menu selections.
     *
     * Depending on the action command, this method will:
     * - Open an image file
     * - Save a filtered image
     * - Exit the application
     * - Apply a selected image filter
     *
     * @param e the ActionEvent triggered by a menu item
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("OPEN".equals(command)) {
            openImage();
        } else if ("SAVE".equals(command)) {
            saveImage();
        } else if ("EXIT".equals(command)) {
            dispose();
        } else if ("BRIGHTNESS".equals(command)) {
            if (this.sourceImage == null)
                return;

            this.sliderPanel.setVisible(true);
            this.brightnessSlider.setEnabled(true);
            this.brightnessBaseImage = this.sourceImage;

            if (this.brightnessSlider.getValue() == 0) {
                applyBrightness();
            } else {
                this.brightnessSlider.setValue(0);
            }
        } else {
            finalizeBrightness();

            this.sliderPanel.setVisible(false);
            this.brightnessSlider.setEnabled(false);

            if ("CROP".equals(command)) {
                applyCrop();
            } else {
                applyFilter(command);
            }
        }
    }

    /**
     * Finalizes any in-progress brightness adjustment and resets the brightness controls.
     */
    private void finalizeBrightness() {
        if (this.sliderPanel.isVisible() && this.filteredImage != null) {
            this.sourceImage = this.filteredImage;
            this.imagePanel.setImage(this.sourceImage);
            this.imagePanel.repaint();
        }

        this.brightnessBaseImage = null;
        this.brightnessItem.setSelected(false);

        if (this.brightnessSlider.getValue() != 0) {
            this.brightnessSlider.setValue(0);
        }

        this.brightnessSlider.setEnabled(false);
        this.sliderPanel.setVisible(false);
    }

    /**
     * Applies the currently selected crop rectangle to the loaded image.
     */
    private void applyCrop() {
        if (this.sourceImage == null || this.cropRectangle == null)
            return;

        this.filteredImage = this.sourceImage.crop(this.cropRectangle);
        this.sourceImage = this.filteredImage;
        this.imagePanel.setImage(this.filteredImage);

        this.cropRectangle = null;
        this.cropReady = false;
        this.imagePanel.repaint();
        this.saveItem.setEnabled(true);
        updateFilterStates();
    }

    /**
     * Updates the image when the brightness slider is moved.
     *
     * @param e the change event from the slider
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.brightnessSlider
                && !this.brightnessSlider.getValueIsAdjusting()
                && this.brightnessItem.isSelected()) {
            applyBrightness();
        }
    }

    /**
     * Applies the current brightness adjustment to the base image.
     */
    private void applyBrightness() {
        if (this.brightnessBaseImage == null)
            return;

        int amount = this.brightnessSlider.getValue();
        this.filteredImage = this.brightnessBaseImage.brightness(amount);

        this.imagePanel.setImage(this.filteredImage);
        this.imagePanel.repaint();
        this.saveItem.setEnabled(true);
    }

    /**
     * Opens an image file selected by the user using a file chooser dialog.
     *
     * If a valid image is selected, it is displayed in the frame and
     * filter options are enabled. If the user cancels, a message is shown.
     */
    private void openImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files (.jpg, .jpeg, .png, .bmp, .gif)",
                "jpg", "jpeg", "png", "bmp", "gif"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "Image open cancelled.");
            return;
        }

        File selected = chooser.getSelectedFile();

        try {
            this.sourceImage = new Image(selected.getAbsolutePath());
            this.filteredImage = null;
            this.cropRectangle = null;
            this.cropReady = false;
            this.brightnessBaseImage = null;

            this.swapRedGreenItem.setEnabled(true);
            this.flipItem.setEnabled(true);
            this.blurItem.setEnabled(true);
            this.grayscaleItem.setEnabled(true);
            this.invertItem.setEnabled(true);
            this.brightnessItem.setEnabled(true);
            this.cropItem.setEnabled(false);
            this.saveItem.setEnabled(false);

            this.sliderPanel.setVisible(false);
            this.brightnessSlider.setEnabled(false);
            this.brightnessSlider.setValue(0);

            this.swapRedGreenItem.setSelected(false);
            this.flipItem.setSelected(false);
            this.blurItem.setSelected(false);
            this.grayscaleItem.setSelected(false);
            this.invertItem.setSelected(false);
            this.brightnessItem.setSelected(false);
            this.cropItem.setSelected(false);

            this.imagePanel.setImage(this.sourceImage);
            this.imagePanel.repaint();
            this.revalidate();

            updateFilterStates();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "That file could not be opened as an image.",
                    "Open Image Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the currently filtered image to a file selected by the user.
     *
     * Only allows saving as a .jpg file. If no filtered image exists,
     * a message is displayed instead.
     */
    private void saveImage() {
        if (this.filteredImage == null) {
            JOptionPane.showMessageDialog(this,
                    "Open an image and apply a filter before saving.",
                    "Save Not Available",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpg)", "jpg"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "Image save cancelled.");
            return;
        }

        File selected = chooser.getSelectedFile();
        String path = selected.getAbsolutePath();

        if (!path.toLowerCase().endsWith(".jpg")) {
            path = path + ".jpg";
        }

        this.filteredImage.writeImage(path);
        JOptionPane.showMessageDialog(this, "Filtered image saved.");
    }

    /**
     * Marks the start of a crop selection and updates filter availability.
     */
    public void selectionStarted() {
        this.cropReady = false;
        this.cropRectangle = null;
        updateFilterStates();
    }

    /**
     * Updates the current crop selection rectangle while the user is dragging.
     *
     * @param rect the current selection rectangle
     */
    public void selectionChanged(Rectangle rect) {
        this.cropRectangle = rect;
        this.cropReady = false;
        updateFilterStates();
    }

    /**
     * Marks the crop selection as complete and enables cropping.
     *
     * @param rect the final selection rectangle
     */
    public void selectionFinished(Rectangle rect) {
        this.cropRectangle = rect;
        this.cropReady = true;
        updateFilterStates();
    }

    /**
     * Clears any active crop selection and disables cropping.
     */
    public void selectionCleared() {
        this.cropRectangle = null;
        this.cropReady = false;
        updateFilterStates();
    }

    /**
     * Enables or disables filter controls based on the current image and crop state.
     */
    private void updateFilterStates() {
        boolean hasImage = (this.sourceImage != null);

        this.swapRedGreenItem.setEnabled(hasImage && !this.cropReady);
        this.flipItem.setEnabled(hasImage && !this.cropReady);
        this.blurItem.setEnabled(hasImage && !this.cropReady);
        this.grayscaleItem.setEnabled(hasImage && !this.cropReady);
        this.invertItem.setEnabled(hasImage && !this.cropReady);
        this.brightnessItem.setEnabled(hasImage && !this.cropReady);

        this.cropItem.setEnabled(hasImage && this.cropReady);
    }

    /**
     * Applies the selected image filter to the currently loaded image.
     *
     * The resulting filtered image is displayed in the frame, and
     * saving is enabled once a filter has been applied.
     *
     * @param command the action command identifying which filter to apply
     */
    private void applyFilter(String command) {
        if (this.sourceImage == null)
            return;

        if ("SWAP_RG".equals(command)) {
            this.filteredImage = this.sourceImage.swapRedGreen();
        } else if ("FLIP".equals(command)) {
            this.filteredImage = this.sourceImage.flip();
        } else if ("BLUR".equals(command)) {
            this.filteredImage = this.sourceImage.blur();
        } else if ("GRAY".equals(command)) {
            this.filteredImage = this.sourceImage.custom();
        } else if ("INVERT".equals(command)) {
            this.filteredImage = this.sourceImage.invert();
        } else {
            return;
        }

        this.imagePanel.setImage(this.filteredImage);
        this.imagePanel.repaint();
        this.saveItem.setEnabled(true);
    }
}