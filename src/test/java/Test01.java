import javafx.util.Pair;

import java.nio.channels.Pipe;
import java.util.LinkedList;
import java.util.List;

public class Test01 {
    public static void main(String[] args) {
        List<Pair<String,String>> list = new LinkedList<>();
        list.add(new Pair<>("213","321"));
        list.add(new Pair<>("123","233"));
        list.add(new Pair<>("333","421"));
        System.out.println(list.contains(new Pair<>("123","231")));
        System.out.println(list.contains(new Pair<>("333","555")));
    }
}
