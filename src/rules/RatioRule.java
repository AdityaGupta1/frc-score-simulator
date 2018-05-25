package rules;

public class RatioRule implements ScoringRule {
    private int gameObjects;
    private int score;

    public RatioRule(int gameObjects, int score) {
        this.gameObjects = gameObjects;
        this.score = score;
    }

    public int getScore(int gameObjects) {
        // int division --> truncate
        return (gameObjects / this.gameObjects) * score;
    }
}
