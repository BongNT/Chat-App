import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        String s = "a@b3@c";
        String[] a = s.split("@",2);
        System.out.println(a.length);
        for (String x : a){
            System.out.println(x);
        }
    }
}
