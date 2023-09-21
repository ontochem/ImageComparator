package com.ontochem.imageHandler;

import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ontochem.chemistry.ChemistryFunctions;
import com.ontochem.filehandler.FileHandler;
import com.ontochem.functions.Functions;
import com.ontochem.structureToImage.StructureImageCreator;
/**
* this java gui helps to compare image generated from a structure with the image file 
* <h3>Changelog</h3>
* <ul>
*   <li>2023-06-14
*     <ul>
*       <li>image comparator</li>
*     </ul> 
*   </li>
* </ul>
* 
* @author shadrack.j.barnabas@ontochem.com
*/
public class ImageComparator extends JFrame implements ActionListener, MouseListener, ComponentListener, MouseWheelListener{
	static Logger LOG = Logger.getLogger( ImageComparator.class.getName() );
	static String path = "";
    private static double zoomFactor = 1.0;
 	static double rotationAngle = 0.0;
 	static JButton button[];
 	static List<ImageContainer> imageContainerList = new ArrayList<ImageContainer>();
 	static JPanel panel;
 	static int offset;
 	static String currentStructure;
 	static JMenu fileMenu ;
	static JMenu helpMenu ;
 	static JMenuBar menuBar;
 	static JMenuItem iAbout ;
    static JMenuItem iSaveAs;
    static JMenuItem iSaveDepictionAs;
    static JMenuItem iOpen ;
 	static JMenuItem iExit ;
 	static String whichButton;
 	static String reactionIdentify;
 	static String imageIdentify;
 	static Map<String,String> id_image_structure = new LinkedHashMap<String,String>();
 	static Map<String,BufferedImage> id_BufferedImage = new LinkedHashMap<String,BufferedImage>();
 	static Map<String,String> id_image_structure_ori = new LinkedHashMap<String,String>();
 	static Map<String,BufferedImage> id_BufferedImage_ori = new LinkedHashMap<String,BufferedImage>();
 	static boolean isAReaction = false;
 	static PopupMenu popup;
 	static MenuItem iAddExplicitH;
 	static MenuItem iAromatize;
 	static MenuItem iKekulize;
 	static MenuItem iRotate;
 	static MenuItem iCopy;
 	static MenuItem iEnterComment;
 	static Integer whichButtonId;
 	static String iStructure;
 	static BufferedImage CurrentBufferedImage;
 	static List<String> responseContainer = new LinkedList<String>();
 	static LinkedHashMap<String, String> responseContainerMap = new LinkedHashMap<String,String>();
 	static Map<String,String> id_buttonId = new LinkedHashMap<String,String>();
 	static Map<String,String> id_comment = new LinkedHashMap<String,String>();
   	Functions functions = new com.ontochem.functions.Functions(this);
   	ChemistryFunctions chemFunctions = new ChemistryFunctions(this);
   	public static Map<String, String> getId_comment() {
		return id_comment;
	}

	public static String getCurrentStructure() {
		return currentStructure;
	}

	public static void setCurrentStructure(String currentStructure) {
		ImageComparator.currentStructure = currentStructure;
	}

	public static JMenuItem getiSaveDepictionAs() {
		return iSaveDepictionAs;
	}

	public static void setiSaveDepictionAs(JMenuItem iSaveDepictionAs) {
		ImageComparator.iSaveDepictionAs = iSaveDepictionAs;
	}

	public static MenuItem getiCopy() {
		return iCopy;
	}

	public static void setiCopy(MenuItem iCopy) {
		ImageComparator.iCopy = iCopy;
	}

	public static void setId_comment(Map<String, String> id_comment) {
		ImageComparator.id_comment = id_comment;
	}

	public static LinkedHashMap<String, String> getResponseContainerMap() {
		return responseContainerMap;
	}

	public static void setResponseContainerMap(LinkedHashMap<String, String> responseContainerMap) {
		ImageComparator.responseContainerMap = responseContainerMap;
	}

	public static Logger getLOG() {
		return LOG;
	}

	public static void setLOG(Logger lOG) {
		LOG = lOG;
	}


	public static Map<String, String> getId_image_structure() {
		return id_image_structure;
	}


	public static void setId_image_structure(Map<String, String> id_image_structure) {
		ImageComparator.id_image_structure = id_image_structure;
	}


	public static PopupMenu getPopup() {
		return popup;
	}


	public static void setPopup(PopupMenu popup) {
		ImageComparator.popup = popup;
	}


	public static Map<String, BufferedImage> getId_BufferedImage() {
		return id_BufferedImage;
	}

	public static void setId_BufferedImage(Map<String, BufferedImage> id_BufferedImage) {
		ImageComparator.id_BufferedImage = id_BufferedImage;
	}

	public static MenuItem getiRotate() {
		return iRotate;
	}

	public static void setiRotate(MenuItem iRotate) {
		ImageComparator.iRotate = iRotate;
	}

	public static BufferedImage getCurrentBufferedImage() {
		return CurrentBufferedImage;
	}

	public static void setCurrentBufferedImage(BufferedImage currentBufferedImage) {
		CurrentBufferedImage = currentBufferedImage;
	}

	public static MenuItem getiAddExplicitH() {
		return iAddExplicitH;
	}


