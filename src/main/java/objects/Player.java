package objects;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import logic.Logic;

public class Player extends GameLaneObject {
  /* Static public vars */
  // static public final double playerLength = 50.0d;
  static public final double playerLength = 64.0d;
  static public final int maxJumpFrames = 40;
  static public final int maxJumpDistance = 2;
  static public final int maxRampFrames =
      (int)(TrailerWithRamp.rampSize / 2.0d);
  static public enum Level { LOW, HIGH }
  static public final double a;
  static public final double b;

  /* Private constants */
  private final Color playerColor = Color.DARKKHAKI;
  // private final Color playerColor = Color.BLACK;

  /* Private vars */
  private Level playerLevel;
  private boolean jumping = false;
  public boolean isClimbingRamp = false;
  private int currentJumpFrame = 0;
  private int currentClimbingFrame = 0;

  private ArrayList<Image> runFrames = null;

  static {
    a = (double)(-4 * maxJumpDistance) /
        (double)(maxJumpFrames * maxJumpFrames);
    b = (double)(4 * maxJumpDistance) / (double)maxJumpFrames;
    System.out.println("a:" + a);
    System.out.println("b:" + b);
  }

  /* Constructors */
  public Player() {
    super(Block.Lane.CENTER, Logic.getBounderies().getPlayerY(), playerLength);
    this.setColor(playerColor);
    this.playerLevel = Level.LOW;
    setAnimations();
  }

  private void setAnimations() {
    this.runFrames = new ArrayList<Image>(4);
    this.runFrames.add(new Image("file:src/main/java/assets/player/run_00.png",
                                 playerLength, playerLength, true, false));
    this.runFrames.add(new Image("file:src/main/java/assets/player/run_11.png",
                                 playerLength, playerLength, true, false));
    this.runFrames.add(new Image("file:src/main/java/assets/player/run_22.png",
                                 playerLength, playerLength, true, false));
    this.runFrames.add(new Image("file:src/main/java/assets/player/run_33.png",
                                 playerLength, playerLength, true, false));
  }

  /* Getters */
  public Level getLevel() { return this.playerLevel; }

  /* Setters */
  public void setLevel(Level level) { this.playerLevel = level; }

  public void moveLeft() {
    switch (getLane()) {
    case CENTER:
      this.setLanePath(Block.Lane.LEFT);
      break;
    case RIGHT:
      this.setLanePath(Block.Lane.CENTER);
      break;
    default:
      break;
    }
  }

  public void moveRight() {
    switch (getLane()) {
    case CENTER:
      setLanePath(Block.Lane.RIGHT);
      break;
    case LEFT:
      setLanePath(Block.Lane.CENTER);
      break;
    default:
      break;
    }
  }

  private void playerLengthIncrease(double diff) {
    this.width += diff;
    this.length += diff;
  }

  private void playerLengthDecrease(double diff) {
    this.width -= diff;
    this.length -= diff;
  }

  private double jumpCurrentValue(int frame) {
    System.out.println("jump:" + (a * frame * frame + b * frame));
    return a * frame * frame + b * frame;
  }

  public void jumpUpdate() {
    if (this.jumping == true) {
      if (this.currentJumpFrame != 0) {
        if (this.currentJumpFrame <= Player.maxJumpFrames / 2) { // Up
          playerLengthIncrease(jumpCurrentValue(this.currentJumpFrame));
        } else { // down
          playerLengthDecrease(jumpCurrentValue(this.currentJumpFrame));
          if (this.currentJumpFrame >= Player.maxJumpFrames) {
            this.currentJumpFrame = 0;
            this.jumping = false;
          }
        }
      }
      this.currentJumpFrame += 1;
    }
  }

  public void setPlayerLevelLow() {
    this.playerLevel = Level.LOW;
    this.length = Player.playerLength;
    this.width = Player.playerLength;
  }

  public void setPlayerLevelHigh() {
    this.playerLevel = Level.HIGH;
    this.length = Player.playerLength * 1.2;
    this.width = Player.playerLength * 1.2;
  }

  public void rampUpdate() {
    if (this.isClimbingRamp == true) {
      if (this.currentClimbingFrame == 0) {
        setPlayerLevelLow();
      }
      if (this.currentClimbingFrame == Player.maxRampFrames) {
        this.currentClimbingFrame = 0;
        this.isClimbingRamp = false;
        setPlayerLevelHigh();
      } else {
        this.length += 0.5;
        this.width += 0.5;
        this.currentClimbingFrame++;
      }
    }
  }

  private void updateRunningAnimation() {
    currentJumpFrame++;
    currentJumpFrame %= runFrames.size();
    // System.out.println("Image: " + currentJumpFrame);
    this.image = runFrames.get(currentJumpFrame);
  }

  public void playerUpdateAnimation() {
    if (this.jumping) {
      jumpUpdate();
    } else if (this.isClimbingRamp) {
      rampUpdate();
      updateRunningAnimation();
    } else {
      updateRunningAnimation();
    }
  }

  public void jump() { this.jumping = true; }

  public boolean isJumping() { return this.jumping; }
}
