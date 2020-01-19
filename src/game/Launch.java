package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Launch is a small static class focused on launching the game on desktop.
 */
public class Launch {
  // It would be cleaner to have a dedicated asset loader for something like this.
  // But we know we won't have too many images or complex code in the beginning.
  // We did commit to a MVC design, but the good practice ends here.
  public static BufferedImage grass;
  public static BufferedImage water;
  public static BufferedImage arrowN;
  public static BufferedImage arrowE;
  public static BufferedImage arrowW;
  public static BufferedImage arrowS;
  public static BufferedImage duck;
  public static BufferedImage koala;
  public static BufferedImage bread;

  public static void main(String args[]) {
    // We will need to load a few assets to make the view work.
    try {
      // I'm not going to pretend I know what is happening here. What is the Class class?
      URL grassURL = Launch.class.getResource("/images/GrassTile.png");
      grass = ImageIO.read(grassURL);
      URL waterURL = Launch.class.getResource("/images/WaterTile.png");
      water = ImageIO.read(waterURL);

      URL arrowEURL = Launch.class.getResource("/images/ae.png");
      arrowE = ImageIO.read(arrowEURL);
      URL arrowNURL = Launch.class.getResource("/images/an.png");
      arrowN = ImageIO.read(arrowNURL);
      URL arrowSURL = Launch.class.getResource("/images/as.png");
      arrowS = ImageIO.read(arrowSURL);
      URL arrowWURL = Launch.class.getResource("/images/aw.png");
      arrowW = ImageIO.read(arrowWURL);

      URL duckURL = Launch.class.getResource("/images/DerricktheDuck.png");
      duck = ImageIO.read(duckURL);
      URL koalaURL = Launch.class.getResource("/images/KareemtheKoala.png");
      koala = ImageIO.read(koalaURL);
      URL breadURL = Launch.class.getResource("/images/BentheBread.png");
      bread = ImageIO.read(breadURL);
    } catch (IOException ie) {
      System.err.println("Failed to load one of the assets needed for the view!");
      ie.printStackTrace();
    }

    Game game = new Game();
  }
}
