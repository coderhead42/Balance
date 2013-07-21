package com.tester.phys;

import org.jbox2d.common.Vec2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

public class PhysicsView extends View {

	public PhysicsWorld mWorld;

	public int W, H;

	public float[][] ghost;

	public Paint[] paint;
	public Paint edgePaint, fulPaint;
	public int play = 0;
	public Path blockPath, fulPath;
	public PhysicsView(Context context, AttributeSet attrs) {
		super(context,attrs);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		W=preferences.getInt("width", 0);
		H=preferences.getInt("height", 0);
		float w = (1)*W;
		float h = (1)*H;
		mWorld = new PhysicsWorld(w, h);
		ghost = new float[3][4];
		paint = new Paint[4];
		for (int j = 0; j < 4; j++) {
		//	paint[j].setStyle(Style.FILL);
		//	paint[j].setColor(1000 * j);
		}
		blockPath = new Path();
		
		fulPath = new Path();
		
		fulPaint = new Paint();
		fulPaint.setStyle(Style.FILL_AND_STROKE);
		fulPaint.setColor(Color.YELLOW);
		
		edgePaint = new Paint();
		edgePaint.setStyle(Style.STROKE);
		edgePaint.setStrokeWidth(0);
		//edgePaint.setStrokeMiter(1);
		edgePaint.setColor(Color.BLACK);
	}

	public void shiftCamera(float add) {
		mWorld.cam.y += add;
	}

	public void drawBlock(float f, float g, int B, int p) {
		Vec2 v = mWorld.blocks[B];
		play = p;
		ghost[play][0] = f - v.x;
		ghost[play][1] = g - v.y;
		ghost[play][2] = f + v.x;
		ghost[play][3] = g + v.y;
		//mWorld.setBlockDraw(true);
		
	}

	public void update() {

		mWorld.world.step(mWorld.timeStep, mWorld.iterations);
		postInvalidate();
	}

	protected void onDraw(Canvas canvas) {
		
	    //	    if (!mWorld.isTower()){
	    //	canvas.drawText("FINAL: "+Integer.toString(mWorld.count), mWorld.World_W /2, mWorld.World_H /2, edgePaint);
	    //	    }
		canvas.drawColor(Color.argb(255, 180, 200, 255));

		fulPath.reset();
		fulPath.moveTo(mWorld.World_W/2, mWorld.World_H - 40 + mWorld.cam.y);
		fulPath.lineTo(mWorld.World_W/2 + 40, mWorld.World_H + mWorld.cam.y);
		fulPath.lineTo(mWorld.World_W/2 - 40, mWorld.World_H + mWorld.cam.y);

		canvas.drawPath(fulPath,fulPaint);
		canvas.drawPath(fulPath,edgePaint);
		// get position of fulcrum points.
//		Vec2 p = mWorld.groundBody.getWorldLocation(new Vec2(0.0f, 40.0f));
	//	p = p.sub(mWorld.cam);
		//Vec2 q = mWorld.groundBody.getWorldLocation(new Vec2(-40.0f, 0.0f));
//		q = q.sub(mWorld.cam);
	//	Vec2 r = mWorld.groundBody.getWorldLocation(new Vec2(40.0f, 0.0f));
		//r = r.sub(mWorld.cam);

		
		// draw the lines they create.
//		canvas.drawLine(p.x, mWorld.World_H - p.y, q.x, mWorld.World_H - q.y,
	//			mWorld.paintone);
		//canvas.drawLine(q.x, mWorld.World_H - q.y, r.x, mWorld.World_H - r.y,
			//	mWorld.paintone);
		//canvas.drawLine(r.x, mWorld.World_H - r.y, p.x, mWorld.World_H - p.y,
			//	mWorld.paintone);

		// draw block being positioned by user right now.

		for(int j = 0; j < mWorld.blockDraw; j++) {
			canvas.drawRect(ghost[j][0], mWorld.World_H - ghost[j][1],
					ghost[j][2], mWorld.World_H - ghost[j][3], mWorld.painttwo);
			// canvas.drawPoint(ghostBlock[0], ghostBlock[1], paintone);
			// canvas.drawPoint(ghostBlock[2], ghostBlock[3], paintone);
		}

		canvas.drawText("score: "+Float.toString(mWorld.count), 0, mWorld.World_H / 2,
				mWorld.painttwo);

		// bodies[] is the array that stores all the blocks placed so far.
		// this is a loop getting each of their positions and drawing them.
		for (int j = 0; j < mWorld.count; j++) {
			int B = (Integer) mWorld.bodies[j].getUserData();
			// gets position of the body (point/sprite).
			Vec2 m = mWorld.bodies[j].getPosition();

			// camera shift
			m = m.sub(mWorld.cam);

			if (m.y > mWorld.World_H - mWorld.width) {
				shiftCamera(mWorld.width);
			}

			// draws the point.
			canvas.drawCircle(m.x, mWorld.World_H - m.y, 2, mWorld.painttwo);

			// gets position of the block corners.
			// if you've changed the body shapes, change these.
			// eg. delete the string ' - j*10' from each line to draw correctly
			// if the provided alternative line has been used.
			Vec2 a = mWorld.bodies[j].getWorldLocation(new Vec2(
					mWorld.blocks[B].x, mWorld.blocks[B].y));
			a = a.sub(mWorld.cam);
			Vec2 b = mWorld.bodies[j].getWorldLocation(new Vec2(
					-mWorld.blocks[B].x, mWorld.blocks[B].y));
			b = b.sub(mWorld.cam);
			Vec2 c = mWorld.bodies[j].getWorldLocation(new Vec2(
					-mWorld.blocks[B].x, -mWorld.blocks[B].y));
			c = c.sub(mWorld.cam);
			Vec2 d = mWorld.bodies[j].getWorldLocation(new Vec2(
					mWorld.blocks[B].x, -mWorld.blocks[B].y));
			d = d.sub(mWorld.cam);
			
			blockPath.reset();

			blockPath.moveTo(a.x, mWorld.World_H - a.y);
			blockPath.lineTo(b.x, mWorld.World_H - b.y);
			blockPath.lineTo(c.x, mWorld.World_H - c.y);
			blockPath.lineTo(d.x, mWorld.World_H - d.y);
			blockPath.lineTo(a.x, mWorld.World_H - a.y);
			canvas.drawPath(blockPath, mWorld.paintone);
			canvas.drawPath(blockPath, edgePaint);
			// draws lines to make block edges based on the points fetched.
			// rounding floats to ints (pixels) is faulty, so rendering has
			// issues.
			// the bottommost bodies intersect, and the topmost ones seem to
			// 'float' on top of the body.
			// let it be for now, we'll work out how to fix it later.
			// nextBlock.draw(canvas);
			//canvas.drawLine(a.x, mWorld.World_H - a.y, b.x, mWorld.World_H
			//		- b.y, mWorld.paintone);
			//canvas.drawLine(b.x, mWorld.World_H - b.y, c.x, mWorld.World_H
				//	- c.y, mWorld.paintone);
			//canvas.drawLine(c.x, mWorld.World_H - c.y, d.x, mWorld.World_H
				//	- d.y, mWorld.paintone);
			//canvas.drawLine(d.x, mWorld.World_H - d.y, a.x, mWorld.World_H
				//	- a.y, mWorld.paintone);

		}

	}

}
