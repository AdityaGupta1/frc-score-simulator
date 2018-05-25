package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import rules.GameObjects;
import rules.ScoringRule;
import rules.ThresholdRule;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private static final ArrayList<ScoringRule> autonRules = new ArrayList<>();
    private static final ArrayList<Time> autonTimes = new ArrayList<>();
    private static int numAutonRules = 0;
    private static final ArrayList<ScoringRule> teleopRules = new ArrayList<>();
    private static final ArrayList<Time> teleopTimes = new ArrayList<>();
    private static int numTeleopRules = 0;

    private static final GameObjects gameObjects = new GameObjects();
    // gameObjects that isn't subtracted from, for threshold rule purposes
    private static final GameObjects thresholdGameObjects = new GameObjects();
    // only threshold rules, so they can be reset before re-simulating
    private static final ArrayList<ThresholdRule> thresholdRules = new ArrayList<>();

    private static final XYChart.Series autonSeries = new XYChart.Series();
    private static final XYChart.Series teleopSeries = new XYChart.Series();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Score Simulator");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("Score");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setId("chart");
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Score Simulation");
        lineChart.setAnimated(false);
        autonSeries.setName("Autonomous");
        teleopSeries.setName("Teleoperated");
        lineChart.getData().addAll(autonSeries, teleopSeries);

        Pane pane = new Pane();
        pane.getChildren().add(lineChart);

        Button simulate = new Button("Simulate");
        simulate.setOnAction(event -> simulate());

        HBox chartBox = new HBox(pane, simulate);
        chartBox.setId("chartBox");
        chartBox.setSpacing(10);
        chartBox.setAlignment(Pos.CENTER_LEFT);

        Label rulesLabel = new Label("Rules");
        rulesLabel.setId("rulesLabel");

        GridPane table = new GridPane();
        table.setId("table");
        Label autonomousLabel = new Label("Autonomous");
        autonomousLabel.getStyleClass().add("modeLabel");
        Label teleoperatedLabel = new Label("Teleoperated");
        teleoperatedLabel.getStyleClass().add("modeLabel");
        table.add(autonomousLabel, 0, 0);
        table.add(teleoperatedLabel, 1, 0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        table.getColumnConstraints().addAll(col1, col2);

        Button addRule = new Button("Add");

        ComboBox<String> ruleTypeChooser = new ComboBox<>();
        ruleTypeChooser.getItems().addAll("score", "threshold", "ratio");

        ComboBox<String> stageChooser = new ComboBox<>();
        stageChooser.getItems().addAll("autonomous", "teleoperated", "both");

        HBox ruleChooser = new HBox(addRule, new Label(" a "), ruleTypeChooser, new Label(" rule for "), stageChooser);
        ruleChooser.setId("ruleChooser");
        ruleChooser.setAlignment(Pos.CENTER_LEFT);

        TextField inField = new TextField();
        inField.setId("inField");
        Label inLabel = new Label("In:");
        HBox inBox = new HBox(inLabel, inField);
        inBox.setSpacing(5);

        TextField outField = new TextField();
        outField.setId("outField");
        Label outLabel = new Label("Out:");
        HBox outBox = new HBox(outLabel, outField);
        outBox.setSpacing(5);

        TextField addField = new TextField();
        addField.setId("addField");
        ComboBox<String> rateChooser = new ComboBox<>();
        rateChooser.setId("rateChooser");
        rateChooser.getItems().addAll("at", "every");
        TextField secondsField = new TextField();
        secondsField.setId("secondsField");
        secondsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                secondsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        HBox addBox = new HBox(new Label("Add"), addField, rateChooser, secondsField, new Label(" seconds"));
        addBox.setSpacing(5);

        VBox ruleAdder = new VBox(ruleChooser, inBox, outBox, addBox);
        ruleAdder.setId("ruleAdder");
        ruleAdder.setSpacing(5);

        ruleTypeChooser.setOnAction(event -> {
            String ruleType = ruleTypeChooser.getValue();

            boolean isThreshold = ruleType.equals("threshold");
            boolean isScore = ruleType.equals("score");

            addBox.setVisible(isScore);
            addBox.setManaged(isScore);
            inLabel.setText(isThreshold ? "Upon reaching" : "In:");
            outLabel.setText(isThreshold ? "Add" : "Out:");

            inBox.setVisible(!isScore);
            inBox.setManaged(!isScore);
            outBox.setVisible(!isScore);
            outBox.setManaged(!isScore);
        });

        addRule.setOnAction(event -> {
            String ruleType = ruleTypeChooser.getValue();
            String in = inField.getText().toLowerCase();
            String out = outField.getText().toLowerCase();
            String add = addField.getText().toLowerCase();
            ScoringRule rule = ScoringRule.parse(in, out, add, ruleType);
            int seconds = 0;
            Time time = null;
            try {
                seconds = Integer.parseInt(secondsField.getText());
                time = new Time(seconds, rateChooser.getValue().equals("at"));
            } catch (Exception exception) { /* do nothing */ }
            String mode = stageChooser.getValue();

            switch(mode) {
                case "autonomous":
                    autonRules.add(rule);
                    autonTimes.add(time);
                    break;
                case "teleoperated":
                    teleopRules.add(rule);
                    teleopTimes.add(time);
                    break;
                case "both":
                    autonRules.add(rule);
                    autonTimes.add(time);
                    teleopRules.add(rule);
                    teleopTimes.add(time);
            }

            if (rule instanceof ThresholdRule) {
                thresholdRules.add((ThresholdRule) rule);
            }

            int[] columnIndices = mode.equals("both") ? new int[]{0, 1} : new int[]{mode.equals("autonomous") ? 0 : 1};

            boolean isThreshold = ruleType.equals("threshold");
            boolean isScore = ruleType.equals("score");

            for (int index : columnIndices) {
                VBox newRule = new VBox(new Label(ruleType + " rule"));
                if (!isScore) {
                    newRule.getChildren().addAll(new Label((isThreshold ? "Upon reaching " : "In: ") + in),
                            new Label((isThreshold ? "Add " : "Out: ") + out));
                } else {
                    newRule.getChildren().add(new Label("Add " + add + " " + rateChooser.getValue() + " " + seconds + " seconds"));
                }
                newRule.getStyleClass().add("rule");
                table.add(newRule, index, index == 0 ? ++numAutonRules : ++numTeleopRules);
            }
        });

        VBox vbox = new VBox(chartBox, rulesLabel, table, ruleAdder);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private static void reset() {
        gameObjects.clear();
        thresholdGameObjects.clear();

        for (ThresholdRule rule : thresholdRules) {
            rule.reset();
        }

        autonSeries.getData().clear();
        teleopSeries.getData().clear();
    }

    private static int getPoints() {
        return gameObjects.get("points");
    }

    private static void simulate() {
        reset();

        GameMode auton = new GameMode(autonRules, autonTimes);
        GameMode teleop = new GameMode(teleopRules, teleopTimes);

        for (int t = 0; t <= 15; t++) {
            auton.simulate(t);
            autonSeries.getData().add(new XYChart.Data(t, getPoints()));
            System.out.println(getPoints());
        }

        for (int t = 15; t <= 150; t++) {
            teleop.simulate(t);
            teleopSeries.getData().add(new XYChart.Data(t, getPoints()));
            System.out.println(getPoints());
        }
    }

    public static void addGameObjects(HashMap<String, Integer> newGameObjects) {
        gameObjects.add(newGameObjects);
        thresholdGameObjects.add(newGameObjects);
    }

    public static GameObjects getGameObjects() {
        return gameObjects;
    }
}
