package rules;

import java.util.HashMap;

public abstract class ScoringRule {
    protected HashMap<String, Integer> in;
    protected HashMap<String, Integer> out;
    protected HashMap<String, Integer> add;

    public ScoringRule(HashMap<String, Integer> in, HashMap<String, Integer> out, HashMap<String, Integer> add) {
        this.in = in;
        this.out = out;
        this.add = add;
    }

    public abstract void simulate();

    public static ScoringRule parse(String inString, String outString, String addString, String type) {
        switch(type) {
            case "score":
                return new ScoreRule(parseGameObjects(addString));
            case "threshold":
                return new ThresholdRule(parseGameObjects(inString), parseGameObjects(outString));
            case "ratio":
                return new RatioRule(parseGameObjects(inString), parseGameObjects(outString));
            default:
                return null;
        }
    }

    private static HashMap<String, Integer> parseGameObjects(String string) {
        HashMap<String, Integer> gameObjects = new HashMap<>();

        for (String gameObject : string.split(", ")) {
            String[] temp = gameObject.split("x ");
            gameObjects.put(temp[1], Integer.parseInt(temp[0]));
        }

        return gameObjects;
    }
}
