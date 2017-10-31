package com.omniworks.demolition.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.utils.ScreenUtils;

public class ItemIcon 
{
	Image itemIcon;
	Group parentGroup;
	Rectangle screenBounds;
	Rectangle worldBounds;
	
	public ItemIcon(TextureRegion iconRegion, Group parentGroup)
	{
		itemIcon = new Image(iconRegion);
		this.parentGroup = parentGroup;
		
		initialize();
	}
	
	private void initialize()
	{
		screenBounds = new Rectangle();
		worldBounds = new Rectangle();
	}
	
	public void setIconParent(Group parentGroup)
	{
		this.parentGroup = parentGroup;
	}
	
	public Group iconParent()
	{
		return this.parentGroup;
	}
	
	public void setIcon(Image itemIcon)
	{
		this.itemIcon = itemIcon;
	}
	
	public Image icon()
	{
		return this.itemIcon;
	}
	
	public void dettachIcon()
	{
		itemIcon.setFillParent(false);
		parentGroup.removeActor(itemIcon);
		parentGroup.getStage().addActor(itemIcon);
		itemIcon.setScale(1f);
		setIconScreenBounds(itemIcon.getX(), itemIcon.getY(), itemIcon.getWidth()*1f, itemIcon.getHeight()*1f);	
	}
	
	public void attachIcon()
	{
		itemIcon.setFillParent(true);
		itemIcon.remove();
		parentGroup.addActor(itemIcon);
		itemIcon.setScale(1f);
		setIconScreenBounds(parentGroup.getX(), parentGroup.getY(), parentGroup.getWidth(), parentGroup.getHeight());
		itemIcon.setPosition(0, 0);
	}
	
	public void setIconWorldBounds(GameManager gm)
	{
		worldBounds = ScreenUtils.stageToWorldBounds(gm.camera(), gm.itemLayer().stage(), screenBounds);
	}
	
	public Rectangle iconWorldBounds()
	{
		return worldBounds;
	}
	
	public void setIconScreenBounds(float x, float y, float width, float height)
	{
		screenBounds.set(x, y, width, height);
		itemIcon.setBounds(x, y, width, height);
	}
	
	public Rectangle iconScreenBounds()
	{
		return screenBounds;
	}
	
	public void updateIconPosition(Vector2 pos)
	{
		if(itemIcon == null) return;
		itemIcon.setPosition(pos.x-(itemIcon.getWidth()/2), pos.y+itemIcon.getHeight());
		setIconScreenBounds(itemIcon.getX(), itemIcon.getY(), itemIcon.getWidth(), itemIcon.getHeight());
	}
}
