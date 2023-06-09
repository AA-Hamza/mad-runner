package eu.anwar.madrunner.objects;
import eu.anwar.madrunner.logic.Logic;
import eu.anwar.madrunner.objects.obstacles.Block;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameObject
{
    protected Color color = Color.GREEN;
    protected Block.Lane lane = null;

    protected double x, y, width, length = 0;
    private Image image = null;

    public GameObject(double x, double y, double width, double length)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
    }

    /*
    public GameObject(Block.Lane lane, double y, double width, double length)
    {
        this.lane = lane;
        this.y = y;
        this.width = width;
        this.length = length;
    }

    public GameObject(Block.Lane lane, double y, double cubeLength)
    {
        this(lane, y, cubeLength, cubeLength);
    }

    public GameObject(double x, double y, double cubeLength)
    {
        this(x, y, cubeLength, cubeLength);
    }
    */

    public double getWidth()
    {
        return this.width;
    }
    public double getLength()
    {
        return this.length;
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
    public void setLane(Block.Lane path)
    {
        this.lane = path;
    }
    public void setImage(Image image)
    {
        this.image = image;
    }

    public Block.Lane getLane()
    {
        return this.lane;
    }
    public Image getImage()
    {
        return this.image;
    }
    public void setY(double y)
    {
        this.y = y;
    }
    public double getY()
    {
        return this.y;
    }
    /*
    public double getX()
    {
        return this.x;
    }
    */

    public void Draw()
    {
        GraphicsContext gc = Logic.gc;
        double objX = this.x;

        if (this.lane != null)
        {
            objX = (Logic.getBounderies().getLaneWidth() - this.width) / 2.0d +
                   Logic.getBounderies().getLaneStartX(this.lane);
            this.x = objX;
        }

        if (this.image != null)
        {
            gc.drawImage(this.image, this.x, this.y, this.width, this.length);
        }
        else
        {
            gc.setFill(this.color);
            gc.fillRect(this.x, this.y, this.width, this.length);
        }
    }

    //public boolean interesects(double x, double y)
    //{
    //    if (x > this.x && x < this.x + this.width && y > this.y && y < this.y + length)
    //    {
    //        return true;
    //    }
    //    return false;
    //}

    //public boolean interesects(Block.Lane lane, double y)
    //{
    //    if (this.lane == null)
    //    {
    //        System.out.println("Trying to get interesection using Lanes on an Object without lane field");
    //    }
    //    else
    //    {
    //        if (this.lane == lane && y > this.y && y < this.y + this.length)
    //        {
    //            return true;
    //        }
    //    }
    //    return false;
    //}

    //public boolean touches(GameObject other)
    //{
    //    if (other.lane != null)
    //    {
    //        if (other.lane != this.lane)
    //        {
    //            return false;
    //        }
    //        double y11 = other.y;
    //        double y12 = other.y + other.length;
    //        double y21 = this.y;
    //        double y22 = this.y + this.length;
    //        if (y11 >= y21 && y11 <= y22)
    //            return true;
    //        else if (y12 >= y21 && y12 <= y22)
    //            return true;
    //        if (y21 >= y11 && y21 <= y12)
    //            return true;
    //        else if (y22 >= y11 && y22 <= y12)
    //            return true;
    //    }
    //    return false;
    //}

    // public boolean within(GameObject other) {
    //   if (this.x >= other.x && this.x + this.width <= other.x + other.width &&
    //       this.y >= other.y && this.y + this.length <= other.y + other.length)
    //       {
    //     return true;
    //   }
    //   return false;
    // }
}
