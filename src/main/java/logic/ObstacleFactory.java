package logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import objects.GameObject;
import objects.Lane;
import objects.Obstacle;
import objects.Player;
import objects.Trailer;
import objects.TrailerWithRamp;

public class ObstacleFactory {
  static public final int buildingScreens = 2;
  static public final int bufferScreens = 0;

  static private final double minSpace = 500d;
  static private final double space = 1000d;

  private final double logicalLaneLength;

  public ObstacleFactory() {
    this.logicalLaneLength =
        Logic.getBounderies().getScreenHeight() * buildingScreens;
  }

  public Lane generateLane() {
    int tries = 10000;
    ArrayList<ArrayList<Obstacle>> result =
        new ArrayList<ArrayList<Obstacle>>(3);
    result.add(generatePath());
    result.add(generatePath());
    result.add(generatePath());
    while (tries-- > 0) {
      if (winningPath(result) == true) {
        return new Lane(result.get(0), result.get(1), result.get(2));
      }
      System.out.println("Got unwinning path, trying again");
      result.set((int)(Math.random() * 3), generatePath());
    }
    System.out.println("Could not generate a winning path");
    System.exit(-1);
    return null;
  }

  public ArrayList<Obstacle> generatePath() {
    ArrayList<Obstacle> path = new ArrayList<Obstacle>();
    double startY = Math.random() * logicalLaneLength * 0.5;
    while (startY < logicalLaneLength) {
      Obstacle newObstacle = generateObstacle();
      if (startY + newObstacle.getLength() < logicalLaneLength) {
        newObstacle.setY(startY);
        startY += newObstacle.getLength() + minSpace + Math.random() * space;
        path.add(newObstacle);
      } else {
        break;
      }
    }
    return path;
  }

  public boolean winningPath(ArrayList<ArrayList<Obstacle>> lanes) {
    GameObject testObj =
        new GameObject(Lane.PATH.LEFT, 0, Player.playerLength * 2);
    while (testObj.getY() + testObj.getLength() < logicalLaneLength) {
      int pointOfContacts = 0;
      for (int laneIndex = 0; laneIndex < lanes.size(); laneIndex++) {
        for (int j = 0; j < lanes.get(laneIndex).size(); j++) {
          if (lanes.get(laneIndex).get(j) instanceof Trailer) {
            Obstacle trailer = lanes.get(laneIndex).get(j);
            // System.out.println("Testing touching");
            if (trailer.touches(testObj)) {
              pointOfContacts += 1;
              break;
            }
          }
        }
      }

      testObj.setY(testObj.getY() + Player.playerLength / 2.0d);
      // System.out.println("Point of contacts: " + pointOfContacts);
      if (pointOfContacts == 3) {
        return false;
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
      for (Obstacle obsCenter : lanes.get(1)) {
        if (obsCenter instanceof Trailer) {
          int touches = 0;
          for (Obstacle obsLeft : lanes.get(0)) {
            if (obsLeft instanceof Trailer) {
              if (obsCenter.touches(obsLeft)) {
                touches++;
                break;
              }
            }
          }

          for (Obstacle obsRight : lanes.get(2)) {
            if (obsRight instanceof Trailer) {
              if (obsCenter.touches(obsRight)) {
                touches++;
                break;
              }
            }
          }

          if (touches == 2) {
            return false;
          }
        }
      }
    }
    return true;
  }

  // public Obstacle generateObstacle() { return new Trailer(Lane.PATH.LEFT); }
  public Obstacle generateObstacle() {
    return new TrailerWithRamp(Lane.PATH.LEFT);
  }
}
