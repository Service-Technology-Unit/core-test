package edu.ucdavis.ucdh.stu.core.test.dao;

import org.apache.commons.logging.Log;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * Base class for Dao TestCases.
 */
@ContextConfiguration(locations = {"/context/applicationContext*.xml"})
public abstract class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests {
	protected final Log log = logger;
}
