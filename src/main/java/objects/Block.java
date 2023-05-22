package objects;

import java.util.ArrayList;
import javafx.scene.image.Image;
import logic.Logic;

public class Block {
  static public enum Lane { LEFT, CENTER, RIGHT }
  static public final double speed = 4d;
  private double currentY;
  // private Image railway;
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

    // this.railway = new Image("file:src/main/java/assets/railway.png");

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
}
