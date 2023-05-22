package logic;

import game.MadRunner;
import java.util.Deque;
import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import objects.Block;
import objects.Block.Lane;
import objects.GameObject;
import objects.Ground;
import objects.Obstacle;
import objects.Player;
import objects.Player.Level;
import objects.RoadBlock;
import objects.Trailer;
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
    obstaclesBlocks.add(obstacleFactory.generateBlock());

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
    Logic.gc.fillRect(0, 0, Logic.getBounderies().getScreenWidth(),
                      Logic.getBounderies().getScreenHeight());
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
    // player.jumpUpdate();
    player.playerUpdateAnimation();
    score.Draw();

    if (playerCollision()) {
      stopPlayer();
    }

    lastFrame = now;
    last100Millisecond = now;
    if ((now - last1000Millisecond) > 1000 * 1_000_000) {
      score.incrementScore();
      // System.out.println("First: " +
      // obstaclesBlocks.peekFirst().getCurrentY());
      Block first = obstaclesBlocks.peekFirst();
      if (first.getCurrentY() > -1 * getBounderies().getScreenHeight() *
                                    ObstacleFactory.bufferScreens) {
        obstaclesBlocks.addFirst(obstacleFactory.generateBlock());
        System.out.println("Generated new block: " +
                           obstaclesBlocks.peekFirst().getCurrentY());
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
    private final double groundStartX, groundWidth, groundEndX, laneWidth,
        screenWidth, screenHeight;

    private final double playerYPercentage = 0.8;

    public Bounderies(double screenWidth, double screenHeight) {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      System.out.println((1 - groundPaddingPercentage * 2) *
                         MadRunner.minScreenX);
      System.out.println(screenWidth);
      if ((1 - groundPaddingPercentage * 2) * screenWidth <=
          MadRunner.minScreenX) {
        this.groundStartX = 0;
        this.groundWidth = screenWidth;
        this.laneWidth = this.groundWidth / 3.0d;
        this.groundEndX = this.groundStartX + this.groundWidth;
      } else {
        // this.scale = screenWidth / MadRunner.minScreenX - 2;
        // System.out.println("scale: " + scale);
        this.groundStartX = screenWidth * groundPaddingPercentage;
        this.groundWidth = screenWidth * (1 - 2 * groundPaddingPercentage);
        this.laneWidth = this.groundWidth / 3.0d;
        this.groundEndX = this.groundStartX + this.groundWidth;
      }
    }

    public double getGroundStartX() { return this.groundStartX; }
    public double getGroundEndX() { return this.groundEndX; }
    public double getPlayerY() {
      return this.playerYPercentage * this.screenHeight;
    }
    public double getScreenHeight() { return this.screenHeight; }
    public double getScreenWidth() { return this.screenWidth; }
    public double getGroundWidth() { return this.groundWidth; }
    public double getLaneWidth() { return this.laneWidth; }
    public double getLaneStartX() { return this.groundStartX; }
    public double getLanePadding() {
      return (this.laneWidth - Obstacle.obestacleWidth) / 2.0d;
    }
    public double getLaneStartX(Block.Lane path) {
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

      return this.groundStartX + multiplier * this.laneWidth;
    }
  }

  public class GameControls implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
      switch (event.getCode()) {
      case RIGHT:
        player.moveRight();
        if (checkIfLaneAvailable(player.getLane()) == false) {
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
        if (checkIfLaneAvailable(player.getLane()) == false)
          stopPlayer();
        break;
      case UP:
        player.jump();
        break;
      case DOWN:
        startPlayer();
        break;
      default:
        break;
      }
    }
  }

  /**
   * Checks if the lane is available so the player will be able to move into
   * the plane without losing
   * @param lane the target lane, the lane the player is moving into
   */
  public boolean checkIfLaneAvailable(Lane lane) {
    // All lanes are available if the user is on a trailer
    if (this.player.getLevel() == Level.HIGH) {
      return true;
    }

    for (Block block : obstaclesBlocks) {
      for (Obstacle obs : block.getLane(lane)) {
        if (obs.contains(lane, player.getY()) ||
            obs.contains(lane, player.getY() + player.getLength() / 1.5d)) {
          System.out.println("Lane not available");
          System.out.println("Lane.y: " + obs.getY() +
                             " player.y: " + player.getY());
          return false;
        }
      }
    }
    return true;
  }

  private Obstacle lastTouch = null;
  /**
   * This function is responsible of detecting if the user has collided with
   * any Obstacle
   */

  public boolean playerCollision() {
    Obstacle obs = getPlayerTouchingObstacle();
    if (obs != null) {
      if (obs == lastTouch) {
        player.setPlayerLevelHigh();
        return false;
      }
      lastTouch = obs;

      if (player.getLevel() == Level.LOW) {
        if ((obs instanceof RoadBlock)) {
          if (player.isJumping()) {
            return false;
          }
          return true;
        }

        if (obs instanceof TrailerWithRamp) {
          player.isClimbingRamp = true;
          return false;
        }
      }
    } else if (lastTouch != null) {
      System.out.println("Player is low");
      // player.setLevel(Level.LOW);
      player.setPlayerLevelLow();
      lastTouch = null;
    } else if (player.isJumping() == false) {
      player.setPlayerLevelLow();
    }
    return false;

    // if (lastTouch != null) {
    //   if (player.touches(lastTouch)) {
    //     return false;
    //   }
    //
    //   // Test if the object tests other objects
    //   for (Block block : obstaclesBlocks) {
    //     for (Obstacle obs : block.getLane(playerLane)) {
    //       // System.out.println("Testing");
    //       if (player.touches(obs)) {
    //         lastTouch = obs;
    //         return false;
    //       }
    //     }
    //   }
    //   if (!player.isJumping()) {
    //     player.setLevel(Player.Level.LOW);
    //     // System.out.println("Player is getting low");
    //     lastTouch = null;
    //   }
    //   return false;
    // }
    //
    // for (Block block : obstaclesBlocks) {
    //   for (Obstacle obs : block.getLane(playerLane)) {
    //     if (player.touches(obs)) {
    //       // System.out.println("Touches another object");
    //       if (obs instanceof TrailerWithRamp) {
    //         // System.out.println("Player is touching ramp");
    //         // player.setLevel(Player.Level.HIGH);
    //         player.isClimbingRamp = true;
    //
    //         lastTouch = obs;
    //         return false;
    //       } else if (obs instanceof RoadBlock) {
    //         if (player.isJumping()) {
    //           return false;
    //         }
    //         return true;
    //       } else {
    //         return true;
    //       }
    //     }
    //   }
    // }
    // return false;
    // }
  }

  public Obstacle getPlayerTouchingObstacle() {
    for (Block block : obstaclesBlocks) {
      for (Obstacle obs : block.getLane(player.getLane())) {
        if (player.touches(obs)) {
          return obs;
        }
      }
    }
    return null;
  }

  public Level getPlayerLevel() {
    Obstacle obs = getPlayerTouchingObstacle();
    if (obs != null) {
      if (obs instanceof TrailerWithRamp || obs instanceof Trailer) {
        return Level.HIGH;
      }
    }
    return Level.LOW;
  }

  public class Score extends GameObject {
    static public final double length = 100;
    static public final double padding = 50;
    private int currentScore;
    Score() {
      super(Logic.getBounderies().getGroundEndX() - padding - length, padding,
            length);
      this.currentScore = 0;
      // this.color = Color.WHEAT;
      this.color = Color.YELLOW;
    }

    public void increaseScore(int diff) { this.currentScore += diff; }

    public void incrementScore() { this.increaseScore(1); }

    public int getScore() { return this.currentScore; }
    public void resetScore() { this.currentScore = 0; }

    @Override
    public void Draw() {
      Paint p = gc.getFill();
      gc.setFill(this.color);
      String s = "Score: " + this.currentScore;
      gc.fillText(s, this.x - (s.length() - 8) * gc.getFont().getSize(),
                  this.y);
      gc.setFill(p);
    }
  }
}
