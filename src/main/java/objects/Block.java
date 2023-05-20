package objects;

import java.util.ArrayList;
import logic.Logic;

public class Block {
  static public enum Lane { LEFT, CENTER, RIGHT }
  static private final double speed = 4d;
  private double currentY;
  public final ArrayList<Obstacle> left;
  public final ArrayList<Obstacle> center;
  public final ArrayList<Obstacle> right;

  public Block(ArrayList<Obstacle> left, ArrayList<Obstacle> center,
               ArrayList<Obstacle> right) {
    if (left == null || center == null || right == null) {
      System.out.println("received null lanes");
      System.exit(-1);
    }

    this.left = left;
    this.center = center;
    this.right = right;

    this.currentY = -1 *
                    (logic.ObstacleFactory.bufferScreens +
                     logic.ObstacleFactory.buildingScreens) *
                    Logic.getBounderies().getScreenHeight();

    // Update left
    for (Obstacle obs : left) {
      obs.setY(obs.getY() + currentY);
      obs.setLanePath(Lane.LEFT);
    }
    // Update center
    for (Obstacle obs : center) {
      obs.setY(obs.getY() + currentY);
      obs.setLanePath(Lane.CENTER);
    }
    // Update right
    for (Obstacle obs : right) {
      obs.setY(obs.getY() + currentY);
      obs.setLanePath(Lane.RIGHT);
    }
  }

  public void frameUpdate() {
    currentY += speed;
    // Update left
    for (Obstacle obs : left) {
      obs.addY(speed);
    }
    // Update center
    for (Obstacle obs : center) {
      obs.addY(speed);
    }
    // Update right
    for (Obstacle obs : right) {
      obs.addY(speed);
    }
  }

  public double getCurrentY() { return currentY; }

  public ArrayList<Obstacle> getLane(Lane lane) {
    switch (lane) {
    case LEFT:
      return this.left;
    case CENTER:
      return this.center;
    case RIGHT:
      return this.right;
    default:
      return null;
    }
  }

  // static public final int maxNumberOfObstacles = 10;
  // static private final int screens = -2;
  // static private final double minSpace = 100.0d;
  // static private final double speed = 0.5d;
  //
  // private final double yStart;
  // private double currentY;
  // private final PATH path;
  // private Queue<Obstacle> obstacles;
  //
  // public Lane(PATH path, ArrayList<Obstacle> source) {
  //   for (int i = 0; i < source.size(); i++) {
  //     obstacles.add
  //   }
  //   this.yStart = screens * Logic.getBounderies().getScreenHeight();
  //   this.currentY = 0;
  //   this.path = path;
  //   obstacles = new LinkedList<Obstacle>();
  // }
  //
  // @Override
  // public Iterator<GameObject> iterator() {
  //   return (Iterator)obstacles.iterator();
  // }
  //
  // public void frameUpdate() {
  //   if (currentY < -1 * Logic.getBounderies().getScreenHeight()) {
  //     currentY += speed;
  //   }
  //   System.out.println(currentY);
  //
  //   Iterator<Obstacle> it = obstacles.iterator();
  //   while (it.hasNext()) {
  //     it.next().addY(speed);
  //     ;
  //   }
  // }
  //
  // public void cleanOffFrame() {
  //   Obstacle first = null;
  //   while ((first = obstacles.peek()) != null) {
  //     if (first.getY() > Logic.getBounderies().getScreenHeight()) {
  //       obstacles.remove();
  //     } else {
  //       break;
  //     }
  //   }
  // }
}
