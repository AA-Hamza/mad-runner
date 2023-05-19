package objects;
import javafx.scene.paint.Color;
import logic.Logic;

public class Player extends GameObject {

  static public final double playerLength = 50;
  private final Color playerColor = Color.GREEN;

  public Player() {
    super(Lane.PATH.CENTER, Logic.getBounderies().getPlayerY(), playerLength);
    super.setColor(playerColor);
  }

  public void moveLeft() {
    switch (getLanePath()) {
    case CENTER:
      this.setLanePath(Lane.PATH.LEFT);
      break;
    case RIGHT:
      this.setLanePath(Lane.PATH.CENTER);
      break;
    default:
      break;
    }
  }

  public void moveRight() {
    switch (getLanePath()) {
    case CENTER:
      setLanePath(Lane.PATH.RIGHT);
      break;
    case LEFT:
      setLanePath(Lane.PATH.CENTER);
      break;
    default:
      break;
    }
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
