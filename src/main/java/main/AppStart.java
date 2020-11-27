package main;

import function.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppStart extends Application {

    FractalFunction fractalFunction = new FractalFunction();
    FXMLLoad screen = new FXMLLoad("/main/FractalScreen.fxml", new FractalScreenCon(this));
    Renderer renderer = new Renderer(this);
    public float zoom = 0;

    public float xPos = 0;
    public float yPos = 0;
    public int reps = 10;
    public float border = 5f;
    boolean changed = false;


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(screen.getScene());
        stage.setOnCloseRequest(t -> {
            renderer.endRendering();
            screen.getController(FractalScreenCon.class).stopPainting();
        });
        fractalFunction.addN(new float[]{0, 0});
        fractalFunction.addN(new float[]{0, 0});
        renderer.startRendering((int)screen.getController(FractalScreenCon.class).canvas.getWidth(), (int)screen.getController(FractalScreenCon.class).canvas.getHeight());
        stage.show();
    }
}
