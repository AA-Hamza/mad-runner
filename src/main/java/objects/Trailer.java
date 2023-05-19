package objects;

import javafx.scene.paint.Color;

public class Trailer extends Obstacle {
  static public final double singleTrailerLength = 100d;
  static public final int maxNumberOfTrailers = 3;
  protected final int count;
  public Trailer(Lane.PATH lane, int count) {
    super(lane, 0, singleTrailerLength * count);
    this.count = count;
    setColor(Color.DARKCYAN);
  }

  public Trailer(Lane.PATH lane) {
    this(lane, (int)(Math.random() * maxNumberOfTrailers + 1));
  }

  public Trailer(Lane.PATH lane, double length) {
    super(lane, 0, length);
    this.count = (int)(length / singleTrailerLength);
  }

  // static public class TrailerWithRamp extends Trailer {
  //   private static final double rampSize = singleTrailerLength / 2.0d;
  //   private Obstacle Ramp;
  //   public TrailerWithRamp(Lane.PATH lane, int count) {
  //     super(lane, singleTrailerLength * count + rampSize);
  //     Ramp = new Obstacle(lane, 0, rampSize);
  //     Ramp.color = Color.BROWN;
  //   }
  //
  //   public TrailerWithRamp(Lane.PATH lane) {
  //     this(lane, (int)(Math.random() * maxNumberOfTrailers + 1));
  //   }
  //
  //   @Override
  //   public void Draw() {
  //     super.Draw();
  //     // Draw the ramp
  //     Ramp.setY(this.getY() + this.getLength());
  //     Ramp.setLanePath(this.lane);
  //     Ramp.Draw();
  //   }
  // }
}