	public static void setiAddExplicitH(MenuItem iAddExplicitH) {
		ImageComparator.iAddExplicitH = iAddExplicitH;
	}


	public static MenuItem getiAromatize() {
		return iAromatize;
	}


	public static void setiAromatize(MenuItem iAromatize) {
		ImageComparator.iAromatize = iAromatize;
	}


	public static MenuItem getiKekulize() {
		return iKekulize;
	}


	public static void setiKekulize(MenuItem iKekulize) {
		ImageComparator.iKekulize = iKekulize;
	}


	public static MenuItem getiEnterComment() {
		return iEnterComment;
	}


	public static void setiEnterComment(MenuItem iEnterComment) {
		ImageComparator.iEnterComment = iEnterComment;
	}


	public static Integer getWhichButtonId() {
		return whichButtonId;
	}


	public static void setWhichButtonId(Integer whichButtonId) {
		ImageComparator.whichButtonId = whichButtonId;
	}


	public static String getiStructure() {
		return iStructure;
	}


	public static void setiStructure(String iStructure) {
		ImageComparator.iStructure = iStructure;
	}

	public static List<String> getResponseContainer() {
		return responseContainer;
	}

	public static void setResponseContainer(List<String> responseContainer) {
		ImageComparator.responseContainer = responseContainer;
	}


	public static Map<String, String> getId_buttonId() {
		return id_buttonId;
	}


	public static void setId_buttonId(Map<String, String> id_buttonId) {
		ImageComparator.id_buttonId = id_buttonId;
	}


	public ChemistryFunctions getChemFunctions() {
		return chemFunctions;
	}


	public void setChemFunctions(ChemistryFunctions chemFunctions) {
		this.chemFunctions = chemFunctions;
	}

	public static String getWhichButton() {
		return whichButton;
	}


	public static void setWhichButton(String whichButton) {
		ImageComparator.whichButton = whichButton;
	}

	public static boolean isAReaction() {
		return isAReaction;
	}


	public static void setAReaction(boolean isAReaction) {
		ImageComparator.isAReaction = isAReaction;
	}


	public Functions getFunctions() {
		return functions;
	}


	public void setFunctions(Functions functions) {
		this.functions = functions;
	}


	public static String getReactionIdentify() {
		return reactionIdentify;
	}


	public static void setReactionIdentify(String reactionIdentify) {
		ImageComparator.reactionIdentify = reactionIdentify;
	}


	public static String getImageIdentify() {
		return imageIdentify;
	}


	public static void setImageIdentify(String imageIdentify) {
		ImageComparator.imageIdentify = imageIdentify;
	}


	public static String getPath() {
		return path;
	}


	public static void setPath(String path) {
		ImageComparator.path = path;
	}

	public static JButton[] getButton() {
		return button;
	}

	
	public static void setButton(JButton[] button) {
		ImageComparator.button = button;
	}
	

	public static List<ImageContainer> getImageContainerList() {
		return imageContainerList;
	}


	public static void setImageContainerList(List<ImageContainer> imageContainerList) {
		ImageComparator.imageContainerList = imageContainerList;
	}

	public static JPanel getPanel() {
		return panel;
	}

	public static void setPanel(JPanel panel) {
		ImageComparator.panel = panel;
	}
	
	public static int getOffset() {
		return offset;
	}

	public static void setOffset(int offset) {
		ImageComparator.offset = offset;
	}


	public static JMenu getFileMenu() {
		return fileMenu;
	}


	public static Map<String, String> getId_image_structure_ori() {
		return id_image_structure_ori;
	}

	public static void setId_image_structure_ori(Map<String, String> id_image_structure_ori) {
		ImageComparator.id_image_structure_ori = id_image_structure_ori;
	}

	public static Map<String, BufferedImage> getId_BufferedImage_ori() {
		return id_BufferedImage_ori;
	}

	public static void setId_BufferedImage_ori(Map<String, BufferedImage> id_BufferedImage_ori) {
		ImageComparator.id_BufferedImage_ori = id_BufferedImage_ori;
	}

	public static void setFileMenu(JMenu fileMenu) {
		ImageComparator.fileMenu = fileMenu;
	}


	public static JMenu getHelpMenu() {
		return helpMenu;
	}


	public static void setHelpMenu(JMenu helpMenu) {
		ImageComparator.helpMenu = helpMenu;
	}


	public static void setMenuBar(JMenuBar menuBar) {
		ImageComparator.menuBar = menuBar;
	}


	public static JMenuItem getiAbout() {
		return iAbout;
	}


	public static void setiAbout(JMenuItem iAbout) {
		ImageComparator.iAbout = iAbout;
	}


	public static JMenuItem getiSaveDespictionAs() {
		return iSaveDepictionAs;
	}

	public static void setiSaveDespictionAs(JMenuItem iSaveDepictionAs) {
		ImageComparator.iSaveDepictionAs = iSaveDepictionAs;
	}

	public static JMenuItem getiSaveAs() {
		return iSaveAs;
	}


	public static void setiSaveAs(JMenuItem iSaveAs) {
		ImageComparator.iSaveAs = iSaveAs;
	}

