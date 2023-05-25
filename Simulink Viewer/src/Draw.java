import AnnotatedClasses.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class Draw {

    
    public static void DrawBlocks(Pane pane, ArrayList<Block> blocks){
        for (Block block : blocks) {
            Rectangle frame = new Rectangle(block.getCenter().getX() - Block.default_length/2, block.getCenter().getY() - Block.default_length/2, Block.default_length, Block.default_length);
            frame.setFill(Color.WHITE);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(2);
            Rectangle shadow = new Rectangle(block.getCenter().getX() - Block.default_length/2 - 2, block.getCenter().getY() - Block.default_length/2 - 2, Block.default_length + 4, Block.default_length + 4);
            shadow.setFill(Color.TRANSPARENT);
            shadow.setStroke(Color.SKYBLUE);
            shadow.setStrokeWidth(3);
            ImageView img = getIcon(block);
            Text text = getText(block);
            pane.getChildren().addAll(frame,
            shadow,
            img,
            text); 

        } 
    }

    public static void DrawLines(Pane pane, ArrayList<Ln> lines, Sys system){
        for (Ln line : lines) {
            ArrayList<Point2D> points = line.getPoints(system.getId_CenterPoint_Map());
            for(int i = 0; i < points.size()-1; i++){
                Line l = new Line(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY());
                l.setStroke(Color.BLACK);
                l.setStrokeWidth(1.5);
                l.setFill(Color.BLACK);
                Line glow = new Line(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY());
                glow.setStroke(Color.SKYBLUE);
                glow.setStrokeWidth(3.5);
                glow.setFill(Color.TRANSPARENT);
                pane.getChildren().addAll(glow,
                l);            }
            if(line.hasDest()){
                pane.getChildren().add(createTriangle(points.get(points.size() - 1), false));
            }
        }
    }


    public static void DrawBranches(Pane pane, ArrayList<Ln> lines, Sys system){
        for (Ln l : lines) {
            ArrayList<Point2D> Line_points = l.getPoints(system.getId_CenterPoint_Map());
            ArrayList<Branch> branches = l.getBranches();
            if(branches == null) continue;
            for(Branch b : branches){
                ArrayList<Point2D> points = b.getPoints(system.getId_CenterPoint_Map(), Line_points.get(Line_points.size()-1));
                for(int i = 0; i < points.size()-1; i++){
                    Line line = new Line(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY());
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(1.5);
                    line.setFill(Color.BLACK);
                    Line glow = new Line(points.get(i).getX(), points.get(i).getY(), points.get(i+1).getX(), points.get(i+1).getY());
                    glow.setStroke(Color.SKYBLUE);
                    glow.setStrokeWidth(3.5);
                    glow.setFill(Color.TRANSPARENT);
                    pane.getChildren().addAll(glow,
                    line);
                }
                Point2D p1 = points.get(points.size() - 2);
                Point2D p2 = points.get(points.size() - 1);
                if(p1.getX() < p2.getX()){
                    pane.getChildren().add(createTriangle(points.get(points.size() - 1), false));
                 
                }
                else{
                    pane.getChildren().add(createTriangle(points.get(points.size() - 1), true)); 
                }
            }
            
        }

    }


    private static ImageView getIcon(Block block){

        Image Icon = new Image("images/"+block.getName()+".png");
        ImageView imageView = new ImageView(Icon);
        imageView.setFitHeight(225);
        imageView.setFitWidth(225);
        imageView.setScaleX(0.15);
        imageView.setScaleY(0.15);
        imageView.setLayoutY(block.getCenter().getY() - 112);
        imageView.setLayoutX(block.getCenter().getX() - 112);
        return imageView;
    }

    private static Text getText(Block block) {
        Text text = new Text(block.getCenter().getX() - 35, block.getCenter().getY() + 30, block.getName());
        text.setFont(new Font(11));
        text.setStyle(
                "-fx-font: bold 11px Arial; -fx-fill:black;"
        );
       text.setTextAlignment(TextAlignment.CENTER);
       text.setTextOrigin(VPos.CENTER);
       text.setWrappingWidth(70);

       return text;

    }

    private static Polygon createTriangle(Point2D endPoint, boolean reverse) {
        Polygon triangle = new Polygon();
        double endX;
        double endY = endPoint.getY();
        triangle.setFill(Color.BLACK);        
        triangle.setStroke(Color.BLACK);

        if(reverse){
            endX = endPoint.getX() + 22;
            triangle.getPoints().addAll(endX + 7, endY - 5, endX, endY, endX + 7, endY + 5);
        }
        else{
            endX = endPoint.getX() - 20.5;
            triangle.getPoints().addAll(endX - 7, endY + 5, endX, endY, endX - 7, endY - 5); 
        }

        return triangle;
    }

}
