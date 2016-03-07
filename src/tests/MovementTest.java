package tests;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

import tests.Turret.Projectile;
import tools.Direction;

public class MovementTest extends BasicGame {

	private static float version = 1.3f;
	private static String title = "Test Game - v" + version;
	private static final int TILE_SIZE = 32;

	private GameContainer gc;

	// TODO: also include the smallfont, and maybe more fonts too
	AngelCodeFont font1;
	Input input;
	int inputDelta;

	Image playerImage;
	Image[] images;
	SpriteSheet moves;

	// TODO: Map "frames": move from 1 section to another of a map
	// - a 2d array of Images for map backgrounds?
	// > actually, "map" objects better so can interact different
	// > for now: test with images

	// TODO: make another class that implements TiledMap
	// - this would help for certain items, creatures, etc to be loaded
	TiledMap[][] tiledMapArr;
	// ArrayList<Shape>[][] staticMapObjects;
	HashMap<TiledMap, ArrayList<Shape>> staticMapObjects;
	Vector2f mapID;
	Vector2f pos;
	Vector2f mapPos;

	PlayerTest player;

	Turret testTurret;

	public MovementTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		this.gc = gc;
		input = gc.getInput();
		inputDelta = 1000;

		font1 = new AngelCodeFont("res/font1.fnt", "res/font1_0.tga");

		images = new Image[5];

		moves = new SpriteSheet("testdata/testMovement2.png",
				TILE_SIZE, TILE_SIZE);
		playerImage = moves.getSprite(0, 0);

		mapID = new Vector2f(0, 1);

		// TODO: Make a loop to initialize the Tiled maps
		int mapRows = 3;
		int mapCols = 1;
		// initialize a Hashmap that connects each TiledMap to array of Shapes
		staticMapObjects = new
				HashMap<TiledMap, ArrayList<Shape>>(mapRows * mapCols);
		// Initialize all of the maps in the array (each one is like a "scene")
		tiledMapArr = new TiledMap[mapCols][mapRows];
		tiledMapArr[0][0] = new TiledMap("testdata/map0-0.tmx", "testdata");
		tiledMapArr[0][1] = new TiledMap("testdata/map0-1.tmx", "testdata");
		// tiledMapArr[0][2] = new TiledMap("testdata/map0-2.tmx", "testdata");
		tiledMapArr[0][2] = new TiledMap("testdata/map1-1.tmx", "testdata");

		// for all maps, load all of the static objects (walls, holes, etc.)
		for (int r = 0; r < mapRows; r++) {
			for (int c = 0; c < mapCols; c++) {
				System.out.printf("Map at (%d, %d)\n", r, c);
				loadMapObjects(tiledMapArr[c][r]);
			}
		}

		// check that all of the objects have been loaded
		// for (TiledMap m : staticMapObjects.keySet()) {
		// for (Shape s : staticMapObjects.get(m)) {
		// // System.out.printf("Shape: (%f, %f)\n", s.getCenterX(),
		// // s.getCenterY());
		// }
		// }

		// TiledMap map11 = new TiledMap("testdata/map1-1.tmx", "testdata");

		int x = (gc.getWidth() / 2) - (playerImage.getWidth() / 2);
		int y = (gc.getHeight() / 2) - (playerImage.getHeight() / 2);
		pos = new Vector2f(x, y);
		mapPos = new Vector2f(0, 0);
		player = new PlayerTest(pos, playerImage);

		Image testTurretImage = new Image("testdata/test-turret-left.png");
		testTurret = new Turret(new Vector2f(gc.getWidth() - 64,
				gc.getHeight() - 64), Direction.LEFT, testTurretImage);

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// map.draw(mapPos.x, mapPos.y);
		// draws the current map
		// mapFrames[(int) mapID.x][(int) mapID.y].draw(0, 0);
		tiledMapArr[(int) mapID.x][(int) mapID.y].render(0, 0);
		if(mapID.x == 0 && mapID.y == 1) {
			testTurret.render(gc, g);
		}

		for (Shape s : staticMapObjects
				.get(tiledMapArr[(int) mapID.x][(int) mapID.y]))
			g.draw(s);
		// playerImage.draw(pos.x, pos.y);

		g.drawString((int) mapID.x + ", " + (int) mapID.y, gc.getWidth() - 64,
				32);

		font1.drawString(32, 32, "player: (" + player.getX() + ", " +
				player.getY() + ")");

		// font1.drawString(32, 64, "part player: " + partPlayer(4,
		// playerImage));
		font1.drawString(32, 64, "player offset: " + player.getOffset());

		font1.drawString(32, 96, "tiledmap width: " +
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth()
				+ " * 32 = " +
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth() * 32);

		player.render(gc, g);

