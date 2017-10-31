package com.omniworks.demolition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class GameTimer
{
	private float currentTime;
	private float levelTime;
	private BitmapFont currentFont;
	private float scale;
	private float maxScale;
	private boolean isStarted;
	private boolean isComplete;
	private boolean isRunning;
	private boolean displayAllSeconds;
	private float warningTime;
	private float minutes;
	private float seconds;
	private float milliSec;
	private String minAsString;
	private String secAsString;
	private String milAsString;
	private Color fontColor;
	private float animateTime;
	private float animateDuration;
	private float deltaCount;
	
	private Vector2 minPos;
	private Vector2 secPos;
	private Vector2 milliPos;
	
	public GameTimer(float levelTime)
	{
		this.setLevelTime(levelTime);
		setCurrentTime(levelTime);
		calculateMinSec();
		setComplete(false);
		setStarted(false);
		setRunning(false);
		setDisplayAllSeconds(true);
		fontColor= new Color();
		fontColor.set(1f,1f,1f,1f);
		setWarningTime(5f);
		animateTime = 0f;
		animateDuration = .25f;
		maxScale = 1.70f;
		scale = 1.6f;
	}

	public void update(float deltaTime)
	{
		calculateMinSec();

		if(isStarted() && !isComplete())
		{
			subTime(deltaTime);
			
			if(currentTime > 0)
			{
				setRunning(true);
				setComplete(false);
				
				if(currentTime <= warningTime)
				{
					setFontColor(new Color(1f,0f,0f,1f));
					animateWarning(deltaTime);
				}
			}
			else
			{
				currentTime = 0;
				setRunning(false);
				setComplete(true);
			}
		}
		
	}
	
	public void animateWarning(float deltaTime)
	{
		animateTime += deltaTime;
		

		if((animateTime < animateDuration/2) && (scale < maxScale))
		{
			scale += 6*deltaTime;
		}
		else if((animateTime >= animateDuration/2) && (animateTime < animateDuration) && (scale > 1))
		{
			scale -= 6*deltaTime;
		}
		else if((animateTime > animateDuration) || (scale <= 1))
		{
			scale = 1;
		}
		
		if(animateTime >= 1)
		{
			animateTime = 0;
		}
		
		
	}
	
	private void calculateMinSec()
	{
		setMinutes((float)Math.floor(this.currentTime/60));
		
		if(displayAllSeconds)
		{
			setSeconds((float)(Math.floor(this.currentTime)));
		}
		else
		{
			setSeconds(this.currentTime%60);
		}
		
		setMilliSec(this.currentTime-(float)Math.floor(currentTime));
	}
	
	public void addTime(float time)
	{
		this.currentTime += time;
	}
	
	public void subTime(float time)
	{
		this.currentTime -= time;
	}
	
	public float currentTime()
	{
		return currentTime;
	}

	public void setCurrentTime(float currentTime)
	{
		this.currentTime = currentTime;
	}

	public float levelTime()
	{
		return levelTime;
	}

	public void setLevelTime(float levelTime)
	{
		this.levelTime = levelTime;
	}

	public BitmapFont currentFont()
	{
		return currentFont;
	}

	public void setCurrentFont(BitmapFont currentFont)
	{
		this.currentFont = currentFont;
	}

	public float scale()
	{
		return scale;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public float maxScale()
	{
		return maxScale;
	}

	public void setMaxScale(float maxScale)
	{
		this.maxScale = maxScale;
	}

	public boolean isStarted()
	{
		return isStarted;
	}

	public void setStarted(boolean isStarted)
	{
		this.isStarted = isStarted;
	}

	public boolean isComplete()
	{
		return isComplete;
	}

	public void setComplete(boolean isComplete)
	{
		this.isComplete = isComplete;
	}

	public boolean isRunning()
	{
		return isRunning;
	}

	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}

	public float warningTime()
	{
		return warningTime;
	}

	public void setWarningTime(float warningTime)
	{
		this.warningTime = warningTime;
	}

	public Vector2 minPos() {
		return minPos;
	}

	public void setMinPos(Vector2 minPos) {
		this.minPos = minPos;
	}

	public Vector2 secPos() {
		return secPos;
	}

	public void setSecPos(Vector2 secPos) {
		this.secPos = secPos;
	}

	public Vector2 milliPos() {
		return milliPos;
	}

	public void setMilliPos(Vector2 milliPos)
	{
		this.milliPos = milliPos;
	}

	public float minutes()
	{
		return minutes;
	}

	public void setMinutes(float minutes)
	{
		this.minutes = minutes;
		setMinAsString(minutes);
	}

	public float seconds()
	{
		return seconds;
	}

	public void setSeconds(float seconds) 
	{
		this.seconds = seconds;
		setSecAsString(seconds);
	}

	public float milliSec()
	{
		return milliSec;
	}

	public void setMilliSec(float milliSec)
	{
		this.milliSec = milliSec;
		setMilAsString(milliSec);
	}

	public String minAsString()
	{
		return minAsString;
	}

	public void setMinAsString(float minutes)
	{

		this.minAsString = Integer.toString((int)minutes);

	}

	public String secAsString()
	{
		return secAsString;
	}

	public void setSecAsString(float seconds)
	{
		if(seconds < 10)
		{
			this.secAsString = "0" + Integer.toString((int)seconds);
		}
		else
		{
			this.secAsString = Integer.toString((int)seconds);
		}
	}

	public String milAsString()
	{
		return milAsString;
	}

	public void setMilAsString(float milliSec)
	{
		int tempInt = (int)(milliSec*1000);
		String temp = Integer.toString(tempInt);
		
		if(tempInt < 10)
		{
			this.milAsString = "0" + temp.substring(0,1);
		}
		else
		{
			this.milAsString = temp.substring(0,2);
		}
	}

	public boolean displayAllSeconds()
	{
		return displayAllSeconds;
	}

	public void setDisplayAllSeconds(boolean displayAllSeconds) 
	{
		this.displayAllSeconds = displayAllSeconds;
	}
	
	public void setFontColor(Color color)
	{
		this.fontColor = color;
	}
	
	public Color getFontColor()
	{
		return this.fontColor;
	}

	public float deltaCount()
	{
		return deltaCount;
	}

	public void setDeltaCount(float deltaCount)
	{
		this.deltaCount = deltaCount;
	}
}
