package objects;
import javafx.scene.paint.Color;
import logic.Logic;

public class Player extends GameObject {

  static public final double playerLength = 50;
  private final Color playerColor = Color.GREEN;
  static public enum Stage { LOW, HIGH }
  private Stage playerStage;
  public Stage lastStage;
  private boolean jumping = false;
  private int jumpFrame = 0;
  private final int maxJumFrames = 100;

  public Player() {
    super(Block.Lane.CENTER, Logic.getBounderies().getPlayerY(), playerLength);
    super.setColor(playerColor);
    this.playerStage = Stage.LOW;
  }

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

  public void jumpUpdate() {
    if (this.jumping == true) {
      if (this.jumpFrame != 0) {
        this.jumpFrame += 1;
        if (this.jumpFrame < this.maxJumFrames / 2) { // Up
          this.length += 0.5;
          this.width += 0.5;
          if (this.jumpFrame == this.maxJumFrames / 10) {
            this.moveUp();
          }
        } else { // down
          this.length -= 0.5;
          this.width -= 0.5;
          if (this.jumpFrame == 9 * this.maxJumFrames / 10) {
            this.moveDown();
          }
          if (this.jumpFrame == this.maxJumFrames) {
            this.jumpFrame = 0;
            this.length = playerLength;
            this.width = playerLength;
            this.jumping = false;
            this.playerStage = this.lastStage;
          }
        }
      }
      this.jumpFrame += 1;
    }
  }

  public void jump() {
    this.lastStage = this.playerStage;
    this.jumping = true;
  }

  public boolean isJumping() { return this.jumping; }

  public void moveUp() {
    this.playerStage = Stage.HIGH;
    System.out.println("Player is HIGH");
  }
  public void moveDown() {
    this.playerStage = Stage.LOW;
    System.out.println("Player is LOW");
  }
  public Stage getStage() { return this.playerStage; }

  // private void updatePlayerX() {
  //   switch (this.lane) {
  //   case LEFT:
  //     this.x = this.startX + this.lanePadding;
  //     break;
  //   case CENTER:
  //     this.x = this.startX + this.laneWidth + this.lanePadding;
  //     break;
  //   case RIGHT:
  //     this.x = this.startX + 2 * this.laneWidth + this.lanePadding;
  //     break;
  //   default:
  //     break;
  //   }
  // }

  // public Lane.PATH getPlayerPOS() { return this.lane; }

  // public int getScore() { return this.score; }
  //
  // public void resetScore() { this.score = 0; }
  //
  // public void increaseScore() { this.score += 1; }
  // public void increaseScore(int value) { this.score += value; }
}
