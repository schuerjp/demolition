package com.omniworks.demolition.widgets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.omniworks.demolition.Assets;

public class UILabel extends Label
{	
	public UILabel(CharSequence text, LabelStyle style)
	{
		super(text, style);
	}

	@Override 
	public void draw(SpriteBatch batch, float parentAlpha)
	{	
		batch.setShader(Assets.shader);
		Assets.shader.setSmoothing(1/(8*super.getFontScaleX()));
		super.draw(batch, 1);
		batch.setShader(null);
	}
	
	public void setScale(float scale)
	{
		super.setFontScale(scale);
	}
}
