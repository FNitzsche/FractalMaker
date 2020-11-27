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
    public int reps = 5;
    public float border = 5;


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(screen.getScene());
        stage.setOnCloseRequest(t -> renderer.endRendering());
        stage.show();
    }
}
