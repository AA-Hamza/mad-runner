import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import logic.Logic;

public class GameLoop extends AnimationTimer {
  private GraphicsContext gc;
  private Logic gameLogic;
  static public final long FPS = 40;

  private long last = 0;

  public GameLoop(Canvas canvas) {
    gc = canvas.getGraphicsContext2D();
    gameLogic = new Logic(gc);
  }

  @Override
  public void handle(long now) {
    if ((now - last) > ((1_000 * 1_000_000 / FPS))) {
      gameLogic.tick(now);
      last = now;
    }
  }
}
