package objects;
import javafx.scene.paint.Color;
import logic.Logic;

public class Player extends GameObject {

  static private final double playerLength = 50;
  private final Color playerColor = Color.GREEN;

  public Player() {
    super(Lane.PATH.CENTER, Logic.getBounderies().getPlayerY(), playerLength);
    super.setColor(playerColor);
  }

  public void moveLeft() {
    switch (this.lane) {
    case CENTER:
      this.lane = Lane.PATH.LEFT;
      break;
    case RIGHT:
      this.lane = Lane.PATH.CENTER;
      break;
    default:
      break;
    }
    // updatePlayerX();
  }

  public void moveRight() {
    switch (this.lane) {
    case CENTER:
      lane = Lane.PATH.RIGHT;
      break;
    case LEFT:
      lane = Lane.PATH.CENTER;
      break;
    default:
      break;
    }
    // updatePlayerX();
  }

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
