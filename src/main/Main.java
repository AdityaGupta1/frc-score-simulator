package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private static boolean isAuton = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Score Simulator");

        Pane pane = new Pane();
        pane.getChildren().add(new ImageView("http://i0.kym-cdn.com/photos/images/newsfeed/001/090/170/192.png"));

        Label rulesLabel = new Label("Rules");
        rulesLabel.setId("rulesLabel");

        GridPane table = new GridPane();
        table.setId("table");
        table.add(new Label("Autonomous"), 1, 0);
        table.add(new Label("Teleoperated"), 2, 0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        ColumnConstraints col3 = new ColumnConstraints();
        col2.setPercentWidth(40);
        table.getColumnConstraints().addAll(col1, col2, col3);

        Button addRule = new Button("Add");
        addRule.setOnAction(event -> {
            // stuff
        });

        ComboBox<String> ruleChooser = new ComboBox<>();
        ruleChooser.getItems().addAll("ratio", "threshold", "table");

        ComboBox<String> timeChooser = new ComboBox<>();
        timeChooser.getItems().addAll("autonomous", "teleoperated");

        HBox ruleType = new HBox(addRule, new Label(" a "), ruleChooser, new Label(" rule for "), timeChooser);
        ruleType.setId("ruleChooser");
        ruleType.setAlignment(Pos.CENTER_LEFT);

        VBox ruleAdder = new VBox(ruleType);
        ruleAdder.setId("ruleAdder");

        VBox vbox = new VBox(pane, rulesLabel, table, ruleAdder);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
