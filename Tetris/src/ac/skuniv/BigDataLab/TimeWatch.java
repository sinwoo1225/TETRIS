package ac.skuniv.BigDataLab;

public class TimeWatch extends Thread {
	private Board gameBoard;
    volatile private boolean stop;
	
	public TimeWatch(Board gameBoard) {
		this.gameBoard = gameBoard;
		stop = false;
	}
	public void setStop(boolean stop) {
		this.stop=stop;
		
	}
	@Override
	public void run() {
		while(true) { 
			while(!stop) {
				try {
					gameBoard.timeEvent();
					Thread.sleep(1000);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
