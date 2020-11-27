package main;

import function.FractalFunction;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

import javax.naming.Binding;
import javax.script.Bindings;
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

    int iMin = 0;
    int iMax = 10;

    float sX = 0;
    float sY = 0;

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
            app.zoom = (float) zoomSlider.getValue();
            app.changed = true;
        });
        borderF.setOnMouseReleased(t -> {
            app.border = (float) borderF.getValue();
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
            sX = (float) e.getX();
            sY = (float) e.getY();
        }
    }

    public void mouseReleased(MouseEvent e){
        if (!paintToggle.isSelected()){
            System.out.println("move");
            double mX = (float)((sX-e.getX())/canvas.getWidth())*(2);
            double mY = (float)((sY-e.getY())/canvas.getWidth())*(2);
            float z = (float) Math.pow(10, app.zoom);
            app.xPos+=mX/z;
            app.yPos+=mY/z;
            System.out.println(app.yPos + ":" + app.xPos);
            app.changed = true;
        } else {
            zoom(app.xPos, app.yPos);
            float cX = (float) ((e.getX() / (double) canvas.getWidth()) * (xMaxZoom - xMinZoom) + xMinZoom);
            float cY = (float) ((e.getY() / (double) canvas.getHeight()) * (yMaxZoom - yMinZoom) + yMinZoom);
            app.fractalFunction.addN(new float[]{cX, cY});
            System.out.println("pointed");
        }
    }

    private void zoom( double mX, double mY){
        float z = (float) Math.pow(10, app.zoom);

        xMaxZoom = mX + 1/Math.max(0.01f, z);
        xMinZoom = mX - 1/Math.max(0.01f, z);
        yMaxZoom = mY + 1/Math.max(0.01f, z);
        yMinZoom = mY - 1/Math.max(0.01f, z);
    }
}
