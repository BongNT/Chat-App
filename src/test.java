import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        String s = "a@b";
        String[] a = s.split("@");
        System.out.println(a.length);
        for (String x : a){
            System.out.println(x);
        }
    }
}
