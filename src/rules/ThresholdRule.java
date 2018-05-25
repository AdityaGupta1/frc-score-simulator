package rules;

import main.Main;

import java.util.HashMap;

public class ThresholdRule extends ScoringRule {
    private boolean added = false;

    public ThresholdRule(HashMap<String, Integer> in, HashMap<String, Integer> out) {
        super(in, out, null);
    }

    public void simulate() {
        if (added) {
            return;
        }

        if (Main.getGameObjects().has(in)) {
            Main.addGameObjects(out);
            added = true;
        }
    }
}
