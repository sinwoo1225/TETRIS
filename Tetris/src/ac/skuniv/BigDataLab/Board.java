package ac.skuniv.BigDataLab;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import ac.skuniv.BigDataLab.Shape.Tetrominos;

public class Board extends JPanel{
	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 22;
    // 레벨에 따른 속도 ( 레벨1 : 1 초에 1칸, 레벨2 : 0.7초에 한칸 ... )
    private static final int[] tetrisSpeeds
            = { 1000, 700, 490, 343, 240, 168, 117, 82, 60, 50 };
    // 점수를 동시 삭제행이 많을수록 점수가 기하급수적으로 늘어나도록 조정
    // 점수 규칙 ( 행삭제갯수가 0 이면 1, 1이면 10, ... )
    private static final int[] scoreRule = { 10, 25, 60, 150 };

	private static final Color[] COLORS = {new Color(0,0,0), 
			new Color(204,102,102), new Color(102, 204, 102),
			new Color(102,102,204), new Color(204,204,102),
			new Color(204,102,204), new Color(102,204,204),
			new Color(218,170,0) };
	private TimeWatch timer;
	private boolean isFallingFinished = false;
	private boolean isStarted = false;
	private boolean isPaused = false;
	private int score = 0;
	private int curX = 0;
	private int curY = 0;
	private JLabel statusBar;
	private Shape curPiece;
	private Tetrominos[][] board;
	
	public Board(Tetris parent) {
		setFocusable(true);
		curPiece = new Shape();
		timer = new TimeWatch(this);
		statusBar = parent.getStatusBar();
		board = new Tetrominos[BOARD_WIDTH][BOARD_HEIGHT];
		clearBoard();
		addKeyListener(new MyTetrisAdapter());
	}
	
	public int squareWidth() {
		return (int) getSize().getWidth() / BOARD_WIDTH;
	}

	public int squareHeight() {
		return (int) getSize().getHeight() / BOARD_HEIGHT;
	}
	
	public Tetrominos shapeAt(int x, int y) {
		return board[x][y];
	}
	
	private void clearBoard() {
		for(int i=0; i<BOARD_WIDTH;i++) {
			for(int j=0; j<BOARD_HEIGHT;j++) {
				board[i][j] = Tetrominos.NOSHAPE;
			}
		}
	}
	
	private void pieceDropped() {
		for(int i=0; i<4; i++) {
			int x = curX + curPiece.x(i);
			int y = curY + curPiece.y(i);
			board[x][y] = curPiece.getShape();	
		}
		removeFullLines();
		if(!isFallingFinished) {
			newPiece();
 		}
	}
	
	synchronized public void newPiece() {
		curPiece.setRandomShape();
		curX = BOARD_WIDTH /2;
		curY = 1;

		if(!tryMove(curPiece,curX,curY+1)) {
			curPiece.setShape(Tetrominos.NOSHAPE);
			timer.setStop(true);
			isStarted = false;
			repaint();
			statusBar.setText("GAME OVER");
		}
	}
	
	private void oneLineDown() {
		if(!tryMove(curPiece,curX,curY+1))
		pieceDropped();
	}
	
	public void timeEvent() {
		if(isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		}
		else {
			oneLineDown();
		}
	}
	
	//DRAW BLOCK
	private void drawSquare(Graphics g,int x, int y, Tetrominos shape) {
		Color color = COLORS[shape.ordinal()];
		g.setColor(color);
		g.fillRect(x+1, y+1, squareWidth()-2, squareHeight()-2);
		g.setColor(color.brighter());
		g.drawLine(x, y+squareHeight()-1, x, y);
		g.drawLine(x,y,x+squareWidth()-1,y);
		g.setColor(color.darker());
		g.drawLine(x+1,y+squareHeight()-1,x+squareWidth()-1,y+squareHeight()-1);
		g.drawLine(x+squareWidth()-1, y+squareHeight()-1, x+squareWidth()-1, y+1);
	}
	//DRAW BOARD
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
		
		for(int i=0; i<BOARD_WIDTH; i++) {
			for(int j=0;j<BOARD_HEIGHT; j++) {
				Tetrominos shape = shapeAt(i,j);
				if(shape != Tetrominos.NOSHAPE) {
					drawSquare(g,i*squareWidth(), boardTop+j * squareHeight(), shape);
				}
			}
		}
		if(curPiece.getShape() != Tetrominos.NOSHAPE) {
			for(int i=0; i<4; ++i) {
				int x = curX + curPiece.x(i);
				int y = curY + curPiece.y(i);
				drawSquare(g,x*squareWidth(),
						boardTop + y*squareHeight(),
						curPiece.getShape());
			}
		}
	}
	
	public void start() {
		if(isPaused) return;
		
		isStarted = true;
		isFallingFinished = false;
		score= 0;
		clearBoard();
		newPiece();
		timer.start();
	}
	
	
	synchronized public void paused() {
		if(!isStarted) return;
		
		isPaused =  !isPaused;
		
		if(isPaused) {
			timer.setStop(true);
			statusBar.setText("Paused");
		}
		else {
			timer.setStop(false);
			statusBar.setText("SCORE : " + String.valueOf(score));
		}
		repaint();
	}
	
	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for(int i =0; i<4;i++) {
			int x = newX + newPiece.x(i); 
			int y = newY + newPiece.y(i);
			 
			if(x <0 || x>=BOARD_WIDTH  || y < 0 || y>=BOARD_HEIGHT) 
				return false;
			
			if(shapeAt(x,y) != Tetrominos.NOSHAPE) {
				return false;
			}
		}
		
		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();
		return true;
	}
	
	private void removeFullLines()  {
		int numFullLines =0;

		for(int i = 1; i<BOARD_HEIGHT; i++) {
			boolean lineIsFull = true;
			for(int j=0;j<BOARD_WIDTH;j++) {
				if(shapeAt(j,i)==Tetrominos.NOSHAPE) {
					lineIsFull = false;
					break;
				}
			}
			
			if(lineIsFull) {
				++numFullLines;
				for(int k = i; k>0;k--) {
					for(int j=0;j<BOARD_WIDTH;j++) {
						board[j][k] = shapeAt(j,k-1);
					}
				}
			}
		}
		if(numFullLines > 0) {
			score += scoreRule[numFullLines-1];
			statusBar.setText("SCORE : " + String.valueOf(score));
			isFallingFinished = true;
			curPiece.setShape(Tetrominos.NOSHAPE);
			repaint();
		}
	}
	
	private void checkLevel() {
		
	}
	
	private void setLevel(int speed) {
	
	}
	
	private void dropDown() {
		int newY = curY;
		
		while(newY < BOARD_HEIGHT) {
			if(!tryMove(curPiece,curX,curY+1)) break;
			++newY;
		}
		pieceDropped();
	}
	
	class MyTetrisAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent ke){
			if(!isStarted || curPiece.getShape() == Tetrominos.NOSHAPE) return;
			
			int keyCode = ke.getKeyCode();
			
			if(keyCode == 'p' || keyCode == 'P') {
				paused();
			}
			
			if(isPaused) return;
			
			switch(keyCode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX-1,curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX+1,curY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece, curX,curY+1);
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotate(),curX,curY);
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			}
			
		}
	}
}


