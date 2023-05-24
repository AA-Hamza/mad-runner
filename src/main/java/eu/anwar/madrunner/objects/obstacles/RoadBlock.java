package eu.anwar.madrunner.objects.obstacles;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class RoadBlock extends Obstacle
{
    static public final double RoadBlockLength = 16d;
    public RoadBlock(Block.Lane lane)
    {
        super(lane, 0, RoadBlockLength);
        setColor(Color.CRIMSON);
        this.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/obstacles/roadblock.png"),
                                Obstacle.obestacleWidth, RoadBlockLength, false, false));
    }
}