		// if paused, show a window or something
		if (gc.isPaused()) {
			Color c = new Color(0, 0, 0, 0.3f);
			g.setColor(c);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			c.a = 0.6f;
			g.setColor(c);
			g.fillRect(gc.getWidth() / 2 - 142, gc.getHeight() / 2 - 192,
					284, 384);
			g.setColor(Color.white);
			font1.drawString(gc.getWidth() / 2 - 30, gc.getHeight() / 2 - 160,
					"PAUSED");
			font1.drawString(gc.getWidth() / 2 - 100, gc.getHeight() / 2 - 160
					+ 48, "Press \'p\' to resume.\n\nPress \'Esc\' to quit.");
		}

		// player.draw(pos.x, pos.y, 2.0f);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

		input = gc.getInput();
		inputDelta -= delta;

		// if you lose focus, pause the game
		if (!gc.hasFocus()) {
			gc.setPaused(true);
		}

		// if you press ESCAPE...
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			// if the game is not paused, then open the pause menu
			if (!gc.isPaused()) {
				gc.setPaused(true);
			}
			// otherwise, game is paused, so exit the game
			else {
				gc.exit();
			}
		}
		
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			gc.setPaused(!gc.isPaused());
		}

		// pause or un-pause the game
		if (input.isKeyPressed(Input.KEY_P)) {
			gc.setPaused(!gc.isPaused());
		}

		if(mapID.x == 0 && mapID.y == 1) {
			testTurret.update(gc, input, delta);
		}

		
		// TODO: Diagonals??
		// movementOne(input, delta);
		// movementTwo(input, delta);
		// 3: Moves the player around the map
		if (inputDelta < 0) {
			movePlayer(input, delta);
			inputDelta = 25;
		}
		// 4: Moves the map around the player
		// moveFour(input, delta);

		input.clearKeyPressedRecord();
	}

	/*
	 * private void movementOne(Input input, int delta) {
	 * if (input.isKeyDown(Input.KEY_DOWN)) {
	 * player = moves.getSprite(0, 1);
	 * pos.y += 5 * delta / 100;
	 * } else if (input.isKeyDown(Input.KEY_LEFT)) {
	 * player = moves.getSprite(1, 1);
	 * pos.x--;
	 * } else if (input.isKeyDown(Input.KEY_RIGHT)) {
	 * player = moves.getSprite(2, 1);
	 * pos.x++;
	 * } else if (input.isKeyDown(Input.KEY_UP)) {
	 * player = moves.getSprite(3, 1);
	 * pos.y--;
	 * } else {
	 * // no movement
	 * player = moves.getSprite(0, 0);
	 * }
	 * }
	 */

	/*
	 * private void movementTwo(Input input, int delta) {
	 * if (input.isKeyDown(Input.KEY_UP)) {
	 * player = moves.getSprite(3, 1);
	 * }
	 * if (input.isKeyDown(Input.KEY_RIGHT)) {
	 * player = moves.getSprite(2, 1);
	 * }
	 * if (input.isKeyDown(Input.KEY_LEFT)) {
	 * player = moves.getSprite(1, 1);
	 * }
	 * if (input.isKeyDown(Input.KEY_DOWN)) {
	 * player = moves.getSprite(0, 1);
	 * }
	 * }
	 */

	/**
	 * Moves the character around the map
	 * @param input
	 * @param delta
	 */
	private void movePlayer(Input input, int delta) {

		// TODO: Diagonals?
		// TODO: check for boundaries

		player.isMoving = false;
		// update the player's position and image/animation
		
		//check if the player collides with any of the current projectiles
		if (hitProjectile(player, 
				tiledMapArr[(int) mapID.x][(int) mapID.y])) {
			//if they do, don't undo movement
			//but, take damage or something?
			System.out.println("HIT");
		}

		int scale = 1;
		if (input.isKeyDown(Input.KEY_LSHIFT) ||
				input.isKeyDown(Input.KEY_RSHIFT)) {
			scale = 3;
		}

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			// moving = true;
			player.isMoving = true;
			// TODO: make this more efficient
			/*
			 * By updating/moving and THEN checking if player is out of bounds,
			 * this is probably slowing things down. We should check first
			 * for a valid movement, then when there is a problem two actions
			 * are not needed.
			 * Again, it's a consideration to be made at some point
			 */
			player.update(Direction.RIGHT, gc, input, scale * delta);
			// update the player's position and image/animation
			// player.update(gc, input, delta);
			// pos.x += spd * delta / 100;

			// check if the player is reachin the RIGHT edge of the map
			if (player.getPos().x + 3 * player.getOffset() > tiledMapArr[(int) mapID.x][(int) mapID.y]
					.getWidth()
					* TILE_SIZE) {

				// if there are more map screens to the right, change map
				if (getNextMap(Direction.RIGHT)) {
					player.setX(-(partPlayer(4, playerImage)));
				} else {
					// no more maps to the right, so collide & stay on screen
					// pos.x -= spd * delta / 100;
					player.move(Direction.LEFT, scale * delta);

				}

				/*
				 * //if there are more map screens to the right, change map
				 * if (mapID.x < tiledMapArr.length - 1) {
				 * mapID.x++;
				 * //pos.x = -(partPlayer(4, playerImage));
				 * player.setX(-(partPlayer(4, playerImage)));
				 * } else {
				 * //no more maps to the right, so collide & stay on screen
				 * //pos.x -= spd * delta / 100;
				 * player.move(Direction.LEFT, delta);
				 * }
				 */
			} else if (isBlocked(player,
					tiledMapArr[(int) mapID.x][(int) mapID.y],
					Direction.RIGHT)) {
				// check if the player is colliding with any static map objects
				// if they do, then undo the movement
				player.move(Direction.LEFT, scale * delta);

			}

		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player.isMoving = true;
			player.update(Direction.LEFT, gc, input, scale * delta);
			// check if the player collided with the left "wall"
			if (player.getX() + player.getOffset() <= 0) {
				if (mapID.x > 0) {
					mapID.x--;
					player.setX(tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getWidth() - 3 * player.getOffset());
				} else
					player.move(Direction.RIGHT, scale * delta);
			} else if (isBlocked(player,
					tiledMapArr[(int) mapID.x][(int) mapID.y],
					Direction.LEFT)) {
				// check if the player is colliding with any static map objects
				// if they do, then undo the movement
				player.move(Direction.RIGHT, scale * delta);

			}

			/*
			 * playerImage = moves.getSprite(1, 1);
			 * pos.x -= spd * delta / 100;
			 * if (pos.x + partPlayer(4, playerImage) <= 0) {
			 * if (mapID.x > 0) {
			 * mapID.x--;
			 * pos.x = tiledMapArr[(int) mapID.x][(int) mapID.y]
			 * .getWidth() - 3 * (partPlayer(4, playerImage));
			 * } else {
			 * pos.x += spd * delta / 100;
			 * }
			 * }
			 */
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			player.isMoving = true;
			player.update(Direction.UP, gc, input, scale * delta);
			// check if the player collided with the top "wall"
			if (player.getY() + player.getOffset() <= 0) {
				if (mapID.y > 0) {
					mapID.y--;
					player.setY(tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getHeight() * TILE_SIZE
							- 3 * player.getOffset());
				} else {
					player.move(Direction.DOWN, scale * delta);
				}
			} else if (isBlocked(player,
					tiledMapArr[(int) mapID.x][(int) mapID.y],
					Direction.UP)) {
				// check if the player is colliding with any static map objects
				// if they do, then undo the movement
				player.move(Direction.DOWN, scale * delta);

			}

			/*
			 * playerImage = moves.getSprite(3, 1);
			 * pos.y -= spd * delta / 100;
			 * if (pos.y + partPlayer(4, playerImage) <= 0) {
			 * if (mapID.y > 0) {
			 * mapID.y--;
			 * pos.y = tiledMapArr[(int) mapID.x][(int) mapID.y]
			 * .getHeight() * 32
			 * - 3 * (partPlayer(4, playerImage));
			 * } else {
			 * pos.y += spd * delta / 100;
			 * }
			 * }
			 */
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player.isMoving = true;
			player.update(Direction.DOWN, gc, input, scale * delta);
			// check if the player collided with the bottom "wall"
			if (player.getY() + 3 * player.getOffset() > tiledMapArr[(int) mapID.x][(int) mapID.y]
					.getHeight()
					* TILE_SIZE) {
				if (mapID.y < tiledMapArr[0].length - 1) {
					mapID.y++;
					pos.y = -(partPlayer(4, playerImage));
				} else {
					player.move(Direction.UP, scale * delta);
				}
			} else if (isBlocked(player,
					tiledMapArr[(int) mapID.x][(int) mapID.y],
					Direction.DOWN)) {
				// check if the player is colliding with any static map objects
				// if they do, then undo the movement
				player.move(Direction.UP, scale * delta);

			} 

			/*
			 * if (pos.y + 3 * partPlayer(4, playerImage) > tiledMapArr[(int)
			 * mapID.x][(int) mapID.y]
			 * .getHeight() * 32 ) {
			 * if (mapID.y < tiledMapArr[0].length - 1) {
			 * mapID.y++;
			 * pos.y = -(partPlayer(4, playerImage));
			 * } else {
			 * pos.y -= spd * delta / 100;
			 * }
			 * }
			 */
		}

		if (!player.isMoving)
			player.update(Direction.NONE, gc, input, scale * delta);
		// playerImage = moves.getSprite(0, 0);

	}

	/**
	 * TODO move the map around the character 
	 * Moves the map around the character.
	 * Useful for larger maps that can't fit on the screen.
	 * @param input
	 * @param delta
	 */
	private void moveFour(Input input, int delta) {

		boolean moving = false;
		int spd = 10;

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			playerImage = moves.getSprite(2, 1);
			mapPos.x -= spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			playerImage = moves.getSprite(1, 1);
			mapPos.x += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			playerImage = moves.getSprite(3, 1);
			mapPos.y += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			playerImage = moves.getSprite(0, 1);

			mapPos.y -= spd * delta / 100;
			moving = true;
		}

		if (!moving)
			playerImage = moves.getSprite(0, 0);
	}

	private void loadMapObjects(TiledMap map) {

		int objectGroupCount = map.getObjectGroupCount();
		// create an ArrayList to fill with a Shape for each object on the map
		ArrayList<Shape> mapObjects = new ArrayList<>();
		// loop through all of the object layers
		for (int gid = 0; gid < objectGroupCount; gid++) {
			System.out.printf("Object Group: %d\n", objectGroupCount);
			// loop through all of the objects in each layer
			for (int oid = 0; oid < map.getObjectCount(gid); oid++) {
				int x = map.getObjectX(gid, oid); // x-location of object
				int y = map.getObjectY(gid, oid); // y-location of object
				int width = map.getObjectWidth(gid, oid); // width of object
				int height = map.getObjectHeight(gid, oid); // height of object
				String type = map.getObjectType(gid, oid); // object type
				System.out.printf("(%d, %d) [%d x %d]\n",
						x, y, width, height);
				System.out.printf("Name: %s, Type: %s\n",
						map.getObjectName(gid, oid), type);
				// if type is "circle", make a Circle Shape
				if (type.equals("circle")) {
					// mapObjects.add(new Circle(x + width / 2, y + height / 2,
					// width / 2, 4));
					mapObjects.add(new Ellipse(x + width / 2, y + height / 2,
							width / 2, height / 2));
				} else {
					// otherwise, assume it is a Rectangle shape
					mapObjects.add(new Rectangle(x, y, width, height));
				}
			}
		}
		staticMapObjects.put(map, mapObjects);
	}

	private boolean isBlocked(PlayerTest p, TiledMap map, Direction d) {

		Shape pShape = p.getShape();
		// for all static objects in the current map
		for (Shape s : staticMapObjects.get(map)) {
			// check if the player is colliding with the shape
			if (s.intersects(pShape)) {
				// TODO: smart collisions
				// if there is a collision, try and move the player along
				// the edge,
				// for example, colliding with a circle will move the player
				// along the edge of the circle, so they aren't stuck
				// OR if the collision is with just 1 pixel, try to move
				// slightly to the left/right/up/down
				Shape testShape = pShape;
				switch (d) {
				case RIGHT:
					break;
				case LEFT:
					break;
				case UP:
					// try right
					testShape.setX(testShape.getX() + 1);
					boolean blockedRight = s.intersects(testShape);
					// testShape.setX(p)
					// boolean blockedLeft
					// if(blocked) {
					//
					// }
					break;
				case DOWN:
					break;
				case NONE:
					// error?
					break;
				}
				return true;
			}
		}

		return false;
	}
	
	private boolean hitProjectile(PlayerTest p, TiledMap map) {
		Shape pShape = p.getShape();
		for(Projectile proj : testTurret.getProjectiles()) {
			if(proj.getShape().intersects(pShape)) {
				proj.setDir(Direction.NONE);
				return true;
			}
		}
		return false;
	}

	/*
	 * private int halfPlayer(Image player) {
	 * return (player.getWidth() / 2);
	 * }
	 */

	/*
	 * This method will return true if there is a valid map to load based on
	 * the player's movement. If there is not a map or an error occurs
	 * (IndexOutOfBounds), then it will return false which will cause either
	 * an error or (for testing purposes) and "Error Map" to load.
	 * @return true if there is a valid map to load; false if there is an error
	 */
	private boolean getNextMap(Direction d) {
		switch (d) {
		case RIGHT:
			if (mapID.x < tiledMapArr.length - 1) {
				mapID.x++;
				return true;
			}
			// player is moving right
			break;
		case LEFT:
			// player is moving left
			break;
		case UP:
			// player is moving up
			break;
		case DOWN:
			// player is moving down
			break;
		default:
			// otherwise...what?
			break;
		}
		return false;
	}

	private int partPlayer(int parts, Image player) {
		return (player.getWidth() / parts);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new MovementTest(title));
			agc.setDisplayMode(640, 480, false);
			agc.setMinimumLogicUpdateInterval(20);
			agc.setMaximumLogicUpdateInterval(20);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
