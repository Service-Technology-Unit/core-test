package edu.ucdavis.ucdh.stu.core.test.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * <p>Generic servlet class used to replace the GenericSpringServlet
 * for testing purposes.</p>
 */
public class GenericSpringTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1;
	private HttpServlet delegate;

	/**
	 * just delegate to the "spring" servlet.
	 * 
	 * @inheritDoc
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest,
	 *	  javax.servlet.ServletResponse)
	 */
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		delegate.service(req,res);
	}

	/**
	 * @param delegate the delegate to set
	 */
	public void setDelegate(HttpServlet delegate) {
		this.delegate = delegate;
	}
}
