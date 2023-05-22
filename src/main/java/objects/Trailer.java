package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Logic;

public class Trailer extends Obstacle {
  static public final double singleTrailerLength = 100d;
  static public final int maxNumberOfTrailers = 3;
  protected final int count;
  public Trailer(Block.Lane lane, int count) {
    super(lane, 0, singleTrailerLength * count);
    this.count = count;
    setColor(Color.CADETBLUE);
    this.image =
        new Image("file:src/main/java/assets/trailer.png",
                  Obstacle.obestacleWidth, singleTrailerLength, false, false);
  }

  public Trailer(Block.Lane lane) {
    this(lane, (int)(Math.random() * maxNumberOfTrailers + 1));
  }

  public Trailer(Block.Lane lane, double length) {
    super(lane, 0, length);
    this.count = (int)(length / singleTrailerLength);
  }

  @Override
  public void Draw() {
    for (int i = 0; i < this.length; i += singleTrailerLength) {
      Logic.gc.drawImage(this.image,
                         Logic.getBounderies().getLaneStartX(this.getLane()) +
                             Logic.getBounderies().getLanePadding(),
                         i + this.getY());
    }
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
