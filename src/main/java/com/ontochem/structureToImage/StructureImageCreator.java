package com.ontochem.structureToImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import com.actelion.research.chem.AbstractDepictor;
import com.actelion.research.chem.StereoMolecule;
import com.actelion.research.gui.generic.GenericDepictor;
import com.actelion.research.gui.generic.GenericRectangle;
import com.actelion.research.gui.swing.SwingDrawContext;
import com.ontochem.chemistry.ChemistryFunctions;
import com.ontochem.chemistry.RuleBasedFilter;
/**
* <h3>Changelog</h3>
* <ul>
*   <li>2023-06-14
*     <ul>
*       <li>structure to image creation using cheminformatic toolkits</li>
*     </ul>
*   </li>
* </ul>
* 
* @author shadrack.j.barnabas@ontochem.com
*/
public class StructureImageCreator {
	private static Logger LOG = Logger.getLogger( StructureImageCreator.class.getName() );
	static int width = 800;
	static int height = 6000;    
	/**
	 * 
	 * @param structure
	 * @return buffered image
	 * @throws Exception
	 */
    public static BufferedImage create(String structure) throws Exception {
        BufferedImage depictedImage = null ;
        boolean isReactionMode = false;
        try {
        	if(isReaction(structure)) {
        		isReactionMode = true;
        		depictedImage = imageGeneration(structure, isReactionMode, "cdk");
          	}
        	else {
        		isReactionMode = false;
        		depictedImage = imageGeneration(structure, isReactionMode, "cdk");        		
        	}
        } catch (Exception e) {
        	LOG.info(" error parsing structure in cdk or ocl " + e); 
        }
        return depictedImage;
    } 
    
    /**
     * 
     * @param structure
     * @return true if the structure is a reaction or else false
     */
    public static boolean isReaction(String structure) {
    	boolean isAReaction = false;
    	try {
    		if(structure.contains(">")) isAReaction = true;
    	}catch (Exception e) {
    	LOG.info(" error in judging wheather the structure is a reaction or not " + e);
    	}
    	return isAReaction;    	
    }
    
    /**
     * 
     * @param structure
     * @param isReactionMode
     * @param module
     * @return bufferedimage
     * @throws Exception
     */
    public static BufferedImage imageGeneration(String structure, boolean isReactionMode, String module) throws Exception {
    	BufferedImage image = null;
    	try {
    		if(module.equals("cdk")) {
    			image = imageGenerationCdk(structure, isReactionMode); 
    		}
    		if(module.equals("ocl")) {
    			if(isReactionMode==false) {
    				image = imageGenerationOcl(structure, "kekulize");
    			}
    			else {
    				LOG.info(" reaction mode not yet implemted in ocl ");
    			}
    		}
    	}catch (Exception e) {
    		LOG.info(" error generating buffered image using cdk or ocl " + e);
    	}
    	return image;
    }
    /**
     * 
     * @param structure
     * @param isReactionMode
     * @return bufferedimage
     * @throws Exception
     */
    public static BufferedImage imageGenerationCdk(String structure, boolean isReactionMode) throws Exception {
        SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    	BufferedImage image = null;
    	IAtomContainer molecule;
        IReaction reaction;
       
    	try {
    		if(isReactionMode) {
        		reaction = parser.parseReactionSmiles(structure);
        		image = imageGenerationCdk(reaction);
    		}
    		else {
    			molecule = parser.parseSmiles(structure);
    			image = imageGenerationCdk(molecule);
    		}
    	}catch (Exception e) {
    		LOG.info(" error generating buffered image using cdk " + e);
    	}
		return image;
    }
    
