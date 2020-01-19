package game.model;

import java.awt.*;
import java.util.Objects;

/**
 * A Utility class useful for pathfinding algorithms. An edge between two points with a weight.
 */
public class Edge {
  public Point from;
  public Point to;
  public int weight;

  /**
   * Create a new Edge with a to and a from.
   * @param to    The cell position we are moving to.
   * @param from  The cell position we are coming from.
   * @param weight The cost of moving across this edge.
   */
  public Edge(Point from, Point to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // We override equals and hashCode behavior to match the the fact that it really is based on the
  // the numbers of the points.

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true; // Fast path for pointer comparison.
    }
    if (!(o instanceof Edge)) {
      return false; // Not an edge can't possibly be the same.
    }
    // So we've established it's an edge, the cast is safe.
    Edge otherEdge = (Edge) o;
    return otherEdge.from.equals(this.from) && otherEdge.to.equals(this.to);
  }

  @Override
  public int hashCode() {
    // This will hash the two fields together in a tuple like object. This is so it matches equals
    // behavior.
    return Objects.hash(from, to);
  }
}
