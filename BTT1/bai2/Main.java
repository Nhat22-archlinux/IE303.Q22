import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int N = 1000000;
        int n = 0;

        Random rand = new Random();

        for (int i = 0; i < N; i++) {
            double x = rand.nextDouble() * 2 - 1;
            double y = rand.nextDouble() * 2 - 1;

            if (x * x + y * y <= 1) {
                n++;
            }
        }

        double pi = 4.0 * n / N;
        System.out.println(pi);
    }
}
