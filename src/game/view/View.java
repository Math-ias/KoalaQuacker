package game.view;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Map;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import game.model.GameState;
import game.model.entities.Duck;
import game.model.tiles.Tile;


/**
 * A view is a class dedicated to viewing the game state.
 */
public class View extends JFrame {
  private static final String WINDOW_TITLE = "KoalaQuacker";
  private static final int WINDOW_WIDTH = 800;
  private static final int WINDOW_HEIGHT = 800;
  private static Color BG_COLOR = Color.BLACK;

  private SpecialCanvas canvas;
  private BufferedImage buffer;
  private GameState gamestate;

  /**
   * A SpecialCanvas is a component that we have given special graphics behavior to.
   */
  static class SpecialCanvas extends Canvas {
    /**
     * Create a SpecialCanvas object that is a slave to View.
     */
    public SpecialCanvas() {
      this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /**
     * Initializes the given Canvas. This needs to be done after the component is added to the
     * JFrame so that it has a valid "OS-peer". We need a little more coupling to the OS graphical
     * system with what we are doing here -- creating a buffer strategy.
     */
    public void initialize() {
      if (this.getBufferStrategy() == null)
        createBufferStrategy(2);
    }

    /**
     * A method to render the given Canvas. This can be done without overriding paint methods
     * apparently.
     * @param buffer A buffered image to draw from. SpecialCanvas is basically a slave that will
     *               accept any buffered image from view. This relinquishes control to the larger
     *               class.
     */
    public void render(BufferedImage buffer) {
      BufferStrategy bs = this.getBufferStrategy();
      // When we initialized we made sure this wasn't null.
      Graphics g = bs.getDrawGraphics();
      g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
      // We draw from a BufferedImage for the benefit of performance and avoiding screen blips.
      // Copying from one memory blob to another I've heard is pretty cheap.
      g.dispose();
      bs.show();
    }
  }

  /**
   * A helper method to mutate a Graphics object with a bunch of rendering settings.
   * @param g The Graphics2D object before doing any drawing.
   */
  static void graphicsSettings(Graphics2D g) {
    // Shamelessly ripped from https://stackoverflow.com/a/31537742
    Map<?, ?> desktopHints =
            (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
    if (desktopHints != null) {
      g.setRenderingHints(desktopHints);
    }
  }

  /**
   * A method to step the view forward one total frame. This will call the internal method to update
   * animations in the foreground since we can assume that type of thing is happening.
   * @param dt  The delta time since the last update in milliseconds.
   */
  public void step(long dt) {
    // We create a new image that is the composition of our foreground and background.
    BufferedImage image = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    // First we throw down the background.
    View.graphicsSettings(g);
    List<List<Tile>> tiles = this.gamestate.getGameTiles();
    int gameHeight = this.gamestate.getGameHeight();
    int gameWidth = this.gamestate.getGameWidth();
    int tileHeight = WINDOW_HEIGHT / gameHeight;
    int tileWidth = WINDOW_WIDTH / gameWidth;
    for (int r = 0; r < gameHeight; r++) {
      for (int c = 0; c < gameWidth; c++) {
        g.setClip(c * tileHeight, r * tileHeight,
                tileWidth, tileHeight);
        tiles.get(r).get(c).draw(g);
      }
    }
    // Now we add the foreground elements.
    for (Duck duck: this.gamestate.ducks) {
      g.setClip(duck.x * tileWidth, duck.y * tileHeight, tileWidth, tileHeight);
      duck.draw(g);
    }
    g.setClip(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Then we have to dispose of the graphics object since we are done with it. I think this GC's
    // it early or something.
    g.dispose();
    // Kind of has to be the final line in our step method. We take the moment to render our work.
    this.canvas.render(image);
  }

  /**
   * Create a new view capable of viewing pieces of the game model.
   * @param gamestate Pass off the model to the view to reflect it.
   */
  public View(GameState gamestate) {
    // Let's first handle some window settings.
    super("KoalaQuacker");
    this.setVisible(true);
    this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Let's now initiate our special canvas.
    this.canvas = new SpecialCanvas();
    this.add(this.canvas);
    this.canvas.initialize();
    this.pack();
    this.buffer = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
    this.gamestate = gamestate;
  }
}
