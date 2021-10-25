import java.util.LinkedList;
import java.util.List;

class Pair{
    int a;
    int b;
    public Pair(int aa,int bb){
    a = aa;
    b = bb;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}

public class Test01 {
    public static void main(String[] args) {
        List<Pair> list = new LinkedList<>();
        list.add(new Pair(213,321));
        list.add(new Pair(123,233));
        list.add(new Pair(333,421));
        Pair p = list.get(1);
        p.a = 3;
        System.out.println(list);
    }
}
