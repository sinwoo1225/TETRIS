package ac.skuniv.BigDataLab;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Tetris extends JFrame {
	private JLabel statusBar;
	public Tetris() {
		//컴포넌트 생성 및 추가
		statusBar = new JLabel("SCORE : 0");
		add(statusBar,BorderLayout.SOUTH);
		
		Board board = new Board(this);
		add(board,BorderLayout.CENTER);
		
		//게임시작
		board.start();
		
		//프레임 설정
		setSize(200,400);
		setTitle("TETRIS");  
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public JLabel getStatusBar() {
		return statusBar;
	}
	
	public static void main(String[] args) {
		new Tetris();
	}
}
