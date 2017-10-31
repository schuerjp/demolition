package com.omniworks.demolition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.omniworks.demolition.item.Item.ItemType;
import com.omniworks.demolition.item.Item;
import com.omniworks.demolition.item.TouchBombItem;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.worldelements.TouchBombElement;

public class DemoRayCastCallback implements RayCastCallback
{

	public enum CastType
	{
		EXPLOSION
	}
	
	public class RayCastResult
	{
		public Vector2 rayStart = new Vector2(0f,0f);
		public Vector2 rayEnd = new Vector2(0f,0f); 
		public float shortestDist = Float.MAX_VALUE;
		public Body hitBody = null;
		
	}
	
	private Body itemBody = null;
	private int numRays;
	private ItemType currentType;
	private RayCastResult rayCastResult;
	private GameManager gm;
	
	public DemoRayCastCallback(GameManager gm)
	{
		this.gm = gm;
		this.initialize();

	}
	
	private void initialize()
	{
		rayCastResult = new RayCastResult();
		currentType = ItemType.BOMB;
		numRays = 0;
	}
	
	public float perfromRayCast(Item item)
	{
		
		rayCastResult.rayStart = item.itemElement().elementBody().getPosition();
		currentType =  item.itemType();
		itemBody = item.itemElement().elementBody();
		
		if(currentType == ItemType.BOMB)
		{
			explosionRayCast((TouchBombItem)item);
		}
		
		return 0;
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) 
	{
		float distance = point.dst(rayCastResult.rayStart);

		if((rayCastResult.shortestDist < distance) || (itemBody == fixture.getBody()))
		{
			return 1;
		}
		
		rayCastResult.hitBody = fixture.getBody();
		rayCastResult.rayEnd = new Vector2(point);
		rayCastResult.shortestDist = distance;

		return 1;
	}
	
	private float explosionRayCast(TouchBombItem touchBombItem)
	{
	
		TouchBombElement touchBombElement = (TouchBombElement)touchBombItem.itemElement();
		Assets.bombSound.play(.50f);
		
		ParticleEffect particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal(Assets.cloudParticlePath), Gdx.files.internal("data"));
		particleEffect.setPosition(touchBombElement.elementBody().getPosition().x, touchBombElement.elementBody().getPosition().y);
		gm.particleManager().addParticleEffect(particleEffect);
		
		numRays = 32;
        rayCastResult.rayStart = touchBombElement.elementBody().getPosition();
        
       for(int i = 0; i < numRays; i++)
        {
            float angle = ((i/(float)numRays)*2*(float)Math.PI);
            Vector2 rayDirection = new Vector2((float)Math.sin(angle),(float)Math.cos(angle));
            Vector2 bombRadius = rayDirection.cpy();
            Vector2 rayEnd = new Vector2(bombRadius.scl(touchBombItem.radius()).add(rayCastResult.rayStart));
            
            rayCastResult.shortestDist = Float.MAX_VALUE;
            gm.world().rayCast(this, rayCastResult.rayStart, rayEnd);
           
			handleExplosion(touchBombItem, rayDirection);
        }
		
        gm.worldModel().processContactItems();
        
		return 0;
	}
	
	private float handleExplosion(TouchBombItem touchBombItem, Vector2 rayDirection)
	{
		
		Body body = rayCastResult.hitBody;
		
		Vector2 blastDir = new Vector2(rayCastResult.rayEnd);
		blastDir.sub(rayCastResult.rayStart); 
		
		float distSqrd = (float)Math.pow(rayCastResult.shortestDist, 2);
		
		if(distSqrd != 0 && body != null)
		{
			float invDistance = 1/distSqrd;
			float impulseMag = Math.min(touchBombItem.power(),touchBombItem.power()*invDistance);
			

			gm.worldModel().addContactItem(new ContactItem(body, null, rayCastResult.rayEnd, impulseMag, new Vector2(50f,50f)));
			gm.worldModel().processContactItems();
			
			if(body != null)
			{
				body.applyLinearImpulse(rayDirection.scl(impulseMag), rayCastResult.rayEnd, true);
			}
			
			gm.worldModel().addContactItem(new ContactItem(body, null, rayCastResult.rayEnd, impulseMag, new Vector2(50f,50f)));

		}
		
		return 0;
	}
	
}