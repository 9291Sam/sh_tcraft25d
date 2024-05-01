package com.sebs.shtcraft_25d;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioManager
{
	private ConcurrentHashMap<String, AudioData> audio;
	
	public AudioManager() {
		this.audio = new ConcurrentHashMap<>();
	}
	
	public void playSound(String name)
	{
		if (!this.audio.contains(name))
		{
			try
			{
				this.audio.put(name, new AudioData(name));
			}
			catch (IllegalArgumentException e)
			{
				return;
			}
		}
		
		this.audio.get(name).startNewClip();		
	}
	
	public void stopAll()
	{
		this.audio.forEach((s, d) -> d.stopAll());
	}
	
	private class AudioData
	{
		AudioData(String name_) throws IllegalArgumentException
		{
			try
			{
				this.file = new File(name_);
			} 
			catch (Exception e)
			{
				System.out.println("Unable To Find File");
				throw new IllegalArgumentException();
			}
			
			try
			{
				this.stream = AudioSystem.getAudioInputStream(this.file.toURI().toURL());
			} catch (Exception e)
			{
				System.out.println("Unable To load audio stream");
				throw new IllegalArgumentException();
			}
			
			this.activeClips = new ArrayList<Clip>();
		}
		
		public void startNewClip()
		{
			Clip clip = null;
			
 			try {
 				clip = AudioSystem.getClip();
 			} catch (LineUnavailableException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    try {
 				clip.open(this.stream);
 				clip.start();
 			} catch (LineUnavailableException | IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
			
 		    this.activeClips.add(clip);
 		    
 		    this.activeClips = new ArrayList<>(this.activeClips.stream()
	    		.filter(c -> c.isActive())
	    		.collect(Collectors.toList()));
		}
		
		public void stopAll()
		{
			this.activeClips.forEach(c -> c.stop());
		}
		
		File file;
		AudioInputStream stream;
		ArrayList<Clip> activeClips;
	}
}
