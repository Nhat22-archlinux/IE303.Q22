import java.util.Random;
import java.util.Scanner;

public class Main {
    public static double approxArea(double r, int N) {
        Random rand = new Random();
        int n = 0;

        for (int i = 0; i < N; i++) {
            double x = (rand.nextDouble() * 2 * r) - r;
            double y = (rand.nextDouble() * 2 * r) - r;

            if (x * x + y * y <= r * r) {
                n++;
            }
        }

        return ((double) n / N) * (4 * r * r);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double r = sc.nextDouble();

        int N = 1000000; // số điểm
        System.out.println(approxArea(r, N));
    }
}
