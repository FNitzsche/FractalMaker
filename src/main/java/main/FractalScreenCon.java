package main;

import function.FractalFunction;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;

import javax.imageio.ImageIO;
import javax.naming.Binding;
import javax.script.Bindings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FractalScreenCon {

    AppStart app;

    @FXML
    TextField function;
    @FXML
    Canvas canvas;
    @FXML
    ToggleButton paintToggle;
    @FXML
    TextField iterF;
    @FXML
    Slider borderF;
    @FXML
    Slider zoomSlider;
    @FXML
    Button clear;
    @FXML
    Slider itShow;
    @FXML
    Slider minIt;
    @FXML
    TextField sRe;
    @FXML
    TextField sIm;
    @FXML
    Button render;

    int iMin = 0;
    int iMax = 10;

    double sX = 0;
    double sY = 0;

    double xMaxZoom, xMinZoom, yMaxZoom, yMinZoom;

    ScheduledExecutorService exe;

    public FractalScreenCon(AppStart appStart){
        app = appStart;
    }

    public void initialize(){
        function.textProperty().bind(app.fractalFunction.functionString);
        startPainting();
        clear.setOnAction(t -> app.fractalFunction.clearN());
        zoomSlider.setOnMouseReleased(t -> {
            app.zoom = (double) zoomSlider.getValue();
            app.changed = true;
        });
        borderF.setOnMouseReleased(t -> {
            app.border = (double) borderF.getValue();
            app.changed = true;
        });
        minIt.setOnMouseReleased(t -> {
            iMin = (int)minIt.getValue();
            iMax = iMin+(int)itShow.getValue();
        });
        itShow.setOnMouseReleased(t -> {
            iMin = (int)minIt.getValue();
            iMax = iMin+(int)itShow.getValue();
        });
        iterF.setOnAction(t -> {
            int r = Integer.parseInt(iterF.getText());
            app.reps = r;
            minIt.setMax(r);
            itShow.setMax(r);
            app.changed = true;
        });
        sRe.setOnAction(t -> {
            app.startReel = Double.parseDouble(sRe.getText());
            app.changed = true;
        });
        sIm.setOnAction(t -> {
            app.startIm = Double.parseDouble(sIm.getText());
            app.changed = true;
        });
        render.setOnAction(t -> saveRender());
    }

    public void startPainting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                canvas.getGraphicsContext2D().drawImage(app.renderer.getImage(iMin, iMax, (int)canvas.getWidth(), (int)canvas.getHeight()), 0, 0);
            }
        };
        exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleWithFixedDelay(runnable, 200, 50, TimeUnit.MILLISECONDS);
    }

    public void stopPainting(){
        if (exe != null) {
            exe.shutdown();
        }
    }

    public void mousePressed(MouseEvent e){
        if (!paintToggle.isSelected()){
            System.out.println("press");
            sX = (double) e.getX();
            sY = (double) e.getY();
        }
    }

    public void mouseReleased(MouseEvent e){
        if (!paintToggle.isSelected()){
            System.out.println("move");
            double mX = (double)((sX-e.getX())/canvas.getWidth())*(2);
            double mY = (double)((sY-e.getY())/canvas.getWidth())*(2);
            double z = (double) Math.pow(10, app.zoom);
            app.xPos+=mX/z;
            app.yPos+=mY/z;
            System.out.println(app.yPos + ":" + app.xPos);
            app.changed = true;
        } else {
            zoom(app.xPos, app.yPos);
            double cX = (double) ((e.getX() / (double) canvas.getWidth()) * (xMaxZoom - xMinZoom) + xMinZoom);
            double cY = (double) ((e.getY() / (double) canvas.getHeight()) * (yMaxZoom - yMinZoom) + yMinZoom);
            app.fractalFunction.addN(new double[]{cX, cY});
            System.out.println("pointed");
        }
    }

    private void zoom( double mX, double mY){
        double z = (double) Math.pow(10, app.zoom);

        xMaxZoom = mX + 1/Math.max(0.01f, z);
        xMinZoom = mX - 1/Math.max(0.01f, z);
        yMaxZoom = mY + 1/Math.max(0.01f, z);
        yMinZoom = mY - 1/Math.max(0.01f, z);
    }

    public void saveRender(){
        app.renderer.endRendering();
        stopPainting();
        app.renderer.renderImage(2160, 2160, true);
        File theDir = new File("G:/VideoSaveFractal");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        String p = theDir.getAbsolutePath() + "\\fractal" + System.currentTimeMillis() + ".mp4";
        VideoWriter videoWriter = new VideoWriter(p, VideoWriter.fourcc('x', '2','6','4'), 25, new Size(2160, 2160));
        for (int i = 0; i < app.reps; i++){
            minIt.setValue(i);
            iMin = (int)minIt.getValue();
            iMax = iMin+(int)itShow.getValue();
            Image img = app.renderer.getImage(iMin, iMax, 2160, 2160);
            videoWriter.write(imageToMat(img));
        }
        videoWriter.release();
        System.out.println("saved");
        app.renderer.startRendering((int)canvas.getWidth(), (int)canvas.getHeight());
        startPainting();
    }

    public Mat imageToMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }

}
