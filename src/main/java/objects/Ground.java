package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Logic;
import objects.Block.Lane;

public class Ground extends GameObject {
  public static final double laneImageWidth = Obstacle.obestacleWidth * 1.1;
  // public static final double tileImageWidth = 64;
  private final Image tile;
  private double yyy = 0;
  public Ground(double x, double width, double length) {
    super(x, 0, width, length);
    super.setColor(Color.GREY);
    this.image = new Image("file:src/main/java/assets/railway.png",
                           Logic.getBounderies().getLaneWidth(),
                           Logic.getBounderies().getLaneWidth(), true, false);
    this.tile = new Image("file:src/main/java/assets/tile.png",
                          Logic.getBounderies().getLaneWidth(),
                          Logic.getBounderies().getLaneWidth(), true, false);
    // this.image = new Image("file:src/main/java/assets/railway.png");
  }

  @Override
  public void Draw() {
    if (this.image == null) {
      super.Draw();
    }

    for (int i = 0; i <= Logic.getBounderies().getScreenWidth();
         i += laneImageWidth) {
      for (int j = (int)(-1 * laneImageWidth);
           j < Logic.getBounderies().getScreenHeight(); j += laneImageWidth) {
        Logic.gc.drawImage(this.tile, i, j + yyy, laneImageWidth,
                           laneImageWidth);
      }
    }
    for (int i = 0; i < 3; i++) {
      for (int j = (int)(-1 * laneImageWidth);
           j < Logic.getBounderies().getScreenHeight(); j += laneImageWidth) {

        double startX =
            Logic.getBounderies().getLaneStartX(Lane.values()[i]) +
            (Logic.getBounderies().getLaneWidth() - laneImageWidth) / 2.0d;
        Logic.gc.drawImage(this.image, startX, j + yyy, laneImageWidth,
                           laneImageWidth);
      }
    }
    yyy += Block.speed;
    yyy %= laneImageWidth;
  }
}
