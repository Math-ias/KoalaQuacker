package game.model.entities;

import java.awt.*;

import game.CardinalDirection;

/**
 * An entity is a moving AI character that will pathfind.
 */
public interface Entity {
  /**
   * Process the algorithm with one more operation and return a decision to move.
   * @return  The direction decided on to move. Null means stay in place (thinking).
   */
  CardinalDirection processStep();

  /**
   * Given an EntityVisitor, the Entity will call the correct code. This allows us to write code
   * about entities without it having to be in an entity.
   * @param <T> the return type of the entity visitor.
   * @param visitor the visitor class that the entity will process.
   */
  <T> T accept(EntityVisitor<T> visitor);

  /**
   * Given a Graphics2D object, the Entity will draw itself onto the given image at the right
   * coordinate.
   * @param g The graphics object to draw across.
   */
  void draw(Graphics2D g);
}
