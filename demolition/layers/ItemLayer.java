package com.omniworks.demolition.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.widgets.AllBombsGroup;
import com.omniworks.demolition.widgets.ItemTable;

public class ItemLayer extends Layer
{
	private Stage stage;
	private GameManager gm;
	private ItemTable itemTable;
	
	float BUTTON_WIDTH = Gdx.graphics.getWidth()*.10f;
	float ITEM_WIDTH = Gdx.graphics.getWidth()*.075f;
	float ITEM_HEIGHT = Gdx.graphics.getWidth()*.075f;
	float BUTTON_HEIGHT = Gdx.graphics.getWidth()*.10f;
	float MARGIN = Gdx.graphics.getWidth()*.025f;
	float MAXCOLUMNS = 5;
	float DELTATIME = 1/60f;
	
	public ItemLayer(GameManager gm)
	{
		this.gm = gm;
		
		initialize();
	}

	private void initialize()
	{
		itemTable = new ItemTable(gm);
		itemTable.itemTable().debug();
		itemTable.itemTable().bottom().pad(MARGIN/2);
		itemTable.itemTable().setFillParent(true);
		
		stage = new Stage();
		
	}
	
	public void update(float delta)
	{
		stage.act(delta);
	}
	
	public void draw()
	{
		stage.draw();
		
		//Table.drawDebug(stage);
	}
	
	public void createLayer()
	{
		stage.addActor(itemTable.createItemTable());
	}

	@Override
	public Stage stage()
	{
		return stage;
	}
	
	public void clear()
	{
		stage.clear();
		itemTable.itemTable().clear();
	}

	@Override
	public void updateLayer() {
		// TODO Auto-generated method stub
		
	}
}
