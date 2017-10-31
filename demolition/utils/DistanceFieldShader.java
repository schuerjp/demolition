package com.omniworks.demolition.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class DistanceFieldShader extends ShaderProgram 
{
	public DistanceFieldShader ()
	{
		super(
			Gdx.files.internal("data/shaders/distancefield.vert"),
			Gdx.files.internal("data/shaders/distancefield.frag"));
		if (!isCompiled()) {
			throw new RuntimeException("Shader compilation failed:\n" + getLog());
		}
	}
	
	/** @param smoothing a value between 0 and 1 */
	public void setSmoothing(float smoothing)
	{
		float delta = 0.35f * MathUtils.clamp(smoothing, 0, 1);
		setUniformf("u_lower", 0.35f - delta);
		setUniformf("u_upper", 0.35f + delta);
	}
}