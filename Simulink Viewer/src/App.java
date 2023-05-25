import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import AnnotatedClasses.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulink Viewer");

        FileChooser fileChooser = new FileChooser();

        Button button = new Button("Select File");
        button.setOnAction(e -> {           
            try {
                Pane pane = new Pane();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                String ex = ExtractSystem(selectedFile);
                StringReader xmlfile = new StringReader(ex);
                JAXBContext jaxbContext = JAXBContext.newInstance(Sys.class, Property.class, Block.class, Ln.class, Branch.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                // initialize the system
                Sys system = (Sys) unmarshaller.unmarshal(xmlfile);
                
                ArrayList<Block> blocks = system.getBlocks();
                ArrayList<Ln> lines = system.getLines();
                Draw.DrawLines(pane, lines, system);
                Draw.DrawBranches(pane, lines, system);
                Draw.DrawBlocks(pane, blocks);
                Scene scene = new Scene(pane, 1300, 600);
                primaryStage.setScene(scene);
                
            } catch (IOException | JAXBException e1) {
                e1.printStackTrace();
            }
        });
        Rectangle frame = new Rectangle();
        frame.setWidth(600);
        frame.setHeight(100);
        frame.setArcWidth(15);
        frame.setArcHeight(15);                
        frame.setFill(Color.SKYBLUE);
        frame.setStroke(Color.BLACK);
        frame.setLayoutX(150);
        frame.setLayoutY(180);
        button.setLayoutX(420);
        button.setLayoutY(450);
        Pane root = new Pane();
        Text text = new Text(260, 250, "SimuLink Viewer");
        text.setFont(new Font(100));   
        text.setStyle(
                "-fx-font: bold 45px Arial; -fx-fill:Black;"
        );
        text.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(
            button,
            frame,
            text);
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static String ExtractSystem(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(fileInputStream)) {
            String data = scanner.useDelimiter("\\A").next();
            Pattern pattern = Pattern.compile("<System>(.*?)</System>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                return "<System>\n" + matcher.group(1) + "\n</System>";
            } else {
                return null;
            }
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}

