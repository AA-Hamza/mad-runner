package objects;

import java.util.ArrayList;
import logic.Logic;

public class Block {
  /* Enums */
  static public enum Lane { LEFT, CENTER, RIGHT }

  /* static vars */
  public static final double speed = 4d;

  /* private vars */
  private double currentY;
  private final ArrayList<Obstacle> left;
  private final ArrayList<Obstacle> center;
  private final ArrayList<Obstacle> right;

  /* Constructors */
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
      obs.setLane(Lane.LEFT);
    }
    // Update center
    for (Obstacle obs : center) {
      obs.setY(obs.getY() + currentY);
      obs.setLane(Lane.CENTER);
    }
    // Update right
    for (Obstacle obs : right) {
      obs.setY(obs.getY() + currentY);
      obs.setLane(Lane.RIGHT);
    }
  }

  /* Getters */
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

  public ArrayList<Obstacle> getLeftLane() { return this.left; }
  public ArrayList<Obstacle> getCenterLane() { return this.center; }
  public ArrayList<Obstacle> getRightLane() { return this.right; }

  /* Utils */
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
}
