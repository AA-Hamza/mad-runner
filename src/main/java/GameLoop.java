import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import logic.Logic;

public class GameLoop extends AnimationTimer {
  private GraphicsContext gc;
  private Logic gameLogic;

  public GameLoop(Canvas canvas) {
    gc = canvas.getGraphicsContext2D();
    gameLogic = new Logic(gc);
  }

  @Override
  public void handle(long now) {
    gameLogic.tick(now);
  }
}
