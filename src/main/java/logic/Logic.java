package logic;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import objects.GameObject;
import objects.Ground;
import objects.Lane;
import objects.Lane.PATH;
import objects.Player;

public class Logic {
  private GraphicsContext gc;
  private final double canvasWidth;
  private final double canvasHeight;
  private final double playerLength = 50;
  private final int zLimit = 5;
  private Ground ground;
  // private ArrayList<GameObject> objects;
  private ArrayList<Iterable<GameObject>>[] objects;
  private Lane[] lanes;

  private Player player;

  public Logic(GraphicsContext gc) {
    this.gc = gc;
    final Canvas canvas = gc.getCanvas();
    this.canvasWidth = canvas.getWidth();
    this.canvasHeight = canvas.getHeight();
    bounderiesOBJ = new Bounderies(this.canvasWidth, this.canvasHeight);

    this.lanes = new Lane[3];
    this.lanes[PATH.LEFT.ordinal()] = new Lane(PATH.LEFT);
    this.lanes[PATH.CENTER.ordinal()] = new Lane(PATH.CENTER);
    this.lanes[PATH.RIGHT.ordinal()] = new Lane(PATH.RIGHT);

    // this.objects = new ArrayList[zLimit];
    // for (int i = 0; i < zLimit; i++) {
    //   this.objects[i] = new ArrayList<GameObject>();
    // }
    this.objects = new ArrayList[zLimit];
    for (int i = 0; i < zLimit; i++) {
      this.objects[i] = new ArrayList<Iterable<GameObject>>();
    }

    // Calculating ground
    final double groundStartX = getBounderies().getGroundStartX();
    final double groundWidth = getBounderies().getGroundWidth();
    ground = new Ground(groundStartX, groundWidth, canvasHeight);
    // objects.add(new ArrayList<GameObject>(ground));

    // Set up Player
    this.player = new Player();

    // Add objects for drawing
    objects[0].add(Arrays.asList(ground));
    objects[1].add(this.lanes[PATH.LEFT.ordinal()]);
    objects[1].add(this.lanes[PATH.CENTER.ordinal()]);
    objects[1].add(this.lanes[PATH.RIGHT.ordinal()]);
    objects[zLimit - 1].add(Arrays.asList(this.player));

    canvas.getScene().setOnKeyReleased(new GameControls());
  }

  public void drawObjects() {
    for (int i = 0; i < zLimit; i++) {
      for (int j = 0; j < objects[i].size(); j++) {
        for (GameObject obj : objects[i].get(j)) {
          obj.Draw(gc);
        }
      }
    }
  }

  private long last100Millisecond = 0;
  private long last1000Millisecond = 0;
  public void tick(Long now) {
    drawObjects();
    for (int i = 0; i < 3; i++) {
      lanes[i].frameUpdate();
      // lanes[i].generateObstacles();
    }

    if ((now - last100Millisecond) > 100 * 1_000_000) {
      last100Millisecond = now;
      if ((now - last1000Millisecond) > 1000 * 1_000_000) {
        last1000Millisecond = now;
        for (int i = 0; i < 3; i++) {
          lanes[i].generateObstacles();
          lanes[i].cleanOffFrame();
        }
      }
    }
  }

  static public Bounderies bounderiesOBJ = null;
  static public Bounderies getBounderies() {
    if (bounderiesOBJ == null) {
      bounderiesOBJ = new Bounderies(1024, 768);
    }
    return bounderiesOBJ;
  }

  static public class Bounderies {
    private final double groundPaddingPercentage = 0.25;
    private final double groundStartX, groundWidth, groundEndX, pathWidth,
        screenWidth, screenHeight;

    private final double playerYPercentage = 0.8;

    public Bounderies(double screenWidth, double screenHeight) {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      this.groundStartX = screenWidth * groundPaddingPercentage;
      this.groundWidth = screenWidth * (1 - 2 * groundPaddingPercentage);
      this.pathWidth = this.groundWidth / 3.0d;
      this.groundEndX = this.groundStartX + this.groundWidth;
    }

    public double getGroundStartX() { return this.groundStartX; }
    public double getGroundEndX() { return this.groundEndX; }
    public double getPlayerY() {
      return this.playerYPercentage * this.screenHeight;
    }
    public double getScreenHeight() { return this.screenHeight; }
    public double getGroundWidth() { return this.groundWidth; }
    public double getPathWidth() { return this.pathWidth; }
    public double getPathStartX() { return this.groundStartX; }
    public double getPathStartX(Lane.PATH path) {
      int multiplier = 0;
      switch (path) {
      case RIGHT:
        multiplier += 1;
      case CENTER:
        multiplier += 1;
      case LEFT:
        multiplier += 0;
      default:
        break;
      }

      return this.groundStartX + multiplier * this.pathWidth;
    }
  }

  public class GameControls implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
      switch (event.getCode()) {
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
      case UP:
        break;
      case DOWN:
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
