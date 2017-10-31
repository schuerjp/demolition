package com.omniworks.demolition.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.omniworks.demolition.ContactItem;
import com.omniworks.demolition.ScoreAnimation;
import com.omniworks.demolition.managers.ElementManager;
import com.omniworks.demolition.managers.GameManager;
import com.omniworks.demolition.managers.ParticleEffectManager;
import com.omniworks.demolition.managers.ScoreManager;
import com.omniworks.demolition.managers.SoundManager;
import com.omniworks.demolition.worldelements.BrickElement;
import com.omniworks.demolition.worldelements.WorldElement;

public class WorldModel implements ContactListener
{

	private final int VEL_ITERATIONS = 8;
	private final int POS_ITERATIONS = 3;
	private float gravity = -10f;

	private World world;
	private WorldLayout worldLayout;

	private ArrayList<Joint> worldJoints;
	private ArrayList<ContactItem> contactItems;

	private GameManager gm;
	
	public WorldModel(GameManager gm)
	{
		this.gm = gm;
		initialize();
	}
	
	private void initialize()
	{
		contactItems = new ArrayList<ContactItem>();
		worldJoints = new ArrayList<Joint>();
		world = new World(new Vector2(0,gravity), true);
	}
	
	public Vector2 generateWorld()
	{
		world = new World(new Vector2(0,gravity), true);
		world.setContactListener(this);

		worldLayout = WorldLayout.generateLevel(world, gm.levelManager().currentStage(), gm.levelManager().currentLevel());
		gm.elementManager().setElementList(worldLayout.worldElements());
		worldJoints = worldLayout.worldJoints();

		return new Vector2(worldLayout.getWidth(), worldLayout.getHeight());
	}

	public void step(float deltaTime)
	{
		world.step(deltaTime, VEL_ITERATIONS, POS_ITERATIONS);
		processContactItems();
	}

	public ArrayList<Joint> worldJoints()
	{
		return worldJoints;
	}

	public World getWorld()
	{
		return this.world;
	}

	public void processContactItems()
	{
		for(ContactItem item : contactItems)
		{
			WorldElement contactElement = gm.elementManager().bodyElementMap().get(item.body());

			if(contactElement != null)
			{
				if(item.impactVel().len() > .5f)
					//contactElement.getContactSound().play(1);

				contactElement.handleContact(item, gm.elementManager().bodyElementMap().get(item.contactedBody()));
			}
		}

		contactItems.clear();

		updateWorldElements();

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		WorldManifold worldManifold = contact.getWorldManifold();
		Vector2[] points = worldManifold.getPoints();

		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();

		Vector2 velA = bodyA.getLinearVelocityFromWorldPoint(points[0]);
		Vector2 velB = bodyB.getLinearVelocityFromWorldPoint(points[0]);

		Vector2 impactVel = velA.sub(velB);

		float pseudoImpulseA = impactVel.len()*bodyA.getMass();
		float pseudoImpulseB = impactVel.len()*bodyB.getMass();

		contactItems.add(new ContactItem(bodyA, bodyB, points[0], pseudoImpulseA, impactVel));
		contactItems.add(new ContactItem(bodyB, bodyA, points[0], pseudoImpulseB, impactVel));
	}

	private void updateWorldElements()
	{
		Iterator<WorldElement> elementIter = gm.elementManager().elementList().iterator();

		while(elementIter.hasNext())
		{
			WorldElement element = elementIter.next();


			for(Float score : element.scoreList())
			{
				gm.scoreManager().addScore(new ScoreAnimation(new Vector2(element.center()),score, 1f, 1f));

			}
			element.clearScoreList();

			if(element.shouldDestroyJoints())
			{
				removeElementJoints(element);
			}

			if(element.shouldDestroyElement())
			{	
				if(element.getBreakingSound() != null)
					gm.soundManager().getSounds().get(element.getBreakingSound()).play(.5f);
				
				if(element.hasParticleEffect())
				{
					gm.particleManager().addParticleEffect(element.getParticleEffect());
				}
				
				world.destroyBody(element.elementBody());

				gm.elementManager().bodyElementMap().remove(element.elementBody());
				gm.elementManager().valuablesList().remove(element);
				elementIter.remove();
			}
		}
	}

	private void removeElementJoints(WorldElement element)
	{
		Iterator<Joint> iter = element.getJointRemovalList().iterator();

		while(iter.hasNext())
		{
			Joint joint = iter.next();

			if(worldJoints.contains(joint))
			{
				world.destroyJoint(joint);
				worldJoints.remove(joint);
			}
		}

		element.clearJointRemovalList();
	}
	
	public void updateWorldFinished()
	{
		updateTruckElements();
	}

	private void updateTruckElements()
	{
		for(Joint joint : worldJoints)
		{
			WorldElement element = gm.elementManager().bodyElementMap().get(joint.getBodyB());
			
			if(element == null) continue;

			if(element.getElementID().contains("tire"))
			{
					if(joint.getBodyB().getType() == BodyType.StaticBody)
					{
						joint.getBodyB().setType(BodyType.DynamicBody);
					}
					
					if(joint.getType() == JointType.RevoluteJoint)
					{
						RevoluteJoint revJoint = (RevoluteJoint)joint;
						revJoint.enableMotor(true);
						revJoint.setMotorSpeed(60f);
						revJoint.setMaxMotorTorque(100f);
					}
			}
		}
	}
	
	@Override
	public void beginContact(Contact contact)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endContact(Contact contact)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
	}

	public void addContactItem(ContactItem item)
	{
		this.contactItems.add(item);
	}
	
	public void clear()
	{
		worldJoints.clear();
		contactItems.clear();
		world.dispose();
	}
}
