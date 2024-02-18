package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class App extends JPanel implements KeyListener
{
	private int characterX = 100;
	private int characterY = 100;
	private final int characterSize = 40;
	private final int borderSize = 20;
	private final int borderOffset = 50;
	private List<Blast> blasts = new ArrayList<>();
	private final int blastSpeed = 5;
	private boolean upPressed, downPressed, leftPressed, rightPressed;
	private BufferedImage playerImage;

	public App() {
		setPreferredSize(new Dimension(400, 400));
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);

		try {
			// Load player image from file
			playerImage = ImageIO.read(new File("C:\\CSE 274 Codes\\CSE201\\src\\player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Blast movement thread
		Thread blastThread = new Thread(() -> {
			while (true) {
				moveBlasts();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		});
		blastThread.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw player
		if (playerImage != null) {
			// is getting called every frame
			g.drawImage(playerImage, characterX, characterY, characterSize, characterSize, null);
		} else {
			// is getting called every frame
			g.setColor(Color.RED);
			g.fillRect(characterX, characterY, characterSize, characterSize);
		}
		// Draw border
		g.setColor(Color.BLACK);
		g.drawRect(borderOffset, borderOffset, getWidth() - 2 * borderOffset, getHeight() - 2 * borderOffset);
		// Draw blasts
		g.setColor(Color.BLUE);
		for (Blast blast : blasts) {
			g.fillRect(blast.getX(), blast.getY(), blast.getSize(), blast.getSize());
		}
	}

	private void moveCharacter() {
		int dx = 0, dy = 0;
		if (upPressed && !downPressed) {
			dy = -5;
		} else if (downPressed && !upPressed) {
			dy = 5;
		}
		if (leftPressed && !rightPressed) {
			dx = -5;
		} else if (rightPressed && !leftPressed) {
			dx = 5;
		}

		int newX = characterX + dx;
		int newY = characterY + dy;
		// Check if new position is within the border
		if (newX >= borderOffset && newX + characterSize <= getWidth() - borderOffset && newY >= borderOffset
				&& newY + characterSize <= getHeight() - borderOffset) {
			characterX = newX;
			characterY = newY;
			repaint();
		}
	}

	private void moveBlasts() {
		for (int i = blasts.size() - 1; i >= 0; i--) {
			Blast blast = blasts.get(i);
			blast.move();
			if (blast.getX() < borderOffset || blast.getX() + blast.getSize() > getWidth() - borderOffset
					|| blast.getY() < borderOffset || blast.getY() + blast.getSize() > getHeight() - borderOffset) {
				blasts.remove(i);
			}
		}
	}

	private void shootBlast() {
		blasts.add(new Blast(characterX + characterSize / 2, characterY + characterSize / 2));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			upPressed = true;
			break;
		case KeyEvent.VK_DOWN:
			downPressed = true;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = true;
			break;
		case KeyEvent.VK_SPACE:
			shootBlast();
			break;
		}
		moveCharacter();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			upPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			downPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = false;
			break;
		}
	} // hi

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private class Blast {
		private int x;
		private int y;
		private final int size = 5;

		public Blast(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void move() {
			x += blastSpeed;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getSize() {
			return size;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Character Game");
		App game = new App();
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
