package game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class MadRunner extends Application {
  /* static vars */
  static public final double minScreenY = 500;
  static public final double minScreenX = 500;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene theScene = new Scene(root);
    stage.setScene(theScene);

    stage.setResizable(false); // disable resizing the screen

    Canvas canvas =
        new Canvas(1024, 768); // We are only using canvas to draw objects

    stage.setMinWidth(minScreenX);
    stage.setMaxWidth(minScreenY);
    stage.setMaximized(true);
    canvas.requestFocus();

    root.getChildren().add(canvas);

    AnimationTimer myGameLoop = new GameLoop(canvas);
    myGameLoop.start();

    stage.show();
  }

  public static void main(String[] args) { launch(); }
}
