/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.components.model;

import com.silverpeas.util.PathTestUtil;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import org.dbunit.IDatabaseTester;
import org.dbunit.JndiBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ehugonnet
 */
public class SilverpeasJndiCase extends JndiBasedDBTestCase {
  
  private String jndiName = "";
  private final String dataSetFileName;
  private final String ddlFile;
  private final DatabaseOperation tearDownOperation;
  private static final Logger logger = LoggerFactory.getLogger(SilverpeasJndiCase.class);
  
  
   public SilverpeasJndiCase(String dataSetFileName, String ddlFile) {
    this(dataSetFileName, ddlFile, DatabaseOperation.DELETE_ALL);
  }
  
  public SilverpeasJndiCase(String dataSetFileName, String ddlFile, DatabaseOperation tearDownOperation) {
    this.dataSetFileName = dataSetFileName;
    this.ddlFile = ddlFile;
    this.tearDownOperation = tearDownOperation;
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  protected DatabaseOperation getTearDownOperation() throws Exception {
    return tearDownOperation;
  }

  @Override
  protected DatabaseOperation getSetUpOperation() throws Exception {
    return DatabaseOperation.CLEAN_INSERT;
  }


  @Override
  protected String getLookupName() {
    return jndiName;
  }

  @Override
  protected IDataSet getDataSet() throws Exception {
    ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSet(
        this.getClass().getClassLoader().getResourceAsStream(this.dataSetFileName)));
    dataSet.addReplacementObject("[NULL]", null);
    return dataSet;
  }

  @Override
  protected Properties getJNDIProperties() {
    Properties env = new Properties();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
    try {
      if (PathTestUtil.class.getClassLoader().getResource("jndi.properties") != null) {
        env.load(PathTestUtil.class.getClassLoader().getResourceAsStream("jndi.properties"));
      }
    } catch (IOException ex) {
      logger.error("", ex);
    }
    return env;
  }
  
  /**
   * Creates the directory for JNDI files ystem provider
   * @throws IOException
   */
  protected void prepareJndi() throws IOException {
    Properties jndiProperties = getJNDIProperties();
    String jndiDirectoryPath = jndiProperties.getProperty(Context.PROVIDER_URL).substring(7);
    File jndiDirectory = new File(jndiDirectoryPath);
    if (!jndiDirectory.exists()) {
      jndiDirectory.mkdirs();
      jndiDirectory.mkdir();
    }
  }
  
  public void configureJNDIDatasource() throws IOException, NamingException {
    prepareJndi();
    Properties env = new Properties();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
    InitialContext ic = new InitialContext(env);
    Properties props = new Properties();
    props.load(SilverpeasJndiCase.class.getClassLoader().getResourceAsStream("jdbc.properties"));
    // Construct BasicDataSource reference
    Reference ref = new Reference("javax.sql.DataSource",
        "org.apache.commons.dbcp.BasicDataSourceFactory", null);
    ref.add(new StringRefAddr("driverClassName", props.getProperty("driverClassName")));
    ref.add(new StringRefAddr("url", props.getProperty("url")));
    ref.add(new StringRefAddr("username", props.getProperty("username")));
    ref.add(new StringRefAddr("password", props.getProperty("password")));
    ref.add(new StringRefAddr("maxActive", "4"));
    ref.add(new StringRefAddr("maxWait", "5000"));
    ref.add(new StringRefAddr("removeAbandoned", "true"));
    ref.add(new StringRefAddr("removeAbandonedTimeout", "5000"));
    jndiName = props.getProperty("jndi.name");
    rebind(ic, jndiName, ref);
    ic.rebind(jndiName, ref);
  }
  
    /**
   * Workaround to be able to use Sun's JNDI file system provider on Unix
   * @param ic : the JNDI initial context
   * @param jndiName : the binding name
   * @param ref : the reference to be bound
   * @throws NamingException
   */
  protected void rebind(InitialContext ic, String jndiName, Reference ref) throws NamingException {
    Context currentContext = ic;
    StringTokenizer tokenizer = new StringTokenizer(jndiName, "/", false);
    while (tokenizer.hasMoreTokens()) {
      String name = tokenizer.nextToken();
      if (tokenizer.hasMoreTokens()) {
        try {
          currentContext = (Context) currentContext.lookup(name);
        } catch (javax.naming.NameNotFoundException nnfex) {
          currentContext = currentContext.createSubcontext(name);
        }
      } else {
        currentContext.rebind(name, ref);
      }
    }
  }

  @Override
  public IDatabaseTester getDatabaseTester() throws Exception {
    return super.getDatabaseTester();
  }

  public String getDdlFile() {
    return this.ddlFile;
  }
  
 

}
