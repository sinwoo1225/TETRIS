package ac.skuniv.BigDataLab;
import java.util.Random;

public class Shape {
	//테트리스 블록 열거형 정의 및 열거형(클래스)생성자 정의
	enum Tetrominos{
		NOSHAPE(new int [][] { {0,0}, {0,0}, {0,0}, {0,0} }),
		ZSHAPE (new int [][] { {0,-1}, {0,0}, {-1,0}, {-1,1} }),
		SSAHPE (new int [][] { {0,-1}, {0,0}, {1,0}, {1,1} } ),
		LINESHAPE( new int[][] { {0,-1}, {0,0}, {0,1}, {0,2} }),
		TSHAPE ( new int[][] { {-1,0}, {0,0}, {1,0}, {0,1} }),
		SQUARESHAPE ( new int[][] { {0,0}, {1,0}, {0,1}, {1,1} }),
		LSHAPE ( new int[][] { {-1,-1}, {0,-1}, {0,0}, {0,1} }),
		MIRROEDLSHAPE (new int [][] { {1,-1}, {0,-1}, {0,0}, {0,1} })
		;
		public int [][] coords;
		private Tetrominos(int[][] coords) {
			this.coords = coords; 
		}
	}
	//Shape 클래스 맴버
	private int[][] coords;
	private Tetrominos 	pieceShape;
	
	//생성자
	public Shape(){
		coords = new int[4][2];
		setShape(Tetrominos.NOSHAPE);
	}
	
	public void setShape(Tetrominos shape) {
		for(int i=0; i<4; i++) {
			for(int j=0; j<2; j++) {
				coords[i][j] = shape.coords[i][j];
			}
		}
		pieceShape = shape;
	}
	
	private void setX(int index, int x) {
		coords[index][0] = x;
	}
	
	private void setY(int index, int y) {
		coords[index][1] = y;
	}
	
	public int x(int index) {
		return coords[index][0];
	}
	
	public int y(int index) {
		return coords[index][1];
	}
	
	public Tetrominos getShape() {
		return pieceShape;
	}
	
	public void setRandomShape() {
		Random r =new Random();
		int index = Math.abs(r.nextInt())%7 +1;
		Tetrominos[] values = Tetrominos.values();
		setShape(values[index]);
	}

	
	public Shape rotate() {
		if(pieceShape == Tetrominos.SQUARESHAPE) return this;
		
		Shape result = new Shape();
		result.pieceShape	= pieceShape;
		
		for(int i=0; i<4; i++) {
			result.setX(i,-y(i));
			result.setY(i, x(i));
		}
		return result;
	}
}
