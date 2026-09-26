package assign11;

/**
 * The main program that launches the Image Processor application.
 * 
 * This class contains the main method, which creates and displays
 * the ImageProcessorFrame GUI using the Swing event dispatch thread.
 * 
 * @author Mervyn Vera
 * @version April 16, 2026
 */
public class ImageProcessorProgram {
    public static void main(String[] args) {
         ImageProcessorFrame frame = new ImageProcessorFrame();
         frame.setVisible(true);
    }
}