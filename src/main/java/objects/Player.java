package objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

enum PlayerPOS { LEFT, CENTER, RIGHT }

public class Player extends GameObject {

  private final Color playerColor = Color.GREEN;
  private final double startX, startY;
  private final double laneLength, lanePadding;

  private PlayerPOS pos;

  public Player(double startX, double startY, double playerLength,
                double laneLength) {
    super(startX, startY, playerLength);
    super.setColor(playerColor);
    this.startX = startX;
    this.startY = startY;
    this.laneLength = laneLength;
    this.lanePadding = (laneLength - playerLength) / 2.0d;
    this.pos = PlayerPOS.CENTER;
    this.updatePlayerX();
  }

  public void moveLeft() {
    switch (this.pos) {
    case CENTER:
      pos = PlayerPOS.LEFT;
      break;
    case RIGHT:
      pos = PlayerPOS.CENTER;
      break;
    default:
      break;
    }
    updatePlayerX();
  }

  public void moveRight() {
    switch (this.pos) {
    case CENTER:
      pos = PlayerPOS.RIGHT;
      break;
    case LEFT:
      pos = PlayerPOS.CENTER;
      break;
    default:
      break;
    }
    updatePlayerX();
  }

  private void updatePlayerX() {
    switch (this.pos) {
    case LEFT:
      this.x = this.startX + this.lanePadding;
      break;
    case CENTER:
      this.x = this.startX + this.laneLength + this.lanePadding;
      break;
    case RIGHT:
      this.x = this.startX + 2 * this.laneLength + this.lanePadding;
      break;
    default:
      break;
    }
  }

  public PlayerPOS getPlayerPOS() { return this.pos; }

  // public int getScore() { return this.score; }
  //
  // public void resetScore() { this.score = 0; }
  //
  // public void increaseScore() { this.score += 1; }
  // public void increaseScore(int value) { this.score += value; }
}
