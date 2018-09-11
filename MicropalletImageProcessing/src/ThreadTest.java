import java.math.BigInteger;

public class ThreadTest {

	public static void main(String[] args) {
		double startTime=System.currentTimeMillis();
		BigInteger bi=BigInteger.valueOf(0);
		int i[]= {0};
		Runnable r1 = new Runnable() {
			  public void run() {
				  
				//  for(int j=0; j<100000; j++)
			    	  bi.add(BigInteger.valueOf(i[0]^2));
			      
			  }
			};
			
		for(int g=0; i[0]<100000;i[0]++)
		{
			Thread thr2 = new Thread(r1);
		thr2.start();
			// bi.add(BigInteger.valueOf(i[0]^2));
		}
		System.out.println("Time: "+(System.currentTimeMillis()-startTime));
	}

}
