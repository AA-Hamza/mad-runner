package objects;

import javafx.scene.paint.Color;

public class RoadBlock extends Obstacle {
  static public final double RoadBlockLength = 30d;
  public RoadBlock(Block.Lane lane) {
    super(lane, 0, RoadBlockLength);
    setColor(Color.CRIMSON);
  }
}
