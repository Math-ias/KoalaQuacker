package game.model.tiles;

import java.awt.*;

import game.CardinalDirection;
import game.Launch;
import game.model.entities.Duck;
import game.model.entities.Entity;
import game.model.entities.EntityVisitor;
import game.model.entities.Koala;

/**
 * A grass tile is a land tile.
 */
public class GrassTile implements Tile {
  /**
   * GrassTileVisitor is a visitor to visit different types of entities and return the cost for that
   * type of entity.
   */
  private static EntityVisitor<Integer> grassTileVisitor = new EntityVisitor<Integer>() {
    @Override
    public Integer visitDuck(Duck d) {
      return 100;
    }

    @Override
    public Integer visitKoala(Koala k) {
      return 20;
    }
  };

  @Override
  public int getCost(Entity travelingEntity, CardinalDirection direction) {
    return travelingEntity.accept(grassTileVisitor);
  }

  @Override
  public void draw(Graphics2D g) {
    Rectangle bounds = g.getClipBounds();
    g.drawImage(Launch.grass,
            (int) bounds.getX(), (int) bounds.getY(),
            (int) bounds.getWidth(), (int) bounds.getHeight(),
            null);
  }
}
