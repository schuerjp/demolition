package com.omniworks.demolition.managers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.omniworks.demolition.Assets;

public class SoundManager 
{

	private HashMap<String, Sound> sounds;
	private Music music;
	
	public SoundManager()
	{
		sounds = new HashMap<String, Sound>();
		
		setSounds();
	}
	
	public void setSounds()
	{
		addSound("brick_break",Assets.brickBreakSound);
		addSound("wood_break",Assets.woodBreakSound);
		addSound("score_sound",Assets.scoreSound);
		addSound("item_drop", Assets.itemDropSound);
	}
	
	public void addSound(String key, Sound sound)
	{		
		if(!sounds.containsKey(key))
		{
			sounds.put(key, sound);
		}
	}
	
	public void clear()
	{
		sounds.clear();
	}
	
	
	public HashMap<String, Sound> getSounds()
	{
		return sounds;
	}
	
	public Music music()
	{
		return this.music;
	}
	
	public void setMusic(Music music)
	{
		this.music = music;
	}
	
	public void setMusicVolume(float volume)
	{
		music.setVolume(volume);
	}
	
	public void setMusicLooping(boolean isLooping)
	{
		music.setLooping(isLooping);
	}
	
	public boolean isLooping()
	{
		return music.isLooping();
	}
	
	public boolean isPlaying()
	{
		return music.isPlaying();
	}
	
	public void startMusic()
	{
		music.play();
	}
	
	public void pauseMusic()
	{
		music.pause();
	}
	
	public void stopMusic()
	{
		music.stop();
	}
}
