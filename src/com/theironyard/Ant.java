package com.theironyard;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by holdenhughes on 12/2/15.
 */
public class Ant {
    double x;
    double y;
    Color antColor = Color.BLACK;

    public Ant(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
