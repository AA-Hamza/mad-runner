import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MadRunner extends Application {

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene theScene = new Scene(root);
    stage.setScene(theScene);

    stage.setResizable(false);

    Canvas canvas = new Canvas(1024, 768);

    canvas.requestFocus();
    root.getChildren().add(canvas);

    AnimationTimer myGameLoop = new GameLoop(canvas);
    myGameLoop.start();

    stage.show();
  }

  public static void main(String[] args) { launch(); }
}
