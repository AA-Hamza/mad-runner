package objects;

import javafx.scene.paint.Color;

public class TrailerWithRamp extends Obstacle {
  static public final double rampSize = Trailer.singleTrailerLength / 2.0d;
  // static public final int maxNumberOfTrailers = 3;
  protected final int count;
  private Trailer trailer;
  private Obstacle ramp;
  public TrailerWithRamp(Lane.PATH lane, double length) {
    super(lane, 0, length);
    int count = (int)(length / Trailer.singleTrailerLength);
    this.count = count;
    this.trailer = new Trailer(lane, count);
    this.ramp = new Obstacle(lane, this.y + this.trailer.getLength(), rampSize);
    this.ramp.setColor(Color.BROWN);
  }

  public TrailerWithRamp(Lane.PATH lane) {
    this(lane, ((int)(Math.random() * Trailer.maxNumberOfTrailers) + 1) *
                       Trailer.singleTrailerLength +
                   rampSize);
  }

  @Override
  public void setLanePath(Lane.PATH path) {
    super.setLanePath(path);
    this.trailer.setLanePath(path);
    this.ramp.setLanePath(path);
  }

  @Override
  public void Draw() {
    this.trailer.setY(this.getY());
    this.ramp.setY(this.getY() + this.trailer.getLength());
    trailer.Draw();
    ramp.Draw();
  }
}
