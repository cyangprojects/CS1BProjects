import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class KochSnowFlake extends Application
{
   public void start(Stage primaryStage)
   {
      KochSnowFlakePane trianglePane = new KochSnowFlakePane();
      TextField tfOrder = new TextField();
      tfOrder.setOnAction(e -> trianglePane.setOrder(Integer.parseInt(tfOrder.getText())));
      tfOrder.setPrefColumnCount(4);
      tfOrder.setAlignment(Pos.BOTTOM_RIGHT);
      
      
      HBox hBox = new HBox(10);
      
      hBox.getChildren().addAll(new Label("Enter an order: "), tfOrder);
      hBox.setAlignment(Pos.CENTER);

      BorderPane borderPane = new BorderPane();
      borderPane.setCenter(trianglePane);
      borderPane.setBottom(hBox);

      Scene scene = new Scene(borderPane, 200, 210);
      primaryStage.setTitle("Koch Snowflake Fractals"); // Set the stage title
      primaryStage.setScene(scene); // Place the scene in the stage
      primaryStage.show();
      // Create a scene and place it in the stages

   }

   /****** Embedded class ****/
   /** Pane for displaying fractal */
   static class KochSnowFlakePane extends Pane
   {

      private int order;

      public KochSnowFlakePane()
      {
         order = 0;
      }

      public boolean setOrder(int n)
      {
         if (n >= 0)
         {
            order = n;
            this.getChildren().clear();
            paint();
            return true;
         }
         return false;
      }

      protected void paint()
      {
         Point2D p1 = new Point2D(getWidth() / 2, 5);
         Point2D p2 = new Point2D(5, getHeight() - 5);
         Point2D p3 = new Point2D(getWidth() - 5, getHeight() - 5);
         displayKochSnowFlake(order, p1, p2);
         displayKochSnowFlake(order, p2, p3);
         displayKochSnowFlake(order, p3, p1);
      }

      private void displayKochSnowFlake(int order, Point2D p1, Point2D p2)
      {
         if (order == 0)
         {
            Line line = new Line(p1.getX(), p1.getY() * 0.8, p2.getX(), p2.getY() * 0.8);
            this.getChildren().add(line);
         }

         else
         {

            // Some of the math to get you down the right path
            double deltaX = p2.getX() - p1.getX();
            double deltaY = p2.getY() - p1.getY();
            Point2D x = new Point2D(p1.getX() + deltaX / 3, p1.getY() + deltaY / 3);
            Point2D y = new Point2D(p1.getX() + deltaX * 2 / 3, p1.getY() + deltaY * 2 / 3);
            Point2D z = new Point2D(
                  (p1.getX() + p2.getX()) / 2 + Math.cos(Math.toRadians(30)) * (p1.getY() - p2.getY()) / 3,
                  (p1.getY() + p2.getY()) / 2 + Math.cos(Math.toRadians(30)) * (p2.getX() - p1.getX()) / 3);
            displayKochSnowFlake(order - 1, p1, x);
            displayKochSnowFlake(order - 1, x, z);
            displayKochSnowFlake(order - 1, z, y);
            displayKochSnowFlake(order - 1, y, p2);

         }
      }
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}
