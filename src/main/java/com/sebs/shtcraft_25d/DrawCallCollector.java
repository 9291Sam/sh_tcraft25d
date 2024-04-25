package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

public class DrawCallCollector {
	private Graphics g;
	private double cameraX, cameraY;
	private double cameraWidth, cameraHeight;
	private double screenPxX, screenPxY;
	private ArrayList<Pair<Integer, Function<Graphics, Boolean>>> functions;

	public DrawCallCollector(Graphics newG, double newCameraX, double newCameraY, double newCameraWidth,
			double newCameraHeight, double newScreenPxX, double newScreenPxY) {
		this.g = newG;
		this.cameraX = newCameraX;
		this.cameraY = newCameraY;
		this.cameraWidth = newCameraWidth;
		this.cameraHeight = newCameraHeight;
		this.screenPxX = newScreenPxX;
		this.screenPxY = newScreenPxY;

		this.functions = new ArrayList<>();
	}

	// TODO: make *World and *ScreenSpace
	// takes in normalized coordinates
	public void drawFilledRectangleScreen(double xScreen, double yScreen, Integer layer, double width,
			double height, Color color) {
		Function<Graphics, Boolean> f = (Graphics g) -> {
			g.setColor(color);
			g.fillRect((int) (xScreen * this.screenPxX), (int) (yScreen * this.screenPxY),
					(int) (width * this.screenPxX), (int) (height * this.screenPxY));

			return true;
		};

		this.functions.add(Pair.of(layer, f));
	}

	public void drawTexturedRectangleScreen(double xScreen, double yScreen, Integer layer, double width,
			double height, Image img) {
		Function<Graphics, Boolean> f = (Graphics g) -> {
			g.drawImage(img, (int) (xScreen * this.screenPxX), (int) (yScreen * this.screenPxY),
					(int) (width * this.screenPxX), (int) (height * this.screenPxY), null);

			return true;
		};

		this.functions.add(Pair.of(layer, f));
	}

	public void drawFilledRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height,
			Color color) {
		double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0,
				this.screenPxX);
		double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0,
				this.screenPxY);

		double widthPx = this.screenPxX * width / this.cameraWidth;
		double heightPx = this.screenPxY * height / this.cameraHeight;

		if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX || yScreenPx <= -heightPx
				|| yScreenPx >= this.screenPxY) {
			return;
		}

		// thank you java very cool, I love not having ZSTs
		Function<Graphics, Boolean> f = (Graphics g) -> {
			g.setColor(color);
			g.fillRect((int) xScreenPx, (int) yScreenPx, (int) widthPx, (int) heightPx);

			return true;
		};

		this.functions.add(Pair.of(layer, f));
	}

	public void drawTexturedRectangleWorld(double xWorld, double yWorld, Integer layer, double width, double height,
			Image img) {
		double xScreenPx = Renderer.map(xWorld - this.cameraX, -this.cameraWidth / 2, this.cameraWidth / 2, 0.0,
				this.screenPxX);
		double yScreenPx = Renderer.map(yWorld - this.cameraY, this.cameraHeight / 2, -this.cameraHeight / 2, 0.0,
				this.screenPxY);

		double widthPx = this.screenPxX * width / this.cameraWidth;
		double heightPx = this.screenPxY * height / this.cameraHeight;

		if (xScreenPx <= -widthPx || xScreenPx >= this.screenPxX || yScreenPx <= -heightPx
				|| yScreenPx >= this.screenPxY) {
			return;
		}

		Function<Graphics, Boolean> f = (Graphics g) -> {
			this.g.drawImage(img, (int) xScreenPx, (int) yScreenPx, (int) widthPx, (int) heightPx, null);

			return true;
		};

		this.functions.add(Pair.of(layer, f));
	}

	public void dispatch() {
		this.functions.sort((l, r) -> l.getLeft().compareTo(r.getLeft()));

		for (Pair<Integer, Function<Graphics, Boolean>> f : this.functions) {
			Function<Graphics, Boolean> func = f.getRight();

			func.apply(this.g);
		}
	}

}