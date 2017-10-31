package com.omniworks.demolition;

import com.badlogic.gdx.math.Vector2;

public class ScoreAnimation
{
	protected Vector2 pos;
	protected float scale;
	protected float score;
	protected float duration;
	protected float maxScale;
	protected boolean isComplete;
	protected float currentTime;
	
	public ScoreAnimation(Vector2 pos, float score, float duration)
	{
		this.setPos(pos);
		this.setScore(score);
		this.setDuration(duration);
		this.setMaxScale(2f);
		this.isComplete = false;
		this.setCurrentTime(0f);
		this.setScale(0f);
	}
	
	public ScoreAnimation(Vector2 pos, float score, float duration, float maxScale)
	{
		this.setPos(pos);
		this.setScore(score);
		this.setDuration(duration);
		this.setMaxScale(maxScale);
		this.isComplete = false;
		this.setCurrentTime(0f);
		this.setScale(0f);
	}
	
	public void update(float deltaTime)
	{
		currentTime += deltaTime;
		
		if(currentTime > duration)
		{
			isComplete = true;
			scale = 1;
			return;
		}
		
		if((currentTime < duration/2) && (scale < maxScale))
		{
			float tempScale = scale += 6*deltaTime;
			if(tempScale > maxScale) scale = maxScale;
		}
		else if((currentTime >= duration/2) && (currentTime < duration) && (scale > 0))
		{
			scale -= 6*deltaTime;
		}
		
		if(scale < .001f)
		{
			scale = .001f;
		}
		
	}
	
	public boolean isComplete()
	{
		return this.isComplete;
	}
	public float scale()
	{
		return scale;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public Vector2 pos()
	{
		return pos;
	}

	public void setPos(Vector2 pos)
	{
		this.pos = pos.add(new Vector2((float)(.75f*(2*Math.random()-1f)),(float)(.75f*(2*Math.random()-1f))));
	}

	public float score()
	{
		return score;
	}

	public void setScore(float score)
	{
		this.score = score;
	}

	public float duration()
	{
		return duration;
	}

	public void setDuration(float duration)
	{
		this.duration = duration;
	}

	public float maxScale()
	{
		return maxScale;
	}

	public void setMaxScale(float maxScale)
	{
		this.maxScale = maxScale;
	}

	public float currentTime()
	{
		return currentTime;
	}

	public void setCurrentTime(float currentTime) 
	{
		this.currentTime = currentTime;
	}
}
