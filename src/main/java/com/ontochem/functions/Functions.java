package com.ontochem.functions;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import org.apache.commons.io.FilenameUtils;

import com.ontochem.imageHandler.ImageComparator;
import com.ontochem.imageHandler.RotatedIcon;

/**
 * Functions for the Java GUI menu bar and other operations.
 * 
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Added menu bar functions for the Java GUI.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author shadrack.j.barnabas@ontochem.com
 * @date 2023-06-14
 */
public class Functions {
    String fileName;
    String fileAddress;
    String encodedQuery;
    ImageComparator imageComparator;
    static Logger LOG = Logger.getLogger(Functions.class.getName());

    public Functions(ImageComparator imageComparator) {
        this.imageComparator = imageComparator;
    }

    /**
     * Open a file function.
     */
    public void openFile() {
        FileDialog fd = new FileDialog(imageComparator, "Open", FileDialog.LOAD);
        fd.setVisible(true);
        try {
            if (fd.getFile() != null) {
                fileName = fd.getFile();
                fileAddress = fd.getDirectory();
                //LOG.info("Loaded file!");
                // Apply the new frame
                ImageComparator ic = new ImageComparator(fileAddress + fileName);
                ic.setVisible(true);
                // Dispose the old frame
                this.imageComparator.dispose();
            }
        } catch (Exception e) {
            LOG.info("Error opening target " + e);
        }
    }

    /**
     * About function that reads the manual.
     */
    public void About() {
        try {
            String address = System.getProperty("user.dir");
            LOG.info(address + "/src/main/resources/Manual.pdf");
            File file = new File(address + "/src/main/resources/Manual.pdf");
            if (file.exists()) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    }
                } catch (Exception e) {
                    // No application registered for PDFs.
                }
            } else {
                LOG.info("File does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save as a file function that saves user responses to a file.
     */
    public void saveAsFile() {
        FileDialog fd = new FileDialog(imageComparator, "Save", FileDialog.SAVE);
        fd.setVisible(true);

        if (fd.getFile() != null) {
            fileName = fd.getFile();
            fileAddress = fd.getDirectory();
            imageComparator.setTitle(fileName);
        }

        try {
            LinkedHashMap<String, String> responseContainerMap = ImageComparator.getResponseContainerMap();
            FileWriter fw = new FileWriter(fileAddress + fileName);
            fw.write("#file,structure,comments");
            fw.write("\n");
            for (String key : responseContainerMap.keySet()) {
                if (!responseContainerMap.get(key).contains("deleted")) {
                    fw.write(responseContainerMap.get(key));
                    fw.write("\n");
                }
            }
            fw.close();
        } catch (Exception e) {
            LOG.info("Error saving file " + e);
        }
    }

    /**
     * Save as an image function that saves the user responses as images.
     */
    public void saveAsImage() {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);
        f.setAcceptAllFileFilterUsed(false);
        String fileAddress = f.getSelectedFile().getAbsolutePath();

        try {
            Map<String, String> i_id_buttonId = ImageComparator.getId_buttonId();
            Map<String, BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage();
            Map<String, String> i_id_image_structure = ImageComparator.getId_image_structure();
            Map<String, String> pathMap = new LinkedHashMap<String, String>();

            for (String id : i_id_buttonId.keySet()) {
                try {
                    String filename1 = "depiction_";
                    if (!i_id_buttonId.get(id).contains("_r")) {
                        String filename = i_id_image_structure.get(id);
                        Path path = Paths.get(filename);
                        filename1 = filename1 + path.getFileName().toString();
                        pathMap.put(id.replace("_i", ""), fileAddress + "/" + filename1);
                    }
                } catch (Exception e) {
                    // Handle the error.
                }
            }

            for (String id : i_id_buttonId.keySet()) {
                try {
                    if (i_id_buttonId.get(id).contains("_r")) {
                        BufferedImage image = i_id_BufferedImage.get(id);
                        String which = i_id_buttonId.get(id);
                        which = which.replace("_r", "");
                        int whichInt = Integer.parseInt(which) - 1;
                        String filename1 = pathMap.get(String.valueOf(whichInt));
                        String filenameWithoutExt = FilenameUtils.removeExtension(filename1);
                        String outputFilename = filenameWithoutExt + ".png";

                        // Create directories if they don't exist
                        File file = new File(outputFilename);
                        file.getParentFile().mkdirs();

                        File file1 = new File(outputFilename);
                        javax.imageio.ImageIO.write(image, "png", file1);
                    }
                } catch (Exception e) {
                    // Handle the error.
                }
            }
        } catch (Exception e) {
            LOG.info("Error saving images " + e);
        }
    }

    /**
     * Exit the Java GUI application.
     */
    public void exitFile() {
        System.exit(imageComparator.DISPOSE_ON_CLOSE);
    }

    /**
     * Convert a rotated icon to an image.
     * 
     * @param rotatedIcon The rotated icon to convert.
     * @return The image of the rotated icon.
     */
    public BufferedImage rotatedIconToImage(RotatedIcon rotatedIcon) {
        // Create a BufferedImage to paint the RotatedIcon onto
        int width = rotatedIcon.getIconWidth();
        int height = rotatedIcon.getIconHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Paint the RotatedIcon onto the BufferedImage
        Graphics2D g2d = bufferedImage.createGraphics();
        rotatedIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return bufferedImage;
    }
}
