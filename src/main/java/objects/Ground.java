package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Logic;
import objects.obstacles.Block;
import objects.obstacles.Block.Lane;
import objects.obstacles.Obstacle;

public class Ground extends GameObject
{
    public static final double laneImageWidth = Obstacle.obestacleWidth * 1.1;
    // public static final double tileImageWidth = 64;
    private final Image tile;
    private double yDisplacement = 0;
    public Ground(double x, double width, double length)
    {
        super(x, 0, width, length);
        super.setColor(Color.GREY);
        this.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/ground/railway.png"),
                                Logic.getBounderies().getLaneWidth(), Logic.getBounderies().getLaneWidth(), true,
                                false));
        this.tile = new Image(getClass().getClassLoader().getResourceAsStream("images/ground/tile.png"),
                              Logic.getBounderies().getLaneWidth(), Logic.getBounderies().getLaneWidth(), true, false);
    }

    public void frameUpdate()
    {
        yDisplacement += Block.speed;
        yDisplacement %= laneImageWidth;
    }

    @Override public void Draw()
    {
        for (int i = 0; i <= Logic.getBounderies().getScreenWidth(); i += laneImageWidth)
        {
            for (int j = (int)(-1 * laneImageWidth); j < Logic.getBounderies().getScreenHeight(); j += laneImageWidth)
            {
                Logic.gc.drawImage(this.tile, i, j + yDisplacement, laneImageWidth, laneImageWidth);
            }
        }
        for (int i = 0; i < 3; i++)
        {
            for (int j = (int)(-1 * laneImageWidth); j < Logic.getBounderies().getScreenHeight(); j += laneImageWidth)
            {

                double startX = Logic.getBounderies().getLaneStartX(Lane.values()[i]) +
                                (Logic.getBounderies().getLaneWidth() - laneImageWidth) / 2.0d;
                Logic.gc.drawImage(this.getImage(), startX, j + yDisplacement, laneImageWidth, laneImageWidth);
            }
        }
    }
}
