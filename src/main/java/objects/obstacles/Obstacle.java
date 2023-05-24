package objects.obstacles;

import logic.Logic;

public class Obstacle extends objects.GameLaneObject
{
    /* static vars */
    static public final double obestacleWidth = 70.0d;

    /* constructors */
    public Obstacle(Block.Lane lane, double y, double length)
    {
        super(lane, y, obestacleWidth, length);
    }

    /**
     * Increase the y position of the obstacle (mimic movement)
     */
    public void addY(double diff)
    {
        this.y += diff;
    }

    /**
     * This function checks if this object is off the screen
     * Off screens objects shouldn't be drawn
     * @return boolean will return true if object is off the screen
     */
    public boolean isOffTheScreen()
    {
        if (this.getY() > Logic.getBounderies().getScreenHeight() || this.getY() + this.getLength() < 0)
        {
            return true;
        }
        return false;
    }

    @Override public void Draw()
    {
        if (!this.isOffTheScreen())
        {
            super.Draw();
        }
    }
}
