package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TrailerWithRamp extends Obstacle {
  static public final double rampSize = Obstacle.obestacleWidth;
  protected final int trailerCount;
  private Trailer trailer;
  private Obstacle ramp;
  public TrailerWithRamp(Block.Lane lane, double length) {
    super(lane, 0, length);
    int count = (int)(length / Trailer.singleTrailerLength);
    this.trailerCount = count;
    this.trailer = new Trailer(lane, count);
    this.ramp = new Obstacle(lane, this.y + this.trailer.getLength(), rampSize);
    this.ramp.setColor(Color.BROWN);
  }

  public TrailerWithRamp(Block.Lane lane) {
    this(lane, ((int)(Math.random() * Trailer.maxNumberOfTrailers) + 1) *
                       Trailer.singleTrailerLength +
                   rampSize);
  }

  /**
   * Update both of TrailerWithRamp objects (Trailer, Obstacle)
   * @param lane new lane
   */
  @Override
  public void setLane(Block.Lane lane) {
    super.setLane(lane);
    this.trailer.setLane(lane);
    this.ramp.setLane(lane);
    this.ramp.image = new Image(getClass().getClassLoader().getResourceAsStream(
                                    "images/obstacles/ramp.png"),
                                ramp.getWidth(), ramp.getLength(), true, false);
  }

  /**
   * Draw both objects seperately
   */
  @Override
  public void Draw() {
    this.trailer.setY(this.getY());
    this.ramp.setY(this.getY() + this.trailer.getLength());
    trailer.Draw();
    ramp.Draw();
  }
}
