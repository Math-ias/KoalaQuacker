package game.model;

import java.util.Comparator;

/**
 * An Edge comparator is a comparator for edges that compares weight.
 */
public class EdgeComparator implements Comparator<Edge> {
  @Override
  public int compare(Edge t1, Edge t2) {
    return Integer.compare(t1.weight, t2.weight);
  }
}
