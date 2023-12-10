import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Solution {
    List<List<Pair>> beginTracker;
    Map<String, List<Integer>> nameMap;
    Map<Integer, Set<String>> valueMap;

    public Solution() {
        this.beginTracker = new ArrayList<>();
        this.nameMap = new HashMap<>();
        this.valueMap = new HashMap<>();
    }

    public void begin() {
        beginTracker.add(new ArrayList<>());
    }

    public void set(String name, int value) {
        if (beginTracker.isEmpty()) {
            beginTracker.add(new ArrayList<>());
        }
        List<Pair> list = beginTracker.get(beginTracker.size() - 1);
        list.add(new Pair(name, value));

        List<Integer> values = nameMap.getOrDefault(name, new ArrayList<>());
        values.add(value);
        nameMap.put(name, values);

        Set<String> set = valueMap.getOrDefault(value, new HashSet<>());
        set.add(name);
        valueMap.put(value, set);
        for (Map.Entry<String, List<Integer>> entry : nameMap.entrySet()) {
            System.out.println("name map:  key " + entry.getKey() +" " + entry.getValue().toString());
        }

        for (Map.Entry<Integer, Set<String>> entry : valueMap.entrySet()) {
            System.out.println("value map:  key " + entry.getKey() +" " + entry.getValue().toString());
        }
    }

    public Integer get(String name) {
        if (!nameMap.containsKey(name)) return null;
        List<Integer> list = nameMap.get(name);
        return list.get(list.size() - 1);
    }

    public void unset(String name) {
        if (!nameMap.containsKey(name)) return ;
        List<Integer> list = nameMap.get(name);
        for (int val : list) {
            Set<String> set = valueMap.get(val);
            set.remove(name);
            if (set.isEmpty()) valueMap.remove(val);
        }
        nameMap.remove(name);
    }

    public void rollback() {
        if (beginTracker.isEmpty()) return ;
        List<Pair> pairs = beginTracker.get(beginTracker.size() - 1);
        for (Pair pair : pairs) {
            List<Integer> values = nameMap.getOrDefault(pair.name, null);
            if (values != null) {
                for (int i = values.size() - 1; i >= 0; i--) {
                    if (values.get(i) == pair.value) {
                        values.remove(i);
//                        continue;
                    }
                }

                if (values.size() == 0) {
                    nameMap.remove(pair.name);
                }
            }

            Set<String> names = valueMap.getOrDefault(pair.value, null);
            if (names != null) {
                names.remove(pair.name);
                if (names.isEmpty()) {
                    valueMap.remove(pair.value);
                }
            }

        }
    }

    public Integer numWithValue(int val) {
        if (!nameMap.containsKey(val)) return null;
        return nameMap.get(val).size();
    }

    public void end() {
        this.nameMap = new HashMap<>();
        this.valueMap = new HashMap<>();
        this.beginTracker = new ArrayList<>();
    }

}

class Pair {
    public String name;
    public int value;
    public Pair(String name, int value) {
        this.name = name;
        this.value = value;
    }
}



public class Main {



    public static void main(String[] args){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Solution solution = new Solution();
        bufferedReader.lines().forEach(s -> {
            String[] strings = s.split(" ");
            if (strings[0].equals("GET")) {
                Integer value = solution.get(strings[1]);
                System.out.println(value);

            } else if (strings[0].equals("BEGIN")) {
                solution.begin();
            } else if (strings[0].equals("SET")) {
                solution.set(strings[1], Integer.valueOf(strings[2]));
            } else if (strings[0].equals("NUMWITHVALUE")) {
                Integer value = solution.numWithValue(Integer.valueOf(strings[1]));
                System.out.println(value);
            }  else if (strings[0].equals("UNSET")) {
                solution.unset(strings[1]);;
            }  else if (strings[0].equals("ROLLBACK")) {
                solution.rollback();
            }
            else if (strings[0].equals("EDN")) {
                solution.end();
                try {
                    bufferedReader.close();
                } catch (IOException e) {

                }

            }
        });
        System.out.println("Hello world!");
    }
}