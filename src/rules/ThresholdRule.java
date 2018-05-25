package rules;

public class ThresholdRule implements ScoringRule {
    private int gameObjects;
    private int score;

    public ThresholdRule(int gameObjects, int score) {
        this.gameObjects = gameObjects;
        this.score = score;
    }

    public int getScore(int gameObjects) {
        return gameObjects > this.gameObjects ? score : 0;
    }
}
