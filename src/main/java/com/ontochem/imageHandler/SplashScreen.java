package com.ontochem.imageHandler;
import java.awt.*;
import javax.swing.*;

/**
 * This class represents a splash screen that is displayed briefly when the application starts.
 *
 * <h3>Changelog</h3>
 * <ul>
 *   <li>2023-06-14
 *     <ul>
 *       <li>Implemented a splash screen with a progress bar.</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * @author shadrack.j.barnabas@ontochem.com
 */
public class SplashScreen extends JWindow {
   private Image splashScreen;  // The image to be displayed as the splash screen
   private ImageIcon imageIcon; // ImageIcon for the splash screen image
   JProgressBar progressBar; // Progress bar to indicate loading progress

   /**
    * Constructor for the SplashScreen class.
    */
   public SplashScreen() {
      // Load the splash screen image from the specified file
      splashScreen = Toolkit.getDefaultToolkit().getImage("src/main/resources/ontochem-logo.png");

      // Create an ImageIcon from the loaded image
      imageIcon = new ImageIcon(splashScreen);

      // Create a progress bar
      progressBar = new JProgressBar();

      // Create the content pane for the splash screen
      JPanel contentPane = new JPanel(new BorderLayout());

      // Create a panel for centering components
      JPanel centerPanel = new JPanel(new GridBagLayout());
      centerPanel.add(new JLabel(imageIcon)); // Add the image to the center panel
      contentPane.add(centerPanel, BorderLayout.CENTER);

      // Add the progress bar to the content pane at the bottom
      contentPane.add(progressBar, BorderLayout.SOUTH);

      // Set the content pane for this JWindow
      setContentPane(contentPane);

      // Pack the JWindow to fit its contents
      pack();

      // Set the size of the JWindow based on the image dimensions
      int iconWidth = imageIcon.getIconWidth();
      int iconHeight = imageIcon.getIconHeight() + 10; // Add some height for the progress bar
      setSize(iconWidth, iconHeight);

      // Center the JWindow on the screen
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

      // Make the JWindow visible
      setVisible(true);
   }

   /**
    * Paint method to draw the image on the JWindow.
    * 
    * @param g The Graphics object to paint on.
    */
   public void paint(Graphics g) {
      super.paint(g);
      // Fill the background with white color
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, getWidth(), getHeight());
      // Draw the splash screen image
      g.drawImage(splashScreen, 0, 0, this);
   }

   /**
    * The main method to create and display the splash screen.
    * 
    * @param args Command-line arguments (not used).
    */
   public static void main(String[] args) {
      // Create an instance of the SplashScreen
      SplashScreen splash = new SplashScreen();
      try {
         // Make the JWindow appear for 3 seconds before disappearing
         for (int i = 0; i <= 100; i++) {
            splash.progressBar.setValue(i); // Update the progress bar value
            Thread.sleep(30); // Sleep for a short duration
         }
         splash.dispose(); // Close the splash screen
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
