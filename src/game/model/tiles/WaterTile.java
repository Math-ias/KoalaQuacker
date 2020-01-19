package game.model.tiles;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import game.CardinalDirection;
import game.Launch;
import game.model.entities.Duck;
import game.model.entities.Entity;
import game.model.entities.EntityVisitor;
import game.model.entities.Koala;

/**
 * A WaterTile is a tile of water.
 */
public class WaterTile implements Tile {
  // The max transparency we interpolate between 0 and in order to draw the transparency of the
  // arrow.
  private static final float CURRENT_TRANSPARENCY = 0.5f;
  private static final int MAX_COST_CHANGE = 50;

  // The direction of the given water tile.
  private CardinalDirection current;
  private double power;
  // The transform we use privately to draw the current symbol.
  private BufferedImage arrow;
  // The composite to use when we are drawing.
  private AlphaComposite composite;

  /**
   * Create a new WaterTile with a given current.
   * @param current the current of the given WaterTile. Don't give me a null value.
   * @param power   the power of the current as a double between 0 and 1.
   */
  public WaterTile(CardinalDirection current, double power) {
    this.current = current;
    this.power = power;

    switch(this.current) {
      case NORTH:
        this.arrow = Launch.arrowN;
        break;
      case EAST:
        this.arrow = Launch.arrowE;
        break;
      case SOUTH:
        this.arrow = Launch.arrowS;
        break;
      case WEST:
        this.arrow = Launch.arrowW;
        break;
    }

    this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
            CURRENT_TRANSPARENCY * (float) this.power);
  }

  /**
   * WaterTileVisitor is a visitor to visit different types of entities and return the cost for that
   * type of entity. This will later get multiplied and changed around by currents.
   */
  private static EntityVisitor<Integer> waterTileVisitor = new EntityVisitor<Integer>() {
    @Override
    public Integer visitDuck(Duck d) {
      return 75;
    }

    @Override
    public Integer visitKoala(Koala k) {
      return 150;
    }
  };

  @Override
  public int getCost(Entity travelingEntity, CardinalDirection direction) {
    int baseCost = travelingEntity.accept(waterTileVisitor);
    int costAdjustment;
    if (direction == current) {
      costAdjustment = (int) -(this.power * MAX_COST_CHANGE);
    } else if (direction == CardinalDirection.opposite(direction)) {
      costAdjustment = (int) (this.power * MAX_COST_CHANGE);
    } else {
      costAdjustment = 0;
    }
    return baseCost + costAdjustment;
  }

  @Override
  public void draw(Graphics2D g) {
    Rectangle bounds = g.getClipBounds();
    g.drawImage(Launch.water,
            (int) bounds.getX(), (int) bounds.getY(),
            (int) bounds.getWidth(), (int) bounds.getHeight(),
            null);
    Composite oldComposite = g.getComposite();
    g.setComposite(this.composite);
    g.drawImage(this.arrow,
            (int) bounds.getX(), (int) bounds.getY(),
            (int) bounds.getWidth(), (int) bounds.getHeight(),
            null);
    g.setComposite(oldComposite);
  }
}
