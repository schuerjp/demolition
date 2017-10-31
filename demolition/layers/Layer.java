package com.omniworks.demolition.layers;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Layer 
{	
	public abstract Stage stage();
	public abstract void update(float delta);
	public abstract void draw();
	public abstract void createLayer();
	public abstract void updateLayer();
}
