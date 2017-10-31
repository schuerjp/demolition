package com.omniworks.demolition.worldelements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.omniworks.demolition.Assets;
import com.omniworks.demolition.ContactItem;
import com.omniworks.demolition.utils.MathUtils;
import com.omniworks.demolition.utils.ShapeUtils;
import com.omniworks.demolition.world.Box2DFactory;

public abstract class WorldElement 
{

	public enum DamageState
	{
		NONE,
		MODERATE,
		HEAVY,
		DESTROY
	}
	
	public enum ElementType
	{
		VALUABLE,
		STANDARD,
		NONE
	}
	
	public enum FixtureType
	{
		    STUCTURE((short)1),
		    TRUCK((short)2),
		    ITEM((short)4),
		    GROUND((short)8),
		    ITEMELEMENT((short)16);
		    
		    public short mask;
		    
		    private FixtureType(short mask)
		    {
		    	this.mask = mask;
		    }
	};
	
	protected Map<DamageState,AtlasRegion> stateAtlasMap;
	protected Map<DamageState,Float> stateDamageMap;
	protected Map<DamageState,Float> stateScoreMap;
	
	protected World world;
	protected String elementID;
	protected Fixture elementFixture;
	protected Body elementBody;
	protected Polygon elementShape;
	protected Rectangle bounds;
	protected Sprite elementSprite;
	protected ArrayList<DamageState> visitedStates;
	protected ArrayList<Float> scoreList;
	protected DamageState currentState;
	protected float damageScore;
	protected boolean isDestroyable;
	protected boolean destroyJoints;
	protected ArrayList<Joint> jointRemovalList;
	protected float maxJointTorque;
	protected ParticleEffect particleEffect;
	protected String breakSoundKey;
	protected Sound contactSound;
	protected boolean hasParticleEffect;
	protected boolean isAttachable;
	protected ElementType elementType;
	
	public static WorldElement createElement (Map params, World world) 
	{
		String id = "";
		
		try 
		{
			WorldElement self = null;
			
			if (params.containsKey("name")) 
			{
				String className = (String)params.get("name");
				id = className;
				if(className.contains("wood")) self = new WoodElement();
				else if(className.contains("steel")) self = new SteelElement();
				else if(className.contains("concrete")) self = new ConcreteElement();
				else if(className.contains("brick")) self = new BrickElement();
				else if(className.contains("glass")) self = new GlassElement();
				else if(className.contains("safe")) self = new SafeElement();
				else if(className.contains("ground")) self = new GroundElement();
				else if(className.contains("truck")) self = new TruckElement();
				else if(className.contains("tire")) self = new TireElement();
				else if(className.contains("touch_bomb")) self = new TouchBombElement();
				else return null;
			} 
			else 
			{
				return null;
				
			}
			
			self.initialize(params, world, id);
			
			return self;
			
		} catch (Exception ex) 
		{
			throw new RuntimeException(ex);
		}
	}
	

	
	public void initialize(Map params, World world, String elementID)
	{
		this.world = world;		
		this.elementID = elementID;
		
		damageScore = 0;
		isDestroyable = false;
		destroyJoints = false;
		jointRemovalList = new ArrayList<Joint>();
		scoreList = new ArrayList<Float>();
		visitedStates = new ArrayList<DamageState>();
		maxJointTorque = 1f;
		isAttachable = true;
		bounds = new Rectangle();
		elementType = ElementType.STANDARD;
		currentState = DamageState.NONE;
		breakSoundKey = "wood_break";
		contactSound = Assets.woodImpactSound;
		hasParticleEffect = true;

		createElementBody(params, world);
		createElementFixture(params, world);
		
		createBounds();
		
		stateAtlasMap = new HashMap<DamageState,AtlasRegion>();
		createStateTextureMap();
		
		stateDamageMap = new HashMap<DamageState,Float>();
		createStateDamageMap();
		
		stateScoreMap = new HashMap<DamageState,Float>();
		createStateScoreMap();
		
		createElementSprite();
		
		createParticleEffect();
		
		setParameters();
	}
	
	protected abstract void createBounds();
		
	protected abstract void setParameters();
	
	protected void createElementBody(Map params, World world)
	{
		elementBody = Box2DFactory.createBody(params, world);
	}
	
	protected abstract void createElementFixture(Map map, World world);
	
