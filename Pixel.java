package assign11;

/**
 * The Pixel class represents a single pixel in an image using
 * red, green, and blue (RGB) color values. Each color component
 * must be in the range 0–255.
 * 
 * Pixel objects are immutable after creation and can be compared
 * using equals().
 * 
 * @author Mervyn Vera
 * @version April 16, 2026
 */
public class Pixel {
    private int red;
    private int green;
    private int blue;
    
    /**
     * Constructs a Pixel with the given red, green, and blue values.
     * 
     * @param red the red component (0–255)
     * @param green the green component (0–255)
     * @param blue the blue component (0–255)
     * @throws IllegalArgumentException if any value is outside 0–255
     */
    public Pixel(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255)
            throw new IllegalArgumentException("Must be between 0 and 255.");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    /**
     * Gets the red component of this pixel.
     * 
     * @return the red value (0–255)
     */
    public int getRed() {
        return red;
    }
    
    /**
     * Gets the green component of this pixel.
     * 
     * @return the green value (0–255)
     */
    public int getGreen() {
        return green;
    }
    
    /**
     * Gets the blue component of this pixel.
     * 
     * @return the blue value (0–255)
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Packs the red, green, and blue values into a single integer
     * in RGB format.
     * 
     * @return an integer representing the packed RGB value
     */
    public int packRGB() {
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Compares this pixel to another object for equality.
     * Two pixels are equal if their red, green, and blue values match.
     * 
     * @param other the object to compare to
     * @return true if the objects are equal pixels, false otherwise
     */
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Pixel))
            return false;
        Pixel otherPixel = (Pixel) other;
        return this.red == otherPixel.red && this.green == otherPixel.green && this.blue == otherPixel.blue;
    }

    /**
     * Generates a hash code for this pixel based on its RGB value.
     * 
     * @return hash code for this pixel
     */
    public int hashCode() {
        return packRGB();
    }
}