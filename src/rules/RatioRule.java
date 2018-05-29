package rules;

import main.Main;

import java.util.HashMap;

public class RatioRule extends ScoringRule {
    public RatioRule(HashMap<String, Integer> in, HashMap<String, Integer> out) {
        super(in, out, null);
    }

    public void simulate() {
        while (Main.getGameObjects().subtract(in)) {
            System.out.println("test");
            Main.addGameObjects(out);
        }
    }
}
