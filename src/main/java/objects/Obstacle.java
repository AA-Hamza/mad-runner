package objects;

public class Obstacle extends GameObject {
  static private final double obestacleWidth = 70.0d;

  public Obstacle(Block.Lane lane, double y, double length) {
    super(lane, y, obestacleWidth, length);
  }

  public void setY(double y) { this.y = y; }
  public void addY(double diff) { this.y += diff; }
}
