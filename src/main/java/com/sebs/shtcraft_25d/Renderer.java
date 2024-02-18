package com.sebs.shtcraft_25d;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;


import javax.swing.JPanel;

public class Renderer extends JPanel implements KeyListener
{
	private Set<Renderable> entities;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}

interface Renderable
{
	/// @return nullable
	public Point getPosition();
}