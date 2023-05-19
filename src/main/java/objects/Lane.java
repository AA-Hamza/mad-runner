package objects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import logic.Logic;

public class Lane implements Iterable<GameObject> {
  static public enum PATH { LEFT, CENTER, RIGHT }
  static public final int maxNumberOfObstacles = 10;
  static private final int screens = -2;
  static private final double minSpace = 100.0d;
  static private final double speed = 0.5d;

  private final double yStart;
  private double currentY;
  private final PATH path;
  private Queue<Obstacle> obstacles;

  public Lane(PATH path) {
    this.yStart = screens * Logic.getBounderies().getScreenHeight();
    this.currentY = 0;
    this.path = path;
    obstacles = new LinkedList<Obstacle>();
  }

  @Override
  public Iterator<GameObject> iterator() {
    return (Iterator)obstacles.iterator();
  }

  public void generateObstacles() {
    for (int i = obstacles.size(); i < maxNumberOfObstacles; i++) {
      if (currentY > yStart + minSpace) {
        Obstacle trailer = new Trailer(path, yStart);
        if (currentY - trailer.getLength() > yStart) {
          obstacles.add(trailer);
          trailer.setY(yStart + Math.random() *
                                    (currentY + trailer.getLength() - yStart));
          currentY = trailer.getY() - minSpace;
        }
      }
    }
  }

  public void frameUpdate() {
    if (currentY < -1 * Logic.getBounderies().getScreenHeight()) {
      currentY += speed;
    }
    System.out.println(currentY);

    Iterator<Obstacle> it = obstacles.iterator();
    while (it.hasNext()) {
      it.next().addY(speed);
      ;
    }
  }

  public void cleanOffFrame() {
    Obstacle first = null;
    while ((first = obstacles.peek()) != null) {
      if (first.getY() > Logic.getBounderies().getScreenHeight()) {
        obstacles.remove();
      } else {
        break;
      }
    }
  }
}
