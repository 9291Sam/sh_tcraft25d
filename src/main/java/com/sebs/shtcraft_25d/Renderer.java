package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;


import javax.swing.JPanel;

public class Renderer extends JPanel implements KeyListener
{
	private Set<WeakReference<Renderable>> entities;
	
	public Renderer()
	{
		setPreferredSize(new Dimension(400, 400));
		setBackground(Color.WHITE);
		setFocusable(true);
		addKeyListener(this);
		
		this.entities = new HashSet<>();
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private static void force_gc() {
	    Object obj = new Object();
	    WeakReference ref = new WeakReference<Object>(obj);
	    obj = null;
	    
        while(ref.get() != null)
        {
	       System.gc();
        }
    }

}

interface Renderable
{
	/// @return nullable
	public Point getPosition();
}