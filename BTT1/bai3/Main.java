import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class Point implements Comparable<Point> {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            if (this.x != other.x) return this.x - other.x;
            return this.y - other.y;
        }
    }

    static long cross(Point O, Point A, Point B) {
        return 1L * (A.x - O.x) * (B.y - O.y) - 1L * (A.y - O.y) * (B.x - O.x);
    }

    static List<Point> convexHull(Point[] pts) {
        int n = pts.length;
        if (n <= 1) return new ArrayList<>(Arrays.asList(pts));

        Arrays.sort(pts);

        List<Point> lower = new ArrayList<>();
        for (Point p : pts) {
            while (lower.size() >= 2 &&
                   cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        List<Point> upper = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            Point p = pts[i];
            while (upper.size() >= 2 &&
                   cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }

        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        lower.addAll(upper);

        return lower;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            pts[i] = new Point(x, y);
        }

        List<Point> hull = convexHull(pts);

        System.out.println(hull.get(0).x + " " + hull.get(0).y);
        for (int i = hull.size() - 1; i >= 1; i--) {
            System.out.println(hull.get(i).x + " " + hull.get(i).y);
        }
    }
}
