package rules;

import main.Main;

import java.util.HashMap;

public class ScoreRule extends ScoringRule {
    public ScoreRule(HashMap<String, Integer> add) {
        super(null, null, add);
    }

    public void simulate() {
        Main.addGameObjects(add);
    }
}
