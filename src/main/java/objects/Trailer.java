package objects;

import javafx.scene.paint.Color;

public class Trailer extends Obstacle {
  static private final double singleTrailerLength = 100d;
  static private final int maxNumberOfTrailers = 3;
  private final int count;
  public Trailer(Lane.PATH lane, int count) {
    super(lane, 0, singleTrailerLength * count);
    this.count = count;
    setColor(Color.DARKCYAN);
  }

  public Trailer(Lane.PATH lane, double y) {
    this(lane, (int)(Math.random() * maxNumberOfTrailers + 1));
  }

  // @Override
  // public void draw(GraphicsContext gc) {}
}
