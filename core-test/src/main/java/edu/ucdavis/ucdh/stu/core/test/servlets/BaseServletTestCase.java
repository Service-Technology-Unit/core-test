package edu.ucdavis.ucdh.stu.core.test.servlets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.StringUtils;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * <p>This abstract class provides the foundation code for
 * all servlet tests.</p>
 */
@ContextConfiguration(locations = {"/context/applicationContext*.xml"})
public abstract class BaseServletTestCase<E> extends AbstractJUnit4SpringContextTests {
	private final Class<E> servletClass;
	private final String beanName;
	private final String servletMapping;
	private final String servletTestMapping;
	private final String servletLabel;
	protected final Log log = logger;

	/**
	 * <p>Constructs a new <code>BaseServletTestCase</code> using the
	 * parameters provided.</p>
	 * 
	 * @param servletClass the class of the servlet to be tested
	 * @param servletMapping the servlet mapping of the servlet to be tested
	 */
	public BaseServletTestCase(Class<E> servletClass, String servletMapping) {
		Assert.assertNotNull("servletClass must not be null", servletClass);
		Assert.assertNotNull("servletMapping must not be null", servletMapping);
		this.servletClass = servletClass;
		this.beanName = null;
		this.servletMapping = servletMapping;
		this.servletLabel = servletClass.getName();
		if (servletMapping.indexOf("/*") != -1) {
			servletTestMapping = servletMapping.substring(0, servletMapping.indexOf("/*"));
		} else {
			servletTestMapping = servletMapping;
		}
	}

	/**
	 * <p>Constructs a new <code>BaseServletTestCase</code> using the
	 * parameters provided.</p>
	 * 
	 * @param beanName the Spring bean name of the servlet to be tested
	 * @param servletMapping the servlet mapping of the servlet to be tested
	 */
	@SuppressWarnings("unchecked")
	public BaseServletTestCase(String beanName, String servletMapping) {
		Assert.assertNotNull("beanName must not be null", beanName);
		Assert.assertNotNull("servletMapping must not be null", servletMapping);
		this.servletClass = (Class<E>) GenericSpringTestServlet.class;
		this.beanName = beanName;
		this.servletMapping = servletMapping;
		this.servletLabel = "bean \"" + beanName + "\"";
		if (servletMapping.indexOf("/*") != -1) {
			servletTestMapping = servletMapping.substring(0, servletMapping.indexOf("/*"));
		} else {
			servletTestMapping = servletMapping;
		}
	}

	/**
	 * <p>Tests the servlet's GET method.</p>
	 */
	public void testServletGet() {
		try {
			WebResponse webResponse = getGetMethodWebResponse();
			Assert.assertEquals("Unexpected response code from testing " + servletLabel + " GET method", 405, webResponse.getResponseCode());
		} catch (Exception e) {
			log.error("Error testing " + servletLabel + " GET method; Exception is " + e, e);
			Assert.fail("Error testing " + servletLabel + " GET method; Exception is " + e);
		}
	}

	/**
	 * <p>Tests the servlet's PUT method.</p>
	 */
	public void testServletPut() {
		try {
			WebResponse webResponse = getPutMethodWebResponse();
			Assert.assertEquals("Unexpected response code from testing " + servletLabel + " PUT method", 405, webResponse.getResponseCode());
		} catch (Exception e) {
			log.error("Error testing " + servletLabel + " PUT method; Exception is " + e, e);
			Assert.fail("Error testing " + servletLabel + " PUT method; Exception is " + e);
		}
	}

	/**
	 * <p>Tests the servlet's POST method.</p>
	 */
	public void testServletPost() {
		try {
			WebResponse webResponse = getPostMethodWebResponse();
			Assert.assertEquals("Unexpected response code from testing " + servletLabel + " POST method", 405, webResponse.getResponseCode());
		} catch (Exception e) {
			log.error("Error testing " + servletLabel + " POST method; Exception is " + e, e);
			Assert.fail("Error testing " + servletLabel + " POST method; Exception is " + e);
		}
	}

	/**
	 * <p>Tests the servlet's DELETE method.</p>
	 */
	public void testServletDelete() {
		try {
			WebResponse webResponse = getDeleteMethodWebResponse();
			Assert.assertEquals("Unexpected response code from testing " + servletLabel + " DELETE method", 405, webResponse.getResponseCode());
		} catch (Exception e) {
			log.error("Error testing " + servletLabel + " DELETE method; Exception is " + e, e);
			Assert.fail("Error testing " + servletLabel + " DELETE method; Exception is " + e);
		}
	}

	/**
	 * <p>Generates a web response to a GET method.</p>
	 * 
	 * @return the web response to the GET method
	 * @throws Exception
	 */
	protected WebResponse getGetMethodWebResponse() throws Exception {
		return getGetMethodWebResponse(null, null);
	}

