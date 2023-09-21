package com.ontochem.filehandler;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import com.ontochem.imageHandler.ImageContainer;
/**
* <h3>Changelog</h3>
* <ul>
*   <li>2023-06-14
*     <ul>
*       <li>read input file to be used in the java GUI. The format of the input file is <image_file><space><structure> </li>
*     </ul>
*   </li>
* </ul>
* 
* @author shadrack.j.barnabas@ontochem.com
*/
public class FileHandler {
	
	/**
	 * 
	 * @param fileName
	 * @return list of image containers to be used in the java GUI
	 * @throws Exception
	 */
	public static List<ImageContainer> readFromFS(String fileName) throws Exception
	{
		List<ImageContainer> listOfImageContainers = new ArrayList<ImageContainer>();
		try {
			File file = new File(fileName);		
			RandomAccessFile f = new RandomAccessFile(file,"r");			
			long length = f.length();
			int curLine = 0;
			boolean restartFile = isARestartFile(fileName);
			while (f.getFilePointer() < length)
			{		
				String line = f.readLine();
				if(restartFile==false) {
					ImageContainer ic = new ImageContainer();
					// reading from new input file 
					//any image file along with its path
					String imageFile = line.split(",")[0];
					// structure contained in the image file
					String structure = line.split(",")[1];
					ic.setImageFile(imageFile);
					ic.setStructure(structure);
					ic.setUserComment("empty");
					listOfImageContainers.add(ic);
				}
				if(restartFile) {
					// reading from restart file
					if(curLine!=0) {
						ImageContainer ic = new ImageContainer();
						//any image file along with its path
						String imageFile = line.split(",")[0];
						// structure contained in the image file
						String structure = line.split(",")[1];
						String comment = line.split(",")[2];
						ic.setImageFile(imageFile);
						ic.setStructure(structure);
						ic.setUserComment(comment);
						listOfImageContainers.add(ic);
					}
				}
				curLine++;
			} 
			f.close();
		}catch (Exception e) {
			//System.out.println("Error loading input " + e);
		}
		return listOfImageContainers;
	}
	
	public static boolean isARestartFile(String fileName) throws Exception
	{
		boolean restartFile = false;
		try {
			File file = new File(fileName);		
			RandomAccessFile f = new RandomAccessFile(file,"r");			
			long length = f.length();
			int curLine = 0;
			String content = "";
			while (f.getFilePointer() < length)
			{				
				String line = f.readLine();
				curLine++;				
				if(curLine==1) {
					if(line.startsWith("#")) restartFile = true;
					break;
				}
				
			} 
			f.close();
		}catch (Exception e) {
			//System.out.println("Error loading input " + e);
		}
		return restartFile;
	}
}