import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameLoop extends AnimationTimer {
  private GraphicsContext gc;
  private Canvas canvas;
  private Logic gameLogic;

  public GameLoop(Canvas canvas) {
    this.canvas = canvas;
    gc = canvas.getGraphicsContext2D();
    gameLogic = new Logic(gc);
  }

  @Override
  public void handle(long now) {
    gameLogic.drawObjects();
  }
}