	/**
	 * <p>Generates a web response to a GET method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @return the web response to the GET method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getGetMethodWebResponse(Hashtable parms) throws Exception {
		return getGetMethodWebResponse(parms, null);
	}

	/**
	 * <p>Generates a web response to a GET method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @param path the path to use to test the servlet
	 * @return the web response to the GET method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getGetMethodWebResponse(Hashtable parms, String path) throws Exception {
		ServletRunner sr = new ServletRunner();
		sr.registerServlet(servletMapping, servletClass.getName(), parms);
		ServletUnitClient sc = sr.newClient();
		String url = "http://localhost/" + servletTestMapping;
		if (StringUtils.hasText(path)) {
			url = "http://localhost" + path;
		}
		log.debug("Testing \"" + servletMapping + "\" with URL " + url);
		WebRequest webRequest = new GetMethodWebRequest(url);
		InvocationContext ic = sc.newInvocation(webRequest);
		if (beanName != null) {
			GenericSpringTestServlet servlet = (GenericSpringTestServlet) ic.getServlet();
			servlet.setDelegate((HttpServlet) applicationContext.getBean(beanName));
		}
		ic.service();
		return ic.getServletResponse();
	}

	/**
	 * <p>Generates a web response to a PUT method.</p>
	 * 
	 * @return the web response to the PUT method
	 * @throws Exception
	 */
	protected WebResponse getPutMethodWebResponse() throws Exception {
		return getPutMethodWebResponse(null, null);
	}

	/**
	 * <p>Generates a web response to a PUT method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @return the web response to the PUT method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getPutMethodWebResponse(Hashtable parms) throws Exception {
		return getPutMethodWebResponse(parms, null);
	}

	/**
	 * <p>Generates a web response to a PUT method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @param path the path to use to test the servlet
	 * @return the web response to the PUT method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getPutMethodWebResponse(Hashtable parms, String path) throws Exception {
		ServletRunner sr = new ServletRunner();
		sr.registerServlet(servletMapping, servletClass.getName(), parms);
		ServletUnitClient sc = sr.newClient();
		String url = "http://localhost/" + servletTestMapping;
		if (StringUtils.hasText(path)) {
			url = "http://localhost" + path;
		}
		log.debug("Testing \"" + servletMapping + "\" with URL " + url);
		InputStream is = new ByteArrayInputStream(new byte[0]);
		WebRequest webRequest = new PutMethodWebRequest(url, is, "text/plain");
		InvocationContext ic = sc.newInvocation(webRequest);
		if (beanName != null) {
			GenericSpringTestServlet servlet = (GenericSpringTestServlet) ic.getServlet();
			servlet.setDelegate((HttpServlet) applicationContext.getBean(beanName));
		}
		ic.service();
		return ic.getServletResponse();
	}

	/**
	 * <p>Generates a web response to a POST method.</p>
	 * 
	 * @return the web response to the POST method
	 * @throws Exception
	 */
	protected WebResponse getPostMethodWebResponse() throws Exception {
		return getPostMethodWebResponse(null, null);
	}

	/**
	 * <p>Generates a web response to a POST method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @return the web response to the POST method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getPostMethodWebResponse(Hashtable parms) throws Exception {
		return getPostMethodWebResponse(parms, null);
	}

	/**
	 * <p>Generates a web response to a POST method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @param path the path to use to test the servlet
	 * @return the web response to the POST method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getPostMethodWebResponse(Hashtable parms, String path) throws Exception {
		ServletRunner sr = new ServletRunner();
		sr.registerServlet(servletMapping, servletClass.getName(), parms);
		ServletUnitClient sc = sr.newClient();
		String url = "http://localhost/" + servletTestMapping;
		if (StringUtils.hasText(path)) {
			url = "http://localhost" + path;
		}
		log.debug("Testing \"" + servletMapping + "\" with URL " + url);
		WebRequest webRequest = new PostMethodWebRequest(url);
		InvocationContext ic = sc.newInvocation(webRequest);
		if (beanName != null) {
			GenericSpringTestServlet servlet = (GenericSpringTestServlet) ic.getServlet();
			servlet.setDelegate((HttpServlet) applicationContext.getBean(beanName));
		}
		ic.service();
		return ic.getServletResponse();
	}

	/**
	 * <p>Generates a web response to a DELETE method.</p>
	 * 
	 * @return the web response to the DELETE method
	 * @throws Exception
	 */
	protected WebResponse getDeleteMethodWebResponse() throws Exception {
		return getDeleteMethodWebResponse(null, null);
	}

	/**
	 * <p>Generates a web response to a DELETE method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @return the web response to the DELETE method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getDeleteMethodWebResponse(Hashtable parms) throws Exception {
		return getDeleteMethodWebResponse(parms, null);
	}

	/**
	 * <p>Generates a web response to a DELETE method.</p>
	 * 
	 * @param parms the initial parameters for the servlet
	 * @param path the path to use to test the servlet
	 * @return the web response to the DELETE method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected WebResponse getDeleteMethodWebResponse(Hashtable parms, String path) throws Exception {
		ServletRunner sr = new ServletRunner();
		sr.registerServlet(servletMapping, servletClass.getName(), parms);
		ServletUnitClient sc = sr.newClient();
		String url = "http://localhost/" + servletTestMapping;
		if (StringUtils.hasText(path)) {
			url = "http://localhost" + path;
		}
		log.debug("Testing \"" + servletMapping + "\" with URL " + url);
		WebRequest webRequest = new DeleteMethodWebRequest(url);
		InvocationContext ic = sc.newInvocation(webRequest);
		if (beanName != null) {
			GenericSpringTestServlet servlet = (GenericSpringTestServlet) ic.getServlet();
			servlet.setDelegate((HttpServlet) applicationContext.getBean(beanName));
		}
		ic.service();
		return ic.getServletResponse();
	}
}
