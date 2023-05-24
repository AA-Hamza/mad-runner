package eu.anwar.madrunner.objects;
import eu.anwar.madrunner.logic.Logic;
import eu.anwar.madrunner.objects.obstacles.Block;

public class GameLaneObject extends GameObject
{
    protected Block.Lane lane = null;

    /* Constructors */
    public GameLaneObject(Block.Lane lane, double y, double width, double length)
    {
        super(0, y, width, length);
        this.lane = lane;
    }

    public GameLaneObject(Block.Lane lane, double y, double cubeLength)
    {
        this(lane, y, cubeLength, cubeLength);
    }

    /* Setters */
    public void setLane(Block.Lane path)
    {
        this.lane = path;
    }

    /* Getters */
    public Block.Lane getLane()
    {
        return this.lane;
    }

    /* Draw Function */
    public void Draw()
    {
        double laneObjectX = this.x;

        laneObjectX =
            (Logic.getBounderies().getLaneWidth() - this.width) / 2.0d + Logic.getBounderies().getLaneStartX(this.lane);
        this.x = laneObjectX;

        super.Draw();
    }

    /**
     * tests if the object contains this point.
     * @param lane the lane to test against
     * @param y the y value to test if it relies inside the object
     * @return if the lane matches the object lane and y is within the object
     */
    public boolean contains(Block.Lane lane, double y)
    {
        if (this.lane == lane && y > this.y && y < this.y + this.length)
        {
            return true;
        }
        return false;
    }

    /**
     * Tests if the objects are touching/colliding, the two objects need to be on
     * the same lane (returns false if they are not). The test is quite simple, we
     * calculate the difference between their Ys if the
     * difference is less than or equal the length of the then they are touching
     * @param other the other object
     * @return if the two objects touch each other
     */
    public boolean touches(GameLaneObject other)
    {
        if (other.lane != this.lane)
        {
            return false;
        }
        else if (this.y > other.y)
        {
            return this.y - other.y <= other.length;
        }
        else if (other.y > this.y)
        {
            return other.y - this.y <= this.length;
        }
        return false;
    }

    /**
     * Get distance between lane objects, Note that it assumes both objects are on
     * the same lane and doesn't test against it by design
     */
    public double distance(GameLaneObject other)
    {
        if (this.y > other.y)
        {
            return this.y - (other.y + other.length);
        }

        if (other.y >= this.y)
        {
            return other.y - (this.y + this.length);
        }
        return 0;
    }
}
