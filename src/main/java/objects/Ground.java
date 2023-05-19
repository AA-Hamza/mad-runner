package objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ground extends GameObject {
  public Ground(double x, double width, double length) {
    super(x, 0, width, length);
    super.setColor(Color.GREY);
  }
}
