package game.model.tiles;

import java.awt.*;

import game.CardinalDirection;
import game.model.entities.Entity;

/**
 * A Tile is a one piece unit of the map.
 */
public interface Tile {
  /**
   * Gets the cost associated with moving outwards with the given direction.
   * @param travelingEntity The entity traveling the given direction, needed for visitor pattern.
   * @param direction       The direction outwards from this tile that we are querying.
   * @return                The cost estimated for moving from
   */
  int getCost(Entity travelingEntity, CardinalDirection direction);

  /**
   * Asks the given Tile to draw itself across the given Graphics object.
   * @param g   The graphics object to allow drawing.
   */
  void draw(Graphics2D g);
}
