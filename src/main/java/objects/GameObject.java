package objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Logic;

public class GameObject {
  protected Color color = Color.GREEN;
  protected Image image = null;
  protected Lane.PATH lane = null;

  protected double x, y, width, length = 0;
  private int zIndex;

  public GameObject(double x, double y, double width, double length) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.length = length;
  }

  public GameObject(Lane.PATH lane, double y, double width, double length) {
    this.lane = lane;
    this.y = y;
    this.width = width;
    this.length = length;
  }

  public GameObject(Lane.PATH lane, double y, double cubeLength) {
    this(lane, y, cubeLength, cubeLength);
  }

  public GameObject(double x, double y, double cubeLength) {
    this(x, y, cubeLength, cubeLength);
  }

  public void setZIndex(int z) { this.zIndex = z; }
  public int getZIndex() { return this.zIndex; }
  public double getWidth() { return this.width; }
  public double getLength() { return this.length; }
  public void setColor(Color color) { this.color = color; }
  public void setY(double y) { this.y = y; }
  public double getY() { return this.y; }

  public void Draw(GraphicsContext gc) {
    double objX = this.x;

    if (this.lane != null) {
      objX = (Logic.getBounderies().getPathWidth() - this.width) / 2.0d +
             Logic.getBounderies().getPathStartX(this.lane);
      this.x = objX;
    }

    if (this.image != null) {
      gc.drawImage(this.image, this.x, this.y, this.width, this.length);
    } else {
      gc.setFill(this.color);
      gc.fillRect(this.x, this.y, this.width, this.length);
    }
  }

  public boolean interesects(double x, double y) {
    if (x > this.x && x < this.x + this.width && y > this.y &&
        y < this.y + length) {
      return true;
    }
    return false;
  }

  public boolean within(GameObject other) {
    if (this.x >= other.x && this.x + this.width <= other.x + other.width &&
        this.y >= other.y && this.y + this.length <= other.y + other.length) {
      return true;
    }
    return false;
  }
}
