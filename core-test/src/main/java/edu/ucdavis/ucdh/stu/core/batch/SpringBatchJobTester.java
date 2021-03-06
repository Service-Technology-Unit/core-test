package edu.ucdavis.ucdh.stu.core.batch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * This class tests a SpringBatchJob by invoking the run() method. This class
 * utilizes reflection to invoke the run() method to avoid a circular dependency
 * between the core .jar file and the core-text .jar file.
 */ 
public class SpringBatchJobTester extends AbstractJUnit4SpringContextTests {
	protected final Log log = logger;

	/**
	 * Test the batch job
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testBatchJob() {
		// initiate batch job run
		log.info("SpringBatchJobTester starting.");
		log.info(" ");

		// run application
		Object application = applicationContext.getBean("application");
		@SuppressWarnings("rawtypes")
		Class clazz = application.getClass();
		String name = clazz.getName();
		log.info("Launching application " + name);
		log.info(" ");
		try {
			Method method = clazz.getMethod("run", new Class[0]);
			method.invoke(application, new Object[0]);
		} catch (InvocationTargetException e) {
			Assert.assertTrue("Application " + name + " terminated with an exception: " + e.getCause(), false);
			log.error("Application " + name + " terminated with an exception: " + e.getCause(), e.getCause());
			System.exit(1);
		} catch (Exception e) {
			Assert.assertTrue("Application " + name + " terminated with an exception: " + e, false);
			log.error("Application " + name + " terminated with an exception: " + e, e);
			System.exit(1);
		}

		// normal batch job completion
		log.info(" ");
		log.info("SpringBatchJobTester complete.");
	}
}
