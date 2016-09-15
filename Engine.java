import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

//creates engine that will piece the game together so that it can be played
public class Engine extends KeyAdapter {
	
	private Canvas canvas;
	private Snake snake;
	private GameBoard board;
	private int score;
	private boolean gameOver;
	private ArrayList<Integer> highScores = new ArrayList<Integer>(Arrays.asList(0, 0, 0));
	
	private static final Font SMALL_FONT = new Font("Times New Roman", Font.BOLD, 20);
	private static final Font LARGE_FONT = new Font("Times New Roman", Font.BOLD, 40);
	
	private static final int UPDATES_PER_SECOND = 10;
	
	//creates the engine
	private Engine(Canvas canvas) {
		this.canvas = canvas;
		this.board = new GameBoard();
		this.snake = new Snake(board);
		resetGame();
		canvas.addKeyListener(this);
	}

	//allows the game to be played
	public void startGame() {
		canvas.createBufferStrategy(2);
		
		Graphics2D g = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		
		long start = 0L;
		long sleepDuration = 0L;
		
		while(true) {
			start = System.currentTimeMillis();
			
			update();
			render(g);
			canvas.getBufferStrategy().show();
			g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			sleepDuration = (1000L/UPDATES_PER_SECOND)-(System.currentTimeMillis()-start);
			
			if(sleepDuration>0) {
				try {
					Thread.sleep(sleepDuration);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//updates the game while you are playing
	public void update() {
		//checks to make sure that you did not lose or leave the window
		if(gameOver || !canvas.hasFocus()) {
			return;
		}
		
		//you lose if you hit the wall or yourself and then the game calculates high scores
		GameBoard.TileType snakeTile = snake.updateSnake(); 
		if(snakeTile==null || snakeTile.equals(GameBoard.TileType.SNAKE)){
			gameOver = true;
			
			getHighScores(highScores);
		}
		
		//if you land on or "eat" a fruit you get 10 points
		else if(snakeTile.equals(GameBoard.TileType.FRUIT)){
			score+=10;
			spawnFruit();
		}
	}
		
	//shows the board and everything on it
	private void render(Graphics2D g) {
		board.draw(g);
		
		g.setColor(Color.WHITE);
		
		//if you lose it displays game over and your score and current high score
		if(gameOver) {
			g.setFont(LARGE_FONT);
			String message = new String("Final Score: " + score);
			g.drawString(message,  canvas.getWidth()/2 - g.getFontMetrics().stringWidth(message)/2,  150);
			g.setFont(SMALL_FONT);
			message = new String("Press Enter to Restart");
			g.drawString(message, canvas.getWidth()/2 - g.getFontMetrics().stringWidth(message)/2, 350);
			g.setFont(SMALL_FONT);
			String highScoreDisplay = new String("High Score: ");
			g.drawString(highScoreDisplay, canvas.getWidth()/2 - g.getFontMetrics().stringWidth(highScoreDisplay), 250);
			g.setFont(SMALL_FONT);
			String highScoreDisp1 = new String("1. " + highScores.get(0));
			g.drawString(highScoreDisp1, canvas.getWidth()/2 - g.getFontMetrics().stringWidth(highScoreDisp1), 270);
			g.setFont(SMALL_FONT);
			String highScoreDisp2 = new String("2. " + highScores.get(1));
			g.drawString(highScoreDisp2, canvas.getWidth()/2 - g.getFontMetrics().stringWidth(highScoreDisp2), 290);
			g.setFont(SMALL_FONT);
			String highScoreDisp3 = new String("3. " + highScores.get(2));
			g.drawString(highScoreDisp3, canvas.getWidth()/2 - g.getFontMetrics().stringWidth(highScoreDisp3), 310);
	}
		//while you are playing displays your score on the screen
		else {
			g.setFont(SMALL_FONT);
			g.drawString("Score: " + score,  10, 20);
		}
	}
	
	//allows you to use arrow keys to move the snake around the board
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_UP){
			snake.setDirection(Snake.Direction.UP);
		}
		if(e.getKeyCode()== KeyEvent.VK_DOWN){
			snake.setDirection(Snake.Direction.DOWN);
		}
		if(e.getKeyCode()== KeyEvent.VK_LEFT){
			snake.setDirection(Snake.Direction.LEFT);
		}
		if(e.getKeyCode()== KeyEvent.VK_RIGHT){
			snake.setDirection(Snake.Direction.RIGHT);
		}
		
		//after you lose you can start a new game by pressing enter
		if(e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
			resetGame();
		}
	}
	
	//makes a new fruit appear in a random spot every time it is eaten
	private void spawnFruit() {
		int random = (int) (Math.random()*((GameBoard.MAP_SIZE*GameBoard.MAP_SIZE)-snake.getSnakeLength()));
		int emptyFound=0;
		int index=0;
		
		while(emptyFound<random) {
			index++;
			if(board.getTile(index%GameBoard.MAP_SIZE,  index/GameBoard.MAP_SIZE).equals(GameBoard.TileType.EMPTY)) {
				emptyFound++;
			}
		}
		board.setTile(index%GameBoard.MAP_SIZE,  index/GameBoard.MAP_SIZE, GameBoard.TileType.FRUIT);
	}
	
	//puts the new score from the game in the correct place in the ArrayList if it is a top 3 score
	public ArrayList<Integer> getHighScores(ArrayList<Integer> highScores) {
		this.highScores = highScores;
			
		if(score>highScores.get(0)) {
			highScores.add(0, score);
		}
		else if(score<=highScores.get(0) && score>highScores.get(1)) {
			highScores.add(1, score);
		}
		else if(score<=highScores.get(1) && score>highScores.get(2)) {
			highScores.add(2, score);
		}
			else {
			highScores.add(score);
		}
		   return highScores;
	}
	
	//restarts the game as new
	private void resetGame() {
		board.resetBoard();
		snake.resetSnake();
		score = 0;
		spawnFruit();
		
		gameOver = false;
	}
	
	//sets up window and makes it visible and starts the game
	public static void main(String[] args) {
		//creates the frame in which the game is played
		JFrame frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//creates black background
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setPreferredSize(new Dimension(GameBoard.MAP_SIZE*GameBoard.TILE_SIZE, GameBoard.TILE_SIZE*GameBoard.MAP_SIZE));
		
		//puts everything together and displays it
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//begins the game
		new Engine(canvas).startGame();
	}
}




