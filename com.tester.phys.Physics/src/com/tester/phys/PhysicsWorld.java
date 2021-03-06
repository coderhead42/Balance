package com.tester.phys;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class PhysicsWorld{

	// this class is both the JBox2D simulation implementer and the vew that
	// draws it.
	// it is instantiated in Physics class.

	protected static final int GUIUPDATEIDENTIFIER = 0x231;

	// details of simulator specifics.

	public int targetFPS = 40;
	public float timeStep = 8.0f / targetFPS;
	public int iterations = 5;

	public Body[] bodies;
	public Vec2[] blocks;
	
	public int count = 0;
	
	private AABB worldAABB;
	public World world;
	
	public Body groundBody;
	private PolygonDef groundShapeDef;
	
	public float World_W;
	public float World_H;

	public Paint paintone, painttwo;
	
	public int[] ghostBlock = {0, 0, 0, 0};

	
//	ImageView nextBlock;

	// these talk about block size.
	//
	protected float length = 140, width = 20;
	
	protected int blockDraw = 0;
	
    public boolean isTower(){
	return (bodies[count-1].getPosition().y > bodies[count - 2].getPosition().y);
    }

	public int setBlockDraw(boolean b) {
		if(b){
			blockDraw+=1;
		}else if(!b && blockDraw > 0){
			blockDraw-=1;
		}
		return blockDraw;
	}
	//Step x: Create Camera
	protected Vec2 cam = new Vec2(0, 0);
	
	// constructor. in this, you need to look at the shape creator for the
	// blocks (PolygonDef block), and little else.
	public PhysicsWorld(float w, float h) {
		//super(context);
		World_W = w;
		World_H = h;

		// Step 1: Create Physics World Boundaries
		Vec2 min = new Vec2(-50, -50);
		worldAABB = new AABB();
		Vec2 max = new Vec2(World_W + 50, 2*World_H);
		worldAABB.lowerBound.set(min);
		worldAABB.upperBound.set(max);
		

		// Step 2: Create Physics World with Gravity
		Vec2 gravity = new Vec2((float) 0.0, (float) -10.0);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);

		// Step 3:
		// Create Ground (Fulcrum) :
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vec2((float) World_W / 2, (float) 0.0));
		groundBody = world.createBody(bodyDef);
		groundShapeDef = new PolygonDef();

		groundShapeDef.setAsBox((float) 8.0, (float) 40.0);
		groundBody.createShape(groundShapeDef);
		bodyDef.position.set(new Vec2((float) World_W / 2, (float) (40)));

		// step 4: initialize
		bodies = new Body[300];
		
		blocks = new Vec2[7];
		blocks[0] = new Vec2(113, 20);
		blocks[1] = new Vec2(135, 11);
		blocks[2] = new Vec2(38, 31);
		blocks[3] = new Vec2(41, 20);
		blocks[4] = new Vec2(18, 18);
		blocks[5] = new Vec2(36, 26);
		blocks[6] = new Vec2(15, 54);

		paintone = new Paint();
		paintone.setStyle(Style.FILL_AND_STROKE);
		paintone.setColor(Color.RED);
		
		painttwo = new Paint();
		painttwo.setStyle(Style.FILL);
		painttwo.setColor(Color.BLUE);

		//this.setOnDragListener(new MyDragListener());
	
	}
	
	/* private final class MyTouchListener implements OnTouchListener {
		    public boolean onTouch(View view, MotionEvent motionEvent) {
		      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		        ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
		        view.setVisibility(View.INVISIBLE);
		        return true;
		      } else {
		        return false;
		      }
		    }
		  }*/

	public void addBlock(float posx, float posy, int B) {
		Vec2 pos = new Vec2(posx, posy);
		pos = pos.add(cam);
		setBlockDraw(false);
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(pos);
		bodies[count] = world.createBody(bodyDef);

		// Create Shape with Properties
		PolygonDef block = new PolygonDef();

		// this defines the body shape.
		// if you change this, change the onDraw function below to reflect it.
		// more details are given there.
		// use the below line instead of the corresponding one for setting the
		// same-length box to be dropped every time:
		//
		
		block.setAsBox(blocks[B].x, blocks[B].y);

		// don't mess with these much, they work well for our purposes.
		block.density = (float) 3;
		block.restitution = 0.0f;

		// Assign shape to Body
		bodies[count].createShape(block);
		bodies[count].setMassFromShapes();
		bodies[count].setUserData(B);
		
//		nextBlock = new ImageView(null);
		//LayoutParams params = new LayoutParams((int)(length - count*10), (int)(width));
		//nextBlock.setLayoutParams(params);
		//nextBlock.setX(10);
		//nextBlock.setY(World_H - 20);
		//nextBlock.setOnTouchListener(new MyTouchListener());
		
		count += 1;
	}	
	

	
}
