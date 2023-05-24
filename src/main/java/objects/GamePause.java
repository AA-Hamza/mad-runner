// package objects;
//
// import javafx.scene.paint.Color;
// import javafx.scene.text.TextAlignment;
// import logic.Logic;
//
// public class GamePause extends GameObject {
//   protected String txt;
//   private Boolean enabled = false;
//   public GamePause(double width, double length) {
//     super(0, 0, width, length);
//     this.txt =
//         "Game Paused\nPress ESC to continue playing\nPress R for restart";
//     super.setColor(Color.BLACK);
//   }
//
//   public void enable() { this.enabled = true; }
//
//   public void disable() { this.enabled = false; }
//
//   public Boolean isEnabled() { return this.enabled; }
//   public void toggle() { this.enabled = !this.enabled; }
//
//   public void updateDeminsions(double width, double length) {
//     this.width = width;
//     this.length = length;
//   }
//
//   @Override
//   public void Draw() {
//     if (!enabled) {
//       return;
//     }
//     super.Draw();
//     Logic.gc.setFill(Color.WHITE);
//     Logic.gc.setTextAlign(TextAlignment.CENTER);
//     Logic.gc.fillText(txt, this.width / 2, this.length / 2, this.width);
//   }
// }
package objects;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.Logic;

public class GamePause extends GameObject {
  protected String centerTxt;
  private Boolean enabled = false;
  public GamePause() {
    super(0, 0, Logic.getBounderies().getScreenWidth(),
          Logic.getBounderies().getScreenHeight());
    this.centerTxt =
        "Game Paused\nPress ESC to continue playing\nPress R for restart";
    super.setColor(Color.BLACK);
  }

  public void enable() { this.enabled = true; }

  public void disable() { this.enabled = false; }

  public Boolean isEnabled() { return this.enabled; }
  public void toggle() { this.enabled = !this.enabled; }

  public void updateDeminsions(double width, double length) {
    this.width = width;
    this.length = length;
  }

  @Override
  public void Draw() {
    if (!enabled) {
      return;
    }
    this.width = Logic.getBounderies().getScreenWidth();
    this.length = Logic.getBounderies().getScreenHeight();
    super.Draw();
    Logic.gc.setFill(Color.WHITE);
    Logic.gc.setTextAlign(TextAlignment.CENTER);
    Logic.gc.fillText(centerTxt, this.width / 2, this.length / 2);
  }
}
