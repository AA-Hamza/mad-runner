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
import javafx.scene.paint.Color;
import objects.Block;
import objects.Block.Lane;
import objects.GameObject;
import objects.Ground;
import objects.Obstacle;
import objects.Player;
import objects.Player.Stage;
import objects.RoadBlock;
import objects.TrailerWithRamp;

public class Logic {
  static public GraphicsContext gc;
  private final double canvasWidth;
  private final double canvasHeight;
  private Ground ground;
  private ObstacleFactory obstacleFactory;
  private Deque<Block> obstaclesBlocks;
  private Player player;
  private Score score;

  private boolean playerStopped;

  public Logic(GraphicsContext gc) {
    this.gc = gc;
    final Canvas canvas = gc.getCanvas();
    this.canvasWidth = canvas.getWidth();
    this.canvasHeight = canvas.getHeight();
    bounderiesOBJ = new Bounderies(this.canvasWidth, this.canvasHeight);

    // Obstacles
    this.obstacleFactory = new ObstacleFactory();
    this.obstaclesBlocks = new LinkedList<Block>();
    obstaclesBlocks.add(obstacleFactory.generateLane());

    // Calculating ground
    final double groundStartX = getBounderies().getGroundStartX();
    final double groundWidth = getBounderies().getGroundWidth();
    ground = new Ground(groundStartX, groundWidth, canvasHeight);

    // Set up Player
    this.player = new Player();
    this.playerStopped = false;

    // score
    this.score = new Score();

    canvas.getScene().setOnKeyReleased(new GameControls());
  }

  private long lastFrame = 0;
  private long last100Millisecond = 0;
  private long last1000Millisecond = 0;
  public void tick(Long now) {
    if (playerStopped == true) {
      return;
    }
    ground.Draw();

    for (Block l : obstaclesBlocks) {
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
    player.jumpUpdate();
    score.Draw();

    if (checkPlayerCollision()) {
      stopPlayer();
    }

    lastFrame = now;
    last100Millisecond = now;
    if ((now - last1000Millisecond) > 1000 * 1_000_000) {
      score.incrementScore();
      System.out.println("First: " + obstaclesBlocks.peekFirst().getCurrentY());
      Block first = obstaclesBlocks.peekFirst();
      if (first.getCurrentY() > -1 * getBounderies().getScreenHeight() *
                                    ObstacleFactory.bufferScreens) {
        obstaclesBlocks.addFirst(obstacleFactory.generateLane());
        System.out.println("Generated new lane: " +
                           obstaclesBlocks.peekFirst().getCurrentY());
        System.out.println("Deleting out of screen: " +
                           obstaclesBlocks.peekLast().getCurrentY());
      }
      Block last = obstaclesBlocks.peekLast();
      if (last.getCurrentY() > getBounderies().getScreenHeight()) {
        System.out.println("Deleting out of screen: " + last.getCurrentY());
        obstaclesBlocks.removeLast();
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

  private void stopPlayer() { this.playerStopped = true; }

  private void startPlayer() { this.playerStopped = false; }

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
    public double getScreenWidth() { return this.screenHeight; }
    public double getGroundWidth() { return this.groundWidth; }
    public double getPathWidth() { return this.pathWidth; }
    public double getPathStartX() { return this.groundStartX; }
    public double getPathStartX(Block.Lane path) {
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
        // if (player.within(ground) == true) {
        //   player.moveRight();
        // } else {
        //   player.moveRight();
        // }
        player.moveRight();
        if (checkIfLaneAvailable(Lane.RIGHT) == false) {
          stopPlayer();
        };
        break;
      case LEFT:
        // if (player.within(ground) == true) {
        //   player.moveLeft();
        // } else {
        //   player.moveRight();
        // }
        player.moveLeft();
        if (checkIfLaneAvailable(Lane.LEFT) == false)
          stopPlayer();
        break;
      case UP:
        player.jump();
        break;
      case DOWN:
        // player.moveDown();
        break;
      default:
        break;
      }
    }
  }

  public boolean checkIfLaneAvailable(Lane lane) {
    if (this.player.getStage() == Stage.HIGH) {
      return true;
    }
    for (Block block : obstaclesBlocks) {
      for (Obstacle obs : block.getLane(lane)) {
        if (obs.interesects(lane, player.getY())) {
          System.out.println("Lane not available");
          System.out.println("Lane.y: " + obs.getY() +
                             " player.y: " + player.getY());
          return false;
        }
        if (obs.interesects(lane, player.getY() + player.getLength())) {
          System.out.println("Lane not available");
          System.out.println("Lane.y: " + obs.getY() +
                             " player.y: " + player.getY());
          return false;
        }
      }
    }
    return true;
  }

  private GameObject lastTouch = null;
  public boolean checkPlayerCollision() {
    Block.Lane playerLane = this.player.getLane();
    for (Block block : obstaclesBlocks) {
      for (Obstacle obs : block.getLane(playerLane)) {
        if (player.touches(obs)) {
          if (obs instanceof TrailerWithRamp) {
            System.out.println("Ramp");
            if (player.getStage() == Stage.LOW) {
              player.moveUp();
              player.lastStage = Stage.HIGH;
            }
          }
          System.out.println(player.getStage());
          if (player.getStage() == Stage.LOW) {
          }
        }
        if (player.getStage() == Stage.LOW && player.touches(obs)) {
          System.out.println("Player touches something");
          if (obs instanceof TrailerWithRamp) {
            System.out.println("Ramp");
            if (player.getStage() == Stage.LOW) {
              player.lastStage = Stage.HIGH;
              player.moveUp();
            }
            System.out.println(player.getStage());
          } else if (obs instanceof RoadBlock &&
                     player.getStage() == Stage.HIGH) {
            return false;
          } else {
            return true;
          }
        } else if (player.getStage() == Stage.HIGH && !player.isJumping() &&
                   !player.touches(obs)) {
          player.moveDown();
        }
      }
    }
    return false;
  }

  public class Score extends GameObject {
    static public final double length = 100;
    static public final double padding = 50;
    private int currentScore;
    Score() {
      super(Logic.getBounderies().getGroundEndX() - padding - length, padding,
            length);
      this.currentScore = 0;
      this.color = Color.WHEAT;
    }

    public void increaseScore(int diff) { this.currentScore += diff; }

    public void incrementScore() { this.increaseScore(1); }

    public int getScore() { return this.currentScore; }
    public void resetScore() { this.currentScore = 0; }

    @Override
    public void Draw() {
      gc.setFill(this.color);
      String s = "Score: " + this.currentScore;
      gc.fillText(s, this.x - (s.length() - 8) * gc.getFont().getSize(),
                  this.y);
    }
  }
}
