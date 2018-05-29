package main;

import rules.ScoringRule;

import java.util.ArrayList;

public class GameMode {
    private ArrayList<ScoringRule> rules = new ArrayList<>();
    private ArrayList<Time> times = new ArrayList<>();

    public GameMode(ArrayList<ScoringRule> rules, ArrayList<Time> times) {
        this.rules = rules;
        this.times = times;
    }

    public void simulate(int t) {
        for (int i = 0; i < rules.size(); i++) {
            ScoringRule rule = rules.get(i);
            Time time = times.get(i);

            if (time == null || (time.isSingleton() && t == time.getTime()) || (!time.isSingleton() && t % time.getTime() == 0 && t != 0 && t != 15)) {
                rule.simulate();
            }
        }
    }
}
