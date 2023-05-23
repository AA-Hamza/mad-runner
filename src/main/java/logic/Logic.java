package logic;

import game.MadRunner;
import java.util.Deque;
import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import objects.*;

public class Logic {
  /* static variables */
  /* Note having these two variables as static vars without locks is just asking
   * for problems, especially if there are multiplle threads accessing it.
   */
  static public GraphicsContext gc;
  static public Bounderies bounderiesOBJ = null;

  /* private variables */
  private Ground ground;
  private ObstacleFactory obstacleFactory;
  private Deque<Block> obstaclesBlocks;
  private Player player;
  private Score score;
  private Obstacle lastTouchedObs = null;
  private boolean playerDied;

  /* Constructors */
  public Logic(GraphicsContext gc) {
    // Setting graphics context
    Logic.gc = gc;

    // Obstacles
    this.obstacleFactory = new ObstacleFactory();
    this.obstaclesBlocks = new LinkedList<Block>();
    obstaclesBlocks.add(obstacleFactory.generateBlock());

    // Calculating ground
    final double groundStartX = getBounderies().getGroundStartX();
    final double groundWidth = getBounderies().getGroundWidth();
    ground = new Ground(groundStartX, groundWidth,
                        getBounderies().getScreenHeight());

    // Set up Player
    this.player = new Player();
    this.playerDied = false;

    // score
    this.score = new Score();

    gc.getCanvas().getScene().setOnKeyReleased(new GameControls());
  }

  private long last1000Millisecond = 0;
  /**
   * This is considered the main function of the game, this function gets
   * called by the game loop and game loop logic mostly happens here
   * This function gets called at the start of every frame
   * @param now contains the current time in nano seconds
   * TODO simplify this function
   */
  public void tick(Long now) {
    // Clear screen
    Logic.gc.fillRect(0, 0, Logic.getBounderies().getScreenWidth(),
                      Logic.getBounderies().getScreenHeight());

    if (playerDied == false) {
      ground.frameUpdate();
    }
    ground.Draw();

    for (Block l : obstaclesBlocks) {
      if (playerDied == false) {
        l.frameUpdate();
      }
      for (Obstacle o : l.getLeftLane()) {
        o.Draw();
      }
      for (Obstacle o : l.getCenterLane()) {
        o.Draw();
      }
      for (Obstacle o : l.getRightLane()) {
        o.Draw();
      }
    }

    if (playerDied == false) {
      if (playerCollision()) {
        playerDied();
        this.player.setIsDying();
      }
    }

    player.Draw();
    player.playerUpdateAnimation();

    if (playerDied == false) {
      score.Draw();

      if ((now - last1000Millisecond) > 1000 * 1_000_000) {
        score.incrementScore();
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
        last1000Millisecond = now;
      }
    }
  }

  static public Bounderies getBounderies() {
    if (bounderiesOBJ == null) {
      bounderiesOBJ = new Bounderies(Logic.gc.getCanvas().getWidth(),
                                     Logic.gc.getCanvas().getHeight());
    }
    return bounderiesOBJ;
  }

  private void playerDied() { this.playerDied = true; }

  private void startPlayer() { this.playerDied = false; }

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
        break;
      case LEFT:
        player.moveLeft();
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
   * This function is responsible of detecting if the user has collided with
   * any Obstacle
   */
  public boolean playerCollision() {
    Obstacle obs = getPlayerTouchingObstacle();
    if (obs != null) {
      if (obs == lastTouchedObs) {
        return false;
      }
      lastTouchedObs = obs;

      if (player.getLevel() == Player.Level.LOW) {
        if ((obs instanceof RoadBlock)) {
          if (player.isJumping()) {
            return false;
          }
          return true;
        }

        if (obs instanceof TrailerWithRamp) {
          player.rampClimb();
          return false;
        }

        return true;
      }
    } else if (lastTouchedObs != null) {
      if (!player.isJumping() && (lastTouchedObs instanceof Trailer ||
                                  lastTouchedObs instanceof TrailerWithRamp)) {

        player.setPlayerLevelLow();
        lastTouchedObs = null;
        return false;
      }
    }
    return false;
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

  public Player.Level getPlayerLevel() {
    Obstacle obs = getPlayerTouchingObstacle();
    if (obs != null) {
      if (obs instanceof TrailerWithRamp || obs instanceof Trailer) {
        return Player.Level.HIGH;
      }
    }
    return Player.Level.LOW;
  }

  public class Score extends GameObject {
    static public final double length = 100;
    static public final double padding = 50;
    private int currentScore;
    Score() {
      super(Logic.getBounderies().getGroundStartX() + padding, padding, length);
      this.currentScore = 0;
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
      gc.fillText(s, this.x, this.y);
      gc.setFill(p);
    }
  }
}
