import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import objects.GameObject;
import objects.Ground;
import objects.Player;

public class Logic {
  private GraphicsContext gc;
  private Canvas canvas;
  private final double canvasWidth;
  private final double canvasHeight;
  private final double playerLength = 50;
  private final double playerY;
  private final double groundPaddingPercentage = 0.25;
  private double playerX;
  private final int zLimit = 3;
  private Ground ground;
  private ArrayList<GameObject>[] objects;

  private Player player;

  public Logic(GraphicsContext gc) {
    this.gc = gc;
    this.canvas = gc.getCanvas();
    this.canvasWidth = canvas.getWidth();
    this.canvasHeight = canvas.getHeight();

    this.objects = new ArrayList[zLimit];
    for (int i = 0; i < 3; i++) {
      this.objects[i] = new ArrayList<GameObject>();
    }

    // Calculating ground
    final double groundStartX = canvasWidth * groundPaddingPercentage;
    final double groundWidth = canvasWidth * (1 - 2 * groundPaddingPercentage);
    ground = new Ground(groundStartX, groundWidth, canvasHeight);
    objects[0].add(ground);

    // Set up Player
    final double laneLength = groundWidth / 3.0d;
    this.playerX = groundStartX;
    this.playerY = canvasHeight * 0.8;
    this.player = new Player(playerX, playerY, playerLength, laneLength);
    objects[2].add(this.player);

    canvas.getScene().setOnKeyReleased(new GameControls());
  }

  public void drawObjects() {
    for (int i = 0; i < zLimit; i++) {
      for (int j = 0; j < objects[i].size(); j++) {
        objects[i].get(j).Draw(gc);
      }
    }
  }

  public class GameControls implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
      switch (event.getCode()) {
      case UP:
      case RIGHT:
        if (player.within(ground) == true) {
          player.moveRight();
        } else {
          player.moveRight();
        }
        break;
      case LEFT:
        if (player.within(ground) == true) {
          player.moveLeft();
        } else {
          player.moveRight();
        }
        break;
      default:
        break;
      }
    }
  }
}
