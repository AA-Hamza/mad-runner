package logic;

import java.util.ArrayDeque;
import java.util.Deque;
import objects.GameObject;

public class ObstacleFactory {
  static private final int screens = -2;
  static private final int numberOfLanes = 3;
  private final double startY;
  private Deque<GameObject>[] lanes;

  public ObstacleFactory() {
    this.startY = screens * Logic.getBounderies().getScreenHeight();
    lanes = new Deque[numberOfLanes];
    for (int i = 0; i < 3; i++) {
      lanes[i] = new ArrayDeque<GameObject>();
    }
  }
}
