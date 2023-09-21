package com.ontochem.filehandler;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import com.ontochem.imageHandler.ImageContainer;

import jdk.internal.org.jline.utils.Log;

/**
 * A file handler for reading input files and managing image containers.
 * 
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Added the ability to read input files for use in the Java GUI. The format of the input file is <image_file><space><structure>.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author shadrack.j.barnabas@ontochem.com
 * @date 2023-06-14
 */
public class FileHandler {
    
    /**
     * Read image file information from the filesystem.
     * 
     * @param fileName The name of the input file.
     * @return List of image containers to be used in the Java GUI.
     * @throws Exception If an error occurs during file reading.
     */
    public static List<ImageContainer> readFromFS(String fileName) throws Exception {
        List<ImageContainer> listOfImageContainers = new ArrayList<ImageContainer>();
        try {
            File file = new File(fileName);
            RandomAccessFile f = new RandomAccessFile(file, "r");
            long length = f.length();
            int curLine = 0;
            boolean restartFile = isARestartFile(fileName);

            while (f.getFilePointer() < length) {
                String line = f.readLine();
                
                if (!restartFile) {
                	String[] parts = line.split(" ");
                    if (parts.length != 2) {
                        Log.info("Invalid line format in file: " + line);
                    }
                    else {
	                    ImageContainer ic = new ImageContainer();
	                    // Reading from a new input file.
	                    // Image file along with its path.
	                    String imageFile = line.split(" ")[0];
	                    // Structure contained in the image file.
	                    String structure = line.split(" ")[1];
	                    ic.setImageFile(imageFile);
	                    ic.setStructure(structure);
	                    ic.setUserComment("empty");
	                    listOfImageContainers.add(ic);
                    }
                }
                if (restartFile) {
                    // Reading from a restart file.
                    if (curLine != 0) {
                    	String[] parts = line.split(" ");
                        if (parts.length != 3) {
                        	Log.info("Invalid line format in restart file: " + line);
                        }
                        else {
	                        ImageContainer ic = new ImageContainer();
	                        // Image file along with its path.
	                        String imageFile = line.split(" ")[0];
	                        // Structure contained in the image file.
	                        String structure = line.split(" ")[1];
	                        String comment = line.split(" ")[2];
	                        ic.setImageFile(imageFile);
	                        ic.setStructure(structure);
	                        ic.setUserComment(comment);
	                        listOfImageContainers.add(ic);
                        }
                    }
                }
                curLine++;
            }
            f.close();
        } catch (Exception e) {
            // Handle or log the error.
        }
        return listOfImageContainers;
    }

    /**
     * Check if the input file is a restart file by examining the first line.
     * 
     * @param fileName The name of the input file.
     * @return True if the file is a restart file, false otherwise.
     * @throws Exception If an error occurs during file reading.
     */
    public static boolean isARestartFile(String fileName) throws Exception {
        boolean restartFile = false;
        try {
            File file = new File(fileName);
            RandomAccessFile f = new RandomAccessFile(file, "r");
            long length = f.length();
            int curLine = 0;
            String content = "";

            while (f.getFilePointer() < length) {
                String line = f.readLine();
                curLine++;

                if (curLine == 1) {
                    if (line.startsWith("#")) {
                        restartFile = true;
                    }
                    break;
                }
            }
            f.close();
        } catch (Exception e) {
            // Handle or log the error.
        }
        return restartFile;
    }
}
