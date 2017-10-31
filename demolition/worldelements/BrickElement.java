package com.omniworks.demolition.worldelements;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.ContactItem;
import com.omniworks.demolition.utils.ShapeUtils;
import com.omniworks.demolition.world.Box2DFactory;
import com.omniworks.demolition.world.WorldLayout;
import com.omniworks.demolition.worldelements.WorldElement.FixtureType;

public class BrickElement extends WorldElement
{
	@Override
	protected void createElementFixture(Map map, World world)
	{
		
		for(Object fixtureMap : WorldLayout.listForKey(map, "fixture"))
		{
			if(!(fixtureMap instanceof Map)) continue;

			short catBits = (short)FixtureType.STUCTURE.mask;
			short maskBits = (short)(FixtureType.GROUND.mask | FixtureType.ITEM.mask | FixtureType.STUCTURE.mask | FixtureType.ITEMELEMENT.mask);

			Map fixtureParams = (Map)fixtureMap;

			elementFixture = Box2DFactory.createFixture(fixtureParams, world, elementBody, catBits, maskBits);
			
			return;
		}
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
		if(item.impactVel().len() > 1)
		{
			updateDamageScore(item.impulse());
			updateCurrentState(getDamageScore());
			handleJointRemoval(item);
		}
	}

	@Override
	protected void createStateDamageMap()
	{
		stateDamageMap.put(DamageState.NONE, 25f);
		stateDamageMap.put(DamageState.MODERATE, 50f);
		stateDamageMap.put(DamageState.HEAVY, 75f);
		stateDamageMap.put(DamageState.DESTROY, 100f);
	}
	
	@Override
	public void createStateTextureMap()
	{
		stateAtlasMap.put(DamageState.NONE, Assets.atlas.findRegion("brick"));
		stateAtlasMap.put(DamageState.MODERATE, Assets.atlas.findRegion("brick"));
		stateAtlasMap.put(DamageState.HEAVY, Assets.atlas.findRegion("brick"));
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
		isDestroyable = true;
		setBreakingSound("brick_break");
	}

	@Override
	protected void createBounds() {
		elementShape = ShapeUtils.createPolygon(elementFixture);
		bounds.set(elementShape.getBoundingRectangle());// TODO Auto-generated method stub
		
	}

}
