package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TrailerWithRamp extends Obstacle {
  // static public final double rampSize = Trailer.singleTrailerLength / 2.0d;
  static public final double rampSize = Obstacle.obestacleWidth;
  // static public final int maxNumberOfTrailers = 3;
  protected final int count;
  private Trailer trailer;
  private Obstacle ramp;
  public TrailerWithRamp(Block.Lane lane, double length) {
    super(lane, 0, length);
    int count = (int)(length / Trailer.singleTrailerLength);
    this.count = count;
    this.trailer = new Trailer(lane, count);
    this.ramp = new Obstacle(lane, this.y + this.trailer.getLength(), rampSize);
    this.ramp.setColor(Color.BROWN);
  }

  public TrailerWithRamp(Block.Lane lane) {
    this(lane, ((int)(Math.random() * Trailer.maxNumberOfTrailers) + 1) *
                       Trailer.singleTrailerLength +
                   rampSize);
  }

  @Override
  public void setLanePath(Block.Lane path) {
    super.setLanePath(path);
    this.trailer.setLanePath(path);
    this.ramp.setLanePath(path);
    this.ramp.image = new Image("file:src/main/java/assets/ramp.png",
                                ramp.getWidth(), ramp.getLength(), true, false);
  }

  @Override
  public void Draw() {
    this.trailer.setY(this.getY());
    this.ramp.setY(this.getY() + this.trailer.getLength());
    trailer.Draw();
    ramp.Draw();
  }
}
