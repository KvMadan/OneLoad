/**
 * 
 */
package in.km.oneview.jmeter;

import java.util.Date;

import org.apache.jmeter.testelement.TestStateListener;

/**
 * @author Madan Kavarthapu
 *
 */
public class JMainListener implements TestStateListener{

	public void testEnded() {
		long currentDateTime = System.currentTimeMillis();
		Date currentDate = new Date(currentDateTime);
		System.out.println("Executing JMeter Test: " + currentDate);			
	}

	public void testEnded(String arg0) {
	
	}

	public void testStarted() {
		long currentDateTime = System.currentTimeMillis();
		Date currentDate = new Date(currentDateTime);
		System.out.println("Executing JMeter Test: " + currentDate);
	}

	public void testStarted(String arg0) {
		System.out.println("Test Started" + arg0);
	}
	
}
