import java.awt.Color;
import java.awt.Graphics2D;

public class GameBoard {
	public static final int TILE_SIZE =25;
	public static final int MAP_SIZE =20;
	
	//creates the fruit and snake constants with their respective colors
	public static enum TileType {
		SNAKE(Color.GREEN), FRUIT(Color.RED), EMPTY(null);
		
		private Color tileColor;
		private TileType(Color color) {
			this.tileColor = color;
		}
		public Color getColor() {
			return tileColor;
		}
	}
	
	private TileType[] tiles;
	
	//creates a new board using an array
	public GameBoard() {
		tiles = new TileType[MAP_SIZE*MAP_SIZE];
	}
	
	//clears the board
	public void resetBoard() {
		for(int i=0; i<tiles.length; i++) {
			tiles[i] = TileType.EMPTY;
		}
	}
	
	//allows you to change what is in a specific space on the board
	public void setTile(int x, int y, TileType type) {
		tiles[y*MAP_SIZE+x] = type;
	}
	
	//returns what is in the tile
	public TileType getTile(int x, int y) {
		return tiles[y*MAP_SIZE+x];
	}
	
	//draws out the board and everything in it by checking each space in array to see what is there
	public void draw(Graphics2D g) {
		g.setColor(TileType.SNAKE.getColor());
		
		for(int i=0; i<tiles.length; i++) {
			int x= i%MAP_SIZE;
			int y= i/MAP_SIZE;
			
			if(tiles[i].equals(TileType.EMPTY)) {
				continue;
			}
			
			if(tiles[i].equals(TileType.FRUIT)) {
				g.setColor(TileType.FRUIT.getColor());
				g.fillRect(x*TILE_SIZE+4, y*TILE_SIZE+4, TILE_SIZE -8, TILE_SIZE-8);
				g.setColor(TileType.SNAKE.getColor());
			}
			else {
				g.fillRect(x*TILE_SIZE +1, y*TILE_SIZE+1, TILE_SIZE-2, TILE_SIZE-2);
			}
		}
	}
}



