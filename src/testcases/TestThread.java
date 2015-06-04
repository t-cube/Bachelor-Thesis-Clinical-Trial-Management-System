package testcases;

public class TestThread extends Thread {
	
	public boolean stopped = false;
	public int counter = 0;
	
	
	public void run(){
		while (!stopped){
			try {
				Thread.sleep(100);
				counter++;
			} catch (InterruptedException e) {
			}
		}
		System.out.println("stopped");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestThread tt = new TestThread();
		
		tt.start();
		
		while (tt.counter!=10);
		
		tt.stopped = true;
		
		System.out.println("Let's see");
		
	}

}
