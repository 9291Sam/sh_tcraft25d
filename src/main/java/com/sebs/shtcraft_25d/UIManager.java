package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class UIManager implements Renderer.Entity
{
    private Renderer renderer;
    boolean run = true;
    double percent = 1.0;
    int rand = (int) Math.random() * 100;
    int checker = 0;
    
    public UIManager(Renderer renderer)
    {
        this.renderer = renderer;   
    }
    
    @Override
    public void tick(double deltaTime) {
    	if (this.percent <= 0) {
    		run = false;
    		if (rand == 1) {
    			File f = new File("[shylily]-womp-womp-sound-effect-made-with-Voicemod.wav");
    		    AudioInputStream audioIn = null;
    			try {
    				audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
    			} catch (MalformedURLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (UnsupportedAudioFileException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}  
    		    Clip clip = null;
    			try {
    				clip = AudioSystem.getClip();
    			} catch (LineUnavailableException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		    try {
    				clip.open(audioIn);
    			} catch (LineUnavailableException | IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		} else {
    			File f = new File("womp-womp-womp-made-with-Voicemod.wav");
    		    AudioInputStream audioIn = null;
    			try {
    				audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
    			} catch (MalformedURLException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (UnsupportedAudioFileException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}  
    		    Clip clip = null;
    			try {
    				clip = AudioSystem.getClip();
    			} catch (LineUnavailableException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		    try {
    				clip.open(audioIn);
    			} catch (LineUnavailableException | IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		    checker++;
    		}
    	}
    }
    
    public void increaseByPercent(double p)
    {
    	this.percent = Utils.clamp(this.percent + p, 0.0, 1.0);
    }

    @Override
    public void draw(DrawCallCollector d)
    {
    	// TODO: do dynamic things
    	
    	
    	
    	d.drawFilledRectangleScreen(0.0, 0.95, 24, 0.25, 0.1, Color.GRAY);
    	d.drawFilledRectangleScreen(0.025, 0.9675, 25, 0.2 * this.percent, 0.025, Color.RED);
 
    }

}
