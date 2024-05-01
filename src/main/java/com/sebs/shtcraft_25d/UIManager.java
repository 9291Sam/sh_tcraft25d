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
    AudioManager audioManager;
    
    boolean triggered = false;
    
    public UIManager(Renderer renderer, AudioManager audioManager_)
    {
        this.renderer = renderer;  
        this.audioManager = audioManager_;
    }
    
    @Override
    public void tick(double deltaTime) {
    	if (this.percent <= 0) {
    		
    		if (!this.triggered)
    		{
    			this.audioManager.stopAll();
    		}
    		
    		this.triggered = true;
    		run = false;
    		if (rand == 1) {
    			this.audioManager.playSound("[shylily]-womp-womp-sound-effect-made-with-Voicemod.wav");
    		} else {
    			this.audioManager.playSound("womp-womp-womp-made-with-Voicemod.wav");
    		}
    		
        	this.renderer._Kill();
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
