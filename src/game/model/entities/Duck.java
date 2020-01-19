package game.model.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.List;

import game.CardinalDirection;
import game.Launch;
import game.model.Edge;
import game.model.EdgeComparator;
import game.model.GameState;
import javafx.util.Pair;

/**
 * A duck is an entity that is good on water. It also implements Dikstra's Algorithm.
 */
public class Duck implements Entity {
  public int x, y; // The duck's position.
  int xd, yd; // The duck's destination
  DuckState state; // The duck's current state.

  PriorityQueue<Edge> processingQueue;
  Map<Point, Pair<Integer, Point>> progress;
  Set<Edge> processed;
  List<Point> path; //

  int width, height;
  GameState model;

  /**
   * Create a new Duck entity at the given position.
   * @param x The x position (in tiles) of the duck on the board.
   * @param y The y position (in tiles) of the duck on the board.
   * @param xd The x position (in tiles) of the destination of the duck.
   * @param yd The y position of the destination.
   * @param width The width of the game board the duck can wander.
   * @param height The height of the game board the duck can wander.
   * @param model The model to query about edge weights.
   */
  public Duck(int x, int y, int xd, int yd, int width, int height, GameState model) {
    this.x = x;
    this.y = y;
    this.xd = xd;
    this.yd = yd;
    this.width = width;
    this.height = height;
    this.model = model;
    this.state = DuckState.INIT;

    this.processingQueue = new PriorityQueue<>(new EdgeComparator());
    this.progress = new HashMap<>();
    this.processed = new HashSet<>();
    this.path = new LinkedList<Point>();
  }

  enum DuckState {
    INIT, // Need to add the first edge.
    THINKING, // Doing operations to process the queue.
    TRANSITIONING, // We are done processing the queue and are moving by following our map.
    DONE // We've reached the destination.
  }

  /**
   * Add edge is a helper method to attempt to add an edge to the processingQueue. First we check
   * against the processed set first.
   */
  void addEdge(Edge e) {
    if (!this.processed.contains(e)) {
      this.processed.add(e);
      this.processingQueue.add(e);
    }
  }

  /**
   * Process node is a helper method to add all of the (unexplored) edges to the graph by checking
   * directions.
   * @param pos The node position to consider.
   */
  void processNode(Point pos) {
    if (pos.x > 0) {
      // It's okay to check left.
      Point to = new Point(pos.x - 1, pos.y);
      addEdge(new Edge(pos, to,
              model.requestCost(this, pos, CardinalDirection.WEST)));
    }
    if (pos.x < this.width - 1) {
      // It's okay to check left.
      Point to = new Point(pos.x + 1, pos.y);
      addEdge(new Edge(pos, to,
              model.requestCost(this, pos, CardinalDirection.EAST)));
    }
    if (pos.y > 0) {
      // It's okay to check left.
      Point to = new Point(pos.x, pos.y - 1);
      addEdge(new Edge(pos, to,
              model.requestCost(this, pos, CardinalDirection.NORTH)));
    }
    if (pos.y < this.height - 1) {
      // It's okay to check left.
      Point to = new Point(pos.x, pos.y + 1);
      addEdge(new Edge(pos, to,
              model.requestCost(this, pos, CardinalDirection.SOUTH)));
    }
  }

  @Override
  public CardinalDirection processStep() {
    // System.out.println("I am Derrick I am doing " + this.state);
    switch (this.state) {
      case INIT:
        Point currentPosition = new Point(this.x, this.y);
        this.progress.put(currentPosition, new Pair<>(0, null));
        processNode(currentPosition);
        this.state = DuckState.THINKING;
        return null;
      case THINKING:
        if (this.processingQueue.isEmpty()) {
          this.initializePath(new Point(this.xd,this.yd));
          this.state = DuckState.TRANSITIONING;
          return null;
        }
        // The processing queue isn't empty.
        // This will be null if the queue is empty. It shouldn't be.
        Edge processedEdge = this.processingQueue.poll();
        // We actually know processedEdge.from is in the map because otherwise the edge couldn't
        // possibly be in the queue.
        // System.out.println(this.progress.get(processedEdge.from));
        int fromDistance = this.progress.get(processedEdge.from).getKey(); // The key is the integer.
        int potentialNewToDistance = fromDistance + processedEdge.weight;
        Pair<Integer, Point> toPair = this.progress.get(processedEdge.to); // The key is the int.
        if (toPair == null || potentialNewToDistance < toPair.getKey()) {
          // Then we need to assign a new cost and redirect in the map.
          this.progress.put(processedEdge.to, new Pair<>(potentialNewToDistance, processedEdge.from));
        }
        processNode(processedEdge.to);
        return null;
      case TRANSITIONING:
        if (this.path.isEmpty()) {
          this.state = DuckState.DONE;
          return null;
        }

        Point pathPoint = this.path.remove(0);

        int pathX = pathPoint.x;
        int pathY = pathPoint.y;

        int curX = this.x;
        int curY = this.y;

        this.x = pathX;
        this.y = pathY;

        if (curX== pathX && curY == pathY) {
          return null;
        }

        else if (curX < pathX) {
          return CardinalDirection.EAST;
        }
        else if (curX > pathX) {
          return CardinalDirection.WEST;
        }
        else if (curY < pathY) {
          return CardinalDirection.SOUTH;
        }
        else if (curY> pathY) {
          return CardinalDirection.NORTH;
        }
        else {
          return null;
        }
      default:
        return null;
    }
  }

  public void initializePath(Point endPoint) {
    Point step = endPoint;
    this.path.add(endPoint);
    while (true) {
      Pair<Integer, Point> halfStep = this.progress.get(step);
      if (halfStep == null) {
        break;
      } else {
        step = halfStep.getValue();
        if (step == null) {
          break;
        } else {
          this.path.add(step);
        }
      }
    }
    Collections.reverse(this.path);
  }

  @Override
  public <T> T accept(EntityVisitor<T> visitor) {
    return visitor.visitDuck(this);
  }

  @Override
  public void draw(Graphics2D g) {
    Rectangle bounds = g.getClipBounds();
    switch (this.state) {
      case THINKING:
        g.setColor(Color.BLACK);
        Edge processingEdge = this.processingQueue.peek();
        String processingString;
        if (processingEdge != null) {
          processingString = processingEdge.to.getX() + ", " + processingEdge.to.getY() + " : " + processingEdge.to.getX() + ", " + processingEdge.to.getY();
        } else {
          processingString = ":D";
        }
        g.drawString(processingString, (int) bounds.getX() + 10, (int) bounds.getY() + 10);
        break;
      case TRANSITIONING:
        g.setColor(Color.BLACK);
        g.drawString("!", (int) bounds.getX() + 10, (int) bounds.getY() + 10);
        break;
    }
    g.drawImage(Launch.duck,
            (int) bounds.getX(), (int) bounds.getY(),
            (int) bounds.getWidth(), (int) bounds.getHeight(),
            null);
  }
}
