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
import com.omniworks.demolition.worldelements.WorldElement.DamageState;
import com.omniworks.demolition.worldelements.WorldElement.FixtureType;

public class SafeElement extends WorldElement
{
	public SafeElement()
	{
		setElementType(ElementType.VALUABLE);
	}
	
	@Override
	protected void createElementFixture(Map map, World world)
	{
		
		for(Object fixtureMap : WorldLayout.listForKey(map, "fixture"))
		{
			if(!(fixtureMap instanceof Map)) continue;

			short catBits = (short)FixtureType.ITEM.mask;
			short maskBits = (short)(FixtureType.GROUND.mask | FixtureType.ITEM.mask | FixtureType.STUCTURE.mask | FixtureType.TRUCK.mask);

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
		if(contactedElement != null)
		{
		if(contactedElement.getElementID().contains("truck"))
		{
			this.currentState = DamageState.DESTROY;
			if(!visitedStates.contains(this.currentState))
			{
				this.visitedStates.add(this.currentState);
				this.scoreList.add(stateScoreMap.get(this.currentState));
			}

			}
		}
	}

	@Override
	protected void createStateDamageMap()
	{
		stateDamageMap.put(DamageState.NONE, 50f);
	}
	
	@Override
	public void createStateTextureMap()
	{
		stateAtlasMap.put(DamageState.NONE, Assets.atlas.findRegion("safe_crop"));
	}
	
	@Override
	protected void createStateScoreMap()
	{
		stateScoreMap.put(DamageState.DESTROY, 1000f);
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
		isAttachable = false;
		hasParticleEffect = false;
		setElementType(ElementType.VALUABLE);
		setBreakingSound("score_sound");

	}
	
	@Override
	protected void createBounds()
	{
		elementShape = ShapeUtils.createPolygon(elementFixture);
		bounds.set(elementShape.getBoundingRectangle());
		
	}
}
