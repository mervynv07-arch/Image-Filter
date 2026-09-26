package assign11;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A panel component used to display an image and handle mouse-based selection
 * for cropping within the Image Processor application.
 * 
 * This class is responsible for rendering the image pixel-by-pixel and
 * visually displaying a rectangular selection made by the user.
 * It also communicates selection changes back to the main frame.
 * 
 * @author Mervyn Vera
 * @version April 16, 2026
 */
public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	private Image image;
	private Rectangle selection;
	private Point startPoint;
	private ImageProcessorFrame frame;

	/**
	 * Creates a panel for displaying an image and handling crop selection input.
	 *
	 * @param image the image to display
	 * @param frame the frame that receives selection updates
	 */
	public ImagePanel(Image image, ImageProcessorFrame frame) {
		this.image = image;
		this.frame = frame;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * Sets the image displayed by this panel and clears any current selection.
	 *
	 * @param image the new image to display
	 */
	public void setImage(Image image) {
		this.image = image;
		this.selection = null;
		repaint();
	}

	/**
	 * Sets the current selection rectangle shown on the panel.
	 *
	 * @param selection the selection rectangle to display
	 */
	public void setSelection(Rectangle selection) {
		this.selection = selection;
		repaint();
	}

	/**
	 * Returns the current selection rectangle.
	 *
	 * @return the current selection, or null if none exists
	 */
	public Rectangle getSelection() {
		return this.selection;
	}

	/**
	 * Returns the preferred size of this panel based on the current image.
	 *
	 * @return the preferred panel size
	 */
	public Dimension getPreferredSize() {
		if (this.image == null)
			return new Dimension(600, 400);
		return new Dimension(this.image.getNumberOfColumns(), this.image.getNumberOfRows());
	}

	/**
	 * Paints the image and any active selection rectangle onto the panel.
	 *
	 * @param g the Graphics context used for drawing
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.image == null)
			return;

		for (int i = 0; i < this.image.getNumberOfRows(); i++)
			for (int j = 0; j < this.image.getNumberOfColumns(); j++) {
				Pixel p = this.image.getPixel(i, j);
				g.setColor(new Color(p.getRed(), p.getGreen(), p.getBlue()));
				g.fillRect(j, i, 1, 1);
			}

		if (this.selection != null) {
			g.setColor(new Color(105, 105, 105, 125));
			g.fillRect(this.selection.x, this.selection.y, this.selection.width, this.selection.height);
			g.setColor(Color.DARK_GRAY);
			g.drawRect(this.selection.x, this.selection.y, this.selection.width, this.selection.height);
		}
	}

	/**
	 * Creates a rectangle from two points, using the top-left and bottom-right bounds.
	 *
	 * @param a one corner of the rectangle
	 * @param b the opposite corner of the rectangle
	 * @return a rectangle spanning the two points
	 */
	private Rectangle makeRectangle(Point a, Point b) {
		int x = Math.min(a.x, b.x);
		int y = Math.min(a.y, b.y);
		int w = Math.abs(a.x - b.x);
		int h = Math.abs(a.y - b.y);
		return new Rectangle(x, y, w, h);
	}

	/**
	 * Starts a new selection when the mouse is pressed.
	 *
	 * @param e the mouse event
	 */
	public void mousePressed(MouseEvent e) {
		if (this.image == null)
			return;
		this.startPoint = e.getPoint();
		this.selection = null;
		this.frame.selectionStarted();
		repaint();
	}

	/**
	 * Updates the current selection while the mouse is being dragged.
	 *
	 * @param e the mouse event
	 */
	public void mouseDragged(MouseEvent e) {
		if (this.startPoint == null || this.image == null)
			return;
		this.selection = makeRectangle(this.startPoint, e.getPoint());
		this.frame.selectionChanged(this.selection);
		repaint();
	}

	/**
	 * Finalizes the selection when the mouse is released.
	 *
	 * @param e the mouse event
	 */
	public void mouseReleased(MouseEvent e) {
		if (this.startPoint == null || this.image == null)
			return;

		this.selection = makeRectangle(this.startPoint, e.getPoint());
		this.startPoint = null;

		if (this.selection.width > 0 && this.selection.height > 0)
			this.frame.selectionFinished(this.selection);
		else {
			this.selection = null;
			this.frame.selectionCleared();
		}

		repaint();
	}
	
	private static final long serialVersionUID = 1L;

	/**
	 * Handles mouse click events.
	 *
	 * @param e the mouse event
	 */
	public void mouseClicked(MouseEvent e) { }

	/**
	 * Handles mouse entry events.
	 *
	 * @param e the mouse event
	 */
	public void mouseEntered(MouseEvent e) { }

	/**
	 * Handles mouse exit events.
	 *
	 * @param e the mouse event
	 */
	public void mouseExited(MouseEvent e) { }

	/**
	 * Handles mouse move events.
	 *
	 * @param e the mouse event
	 */
	public void mouseMoved(MouseEvent e) { }
}