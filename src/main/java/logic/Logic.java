package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import objects.GameObject;
import objects.Ground;
import objects.Lane;
import objects.Lane.PATH;
import objects.Obstacle;
import objects.Player;

public class Logic {
  static public GraphicsContext gc;
  private final double canvasWidth;
  private final double canvasHeight;
  private final double playerLength = 50;
  static private final double targetFPS = 60;
  private final int zLimit = 5;
  private Ground ground;
  private ObstacleFactory obstacleFactory;
  private Deque<Lane> Obstacles;
  private Player player;

  public Logic(GraphicsContext gc) {
    this.gc = gc;
    final Canvas canvas = gc.getCanvas();
    this.canvasWidth = canvas.getWidth();
    this.canvasHeight = canvas.getHeight();
    bounderiesOBJ = new Bounderies(this.canvasWidth, this.canvasHeight);

    // this.objects = new Iterable<Iterable<GameObject>>;
    // for (int i = 0; i < zLimit; i++) {
    //   this.objects.add(null);
    // }
    this.obstacleFactory = new ObstacleFactory();
    this.Obstacles = new LinkedList<Lane>();
    Obstacles.add(obstacleFactory.generateLane());
    // this.lanes = new LinkedList<Iterable<GameObject>>();
    // ((LinkedList<Iterable<GameObject>>)lanes)
    //     .add(new Lane(obstacleFactory.generateLanes()));
    // System.out.println(obstacleFactory.generateLanes());

    // Calculating ground
    final double groundStartX = getBounderies().getGroundStartX();
    final double groundWidth = getBounderies().getGroundWidth();
    ground = new Ground(groundStartX, groundWidth, canvasHeight);
    // objects.add(new ArrayList<GameObject>(ground));

    // Set up Player
    this.player = new Player();

    // Add objects for drawing
    // objects.set(0, Arrays.asList(Arrays.asList(ground)));
    // objects.add(Arrays.asList(Arrays.asList(ground)));
    // objects.set(1, new ArrayList<>(c))
    //     objects.set(1, Arrays.asList(this.lanes.peek().getArrayList()));
    // Iterable<Iterable<GameObject>> test = new
    // ArrayList<Iterable<GameObject>>();
    // ((ArrayList<Iterable<GameObject>>)test).add(Arrays.asList(player));
    // objects.set(zLimit - 1, test);
    // objects.set(1, lanes);
    // objects.set(zLimit - 1, Arrays.asList(Arrays.asList(this.player)));

    canvas.getScene().setOnKeyReleased(new GameControls());
  }

  private long lastFrame = 0;
  private long last100Millisecond = 0;
  private long last1000Millisecond = 0;
  public void tick(Long now) {
    ground.Draw();
    for (Lane l : Obstacles) {
      l.frameUpdate();
      for (Obstacle o : l.left) {
        o.Draw();
      }
      for (Obstacle o : l.center) {
        o.Draw();
      }
      for (Obstacle o : l.right) {
        o.Draw();
      }
    }
    player.Draw();
    lastFrame = now;
    last100Millisecond = now;
    if ((now - last1000Millisecond) > 1000 * 1_000_000) {
      System.out.println("First: " + Obstacles.peekFirst().getCurrentY());
      Lane first = Obstacles.peekFirst();
      if (first.getCurrentY() > -1 * getBounderies().getScreenHeight() *
                                    ObstacleFactory.bufferScreens) {
        Obstacles.addFirst(obstacleFactory.generateLane());
        System.out.println("Generated new lane: " +
                           Obstacles.peekFirst().getCurrentY());
        System.out.println("Deleting out of screen: " +
                           Obstacles.peekLast().getCurrentY());
      }
      Lane last = Obstacles.peekLast();
      if (last.getCurrentY() > getBounderies().getScreenHeight()) {
        System.out.println("Deleting out of screen: " + last.getCurrentY());
        Obstacles.removeLast();
      }
      // System.gc();
      last1000Millisecond = now;
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
