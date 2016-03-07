package tests;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import tools.Direction;

public class PlayerTest {
	
	/*protected enum Direction {
		NONE, RIGHT, LEFT, UP, DOWN
	}*/
	
	private Vector2f pos;
	
	private Shape playerShape;
	Image currImage;
	SpriteSheet playerSheet;
	Animation playerAnim;
	boolean isMoving;
	
	float speed;

	public PlayerTest(Image playerImage) throws SlickException{
		this(new Vector2f(0.0f, 0.0f), playerImage);
	}
	
	public PlayerTest(Vector2f initPos, Image playerImage) throws SlickException{
		//pos.set(initPos);
		pos = initPos;
		currImage = playerImage;
		speed = 10;
		isMoving = false;
		
		playerShape = new Rectangle(pos.x, pos.y, 
				playerImage.getWidth(), playerImage.getHeight());
		
		playerSheet = new SpriteSheet("testdata/testMovement2.png", 
		  32, 32);
		//TODO: add other attributes
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos.set(pos);
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public void setX(float newx) {
		pos.x = newx;
	}
	
	public void setY(float newy) {
		pos.y = newy;
	}
	
	public int getOffset() {
		return (currImage.getWidth() / 4);
	}
	
	public Shape getShape() {
		return playerShape;
	}
	
	private void setSpriteFromDirection(Direction d) {
		switch(d) {
		case NONE:
			currImage = playerSheet.getSprite(0,0);
			break;
		case EAST:
			currImage = playerSheet.getSprite(2, 1);
			break;
		case WEST:
			currImage = playerSheet.getSprite(1, 1);
			break;
		case NORTH:
			currImage = playerSheet.getSprite(3, 1);
			break;
		case SOUTH:
			currImage = playerSheet.getSprite(0, 1);
			break;
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		//TODO: Render images and animations
		//currImage.draw(pos.x, pos.y);
		g.drawImage(currImage, pos.x, pos.y);
		//g.draw(playerShape);
	}
	
	public void update(Direction dir, GameContainer gc, Input input, int delta) {
		//TODO: handle smooth movement of the player
		// collisions are handled by the caller of this method
		//isMoving = false;
		
		setSpriteFromDirection(dir);
		move(dir, delta);
		
		/*
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			//isMoving = true;
			// if not moving, set the sprite to be moving
			// I think this will improve efficiency slightly
			//if (!moving)
			// ^ did not work. 
			// TODO: make movement efficient, so the player sprite is not
			// 		re-set every loop
			
			//set the player's sprite to point right
			//TODO: use animations
			setSpriteFromDirection(Direction.RIGHT);
			//move the player right
			move(Direction.RIGHT, delta);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			//isMoving = true;
			//set the player's sprite to point left
			setSpriteFromDirection(Direction.LEFT);
			//move the player left
			move(Direction.LEFT, delta);
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			//isMoving = true;
			//set the player's sprite to point up
			setSpriteFromDirection(Direction.UP);
			//move the player up
			move(Direction.UP, delta);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			//isMoving = true;
			//set the player's sprite to point down
			setSpriteFromDirection(Direction.DOWN);
			//move the player down
			move(Direction.DOWN, delta);
		}
		if(!isMoving)
			currImage = playerSheet.getSprite(0, 0);
		*/
	}
	
	protected void move(Direction dir, int delta) {
		//System.out.println("ratio = " + (speed + delta) / 10);
		//System.out.println("delta = " + delta);
		switch(dir) {
		case EAST:
			//move right
			//pos.x += speed * delta / 100;
			pos.x += (speed + delta) / 10;
			break;
		case WEST:
			//move left
			//pos.x -= speed * (delta / 100.0f);
			//pos.x -= (speed + delta) / 100.0f;
			pos.x -= (speed + delta) / 10;
			break;
		case NORTH:
			//move up
			//pos.y -= speed * (delta / 100.0f);
			//pos.y -= (speed + delta) / 100.0f;
			pos.y -= (speed + delta) / 10;
			break;
		case SOUTH:
			//move down
			//pos.y += speed * (delta / 100.0f);
			//pos.y += (speed + delta) / 100.0f;
			pos.y += (speed + delta) / 10;
			break;
		default:
			//do nothing
			break;
		}
		playerShape.setLocation(pos);
	}
	
}
