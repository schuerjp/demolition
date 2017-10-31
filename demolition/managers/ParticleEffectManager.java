package com.omniworks.demolition.managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectManager 
{

	private ArrayList<ParticleEffect> particleEffects;
	
	public ParticleEffectManager()
	{
		particleEffects = new ArrayList<ParticleEffect>();
	}
	
	public void addParticleEffect(ParticleEffect effect)
	{	
		effect.start();
		particleEffects.add(effect);
	}
	
	public void clear()
	{
		for(ParticleEffect effect : particleEffects)
		{
			effect.dispose();
		}
		
		particleEffects.clear();
	}
	
	public void removeParticleEffect(ParticleEffect effect)
	{
		if(particleEffects.contains(effect) && effect.isComplete())
		{
			particleEffects.remove(effect);
		}
	}
	
	public ArrayList<ParticleEffect> getParticleEffects()
	{
		return particleEffects;
	}
}
