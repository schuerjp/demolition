package com.omniworks.demolition.worldelements;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.ContactItem;
import com.omniworks.demolition.utils.FixtureData;
import com.omniworks.demolition.utils.ShapeUtils;
import com.omniworks.demolition.world.Box2DFactory;
import com.omniworks.demolition.world.WorldLayout;

public class TruckElement extends WorldElement
{
	Vector2 minXY = new Vector2(0f, 0f);
	Vector2 maxXY = new Vector2(0f, 0f);
	
	@Override
	protected void createElementFixture(Map map, World world)
	{
		
		ArrayList<Fixture> fixtureList = new ArrayList<Fixture>();
		
		for(Object fixtureMap : WorldLayout.listForKey(map, "fixture"))
		{
			if(!(fixtureMap instanceof Map)) continue;

			short catBits = (short)FixtureType.TRUCK.mask;
			short maskBits = (short)(FixtureType.GROUND.mask | FixtureType.TRUCK.mask | FixtureType.ITEM.mask);

			Map fixtureParams = (Map)fixtureMap;

			elementFixture = Box2DFactory.createFixture(fixtureParams, world, elementBody, catBits, maskBits);
			
			fixtureList.add(elementFixture);
		}
		
		setMultipleFixtureBounds(fixtureList);
	}
	
	private void setMultipleFixtureBounds(ArrayList<Fixture> fixtureList)
	{

		
		for(Fixture fixture : fixtureList)
		{
			FixtureData fixData = (FixtureData)fixture.getUserData();
			updateXY(fixData.minXY(), fixData.maxXY());
		}
	}
	
	public void updateXY(Vector2 min, Vector2 max)
	{
		if(minXY.x > min.x) minXY.x = min.x;
		if(minXY.y > min.y) minXY.y = min.y;
		if(maxXY.x < max.x) maxXY.x = max.x;
		if(maxXY.y < max.y) maxXY.y = max.y;
	}
	
	@Override
	protected void createParticleEffect()
	{
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal(Assets.particlePath), Gdx.files.internal("data"));
	}
	
	@Override
	public void updateDamageScore(float damage)
	{
		damageScore += Math.abs(damage);
	}

	@Override
	public void handleContact(ContactItem item, WorldElement contactedElement)
	{
	}

	@Override
	protected void createStateDamageMap()
	{
		stateDamageMap.put(DamageState.NONE, 50f);
		stateDamageMap.put(DamageState.MODERATE, 100f);
		stateDamageMap.put(DamageState.HEAVY, 200f);
		stateDamageMap.put(DamageState.DESTROY, 250f);
	}
	
	@Override
	public void createStateTextureMap()
	{
		stateAtlasMap.put(DamageState.NONE, Assets.buttonAtlas.findRegion("truck"));
		stateAtlasMap.put(DamageState.MODERATE, Assets.buttonAtlas.findRegion("truck"));
		stateAtlasMap.put(DamageState.HEAVY, Assets.buttonAtlas.findRegion("truck"));
	}
	
	@Override
	protected void createStateScoreMap()
	{
		stateScoreMap.put(DamageState.MODERATE, 25f);
		stateScoreMap.put(DamageState.HEAVY, 50f);
		stateScoreMap.put(DamageState.DESTROY, 75f);

	}

	@Override
	protected void setElementTexture()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setParameters()
	{
		isAttachable = false;
	}
	
	@Override
	protected void createBounds()
	{
		elementShape = ShapeUtils.createPolygon(minXY, maxXY);
		bounds.set(elementShape.getBoundingRectangle());// TODO Auto-generated method stub
	}
}
