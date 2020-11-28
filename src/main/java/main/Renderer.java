package main;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Renderer {

    AppStart app;

    int[][] image = null;
    int oX, oY;

    double xMaxZoom, xMinZoom, yMaxZoom, yMinZoom;

    ScheduledExecutorService exe = null;

    public Renderer(AppStart appStart){
        app = appStart;
    }

    private void zoom( double mX, double mY){
        double z = (double) Math.pow(10, app.zoom);

        xMaxZoom = mX + 1/Math.max(0.01f, z);
        xMinZoom = mX - 1/Math.max(0.01f, z);
        yMaxZoom = mY + 1/Math.max(0.01f, z);
        yMinZoom = mY - 1/Math.max(0.01f, z);
    }

    public void render(int px, int py, double x, double y, int reps, double border, boolean save){
            zoom(x, y);
            if (px != oX || py != oY || image == null) {
                image = new int[px][py];
                oX = px;
                oY = py;
            }
            L:
            for (int i = 0; i < px; i++) {
                for (int j = 0; j < py; j++) {
                    double cX = (double) ((i / (double) px) * (xMaxZoom - xMinZoom) + xMinZoom);
                    double cY = (double) ((j / (double) py) * (yMaxZoom - yMinZoom) + yMinZoom);
                    int iter = app.fractalFunction.calculatePoint(new double[]{cX, cY}, reps, border);
                    image[i][j] = iter;
                    if (app.fractalFunction.changed || app.changed){
                        break L;
                    }
                }
            }
    }

    public void startRendering(int px, int py){
        exe = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                renderImage(px, py, false);
                //System.out.println("rendered");
            }
        };
        exe.scheduleWithFixedDelay(runnable, 100, 500, TimeUnit.MILLISECONDS);
    }

    public void endRendering(){
        if (exe != null) {
            exe.shutdown();
        }
    }



    public Image getImage(int iterMin, int iterMax, int x, int y){
        WritableImage wimg = new WritableImage(x, y);
        if (image != null){
            int[][] cp = Arrays.copyOf(image, image.length);
            //System.out.println(cp.length);
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (i < cp.length && j < cp[0].length) {
                        int iter = cp[i][j];
                        //System.out.println(iter);
                        if (iter >= iterMin && iter <= iterMax) {
                            double v = (iter - iterMin) / (double)(iterMax - iterMin);
                            v = Math.max(0, Math.min(1, v));
                            wimg.getPixelWriter().setColor(i, j, Color.color(v, 0, v));
                        } else if (iter == -1) {
                            wimg.getPixelWriter().setColor(i, j, Color.color(0, 0, 0));
                        } else {
                            wimg.getPixelWriter().setColor(i, j, Color.color(0, 0, 0));
                        }
                    }
                }
            }
        }
        return wimg;
    }

    public void renderImage(int px, int py, boolean save){
        if (app.fractalFunction.changed || app.changed || save) {
            app.changed = false;
            app.fractalFunction.changed = false;
            app.fractalFunction.createFunction();
            if (app.fractalFunction.function != null) {
                System.out.println(app.fractalFunction.function);
                app.fractalFunction.function.shortenInPlace();
                System.out.println(app.fractalFunction.function);
            }
            render(px, py, app.xPos, app.yPos, app.reps, app.border, save);
        }

    }

}
