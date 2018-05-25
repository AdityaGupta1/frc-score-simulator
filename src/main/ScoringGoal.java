package main;

import rules.ScoringRule;

public class ScoringGoal {
    private String name;
    private int amount = 0;
    private ScoringRule[] rules;
    
    public ScoringGoal(String name, ScoringRule... rules) {
        this.name = name;
        this.rules = rules;
    }

    public void score(int amount) {
        this.amount += amount;
    }

    public int getScore() {
        int score = 0;

        for (ScoringRule rule : rules) {
            score += rule.getScore(amount);
        }

        return score;
    }
}
