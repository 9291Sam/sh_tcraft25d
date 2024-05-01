package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

public class DrawCallCollector {
    private Graphics g; // Graphics object to draw on
    private double cameraX, cameraY; // Camera position in world coordinates
    private double cameraWidth, cameraHeight; // Camera dimensions
    private double screenPxX, screenPxY; // Screen dimensions in pixels
    private ArrayList<Pair<Integer, Function<Graphics, Boolean>>> functions; // List of draw functions

    // Constructor
    public DrawCallCollector(Graphics newG, double newCameraX, double newCameraY, double newCameraWidth,
            double newCameraHeight, double newScreenPxX, double newScreenPxY) {
        this.g = newG;
        this.cameraX = newCameraX;
        this.cameraY = newCameraY;
        this.cameraWidth = newCameraWidth;
        this.cameraHeight = newCameraHeight;
        this.screenPxX = newScreenPxX;
        this.screenPxY = newScreenPxY;

        this.functions = new ArrayList<>(); // Initialize list of draw functions
    }

    // Method to draw a filled rectangle in screen space
    public void drawFilledRectangleScreen(double xScreen, double yScreen, Integer layer, double width,
            double height, Color color) {
        // Create a draw function and add it to the list
        Function<Graphics, Boolean> f = (Graphics g) -> {
            g.setColor(color);
            g.fillRect((int) (xScreen * this.screenPxX), (int) (yScreen * this.screenPxY),
                    (int) (width * this.screenPxX), (int) (height * this.screenPxY));

            return true;
        };

        this.functions.add(Pair.of(layer, f)); // Add draw function to the list
    }

    // Method to draw a textured rectangle in screen space
    public void drawTexturedRectangleScreen(double xScreen, double yScreen, Integer layer, double width,
            double height, Image img) {
        // Create a draw function and add it to the list
        Function<Graphics, Boolean> f = (Graphics g) -> {
            g.drawImage(img, (int) (xScreen * this.screenPxX), (int) (yScreen * this.screenPxY),
                    (int) (width * this.screenPxX), (int) (height * this.screenPxY), null);

            return true;
        };

        this.functions.add(Pair.of(layer, f)); // Add draw function to the list
    }

    // Method to draw a filled rectangle in world space
    public void drawFilledRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height,
            Color color) {
        // Convert world coordinates to screen coordinates
        double xScreenPx = Utils.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0,
                this.screenPxX);
        double yScreenPx = Utils.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0,
                this.screenPxY);

        double widthPx = this.screenPxX * width / this.cameraWidth;
        double heightPx = this.screenPxY * height / this.cameraHeight;

        // Check if rectangle is outside the screen
        if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX || yScreenPx <= -heightPx
                || yScreenPx >= this.screenPxY) {
            return; // If outside, do not draw
        }

        // Create a draw function and add it to the list
        Function<Graphics, Boolean> f = (Graphics g) -> {
            g.setColor(color);
            g.fillRect((int) xScreenPx, (int) yScreenPx, (int) widthPx, (int) heightPx);

            return true;
        };

        this.functions.add(Pair.of(layer, f)); // Add draw function to the list
    }

    // Method to draw a textured rectangle in world space
    public void drawTexturedRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height,
            Image img) {
        // Convert world coordinates to screen coordinates
        double xScreenPx = Utils.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0,
                this.screenPxX);
        double yScreenPx = Utils.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0,
                this.screenPxY);

        double widthPx = this.screenPxX * width / this.cameraWidth;
        double heightPx = this.screenPxY * height / this.cameraHeight;

        // Check if rectangle is outside the screen
        if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX || yScreenPx <= -heightPx
                || yScreenPx >= this.screenPxY) {
            return; // If outside, do not draw
        }

        // Create a draw function and add it to the list
        Function<Graphics, Boolean> f = (Graphics g) -> {
            this.g.drawImage(img, (int) xScreenPx, (int) yScreenPx, (int) widthPx, (int) heightPx, null);

            return true;
        };

        this.functions.add(Pair.of(layer, f)); // Add draw function to the list
    }

    // Method to dispatch draw functions
    public void dispatch() {
        this.functions.sort((l, r) -> l.getLeft().compareTo(r.getLeft())); // Sort draw functions by layer

        for (Pair<Integer, Function<Graphics, Boolean>> f : this.functions) {
            Function<Graphics, Boolean> func = f.getRight(); // Get draw function
            func.apply(this.g); // Execute draw function
        }
    }
}
