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
        float z = (float) Math.pow(10, app.zoom);

        xMaxZoom = mX + 1/Math.max(0.01f, z);
        xMinZoom = mX - 1/Math.max(0.01f, z);
        yMaxZoom = mY + 1/Math.max(0.01f, z);
        yMinZoom = mY - 1/Math.max(0.01f, z);
    }

    public void render(int px, int py, float x, float y, int reps, float border){
        zoom(x, y);
        if (px != oX || py != oY || image == null) {
            image = new int[px][py];
        }
        for (int i = 0; i < px; i++){
            for (int j = 0; j < py; j++){
                float cX = (float) ((i/(double)px)*(xMaxZoom-xMinZoom)+xMinZoom);
                float cY = (float) ((j/(double)py)*(yMaxZoom-yMinZoom)+yMinZoom);
                int iter = app.fractalFunction.calculatePoint(new float[]{cX, cY}, reps, border);
                image[i][j] = iter;
            }
        }
    }

    public void startRendering(int px, int py){
        exe = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                render(px, py, app.xPos, app.yPos, app.reps, app.border);
            }
        };
        exe.scheduleWithFixedDelay(runnable, 100, 20, TimeUnit.MILLISECONDS);
    }

    public void endRendering(){
        if (exe != null) {
            exe.shutdown();
        }
    }



    public Image getImage(int iterMin, int iterMax, int x, int y){
        int[][] cp = Arrays.copyOf(image, image.length);
        WritableImage wimg = new WritableImage(x, y);
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                int iter = cp[i][j];
                if (iter >= iterMin && iter <= iterMax){
                    float v = (iter -iterMin)/(iterMax-iterMin);
                    wimg.getPixelWriter().setColor(i, j, Color.color(v, 0, v));
                } else if (iter == -1){
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, 0.4, 0));
                } else {
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, 0, 0));
                }
            }
        }
        return wimg;
    }

}
