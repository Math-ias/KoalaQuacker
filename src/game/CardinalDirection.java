package game;

/**
 * A direction is a way to move on a 2D plane.
 *
 * Null represents no movement. No direction.
 */
public enum CardinalDirection {
  NORTH, SOUTH, EAST, WEST;

  /**
   * A method to return the opposite of the given direction.
   * @param direction The direction to return the opposite of.
   * @return The opposite direction, null if null input.
   */
  public static CardinalDirection opposite(CardinalDirection direction) {
    switch(direction) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case WEST:
        return EAST;
      case EAST:
        return WEST;
      default:
        return null;
    }
  }
}
