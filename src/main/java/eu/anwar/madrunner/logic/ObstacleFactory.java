package eu.anwar.madrunner.logic;

import eu.anwar.madrunner.objects.GameLaneObject;
import eu.anwar.madrunner.objects.Player;
import eu.anwar.madrunner.objects.obstacles.Block;
import eu.anwar.madrunner.objects.obstacles.Obstacle;
import eu.anwar.madrunner.objects.obstacles.RoadBlock;
import eu.anwar.madrunner.objects.obstacles.Trailer;
import eu.anwar.madrunner.objects.obstacles.TrailerWithRamp;
import java.util.ArrayList;

public class ObstacleFactory
{
    static public final int buildingScreens = 2;
    static public final int bufferScreens = 0;

    static private final double minSpace = 100d;
    static private final double space = 250d;
    static private final double testObjectStep = Player.playerLength / 2.0d;
    static private final double testObjectLength = Player.playerLength;

    // private final double logicalLaneLength;

    public Block generateBlock()
    {
        int tries = 100;
        ArrayList<ArrayList<Obstacle>> result = new ArrayList<ArrayList<Obstacle>>(3);
        result.add(generatePath());
        result.add(generatePath());
        result.add(generatePath());
        while (tries-- > 0)
        {
            if (winningPath(result) == true)
            {
                return new Block(result.get(0), result.get(1), result.get(2));
            }
            System.out.println("Got unwinning path, trying again");
            result.set((int)(Math.random() * 3), generatePath());
        }
        System.out.println("Could not generate a winning path");
        System.exit(-1);
        return null;
    }

    /**
     * Generate one lane with random obstacles
     * @return an array containing a random number of obstacles with random
     *     locations (never touching though).
     */
    public ArrayList<Obstacle> generatePath()
    {
        double logicalLaneLength = Logic.getBounderies().getScreenHeight() * buildingScreens;
        ArrayList<Obstacle> path = new ArrayList<Obstacle>();
        double startY = Math.random() * logicalLaneLength * 0.5;
        while (startY < logicalLaneLength)
        {
            Obstacle newObstacle = generateObstacle();
            if (startY + newObstacle.getLength() < logicalLaneLength)
            {
                newObstacle.setY(startY);
                startY += newObstacle.getLength() + minSpace + Math.random() * space;
                path.add(newObstacle);
            }
            else
            {
                break;
            }
        }
        return path;
    }

    /**
     * Test if there is at least one possible way for the player to win.
     * Note only Trailers will be able to generate an unwinning path. so we only
     * test them.
     * the test have two tests. The first test uses a test object
     * with double the length of the player, this object moves using a step which
     * is half the player length (Assumption: all lanes are in left lanes).
     * If this object touches three trailers at once, then that will be unwinable
     * path
     *
     * the second test
     * We
     */
    public boolean winningPath(ArrayList<ArrayList<Obstacle>> lanes)
    {
        double logicalLaneLength = Logic.getBounderies().getScreenHeight() * buildingScreens;
        // testObj is in the left lane because we expect all lanes obstacles to be
        // in left lane
        GameLaneObject testObj = new GameLaneObject(Block.Lane.LEFT, 0, testObjectLength);

        // Test if there is enough space between trailers for the player
        // un winnable
        // |   |
        // |   |
        //   |
        //   |
        while (testObj.getY() + testObj.getLength() < logicalLaneLength)
        {
            int pointOfContacts = 0;
            for (int laneIndex = 0; laneIndex < lanes.size(); laneIndex++)
            {
                for (int j = 0; j < lanes.get(laneIndex).size(); j++)
                {
                    if (lanes.get(laneIndex).get(j) instanceof Trailer)
                    {
                        Obstacle trailer = lanes.get(laneIndex).get(j);
                        if (trailer.touches(testObj))
                        {
                            pointOfContacts += 1;
                            break;
                        }
                    }
                }
            }

            testObj.setY(testObj.getY() + testObjectStep);
            if (pointOfContacts == 3)
            {
                return false;
            }
        }
        // We only need to test lane 1 & 2, lane 1 & 3, lane 2 & 3
        for (int currentLaneIndex = 0; currentLaneIndex < lanes.size(); currentLaneIndex++)
        {
            for (int otherLaneIndex = currentLaneIndex + 1; otherLaneIndex < lanes.size(); otherLaneIndex++)
            {

                for (Obstacle curr : lanes.get(currentLaneIndex))
                {
                    double max = 0;
                    for (Obstacle other : lanes.get(otherLaneIndex))
                    {
                        if (!curr.touches(other))
                        {
                            // System.out.println(curr.getY() + " " + other.getY());
                            // System.out.println(curr.getLength() + " " + other.getLength());
                            // System.out.println(curr.distance(other));
                            max = Math.max(curr.distance(other), max);
                            // if (curr.distance(other) <= Player.playerLength) {
                            //   return false;
                            // }
                        }
                    }
                    if (max <= testObjectLength)
                    {
                        return false;
                    }
                }
            }
        }

        // Test if a trailer in the center PATH is adjacent to 2 trailers on each
        // PATH
        //
        // Un winnable
        //     |
        //   | |
        // | |
        // | |
        // |
        // |
        //
        //
        // Winnable
        // |
        // | |
        // |
        // |   |
        //     |
        //     |
        for (Obstacle obsCenter : lanes.get(1))
        {
            if (obsCenter instanceof Trailer)
            {
                int touches = 0;
                for (Obstacle obsLeft : lanes.get(0))
                {
                    if (obsLeft instanceof Trailer)
                    {
                        if (obsCenter.touches(obsLeft))
                        {
                            touches++;
                            break;
                        }
                    }
                }

                for (Obstacle obsRight : lanes.get(2))
                {
                    if (obsRight instanceof Trailer)
                    {
                        if (obsCenter.touches(obsRight))
                        {
                            touches++;
                            break;
                        }
                    }
                }

                if (touches == 2)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public Obstacle generateObstacle()
    {
        double chance = Math.random();
        if (chance < 0.3)
        {
            return new RoadBlock(Block.Lane.LEFT);
        }
        else if (chance < 0.5)
        {
            return new TrailerWithRamp(Block.Lane.LEFT);
        }
        else
        {
            return new Trailer(Block.Lane.LEFT);
        }
    }
}