	public static void setiOpen(JMenu iOpen) {
		ImageComparator.iOpen = iOpen;
	}


	public static JMenuItem getiExit() {
		return iExit;
	}


	public static void setiExit(JMenuItem iExit) {
		ImageComparator.iExit = iExit;
	}

	public static void main(String[] args) throws Exception {
	 	Runnable runnable = new Runnable() {

			@Override
			public void run() {
					try {
						ImageComparator ic = new ImageComparator(path);
						ic.setVisible(true);
				} catch (Exception e) {
						e.printStackTrace();
				}	
			}
			};
			SplashScreen splash = new SplashScreen();
			try {
		    	  // Make JWindow appear for 3 seconds before disappear
		          for (int i = 0; i <= 100; i++) {
		              splash.progressBar.setValue(i); // Update the progress bar value
		              Thread.sleep(30);
		          }
		          splash.dispose();
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
			SwingUtilities.invokeLater(runnable);
	}
	
	/**
	 * java GUI widget
	 * @param path of the inputfile in which each line contains information about an image path and its corresponding structure
	 * @throws Exception
	 */
	public ImageComparator(String path) throws Exception {
		ImageComparator.path = path;
		imageContainerList = FileHandler.readFromFS(ImageComparator.path);
		imageContainerList = generateImageContainer(imageContainerList);
        //button = new JButton[(imageContainerList.size()*2) + 1];
        button = new JButton[(imageContainerList.size()*2) + 1];
   		createWindow();
   		createButtons();
  		createMenuBar();
  		createFileMenu();
  		createScrollPane();
  		createPopupMenu();
  		setResizable(true);
  		add(popup);
	}
	
	/**
	 * java GUI frame widget
	 */
	public   void createWindow() {
  		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  		setBounds(0,0,800,600);
  		Container controlHost = getContentPane();
		controlHost.setLayout(new BorderLayout());
		setLocationRelativeTo(null);
	    addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            int buttonWidth = (panel.getWidth() - offset) / 3; // calculate the new button width
	        	int buttonHeight = (panel.getWidth() - offset) / 3;
	            // Loop through each button and set the new preferred size
	            for (int i = 1; i < button.length; i++) {
	                button[i].setPreferredSize(new Dimension(buttonWidth, buttonHeight));
	            }

	            panel.revalidate(); // revalidate the panel to update the layout
	        }
	    });
	}
	
	/**
	 * java GUI menu widget
	 */
	public void createFileMenu() {
		iOpen = new JMenuItem("Open");
    	iExit = new JMenuItem("Exit");
    	iAbout = new JMenuItem("About");
    	iSaveAs = new JMenuItem("SaveAs");
    	iSaveDepictionAs = new JMenuItem("SaveDepictionAs");
    	
    	iSaveDepictionAs.addActionListener(this);
  		iSaveDepictionAs.setActionCommand("SaveDepictionAs");
    	
    	iSaveAs.addActionListener(this);
  		iSaveAs.setActionCommand("SaveAs");
  		
    	iOpen.addActionListener(this);
  		iOpen.setActionCommand("Open");
  
  		iExit.addActionListener(this);
  		iExit.setActionCommand("Exit");
  		
  		iAbout.addActionListener(this);
  		iAbout.setActionCommand("About");

   		iOpen.setMnemonic(KeyEvent.VK_O);
   		iSaveAs.setMnemonic(KeyEvent.VK_S);
   		iExit.setMnemonic(KeyEvent.VK_E);
  		
   		fileMenu.add(iOpen);
   		fileMenu.add(iSaveAs);
   		fileMenu.add(iSaveDepictionAs);
    	fileMenu.add(iExit);
		helpMenu.add(iAbout);
 	}
	
	/**
	 * java GUI popup menu widget
	 */
	public void createPopupMenu() {
		popup = new PopupMenu();
		iCopy = new MenuItem("CopyStructure");
		iKekulize = new MenuItem("Kekulize");
		iAromatize = new MenuItem("Aromatize");
		iAddExplicitH = new MenuItem("AddExplicitH");
		iRotate = new MenuItem("Rotate");
		
		iRotate.addActionListener(this);
		iRotate.setActionCommand("Rotate");
		
		iCopy.addActionListener(this);
		iCopy.setActionCommand("CopyStructure");
		
		iKekulize.addActionListener(this);
		iKekulize.setActionCommand("Kekulize");
		
		iAromatize.addActionListener(this);
		iAromatize.setActionCommand("Aromatize");
		
		iAddExplicitH.addActionListener(this);
		iAddExplicitH.setActionCommand("AddExplicitH");
		
		popup.add(iCopy);
		popup.add(iRotate);
		popup.add(iAromatize);
		popup.add(iKekulize);
		popup.add(iAddExplicitH);	
 	}
	/**
	 * 
	 * @param list of image containers
	 * @return updated list of image containers
	 */
	public static List<ImageContainer> generateImageContainer(List<ImageContainer> imageContainerList) {
		Map<String,String> i_id_image_structure = new LinkedHashMap<String,String>();
		int id = 0;
		for(int i = 0 ;i<imageContainerList.size();i++) {
 			try {
 				ImageContainer ic = imageContainerList.get(i);
 				id = i_id_image_structure.size();
 				
 				String imageFile = "noImageFileFound";
 				try {
 					imageFile = ic.getImageFile();
 					id = id + 1;
 					i_id_image_structure.put(String.valueOf(id),imageFile);
 				}catch(Exception e) {
	 				LOG.info("error getting image file " + imageFile+ " " + e);
	 			}
 				
 				String smi = "noStructureFound";
 				try {
 					smi = ic.getStructure();
 	 				LOG.info(" generating images from structures " + i + " " + imageFile + " " + smi);
 					id = id + 1;
	 				i_id_image_structure.put(String.valueOf(id),smi);
 				}catch(Exception e) {
	 				LOG.info("error getting structure " + smi + " " + e);
	 			}
 				
	 			BufferedImage imageFromFile = null;
	 			try {
	 				imageFromFile = CropImage.generate(loadImage(imageFile));
	 				ic.setImageFromFile(imageFromFile);
	 			}catch(Exception e) {
	 				LOG.info("error generating image from file " +imageFromFile + " " + e);
	 			}
	 			
	 			BufferedImage imageFromStructure = null;
	 			try {	 			
	 				imageFromStructure = CropImage.generate(StructureImageCreator.create(smi));
	 				ic.setImageFromStructure(imageFromStructure);
	 			}catch(Exception e) {
	 				LOG.info("error generating image from structure " +imageFromStructure + " " + e);
	 			}
	 			
 			}catch(Exception e) {
 				LOG.info("error generating image container " +e);
 			}
 		}
		ImageComparator.setId_image_structure(i_id_image_structure);
		ImageComparator.setId_image_structure_ori(i_id_image_structure);
		//System.out.println(i_id_image_structure);
		return imageContainerList;
	}
	
	/**
	 * java GUI buttons widget
	 */
	
	public   void createButtons() { 	
	 	Map<String,String> i_id_buttonId = new LinkedHashMap<String,String>();
	 	Map<String,BufferedImage> i_id_BufferedImage = new LinkedHashMap<String,BufferedImage>();
	 	Map<String,String> i_id_userComment = new LinkedHashMap<String,String>();
 		panel = new JPanel();
		int hgap = 0; // horizontal space between buttons in a grid
		int vgap = 0; // vertical space between buttons in a grid
		GridLayout gl = null;
		int imageId = 0;
		boolean itr = true;
		int cnt = 0;
		panel.setBounds(100,100,800,600);
   		for(int i = 0 ;i<imageContainerList.size();i++) {
 			try {
 				itr = true;
 				ImageContainer ic = imageContainerList.get(i);
 				ic.getStructure();
 				String imageFile = ic.getImageFile();
 				removeExtension(imageFile);
 				BufferedImage imageFromFile = ic.getImageFromFile();
 				BufferedImage imageFromStructure = ic.getImageFromStructure();
 				BufferedImage img = imageFromFile;
   				//ImageIO.write(img, "png", new File(fileNameWithoutExtension+"_final_"+imageId+".png"));
				
 				while(itr) {
 					cnt = cnt + 1;
 					if(cnt==1) {
 		 				imageId = imageId + 1;
 						img = imageFromFile;
 						button[imageId] = new JButton(String.valueOf(imageId));
 	 					i_id_buttonId.put(String.valueOf(imageId),String.valueOf(imageId)+"_i");
 	 					i_id_BufferedImage.put(String.valueOf(imageId),img);
 	 					i_id_userComment.put(String.valueOf(imageId), ic.getUserComment());
 					}
 					if(cnt==2) {
 						cnt = 0;
 						itr = false;
 						img = imageFromStructure;
 						imageId = imageId + 1;
 	 					button[imageId] = new JButton(String.valueOf(imageId));
 	 					i_id_buttonId.put(String.valueOf(imageId),String.valueOf(imageId)+"_r" );
 	 					i_id_BufferedImage.put(String.valueOf(imageId),img);
 	 					i_id_userComment.put(String.valueOf(imageId), ic.getUserComment());
 	 					if(!ic.getUserComment().contains("empty")) {
 	 	 					button[imageId].setBorder(new LineBorder(Color.green.darker(),4));
 	 					}
 	 					else {
 	 						button[imageId].setBorder(new LineBorder(Color.black,1));
 	 					}
 			            responseContainerMap.put(imageId+","+ic.getImageFile(), ic.getImageFile()+ "," +ic.getStructure() + "," + ic.getUserComment());
 					}
 					offset = button[imageId].getInsets().left;
					button[imageId].setIcon(progressiveScaling(img, panel.getWidth()-offset));
 					button[imageId].setContentAreaFilled(false);
 					button[imageId].setBorderPainted(true);
 					panel.setBackground(Color.WHITE);
 					button[imageId].setBackground(Color.WHITE);
 					button[imageId].setContentAreaFilled(false);
 					button[imageId].setOpaque(true);
 					button[imageId].setFocusPainted(false);
 					button[imageId].addActionListener(this);
 					button[imageId].addMouseListener(this);
 					button[imageId].addMouseWheelListener(this);
 					gl = new GridLayout(0,2,hgap, vgap); // 0 - infinite rows items, 1 - column
 					/*
 					// Create a scroll pane to wrap the button
 			        JScrollPane scrollPane = new JScrollPane(button[imageId]);
 			        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
 			        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 			        
 			        // Add the scroll pane to the panel
 			        panel.add(scrollPane);
 			        */
 					panel.add(button[imageId]);
 				}
    			panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    			panel.setLayout(gl);
    			
 			}catch(Exception e) {
 				LOG.info("error creating buttons panel " +e);
 			}
 		}
   		ImageComparator.setId_buttonId(i_id_buttonId);
		ImageComparator.setId_BufferedImage(i_id_BufferedImage);
		ImageComparator.setId_BufferedImage_ori(i_id_BufferedImage);
		ImageComparator.setId_comment(i_id_userComment);
		//LOG.info(" created button icons " + button.length);		
	}
	
	/**
	 * 
	 * @param string
	 * @return remove extensions in a string
	 */
	public static String removeExtension(final String s)
    {
        return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
    }
	
	/**
	 * java GUI menu bar widget
	 */
	public void createMenuBar() {
		menuBar = new JMenuBar();
  		fileMenu = new JMenu("File");
  		fileMenu.setMnemonic(KeyEvent.VK_F);
  		helpMenu = new JMenu("Help");
  		helpMenu.setMnemonic(KeyEvent.VK_H);
  		menuBar.add(fileMenu);
  		menuBar.add(helpMenu);
  		setJMenuBar(menuBar);
	}	

	/**
	 * java GUI scroll panel widget
	 */
	public void createScrollPane1() {
		Container controlHost = getContentPane();
		controlHost.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  		controlHost.add(scroll);
	}
	public void createScrollPane() {
	    Container controlHost = getContentPane();
	    controlHost.setLayout(new BorderLayout());
	    
	    // Create the panel with your content (e.g., JPanel panel = new JPanel();)
	    // ...

	    JScrollPane scroll = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    
	    // Set the unit increment for vertical and horizontal scrolling
	    int verticalScrollSpeed = 25; // Adjust this value to control vertical scroll speed
	    int horizontalScrollSpeed = 25; // Adjust this value to control horizontal scroll speed
	    scroll.getVerticalScrollBar().setUnitIncrement(verticalScrollSpeed);
	    scroll.getHorizontalScrollBar().setUnitIncrement(horizontalScrollSpeed);

	    controlHost.add(scroll);
	}
	/**
	 * 
	 * @param imagePath
	 * @return buffered image
	 */
	private static BufferedImage loadImage(String imagePath) {
    	BufferedImage img = null;
    	try {
        	img = ImageIO.read(new File(imagePath)); 
        } catch (Exception e) {
            LOG.info(" image not found " + e);
        }
        return img;
    }
	

	/**
	 * 
	 * @param buffered image
	 * @param longestSideLength of the buffered image
	 * @return scaled buffered image
	 */
    public static ImageIcon progressiveScaling(BufferedImage before, Integer longestSideLength) {
	    if (before != null) {
	        Integer w = before.getWidth();
	        Integer h = before.getHeight();

	        Double ratio = h > w ? longestSideLength.doubleValue() / h : longestSideLength.doubleValue() / w;

	        //Multi Step Rescale operation
	        //This technique is describen in Chris Campbell’s blog The Perils of Image.getScaledInstance(). As Chris mentions, when downscaling to something less than factor 0.5, you get the best result by doing multiple downscaling with a minimum factor of 0.5 (in other words: each scaling operation should scale to maximum half the size).
	        while (ratio < 0.5) {
	            BufferedImage tmp = scale(before, 0.5);
	            before = tmp;
	            w = before.getWidth();
	            h = before.getHeight();
	            ratio = h > w ? longestSideLength.doubleValue() / h : longestSideLength.doubleValue() / w;
	        }
	        BufferedImage after = scale(before, ratio);
	        ImageIcon icon = new ImageIcon(after);
		    return icon;
 	    }
	    return null;
	}
   
    /**
     * 
     * @param imageToScale
     * @param ratio
     * @return scaled buffered image
     */
	public static BufferedImage scale(BufferedImage imageToScale, Double ratio) {
	    Integer dWidth = ((Double) (imageToScale.getWidth() * ratio)).intValue();
	    Integer dHeight = ((Double) (imageToScale.getHeight() * ratio)).intValue();
	    BufferedImage scaledImage = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = scaledImage.createGraphics();
	    graphics2D.setBackground(java.awt.Color.WHITE);
	    graphics2D.clearRect(0,0,dWidth, dHeight);
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
	    graphics2D.dispose();
	    return scaledImage;
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON3) {
			JButton button = (JButton) arg0.getSource();
			Map<String,String> i_id_buttonId = ImageComparator.getId_buttonId();
			Map<String,String> i_id_image_structure = ImageComparator.getId_image_structure();
			Map<String,String> i_id_comment = ImageComparator.getId_comment();
			
			String val = button.getText();
 			String buttonId = i_id_buttonId.get(val);
 			String userComment = i_id_comment.get(val);
 			
	 		if(buttonId.contains("_r")) {
	 			String iStructure = i_id_image_structure.get(val);
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			boolean isAReaction = StructureImageCreator.isReaction(iStructure);
	 			ImageComparator.setAReaction(isAReaction);
	   			popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	 			Integer whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			int whichButtonIdMinusOne = whichButtonId - 1;
	 			JTextArea view = new JTextArea(userComment);
	            JScrollPane message = new JScrollPane(view, 20, 30);	           
	            message.setPreferredSize(new Dimension(50, 50));
	            JOptionPane.showMessageDialog(null, message);
	            
	            //refresh user comment
	            i_id_comment.put(val, view.getText());
	            ImageComparator.setId_comment(i_id_comment);
	            
	            // Highlight the button with a green border
	            if(view.getText().length()<1) {
	            	view.setText("deleted");
	            }
	            if(!view.getText().isBlank()) {
	            	if(!view.getText().equals("empty")) {
			            button.setBorder(new LineBorder(Color.green.darker(), 4));
		            }
	            	else {
	            		button.setBorder(new LineBorder(Color.black, 1));
	            	}
		            if(view.getText().equals("deleted")) button.setBorder(new LineBorder(Color.black, 1));		
		            String uniqueKey = val+","+i_id_image_structure.get(String.valueOf(whichButtonIdMinusOne));
		            String response = i_id_image_structure.get(String.valueOf(whichButtonIdMinusOne))+","+iStructure + "," + view.getText();
	            	responseContainerMap.put(uniqueKey, response);
	            	responseContainer.add("imageId:"+val  + ",image:" + i_id_image_structure.get(String.valueOf(whichButtonIdMinusOne)) + ",structure:" + iStructure + ",comment:" + view.getText());
		            LOG.info("added response ...! " + "imageId:"+val  + ",image:" + i_id_image_structure.get(String.valueOf(whichButtonIdMinusOne)) + ",structure:" + iStructure + ",comment:" + view.getText());
	            }
	 		}
	 		if(!buttonId.contains("_r")) {
	 			String iStructure =  "isNotAStructure";
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			String iOriginalImage = 	i_id_image_structure.get(val);
	 			int whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			int whichButtonIdPlusOne = whichButtonId + 1;
	 			/*
	 			JTextArea view = new JTextArea(userComment);
	            JScrollPane message = new JScrollPane(view, 20, 30);
	            message.setPreferredSize(new Dimension(50, 50));
	            JOptionPane.showMessageDialog(null, message);
	            //refresh user comment
	            i_id_comment.put(val, view.getText());
	            ImageComparator.setId_comment(i_id_comment);
	            if(view.getText().length()<1) {
	            	view.setText("deleted");
	            }
	            LOG.info("imageId:"+val  + ",image:" + iOriginalImage + ",structure:" + i_id_image_structure.get(String.valueOf(whichButtonIdPlusOne)) + ",comment:" + view.getText());
	            if(!view.getText().isBlank()) {
	            	if(!view.getText().equals("empty")) {
			            button.setBorder(new LineBorder(Color.green.darker(), 4));
		            }
	            	else {
	            		button.setBorder(new LineBorder(Color.black, 1));
	            	}
		            if(view.getText().equals("deleted")) button.setBorder(new LineBorder(Color.black, 1));
		         	String uniqueKey = val+","+iOriginalImage;
		            responseContainerMap.put(uniqueKey, iOriginalImage+","+i_id_image_structure.get(String.valueOf(whichButtonIdPlusOne)) + "," + view.getText());
		            responseContainer.add("imageId:"+val  + ",image:" + iOriginalImage + ",structure:" + i_id_image_structure.get(String.valueOf(whichButtonIdPlusOne)) + ",comment:" + view.getText());
		            LOG.info("added response ...! " + "imageId:"+val  + ",image:" + iOriginalImage + ",structure:" + i_id_image_structure.get(String.valueOf(whichButtonIdPlusOne)) + ",comment:" + view.getText());
	            }
	            */
	            LOG.info("added response ...! " + "imageId:"+val  + ",image:" + iOriginalImage + ",structure:" + i_id_image_structure.get(String.valueOf(whichButtonIdPlusOne)));
	 		}
		}
		if(arg0.getButton() == MouseEvent.BUTTON2) {
	        JButton button = (JButton) arg0.getSource();
	        Map<String,String> i_id_buttonId = ImageComparator.getId_buttonId();
			Map<String,String> i_id_image_structure = ImageComparator.getId_image_structure();
			Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage();
			//System.out.println(i_id_buttonId);
			//System.out.println(i_id_image_structure);
			//System.out.println(i_id_BufferedImage);
			String val = button.getText();
 			String buttonId = i_id_buttonId.get(val);
 			ImageComparator.setCurrentBufferedImage(i_id_BufferedImage.get(val));
	 		if(buttonId.contains("_r")) {
	 			String iStructure = i_id_image_structure.get(val);
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			String iOriginalImage = "isNotAOriginalImage";
	 			boolean isAReaction = StructureImageCreator.isReaction(iStructure);
	 			ImageComparator.setAReaction(isAReaction);
	   			popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	 			Integer whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			LOG.info("imageId:" + val + ",image:" + iOriginalImage + ",structure:" + iStructure + ",isAReaction:" + isAReaction);
	 		}
	 		if(!buttonId.contains("_r")) {
	 			String iStructure =  "isNotAStructure";
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			String iOriginalImage = 	i_id_image_structure.get(val);
	 			Integer whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
	 			LOG.info("imageId:" + val + ",image:" + iOriginalImage + ",structure:" + iStructure);
	 		}
		}
		if(arg0.getButton() == MouseEvent.BUTTON1) {
			JButton button = (JButton) arg0.getSource();
			Map<String,String> i_id_buttonId = ImageComparator.getId_buttonId();
			Map<String,String> i_id_image_structure = ImageComparator.getId_image_structure();
			ImageComparator.getId_BufferedImage();
	 		String val = button.getText();
 			String buttonId = i_id_buttonId.get(val);
	 		if(buttonId.contains("_r")) {
	 			String iStructure = i_id_image_structure.get(val);
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			String iOriginalImage = "isNotAOriginalImage";
	 			boolean isAReaction = StructureImageCreator.isReaction(iStructure);
	 			ImageComparator.setAReaction(isAReaction);
	 			int whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			LOG.info("imageId:" + val + ",image:" + iOriginalImage + ",structure:" + iStructure + ",isAReaction:" + isAReaction);
	 		}
	 		if(!buttonId.contains("_r")) {
	 			String iStructure =  "isNotAStructure";
	 			ImageComparator.setiStructure(iStructure);
				ImageComparator.setCurrentStructure(iStructure);
	 			String iOriginalImage = 	i_id_image_structure.get(val);
	 			int whichButtonId = Integer.valueOf(val);
	 			ImageComparator.setWhichButtonId(whichButtonId);
	 			LOG.info("imageId:" + val + ",image:" + iOriginalImage + ",structure:" + iStructure);
	 		}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	   
	}  
	@Override
	public void actionPerformed(ActionEvent arg0) {
		 		String command = arg0.getActionCommand();
				switch(command) {
				
					case "Open": functions.openFile();;
					break;
					
					case "SaveAs": functions.saveAsFile();
					break;
					
					case "SaveDepictionAs": functions.saveAsImage();
					break;
					
					case "Exit": functions.exitFile();
					break;
					
					case "About": functions.About();
					break;
					
					case "Aromatize":
						try {
							if(!ImageComparator.getiStructure().equals("isNotAStructure")) {
								String aromSmiles = ChemistryFunctions.aromatizeStructure(ImageComparator.getiStructure(),ImageComparator.isAReaction());
								LOG.info(" aromatized structure " + aromSmiles);
								ImageComparator.setCurrentStructure(aromSmiles);
								if(ImageComparator.isAReaction()) {
									BufferedImage image1 = CropImage.generate(StructureImageCreator.create(aromSmiles));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image1, 800));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image1);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), aromSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								} else {
									BufferedImage image2 = CropImage.generate(StructureImageCreator.create(aromSmiles));
									//BufferedImage image2 = CropImage.generate(StructureImageCreator.imageGenerationOcl(aromSmiles, "aromatize"));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image2, 600));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image2);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), aromSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								}
							}
							else {
								// do nothing
							}
 						} catch (Exception e) {
 							//e.printStackTrace();
 						}
 					break;
					
					case "Kekulize":
						try {
							if(!ImageComparator.getiStructure().equals("isNotAStructure")) {
								String kekulizeSmiles = ChemistryFunctions.kekulizeStructure(ImageComparator.getiStructure(),ImageComparator.isAReaction());
								LOG.info(" kekulized structure "+kekulizeSmiles);
								ImageComparator.setCurrentStructure(kekulizeSmiles);
								if(ImageComparator.isAReaction()) {
									BufferedImage image1 = CropImage.generate(StructureImageCreator.create(kekulizeSmiles));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image1, 800));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image1);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), kekulizeSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								} else {
									BufferedImage image2 = CropImage.generate(StructureImageCreator.create(kekulizeSmiles));
									//BufferedImage image2 = CropImage.generate(StructureImageCreator.imageGenerationOcl(kekulizeSmiles, "kekulize"));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image2, 600));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image2);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), kekulizeSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								}
							}
							else {
								// do nothing
							}
 						} catch (Exception e) {
 							//e.printStackTrace();
 						}
 					break;
 					
					case "AddExplicitH": 
						try {
							if(!ImageComparator.getiStructure().equals("isNotAStructure")) {
								String explicitHAddedSmiles = ChemistryFunctions.addExplicitH(ImageComparator.getiStructure(),ImageComparator.isAReaction);
								LOG.info(" add explicit hydrogens to structure " + explicitHAddedSmiles);
								ImageComparator.setCurrentStructure(explicitHAddedSmiles);
								if(ImageComparator.isAReaction) {
									BufferedImage image1 = CropImage.generate(StructureImageCreator.create(explicitHAddedSmiles));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image1, 800));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image1);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), explicitHAddedSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								} else {
									BufferedImage image2 = CropImage.generate(StructureImageCreator.create(explicitHAddedSmiles));
									//BufferedImage image2 = CropImage.generate(StructureImageCreator.imageGenerationOcl(explicitHAddedSmiles, "addExplicitHydrogen"));
									button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(image2, 600));
									Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
									i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), image2);
									ImageComparator.setId_BufferedImage(i_id_BufferedImage);
									
									//Map<String,String> i_id_structure = ImageComparator.getId_image_structure_ori();
									//i_id_structure.put(String.valueOf(ImageComparator.getWhichButtonId()), explicitHAddedSmiles);
									//ImageComparator.setId_image_structure(i_id_structure);
								}
							}
							else {
								// do nothing
							}
						} catch (Exception e) {
 							//e.printStackTrace();
 						}
 					break;
 					
					case "Rotate":
					    try {
					        if (!ImageComparator.getiStructure().equals("isNotAStructure")) {
					            rotationAngle += 90;
					            BufferedImage img = rotate(ImageComparator.getCurrentBufferedImage(), rotationAngle);
					            if (ImageComparator.isAReaction()) {
					                button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(img, 800));
					                Map<String, BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
					                i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), img);
					                ImageComparator.setId_BufferedImage(i_id_BufferedImage);
					            } else {
					                button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(img, 600));
					                Map<String, BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
					                i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), img);
					                ImageComparator.setId_BufferedImage(i_id_BufferedImage);
					            }
					        } else {
					            rotationAngle += 90;
					            BufferedImage img = rotate(ImageComparator.getCurrentBufferedImage(), rotationAngle);
					            button[ImageComparator.getWhichButtonId()].setIcon(progressiveScaling(img, 800));
					            Map<String, BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage_ori();
				                i_id_BufferedImage.put(String.valueOf(ImageComparator.getWhichButtonId()), img);
				                ImageComparator.setId_BufferedImage(i_id_BufferedImage);
					        }
					    } catch (Exception e) {
					        // Handle the exception
					    }
					    break;
					
					case "CopyStructure":
						try {
							if(!ImageComparator.getiStructure().equals("isNotAStructure")) {
								String structure = ImageComparator.getCurrentStructure();
								LOG.info(" copied structure " + structure);
								Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
								StringSelection strse1 = new StringSelection(structure);
								clip.setContents(strse1, strse1);
								//JOptionPane.showMessageDialog(null,"TEXTS ARE COPIED!");
							}
							else {
								// do nothing
								String structure = ImageComparator.getCurrentStructure();
								LOG.info(" copied structure " + structure);
								Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
								StringSelection strse1 = new StringSelection(structure);
								clip.setContents(strse1, strse1);
								//JOptionPane.showMessageDialog(null,"TEXTS ARE COPIED!");
							}
 						} catch (Exception e) {
 							//e.printStackTrace();
 						}
 					break;
 					
				}
  	}
	public static BufferedImage rotate(BufferedImage originalImage, double degrees) {
	    double radians = Math.toRadians(degrees);

	    // Calculate the new size to accommodate the rotated image
	    double sin = Math.abs(Math.sin(radians));
	    double cos = Math.abs(Math.cos(radians));
	    int newWidth = (int) Math.floor(originalImage.getWidth() * cos + originalImage.getHeight() * sin);
	    int newHeight = (int) Math.floor(originalImage.getHeight() * cos + originalImage.getWidth() * sin);

	    BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = rotatedImage.createGraphics();
	    g2d.setBackground(Color.WHITE); // Set the background to white
	    g2d.clearRect(0, 0, newWidth, newHeight);

	    // Apply rotation with interpolation
	    AffineTransform transform = new AffineTransform();
	    transform.translate(newWidth / 2.0, newHeight / 2.0);
	    transform.rotate(radians);
	    transform.translate(-originalImage.getWidth() / 2.0, -originalImage.getHeight() / 2.0);
	    g2d.drawImage(originalImage, transform, null);
	    g2d.dispose();

	    return rotatedImage;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	   
	}

	/**
	 * 
	 * @param QueryWithPattern
	 * @param patternType
	 * @return segment stings as a list
	 */
	public static List<String> Segmenter( String QueryWithPattern, String patternType ) {
			try {
				List<String> newQryList = Lists.newArrayList(Splitter.on(patternType).trimResults().omitEmptyStrings().splitToList(QueryWithPattern));
				return newQryList;
			} catch( Exception e ) {
				LOG.info( "error smarts segmenter: " + e );
			}
			return null;
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Find the button that triggered the event
        JButton button = (JButton) e.getComponent();
		String val = button.getText();
		Map<String,BufferedImage> i_id_BufferedImage = ImageComparator.getId_BufferedImage();

            double scaleFactor = 1.0;
            int wheelRotation = e.getWheelRotation();
            if (wheelRotation > 0) {
                scaleFactor = 0.9; // Zoom out
            } else if (wheelRotation < 0) {
                scaleFactor = 1.1; // Zoom in
            }

            // Update the zoomFactor for this button
            zoomFactor *= scaleFactor;

            // Scale the button's icon using progressiveScaling method
            BufferedImage currentImage =  i_id_BufferedImage.get(val);
            if (currentImage != null) {
                ImageIcon scaledIcon = progressiveScaling(currentImage, (int) (currentImage.getWidth() * zoomFactor));
                button.setIcon(scaledIcon);
            }
     }
	
}