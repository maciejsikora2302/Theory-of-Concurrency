import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Mandelbrot{
    public void perPixelSolution(int MAX_ITER, double ZOOM, int threads){
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        Set<Future<Object>> tasks = new HashSet<>();
        MandelbrotTable table = new MandelbrotTable();
        int numberOfTasks = 0;
        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 600; j++) {
                Runnable task = new PerPixel(table, i, j, MAX_ITER, ZOOM);
                tasks.add((Future<Object>) pool.submit(task));
                numberOfTasks++;
            }
        }

        int numberOfTasksCheck = 0;
        int loopRepeated = 1;
        while (true) {
            loopRepeated++;
            for (Future<Object> task : tasks) {
                if (task.isDone()) {
                    numberOfTasksCheck++;
                }
            }
            if (numberOfTasksCheck == numberOfTasks) {
                break;
            } else {
                numberOfTasksCheck = 0;
                System.out.printf("Doing while loop for the %s time.\n", loopRepeated);
            }
        }


        table.drawPicture();
        table.setVisible(true);
        pool.shutdown();
    }

    public static void main(String[] args){
        final int MAX_ITER = 570;
        final double ZOOM = 150;
//        perPixelSolution(MAX_ITER, ZOOM, 12);

    }
}