package game.model.entities;

import java.awt.*;

import game.CardinalDirection;

public class Koala implements Entity {

  @Override
  public CardinalDirection processStep() {
    return null;
  }

  @Override
  public <T> T accept(EntityVisitor<T> visitor) {
    return visitor.visitKoala(this);
  }

  @Override
  public void draw(Graphics2D g) {

  }
}