    /**
     * 
     * @param reaction
     * @return bufferedimagee
     * @throws Exception
     */
    public static BufferedImage imageGenerationCdk(IReaction reaction) throws Exception {
    	// Generate a 2D depiction of the molecule
		DepictionGenerator generator = new DepictionGenerator().withSize(width, height).withAtomColors().withFillToFit().withZoom(2.0);
        BufferedImage awtImage = generator.depict(reaction).toImg();
        // Create a new BufferedImage with a white background
        BufferedImage finalImage = new BufferedImage(awtImage.getWidth(), awtImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = finalImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, awtImage.getWidth(), awtImage.getHeight());
        graphics.drawImage(awtImage, 0, 0, null);
        return finalImage;
    }
    
    /**
     * 
     * @param IAtomContainer molecule
     * @return bufferedimage
     * @throws Exception
     */
    public static BufferedImage imageGenerationCdk(IAtomContainer molecule) throws Exception {
		// Generate a 2D depiction of the molecule
		DepictionGenerator generator = new DepictionGenerator().withSize(width, height).withAtomColors().withFillToFit().withZoom(10.0);
        BufferedImage awtImage = generator.depict(molecule).toImg();
        // Create a new BufferedImage with a white background
        BufferedImage finalImage = new BufferedImage(awtImage.getWidth(), awtImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = finalImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, awtImage.getWidth(), awtImage.getHeight());
        graphics.drawImage(awtImage, 0, 0, null);
        return finalImage;
    }

    /**
     * 
     * @param stereomolecule 
     * @return bufferedimage
     * @throws Exception
     */
    public static BufferedImage imageGenerationOcl(StereoMolecule mol) throws Exception {
		 // This is the intended image size
		   final int BOND_LENGTH = 64; // in image pixel

		   // create an image that supports transparency
		   BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		   // get the Graphics object from the image, which allows drawing into the image
		   Graphics2D imageG = image.createGraphics();
		   
		   // A Depictor2D is an object that can draw a molecule onto a Graphics2D
		   GenericDepictor depictor = new GenericDepictor(mol);
		   
		   // this is required to suppress all unwanted labels
		   depictor.setDisplayMode(AbstractDepictor.cDModeSuppressCIPParity| AbstractDepictor.cDModeSuppressChiralText | AbstractDepictor.cDModeSuppressESR |AbstractDepictor.cDModeHiliteAllQueryFeatures);

		   SwingDrawContext context = new SwingDrawContext(imageG);
		   context.setFont(0, false, false);
		   
		   // The molecule has its own atom coordinates. We ask the depictor to calculate its own
		   // scaling factor and translation vector to optimally position the molecule into the view
		   depictor.validateView(context, new GenericRectangle(0, 0, width, height), AbstractDepictor.cModeInflateToMaxAVBL | BOND_LENGTH );  
		   
		   // Set some hints for the Java rendering engine to improve image quality
		   imageG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		   imageG.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		   imageG.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		   
		   // draw the molecule into the otherwise transparent image
		   depictor.paint(context);
		  
		  return image;
	}
		
    /**
     * 
     * @param _smiles
     * @param style either kekulize, aromatize or to add explicit hydrogens using OpenChemLib 
     * @return bufferedimage
     */
	public static BufferedImage imageGenerationOcl(String _smiles, String style) {
		BufferedImage image = null;
		try {
					int mode = com.actelion.research.chem.SmilesParser.SMARTS_MODE_IS_SMILES ;
					StereoMolecule mol = new StereoMolecule();
					com.actelion.research.chem.SmilesParser sp = new com.actelion.research.chem.SmilesParser( mode, false);
					sp.parse(mol, _smiles);
					boolean isAbnormallyValent = RuleBasedFilter.isMoleculeAbnormallyValent(_smiles,"ocl");
					boolean isRingCountAbnormal = RuleBasedFilter.isRingCountAbnormal(_smiles, "ocl", 30);
					if(isAbnormallyValent || isRingCountAbnormal) {
						LOG.info(" molecule not sane "+_smiles);
					}
					else {
						mol = ChemistryFunctions.processSmi(mol,style);
						image = imageGenerationOcl(mol);
					}
			}catch (Exception e) {
			 LOG.info("error generating image from smiles using OpenChemLib "+e);
		}
		return image;
	}
	
}