	protected void createElementSprite()
	{
		elementSprite = new Sprite(stateAtlasMap.get(currentState));
		
		elementSprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	protected abstract void setElementTexture();
		
	protected abstract void createStateTextureMap();

	protected abstract void createStateDamageMap();
	
	protected abstract void createStateScoreMap();
	
	public void updateCurrentState(float damageScore)
	{
		if(!isDestroyable) return;

		float currentDamage = stateDamageMap.get(currentState);

		for(DamageState state : stateDamageMap.keySet())
		{
			float damage = stateDamageMap.get(state);

			if((damageScore > damage) && (damage > currentDamage))
			{
				currentDamage = damage;
				currentState = state;

				if(stateAtlasMap.containsKey(currentState))
				{
					elementSprite.setRegion(stateAtlasMap.get(currentState));
				}

				if(stateScoreMap.containsKey(currentState) && !visitedStates.contains(currentState))
				{
					visitedStates.add(currentState);
					scoreList.add(stateScoreMap.get(currentState));
				}
			}
		}
	}
	
	public abstract void handleContact(ContactItem item, WorldElement contactedElement);
	
	public abstract void updateDamageScore(float damage);
		
	public Fixture elementFixture()
	{
		return elementFixture;
	}

	public Body elementBody()
	{
		return elementBody;
	}

	protected abstract void createParticleEffect();

	protected void updateParticlePosition(Vector2 center)
	{
		particleEffect.setPosition(center().x, center().y);
	}

	public boolean isDrawable()
	{
		return true;
	}
	
	public String getElementID()
	{
		return elementID;
	}

	public void draw(SpriteBatch batch)
	{
		updateElementSprite();
		elementSprite.draw(batch);
	}
	
	private void updateElementSprite()
	{
		elementSprite.setOrigin(bounds.width/2, bounds.height/2);
		elementSprite.setPosition(center().x-bounds.width/2, center().y-bounds.height/2);
		elementSprite.setRotation(MathUtils.toDegrees(elementBody.getAngle()));
	}
	
	public boolean shouldDestroyElement()
	{
		if((currentState == DamageState.DESTROY) && (isDestroyable == true))
			return true;
		
		return false;
	}
	
	public void handleJointRemoval(ContactItem item)
	{		
		item.body().setType(BodyType.DynamicBody);
//		for(JointEdge jEdge : item.body().getJointList())
//		{
//			Joint joint = jEdge.joint;
//			Vector2 anchor = joint.getAnchorB();
//			float lever = anchor.dst(item.contactPoint());
//			
//			float pseudoTorque = lever*item.impulse();
//			
//			if(pseudoTorque > maxJointTorque && joint.getType() == JointType.WeldJoint)
//			{
//				if(!jointRemovalList.contains(joint)) jointRemovalList.add(joint);
//			}
//			
//		}
	}
	
	public boolean shouldDestroyJoints()
	{
		if(!jointRemovalList.isEmpty()) 
			return true;
	
		return false;
	}
	
	public ParticleEffect getParticleEffect()
	{
		particleEffect.setPosition(elementBody.getPosition().x, elementBody.getPosition().y);
		
		return this.particleEffect;
	}
	
	public ArrayList<Joint> getJointRemovalList()
	{
		return this.jointRemovalList;
	}
	
	public void clearJointRemovalList()
	{
		this.jointRemovalList.clear();
	}
	
	public Vector2 center()
	{
		return elementBody.getPosition();
	}
	
	public String getBreakingSound()
	{
		return this.breakSoundKey;
	}
	
	public Sound getContactSound()
	{
		return contactSound;
	}
	
	public void setBreakingSound(String key)
	{
		this.breakSoundKey = key;
	}
	
	public boolean isAttachable()
	{
		return isAttachable;
	}
	
	public void setAttachable(boolean isAttachable)
	{
		this.isAttachable = isAttachable;
	}

	public float score()
	{
		if(stateScoreMap.containsKey(currentState))
		{
				return stateScoreMap.get(currentState);
		}
		
		return 0;
	}
	
	public ArrayList<Float> scoreList()
	{
		return scoreList;
	}
	
	public void clearScoreList()
	{
		scoreList.clear();
	}
	
	public void setMaxJointTorque(float maxTorque)
	{
		this.maxJointTorque = maxTorque;
	}
	
	public float getDamageScore()
	{
		return this.damageScore;
	}


	public boolean hasParticleEffect()
	{
		return hasParticleEffect;
	}



	public Rectangle elementBounds()
	{
		return bounds;
	}


	public Polygon elementShape()
	{
		return elementShape;
	}

	public void setElementBounds(Rectangle bounds)
	{
		this.bounds = bounds;
	}
	
	public void setElementType(ElementType elementType)
	{
		this.elementType = elementType;
	}
	
	public ElementType elementType()
	{
		return this.elementType;
	}
	
	public void setCurrentState(DamageState state)
	{
		this.currentState = state;
	}
	
	public Sprite elementSprite()
	{
		return elementSprite;
	}
	
}
