package rules;

import java.util.TreeMap;

public class TableRule implements ScoringRule {
    private TreeMap<Integer, Integer> table = new TreeMap<>();

    public TableRule(int... table) {
        for (int i = 0; i < table.length; i += 2) {
            this.table.put(table[i], table[i + 1]);
        }
    }

    public int getScore(int gameObjects) {
        int[] tableInputs = new int[table.keySet().size()];
        System.arraycopy(table.keySet().toArray(), 0, tableInputs, 0, tableInputs.length);

        if (gameObjects < tableInputs[0]) {
            return 0;
        }

        for (int i = 0; i < tableInputs.length - 1; i++) {
            if (i < tableInputs[i + 1]) {
                return table.get(tableInputs[i]);
            }
        }

        return table.get(tableInputs[tableInputs.length - 1]);
    }
}
