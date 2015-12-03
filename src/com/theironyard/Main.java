package com.theironyard;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int ANT_COUNT = 100;

    ArrayList<Ant> ants;
    long lastTimeStamp = 0;

    ArrayList<Ant> createAnts(){
        ArrayList<Ant> ants = new ArrayList<>();
        for (int i = 0; i < ANT_COUNT; i++){
            Random r = new Random();
            ants.add(new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT)));
        }
        return ants;
    }

    void drawAnts(GraphicsContext context){
        context.clearRect(0,0, WIDTH, HEIGHT);
        for (Ant ant :ants){
            /*if (aggravateAnts(ant)) {
                context.setFill(Color.RED);
            } else {
                context.setFill(Color.BLACK);
            }*/
            context.setFill(ant.antColor);
            context.fillOval(ant.x, ant.y, 5, 5);

        }

    }
/*
    boolean aggravateAnts(Ant ant) {
        for (Ant ant1 : ants) {
            if (ant1 != ant) {
                double distance = Math.sqrt(Math.pow((ant1.x - ant.x), 2) + Math.pow((ant1.y - ant.y), 2));
                if (distance <= Math.abs(10)) {
                    return true;
                }
            }
        }
        return false;
    }
    */

    //another method

    Ant aggravateAnts(Ant ant) {
        ArrayList<Ant> madAnts = new ArrayList<>();

        for (Ant ant1 : ants) {
            if (Math.abs(ant.x - ant1.x) <= 10 && Math.abs(ant.y - ant1.y) <= 10) {
                madAnts.add(ant);
            }
        }
        if (madAnts.size() > 1) {
            ant.antColor = Color.GREEN;
        } else {
            ant.antColor = Color.BLACK;
        }
        return ant;
    }

    double randomStep(){
        return Math.random() * 2 - 1;
    }

    Ant moveAnt(Ant ant){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ant.x += randomStep();
        ant.y += randomStep();
        return ant;
    }

    void updateAnts(){
        ants = ants.parallelStream()
                .map(this::moveAnt)
                .map(this::aggravateAnts)
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
    }

    int fps(long now){
        double diff = now -lastTimeStamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1/diffSeconds);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = (Canvas) scene.lookup("#canvas");
        Label fpsLabel = (Label) scene.lookup("#fps");
        GraphicsContext context = canvas.getGraphicsContext2D();

        primaryStage.setTitle("Ants");
        primaryStage.setScene(scene);
        primaryStage.show();

        ants = createAnts();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                fpsLabel.setText(fps(now)+"");
                lastTimeStamp = now;
                updateAnts();
                drawAnts(context);
            }
        };
        timer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
