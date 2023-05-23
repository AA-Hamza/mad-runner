package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class RoadBlock extends Obstacle {
  static public final double RoadBlockLength = 16d;
  public RoadBlock(Block.Lane lane) {
    super(lane, 0, RoadBlockLength);
    setColor(Color.CRIMSON);
    this.image =
        new Image("file:src/main/java/assets/images/obstacles/roadblock.png",
                  Obstacle.obestacleWidth, RoadBlockLength, false, false);
  }
}
