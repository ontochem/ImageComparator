package com.ontochem.imageHandler;

import java.awt.Color;
import java.awt.image.BufferedImage;
//package com.java2s;
import javax.imageio.ImageIO;
import java.io.File;
	import java.awt.Graphics2D;
	import java.awt.Image;
	import java.awt.Toolkit;
	import java.awt.image.FilteredImageSource;
	import java.awt.image.ImageFilter;
	import java.awt.image.ImageProducer;
	import java.awt.image.RGBImageFilter;
	/**
	* <h3>Changelog</h3>
	* <ul>
	*   <li>2023-06-14
	*     <ul>
	*       <li>remove any background and add a transparent background to access the true width and height of an image</li>
	*     </ul>
	*   </li>
	* </ul>
	* 
	* @author shadrack.j.barnabas@ontochem.com
	*/
	
	public class ImageTransparency
	{
	   /**
	    * Main function for converting image at provided path/file name to have
	    * transparent background.
	    *
	    * @param arguments Command-line arguments: only one argument is required
	    *    with the first (required) argument being the path/name of the source
	    *    image and the second (optional) argument being the path/name of the
	    *    destination file.
	    */
	   public static void generate(String inputFileName, String outputFileName) throws Exception
	   {
	      final File in = new File(inputFileName);
	      final BufferedImage source = ImageIO.read(in);

	      final int color = source.getRGB(0, 0);

	      final Image imageWithTransparency = makeColorTransparent(source, new Color(color));

	      final BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);

	      final File out = new File(outputFileName);
	      ImageIO.write(transparentImage, "PNG", out);
	   }
	   public static BufferedImage  generate(BufferedImage source) throws Exception
	   {
	      final int color = source.getRGB(0, 0);

	      final Image imageWithTransparency = makeColorTransparent(source, new Color(color));

	      final BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);
	      return transparentImage;
	   }
	   /**
	    * Convert Image to BufferedImage.
	    *
	    * @param image Image to be converted to BufferedImage.
	    * @return BufferedImage corresponding to provided Image.
	    */
	   private static BufferedImage imageToBufferedImage(final Image image)
	   {
	      final BufferedImage bufferedImage =
	         new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	      final Graphics2D g2 = bufferedImage.createGraphics();
	      g2.drawImage(image, 0, 0, null);
	      g2.dispose();
	      return bufferedImage;
	    }

	   /**
	    * Make provided image transparent wherever color matches the provided color.
	    *
	    * @param im BufferedImage whose color will be made transparent.
	    * @param color Color in provided image which will be made transparent.
	    * @return Image with transparency applied.
	    */
	   public static Image makeColorTransparent(final BufferedImage im, final Color color)
	   {
	      final ImageFilter filter = new RGBImageFilter()
	      {
	         // the color we are looking for (white)... Alpha bits are set to opaque
	         public int markerRGB = color.getRGB() | 0xFFFFFFFF;

	         public final int filterRGB(final int x, final int y, final int rgb)
	         {
	            if ((rgb | 0xFF000000) == markerRGB)
	            {
	               // Mark the alpha bits as zero - transparent
	               return 0x00FFFFFF & rgb;
	            }
	            else
	            {
	               // nothing to do
	               return rgb;
	            }
	         }
	      };

	      final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	   }	
}