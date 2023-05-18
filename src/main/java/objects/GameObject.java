package objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameObject {
  protected Color color = Color.GREEN;
  protected Image image = null;

  protected double x, y, width, height = 0;
  private int zIndex;

  public GameObject(double x, double y, double width, double height) {
    System.out.println(x + " " + y + " " + width + " " + height);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public GameObject(double x, double y, double cubeLength) {
    this(x, y, cubeLength, cubeLength);
  }

  public void setZIndex(int z) { this.zIndex = z; }
  public int getZIndex() { return this.zIndex; }
  public void setColor(Color color) { this.color = color; }

  public void Draw(GraphicsContext gc) {
    if (this.image != null) {
      gc.drawImage(this.image, this.x, this.y, this.width, this.height);
    } else {
      gc.setFill(this.color);
      gc.fillRect(this.x, this.y, this.width, this.height);
    }
  }

  public boolean interesects(double x, double y) {
    if (x > this.x && x < this.x + this.width && y > this.y &&
        y < this.y + height) {
      return true;
    }
    return false;
  }

  public boolean within(GameObject other) {
    if (this.x >= other.x && this.x + this.width <= other.x + other.width &&
        this.y >= other.y && this.y + this.height <= other.y + other.height) {
      return true;
    }
    return false;
  }
}
