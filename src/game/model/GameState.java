package game.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.CardinalDirection;
import game.model.entities.*;
import game.model.tiles.*;
import noise.OpenSimplexNoise;

/**
 * A GameState is a class that represents the data model of the game. It stores all our objects.
 */
public class GameState {
  // Fields
  public List<List<Tile>> gameTiles;
  public List<Duck> ducks;
  public List<Koala> koalas;

  // The scale to use when calculating what to compare to for water versus grass.
  private static final double THRESHOLD = 0;
  private static final double COORDINATE_SCALE = 1.5 / 5.0;

  /**
   * Create a new GameState with the given random seed.
   * @param height The height of the map to generate.
   * @param width The width of the map to generate.
   * @param seed  The seed to use when generating the map.
   */
  public GameState(int height, int width, int seed) {
    Random random = new Random(seed);
    this.gameTiles = new ArrayList<>();
    // Now let's generate the game map.
    OpenSimplexNoise generator = new OpenSimplexNoise(seed);
    for (int r = 0; r < height; r++) {
      ArrayList<Tile> row = new ArrayList<>();
      gameTiles.add(row);
      for (int c = 0; c < height; c++) {
        double noise = generator.eval(((double) r) * COORDINATE_SCALE,
                ((double) c) * COORDINATE_SCALE);
        if (noise > THRESHOLD) {
          row.add(new GrassTile());
        } else {
          // noise < THRESHOLD, so noise / THRESHOLD is distance from zero to the land mass.
          // closer to the land mass means less current (maybe more in real life).

          // We will flow into the direction with the biggest drop from our current y level.
          double diffAbove = noise - generator.eval(((double) r) * COORDINATE_SCALE,
                  ((double) c) * COORDINATE_SCALE - 1);
          double diffBelow = generator.eval(((double) r) * COORDINATE_SCALE,
                  ((double) c) * COORDINATE_SCALE + 1);
          double diffLeft = generator.eval(((double) r) * COORDINATE_SCALE - 1,
                  ((double) c) * COORDINATE_SCALE);
          double diffRight = generator.eval(((double) r) * COORDINATE_SCALE + 1,
                  ((double) c) * COORDINATE_SCALE);

          CardinalDirection direction;
          if (diffAbove > diffBelow && diffAbove > diffLeft && diffAbove > diffRight) {
            direction = CardinalDirection.NORTH;
          } else if (diffBelow > diffAbove && diffBelow > diffLeft && diffBelow > diffRight) {
            direction = CardinalDirection.SOUTH;
          } else if (diffLeft > diffAbove && diffLeft > diffBelow && diffLeft > diffRight) {
            direction = CardinalDirection.WEST;
          } else {
            direction = CardinalDirection.EAST;
          }

          row.add(new WaterTile(direction, (noise + 1) / (THRESHOLD + 1)));
        }
      }
    }
    // Time to put down a duck.
    int duckX = random.nextInt(width);
    int duckY = random.nextInt(height);
    int randomX = random.nextInt(width);
    int randomY = random.nextInt(height);
    Duck sampleDuck = new Duck(duckX, duckY, randomX, randomY, width, height, this);
    this.ducks = new ArrayList<Duck>();
    this.ducks.add(sampleDuck);
    this.koalas = new ArrayList<Koala>();
  }

  /**
   * A method to fetch all the game tiles on the board.
   * @return All of the game tiles on the board.
   */
  public List<List<Tile>> getGameTiles() {
    return this.gameTiles;
  }

  /**
   * A method to return the width of the game board.
   * @return The width of the game board in tiles.
   */
  public int getGameWidth() {
    // The size of the first row will be the game board.
    return this.gameTiles.get(0).size();
  }

  /**
   * A method to return the height of the game board.
   * @return The height of the game board in tiles.
   */
  public int getGameHeight() {
    // The number of rows is the height.
    return this.gameTiles.size();
  }

  /**
   * A method to request the cost for some edge in the game board.
   * @param entity The entity moving between the given points.
   * @param from   The point of where the entity would be.
   * @param direction The direction the entity wants to move from that point.
   * @return the cost for the given entity to do the given move.
   */
  public int requestCost(Entity entity, Point from, CardinalDirection direction) {
    return gameTiles.get(from.y).get(from.x).getCost(entity, direction);
  }
}
