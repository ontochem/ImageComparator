package com.ontochem.imageHandler;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Utility class for cropping images based on transparency.
 * 
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Added crop image functionality.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author shadrack.j.barnabas@ontochem.com
 * @date 2023-06-14
 */
public class CropImage {
    
    /**
     * Crop the input image based on transparency and save it as the output image.
     * 
     * @param inputFilename The path to the input image.
     * @param outputFilename The path to save the cropped image.
     * @throws Exception If there's an error during cropping or saving.
     */
    public static void generate(String inputFilename, String outputFilename) throws Exception {
        try {
            BufferedImage img = ImageIO.read(new File(inputFilename));
            // Set background transparent internally and calculate bounds.
            BufferedImage imgTrans = ImageTransparency.generate(img);
            WritableRaster tempRaster = imgTrans.getAlphaRaster();
            int x1 = getX1(tempRaster);
            int y1 = getY1(tempRaster);
            int x2 = getX2(tempRaster);
            int y2 = getY2(tempRaster);
            // Crop the source image based on transparency.
            BufferedImage temp1 = img.getSubimage(x1, y1, x2 - x1, y2 - y1);
            ImageIO.write(temp1, "png", new File(outputFilename));
        } catch (IOException e) {
            // Handle the error appropriately.
            e.printStackTrace();
        }
    }

    /**
     * Crop the input buffered image based on transparency and return the cropped image.
     * 
     * @param img The input buffered image.
     * @return The trimmed buffered image.
     * @throws Exception If there's an error during cropping.
     */
    public static BufferedImage generate(BufferedImage img) throws Exception {
        BufferedImage imgTrans = ImageTransparency.generate(img);
        BufferedImage temp1 = null;
        try {
            WritableRaster tempRaster = imgTrans.getAlphaRaster();
            int x1 = getX1(tempRaster);
            int y1 = getY1(tempRaster);
            int x2 = getX2(tempRaster);
            int y2 = getY2(tempRaster);
            // Crop the source image based on transparency.
            temp1 = img.getSubimage(x1, y1, x2 - x1, y2 - y1);
        } catch (Exception e) {
            // Handle the error appropriately.
            e.printStackTrace();
        }
        return temp1;
    }

    /**
     * Get the y-coordinate of the top of the character in the raster image.
     * 
     * @param raster The image raster.
     * @return The y1 coordinate.
     */
    public static int getY1(WritableRaster raster) {
        for (int y = 0; y < raster.getHeight(); y++) {
            for (int x = 0; x < raster.getWidth(); x++) {
                if (raster.getSample(x, y, 0) != 0) {
                    if (y > 0) {
                        return y - 1;
                    } else {
                        return y;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Get the y-coordinate of the bottom of the character in the raster image.
     * 
     * @param raster The image raster.
     * @return The y2 coordinate.
     */
    public static int getY2(WritableRaster raster) {
        // Ground plane of the character
        for (int y = raster.getHeight() - 1; y > 0; y--) {
            for (int x = 0; x < raster.getWidth(); x++) {
                if (raster.getSample(x, y, 0) != 0) {
                    return y + 1;
                }
            }
        }
        return 0;
    }

    /**
     * Get the x-coordinate of the left side of the character in the raster image.
     * 
     * @param raster The image raster.
     * @return The x1 coordinate.
     */
    public static int getX1(WritableRaster raster) {
        // Left side of the character
        for (int x = 0; x < raster.getWidth(); x++) {
            for (int y = 0; y < raster.getHeight(); y++) {
                if (raster.getSample(x, y, 0) != 0) {
                    if (x > 0) {
                        return x - 1;
                    } else {
                        return x;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Get the x-coordinate of the right side of the character in the raster image.
     * 
     * @param raster The image raster.
     * @return The x2 coordinate.
     */
    public static int getX2(WritableRaster raster) {
        // Right side of the character
        for (int x = raster.getWidth() - 1; x > 0; x--) {
            for (int y = 0; y < raster.getHeight(); y++) {
                if (raster.getSample(x, y, 0) != 0) {
                    return x + 1;
                }
            }
        }
        return 0;
    }
}
