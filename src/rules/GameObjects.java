package rules;

import java.util.HashMap;

public class GameObjects {
    private HashMap<String, Integer> gameObjects = new HashMap<>();

    public void add(HashMap<String, Integer> newObjects) {
        for (String gameObject : newObjects.keySet()) {
            if (gameObjects.containsKey(gameObject)) {
                gameObjects.put(gameObject, gameObjects.get(gameObject) + newObjects.get(gameObject));
            } else {
                gameObjects.put(gameObject, newObjects.get(gameObject));
            }
        }
    }

    public int get(String gameObject) {
        return gameObjects.getOrDefault(gameObject, 0);
    }

    public void put(String gameObject, int amount) {
        gameObjects.put(gameObject, amount);
    }

    public boolean has(HashMap<String, Integer> minus) {
        for (String gameObject : minus.keySet()) {
            if (get(gameObject) < minus.get(gameObject)) {
                return false;
            }
        }

        return true;
    }

    public boolean subtract(HashMap<String, Integer> minus) {
        if (!has(minus)) {
            return false;
        }

        for (String gameObject : minus.keySet()) {
            put(gameObject, get(gameObject) - minus.get(gameObject));
        }

        return true;
    }

    public void clear() {
        gameObjects.clear();
    }

    public String toString() {
        String string = "[";

        for (String key : gameObjects.keySet()) {
            string.concat(key + ":" + gameObjects.get(key) + ", ");
        }

        if (string.equals("[")) {
            return "[]";
        }

        return string.substring(0, string.length() - 2) + "]";
    }
}
