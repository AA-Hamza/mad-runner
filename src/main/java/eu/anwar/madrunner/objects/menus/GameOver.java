// package objects;
//
// import javafx.scene.paint.Color;
// import javafx.scene.text.TextAlignment;
// import logic.Logic;
//
// public class GameOver extends GamePause {
//   private int currentScore = 0;
//   public GameOver(double width, double length) {
//     super(width, length);
//     super.txt = "Game Over\nPress R for restart";
//     super.setColor(Color.RED);
//   }
//
//   public void setScore(int score) { this.currentScore = score; }
//
//   @Override
//   public void Draw() {
//     if (!this.isEnabled()) {
//       return;
//     }
//
//     super.Draw();
//     Logic.gc.setFill(Color.WHITE);
//     Logic.gc.setTextAlign(TextAlignment.CENTER);
//     Logic.gc.fillText("Current Score: " + this.currentScore, this.width / 2,
//                       this.length / 10 + Logic.gc.getFont().getSize(),
//                       this.width);
//   }
// }
package eu.anwar.madrunner.objects.menus;

import eu.anwar.madrunner.logic.Logic;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class GameOver extends GamePause
{
    public static String gameOverText = "Game Over\nPress R for restart";
    public static String scoreTxt = "Current Score: %d\nMax Score: %d";
    private int maxScore = 0;
    private int currentScore = 0;
    public GameOver()
    {
        super();
        super.centerTxt = gameOverText;
        super.setColor(Color.RED);
    }

    public void setScore(int score)
    {
        maxScore = Math.max(maxScore, score);
        this.currentScore = score;
    }

    @Override public void Draw()
    {
        if (!this.isEnabled())
        {
            return;
        }

        super.Draw();
        Logic.gc.setFill(Color.WHITE);
        Logic.gc.setTextAlign(TextAlignment.CENTER);
        Logic.gc.fillText(String.format(scoreTxt, currentScore, maxScore), this.width / 2,
                          this.length / 10 + Logic.gc.getFont().getSize(), this.width);
    }
}
