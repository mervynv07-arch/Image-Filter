package assign11;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and provides a number 
 * of image filters (via instance methods) for producing a new Image with an altered appearance.
 * 
 * Note:
 *   - The pixel in the northwest corner of the image is stored in the first row, first column.
 *   - The pixel in the northeast corner of the image is stored in the first row, last column.
 *   - The pixel in the southeast corner of the image is stored in the last row, last column.
 *   - The pixel in the southwest corner of the image is stored in the last row, first column.
 *   
 * DO NOT MODIFY ANY PROVIDED CODE, unless directed in the assignment instructions.
 * 
 * @author Prof. Parker and Mervyn Vera
 * @version April 16, 2026
 */
public class Image {
	private Pixel[][] pixels;
	
	/**
	 * Creates a new Image object from the given two-dimensional array of pixels.
	 * 
	 * @param pixels - two-dimensional array of pixels, with cannot be jagged
	 */
	public Image(Pixel[][] pixels) {		
		this.pixels = new Pixel[pixels.length][pixels[0].length];
		for(int i = 0; i < pixels.length; i++) {
			if(pixels[i].length != pixels[0].length)
				throw new IllegalArgumentException("Given two-dimensional array of pixels must have " +
						"the same number of columns for each row.");
			this.pixels[i] = new Pixel[pixels[0].length];
			for(int j = 0; j < pixels[0].length; j++)
				this.pixels[i][j] = pixels[i][j];
		}
	}
	
	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 * 
	 * @param filename - name of the image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		}
		catch(IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}
		
		this.pixels = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for(int i = 0; i < this.pixels.length; i++)
			for(int j = 0; j < this.pixels[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				this.pixels[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}		
	}
	
	/**
	 * Creates a new "default" Image object, whose purpose is to be used in testing.
	 * 
	 * The orientation of this image:
	 * 		cyan 	 red
	 *		green	 magenta
	 *		yellow	 blue
	 */
	public Image() {		
		this.pixels = new Pixel[3][2];
		this.pixels[0][0] = new Pixel(0, 255, 255);  // cyan
		this.pixels[0][1] = new Pixel(255, 0, 0);  // red
		this.pixels[1][0] = new Pixel(0, 255, 0);  // green
		this.pixels[1][1] = new Pixel(255, 0, 255);  // magenta
		this.pixels[2][0] = new Pixel(255, 255, 0);  // yellow
		this.pixels[2][1] = new Pixel(0, 0, 255);  // blue
	}
	
	/**
	 * Gets the number of rows in this image.
	 * 
	 * @return the number of rows in the pixel array
	 */
	public int getNumberOfRows() {
		   return this.pixels.length;
		}
	
	/**
	 * Gets the number of columns in this image.
	 * 
	 * @return the number of columns in the pixel array,
	 *         or 0 if the image has no rows
	 */
	public int getNumberOfColumns() {
		if (this.pixels.length == 0)
			return 0;
		return this.pixels[0].length;
	}
	
	/**
	 * Gets the pixel at the specified row and column indexes.
	 * 
	 * @param rowIndex - row index
	 * @param columnIndex - column index
	 * @return pixel at the given row and column indexes
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if(rowIndex < 0 || rowIndex >= this.pixels.length)
			throw new IndexOutOfBoundsException("The row index must be in range 0-" + (this.pixels.length - 1) + ".");

		if(columnIndex < 0 || columnIndex >= this.pixels[0].length)
			throw new IndexOutOfBoundsException("The column index must be in range 0-" + (this.pixels[0].length - 1) + ".");

		return this.pixels[rowIndex][columnIndex];
	}
	
	/**
	 * Writes the image represented by this object to file.
	 * Does nothing if the image length is 0.
	 * 
	 * @param filename - name of image file to write
	 * @throws IOException if file cannot be written
	 */
	public void writeImage(String filename) {
		if(this.pixels.length > 0) {
			BufferedImage imageOutput = new BufferedImage(this.pixels[0].length, 
					this.pixels.length, BufferedImage.TYPE_INT_RGB);
		
			for(int i = 0; i < this.pixels.length; i++)
				for(int j = 0; j < this.pixels[0].length; j++) 
					imageOutput.setRGB(j, i, this.pixels[i][j].packRGB());
			
			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			}
			catch(IOException e) {
				System.out.println("The image cannot be written to file " + filename + ".");
			}
		}
	}
	
	/**
	 * Generates a new image that is a red-green swapped version of this Image object
	 * (i.e., each pixel's color has the red and green amounts of this Image's pixel swapped).
	 * 
	 * @return new image that is a red-green swapped version of this image
	 */
	public Image swapRedGreen() {
		Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];

		for (int i = 0; i < this.pixels.length; i++)
			for (int j = 0; j < this.pixels[0].length; j++) {
				Pixel p = this.pixels[i][j];
				newPixels[i][j] = new Pixel(p.getGreen(), p.getRed(), p.getBlue());
			}
		return new Image(newPixels);
	}
	
	/**
	 * Generates a new image that is a flipped version of this Image object
	 * (i.e., mirror image around x-axis).
	 * 
	 * @return new image that is a flipped version of this image
	 */
	public Image flip() {
		Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];
		for (int i = 0; i < this.pixels.length; i++)
			for (int j = 0; j < this.pixels[0].length; j++)
				newPixels[i][j] = this.pixels[this.pixels.length - 1 - i][j];
		return new Image(newPixels);
	}
		
	/**
	 * Generates a new image that is a blurred version of this Image object
	 * (i.e., each pixel's red amount is an average of the red amount for itself 
	 * and its four neighbors, and similarly for green and blue).
	 * 
	 * CAUTION: For pixels on the edge of the image, use only the existing neighbors;
	 *          e.g., the pixel at [0][0] has only two neighbors at [0][1] and [1][0].
	 * 
	 * @return new image that is a blurred version of this image
	 */
	public Image blur() {
		Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];

		for (int i = 0; i < this.pixels.length; i++)
			for (int j = 0; j < this.pixels[0].length; j++) {
				int redSum = 0;
				int greenSum = 0;
				int blueSum = 0;
				int count = 0;
				redSum += this.pixels[i][j].getRed();
				greenSum += this.pixels[i][j].getGreen();
				blueSum += this.pixels[i][j].getBlue();
				count++;

				if (i - 1 >= 0) {
					redSum += this.pixels[i - 1][j].getRed();
					greenSum += this.pixels[i - 1][j].getGreen();
					blueSum += this.pixels[i - 1][j].getBlue();
					count++;
				}

				if (i + 1 < this.pixels.length) {
					redSum += this.pixels[i + 1][j].getRed();
					greenSum += this.pixels[i + 1][j].getGreen();
					blueSum += this.pixels[i + 1][j].getBlue();
					count++;
				}

				if (j - 1 >= 0) {
					redSum += this.pixels[i][j - 1].getRed();
					greenSum += this.pixels[i][j - 1].getGreen();
					blueSum += this.pixels[i][j - 1].getBlue();
					count++;
				}

				if (j + 1 < this.pixels[0].length) {
					redSum += this.pixels[i][j + 1].getRed();
					greenSum += this.pixels[i][j + 1].getGreen();
					blueSum += this.pixels[i][j + 1].getBlue();
					count++;
				}

				newPixels[i][j] = new Pixel(redSum / count, greenSum / count, blueSum / count);
			}
		return new Image(newPixels);
	}
	
	/**
	 * Generates a new image that is a grayscale version of this Image object.
	 * Each pixel is replaced by a pixel whose red, green, and blue values are all
	 * the average of the original pixel's red, green, and blue values.
	 *
	 * @return new image that is a grayscale version of this image
	 */
	public Image custom() {
		Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];
		for (int i = 0; i < this.pixels.length; i++)
			for (int j = 0; j < this.pixels[0].length; j++) {
				Pixel p = this.pixels[i][j];
				int average = (p.getRed() + p.getGreen() + p.getBlue()) / 3;
				newPixels[i][j] = new Pixel(average, average, average);
			}
		return new Image(newPixels);
	}

	/**
	 * Generates a new image with all pixels brightened or darkened by the given amount.
	 *
	 * Positive values increase brightness, while negative values decrease brightness.
	 *
	 * @param amount the amount to add to each RGB component of every pixel
	 * @return a new image with adjusted brightness
	 */
	public Image brightness(int amount) {
		Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];
		for (int i = 0; i < this.pixels.length; i++)
			for (int j = 0; j < this.pixels[0].length; j++) {
				Pixel p = this.pixels[i][j];
				int newRed = clamp(p.getRed() + amount);
				int newGreen = clamp(p.getGreen() + amount);
				int newBlue = clamp(p.getBlue() + amount);
				newPixels[i][j] = new Pixel(newRed, newGreen, newBlue);
			}
		return new Image(newPixels);
	}

	/**
	 * Clamps an RGB value to the valid range of 0 to 255.
	 *
	 * @param value the value to clamp
	 * @return 0 if the value is below 0, 255 if the value is above 255,
	 *         otherwise the original value
	 */
	private int clamp(int value) {
		if (value < 0)
			return 0;
		if (value > 255)
			return 255;
		return value;
	}
	
	/**
	 * Returns a new image containing only the pixels inside the given rectangular region.
	 *
	 * The region is clamped to the bounds of the image before cropping.
	 *
	 * @param region the rectangular area to crop
	 * @return a new image representing the cropped region
	 * @throws IllegalArgumentException if the region is null or has no positive area
	 */
	public Image crop(Rectangle region) {
		if (region == null)
			throw new IllegalArgumentException("Crop region cannot be null.");

		if (this.pixels.length == 0 || this.pixels[0].length == 0)
			return new Image(new Pixel[0][0]);

		int x1 = clamp(region.x, 0, this.getNumberOfColumns() - 1);
		int y1 = clamp(region.y, 0, this.getNumberOfRows() - 1);
		int x2 = clamp(region.x + region.width - 1, 0, this.getNumberOfColumns() - 1);
		int y2 = clamp(region.y + region.height - 1, 0, this.getNumberOfRows() - 1);

		int newWidth = x2 - x1 + 1;
		int newHeight = y2 - y1 + 1;

		if (newWidth <= 0 || newHeight <= 0)
			throw new IllegalArgumentException("Crop region must have positive width and height.");

		Pixel[][] newPixels = new Pixel[newHeight][newWidth];
		for (int i = 0; i < newHeight; i++)
			for (int j = 0; j < newWidth; j++)
				newPixels[i][j] = this.pixels[y1 + i][x1 + j];

		return new Image(newPixels);
	}

	/**
	 * Clamps an integer to the provided inclusive range.
	 *
	 * @param value the value to clamp
	 * @param min the minimum allowed value
	 * @param max the maximum allowed value
	 * @return the clamped value
	 */
	private int clamp(int value, int min, int max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}
	
	/**
	 * Generates a new image whose colors are the inverse of this image.
	 *
	 * Each RGB component is replaced by its distance from 255.
	 *
	 * @return a new image with inverted colors
	 */
	public Image invert() {
	    Pixel[][] newPixels = new Pixel[this.pixels.length][this.pixels[0].length];

	    for (int i = 0; i < this.pixels.length; i++) {
	        for (int j = 0; j < this.pixels[0].length; j++) {
	            Pixel p = this.pixels[i][j];
	            newPixels[i][j] = new Pixel(
	                    255 - p.getRed(),
	                    255 - p.getGreen(),
	                    255 - p.getBlue());
	        }
	    }
	    return new Image(newPixels);
	}
}