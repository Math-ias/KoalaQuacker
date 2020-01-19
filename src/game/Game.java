package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Timer;

import game.model.GameState;
import game.model.entities.Duck;
import game.model.entities.Koala;
import game.view.View;

/**
 * The Game class is a controller for the entire game. It is also how we are going to run stuff.
 */
public class Game {
  private static int FRAMES_PER_SECOND = 30;
  private static int UPDATE_TICK_MILLISECONDS = 70;

  private GameState gamestate;
  private View view;

  /**
   * Create a new Game controller with all the bells and whistles.
   */
  public Game() {
    this.gamestate = new GameState(16, 16, 5);
    this.view = new View(this.gamestate);



    // We create an anonymous class here that is an action listener for the Timer.
    // It will simply repaint stuff.
    ActionListener drawingListener = new ActionListener() {
      private long time = System.currentTimeMillis();
      private long updateCounter = 0;
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        long updatedTime = System.currentTimeMillis();
        long dt = updatedTime - this.time;
        this.updateCounter += dt;
        if (this.updateCounter >= UPDATE_TICK_MILLISECONDS) {
          this.updateCounter = 0;
          for (Duck duck: gamestate.ducks) {
            duck.processStep();
          }
          for (Koala koala: gamestate.koalas) {
            koala.processStep();
          }
        }
        view.step(dt);
        this.time = updatedTime;
      }
    };



    Timer scheduler = new Timer(1000 / FRAMES_PER_SECOND, drawingListener);
    scheduler.start();
  }
}
