import java.awt.Point;
import java.util.LinkedList;

public class Snake {
	//creates constants that are the snakes 4 different directions
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT, NONE;
	}
	
	private GameBoard board;
	private LinkedList<Point> points;
	private Direction currentDirection;
	private Direction temporaryDirection;
	
	//creates the snake in the given board using a linked list
	public Snake(GameBoard board) {
		this.board = board;
		this.points = new LinkedList<Point>();
	}
	
	//sets the snake back to its original size and location with no direction
	public void resetSnake() {
		this.currentDirection = Direction.NONE;
		this.temporaryDirection = Direction.NONE;
		
		Point head = new Point(GameBoard.MAP_SIZE/2, GameBoard.MAP_SIZE/2);
		points.clear();
		points.add(head);
		board.setTile(head.x, head.y, GameBoard.TileType.SNAKE);
	}
	
	//allows the snake to get larger when in eats a fruit and change direction 
	public GameBoard.TileType updateSnake() {

		this.currentDirection = temporaryDirection;
		Point head = points.getFirst();
		
		switch(currentDirection) {
		case UP:
			if(head.y<=0){
				return null;
			}
			points.push(new Point(head.x, head.y -1));
			break;
			
		case DOWN:
			  if(head.y >= GameBoard.MAP_SIZE - 1) {
			   return null;
			  }
			  points.push(new Point(head.x, head.y + 1));
			  break;
			  
		case LEFT:
			  if(head.x <= 0) {
			   return null;
			  }
			  points.push(new Point(head.x - 1, head.y));
			  break;
			  
		case RIGHT:
			  if(head.x >= GameBoard.MAP_SIZE - 1) {
			   return null;
			  }
			  points.push(new Point(head.x + 1, head.y));
			  break;
			  
		case NONE:
			  return GameBoard.TileType.EMPTY;
			 }
		
		head = points.getFirst();
		
		GameBoard.TileType oldType = board.getTile(head.x, head.y);
		if(!oldType.equals(GameBoard.TileType.FRUIT)) {
			Point last = (Point) points.removeLast();
			board.setTile(last.x, last.y, GameBoard.TileType.EMPTY);
			oldType = board.getTile(head.x, head.y);
		}
		
		board.setTile(head.x, head.y, GameBoard.TileType.SNAKE);
		return oldType;
	}
	
	//logic to prevent error if person tries to move snake in exact opposite direction 
	public void setDirection(Direction direction) {
		if(direction.equals(Direction.UP)&&currentDirection.equals(Direction.DOWN)) {
			return; 
		}
		else if (direction.equals(Direction.DOWN)&&currentDirection.equals(Direction.UP)) {
			return;
		}
		else if (direction.equals(Direction.LEFT)&&currentDirection.equals(Direction.RIGHT)) {
			return;
		}
		else if (direction.equals(Direction.RIGHT)&&currentDirection.equals(Direction.LEFT)) {
			return;
		}
		this.temporaryDirection = direction;
		}
	
	//returs the current length of the snake
	public int getSnakeLength() {
		return points.size();
		}
	
	//returns the current direction of the snake
	public Direction getCurrentDirection() {
		return currentDirection;
		}
	}




