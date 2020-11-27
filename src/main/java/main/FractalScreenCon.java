package main;

import function.FractalFunction;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.naming.Binding;
import javax.script.Bindings;

public class FractalScreenCon {

    AppStart app;

    @FXML
    TextField function;

    public FractalScreenCon(AppStart appStart){
        app = appStart;
    }

    public void initialize(){
        function.textProperty().bind(app.fractalFunction.functionString);
    }
}
