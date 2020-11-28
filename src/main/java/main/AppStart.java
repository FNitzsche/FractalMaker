package main;

import function.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppStart extends Application {

    FractalFunction fractalFunction = new FractalFunction(this);
    FXMLLoad screen = new FXMLLoad("/main/FractalScreen.fxml", new FractalScreenCon(this));
    Renderer renderer = new Renderer(this);
    public double zoom = 0;

    public double xPos = 0.5f;
    public double yPos = 0;
    public int reps = 10;
    public double border = 5f;
    boolean changed = false;

    public double startReel = 0;
    public double startIm = 0;


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(screen.getScene());
        stage.setOnCloseRequest(t -> {
            renderer.endRendering();
            screen.getController(FractalScreenCon.class).stopPainting();
        });
        fractalFunction.addN(new double[]{0, 0});
        fractalFunction.addN(new double[]{0, 0});
        renderer.colorize(20, 12);
        renderer.startRendering((int)screen.getController(FractalScreenCon.class).canvas.getWidth(), (int)screen.getController(FractalScreenCon.class).canvas.getHeight());
        stage.show();

    }

    public void comTest(){
        Produkt pr = new Produkt(new ReeleZahl(2, true));
        pr.addFactor(new KomplexeZahl(3, true));
        pr.addFactor(new KomplexeZahl(0, true));
        //Zahl z = pr.shortenInPlace();
        //System.out.println(z);
        System.out.println(pr);
        System.out.println(pr.isNull);
        System.out.println(pr.fest);
        SummenKlammer kl = new SummenKlammer(pr);
        kl.addProdukt(new Produkt(new ReeleZahl(5, true)));
        System.out.println(kl);
        kl.shortenInPlace();
        System.out.println(kl);
    }

}
