/**
 * 
 */
package in.km.oneview.jmeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Madan Kavarthapu
 *
 */
public class JMain {

	private static Logger log = LogManager.getLogger(JMain.class);
	
	private StandardJMeterEngine engine;
	private File jmeterHome;
	private String slash;
	private File testPlanPath;
	private HashTree testPlan;
	private String resultsFile;

	public JMain(String jhome, String path, String testResults) {
		try {

			jmeterHome = new File(jhome);
			slash = System.getProperty("file.separator");
			testPlanPath = new File(path);
			this.resultsFile = testResults + slash + "Results-" + System.currentTimeMillis() + ".jtl";
			log.warn("Results Path: " + resultsFile);

			if (jmeterHome.exists()) {
				// Doc @
				// http://jmeter.apache.org/api/org/apache/jmeter/engine/StandardJMeterEngine.html
				engine = new StandardJMeterEngine();

				JMeterUtils.loadJMeterProperties(jmeterHome.getPath() + slash
						+ "bin" + slash + "jmeter.properties");
				JMeterUtils.setJMeterHome("C:\\apache-jmeter-4.0");
				JMeterUtils.initLocale();

				SaveService.loadProperties();

				testPlan = SaveService.loadTree(testPlanPath);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void executeLoadTest(){
		try{
			// Summary.
			Summariser summer = null;
			String summariserName = JMeterUtils.getPropDefault(
					"summariser.name", "summary");
			if (summariserName.length() > 0) {
				summer = new Summariser(summariserName);
			}
			ResultCollector logger = new ResultCollector(summer);
			logger.setFilename(resultsFile);
			testPlan.add(testPlan.getArray()[0], logger);

			// Execute JMeter Test
			engine.configure(testPlan);

			// register to events
			StandardJMeterEngine.register(new JMainListener());

			engine.run();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void closeEngine(){
		engine.exit();
	}

	public static void main(String[] args) {
		JMain main = new JMain(System.getProperty("jmeter.home"), System.getProperty("jmeter.testPlan"), System.getProperty("jmeter.testResults"));
		main.executeLoadTest();
		main.closeEngine();
	}
}
