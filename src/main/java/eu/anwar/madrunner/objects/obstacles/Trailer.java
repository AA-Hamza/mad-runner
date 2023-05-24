package eu.anwar.madrunner.objects.obstacles;

import eu.anwar.madrunner.logic.Logic;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Trailer extends Obstacle
{
    static public final double singleTrailerLength = 100d;
    static public final int maxNumberOfTrailers = 4;
    protected final int TrailerCount;
    public Trailer(Block.Lane lane, int count)
    {
        super(lane, 0, singleTrailerLength * count);
        this.TrailerCount = count;
        setColor(Color.CADETBLUE);
        super.setImage(new Image(getClass().getClassLoader().getResourceAsStream("images/obstacles/trailer.png"),
                                 Obstacle.obestacleWidth, singleTrailerLength, false, false));
    }

    public Trailer(Block.Lane lane)
    {
        this(lane, (int)(Math.random() * maxNumberOfTrailers + 1));
    }

    public Trailer(Block.Lane lane, double length)
    {
        super(lane, 0, length);
        this.TrailerCount = (int)(length / singleTrailerLength);
    }

    @Override public void Draw()
    {
        for (int i = 0; i < this.length; i += singleTrailerLength)
        {
            Logic.gc.drawImage(this.getImage(),
                               Logic.getBounderies().getLaneStartX(this.getLane()) +
                                   Logic.getBounderies().getLanePadding(),
                               i + this.getY());
        }
    }
}
