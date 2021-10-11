package org.ecd3.samples.transactions;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Test template for running multiple transactions in different threads in the context of one junit test.
 * The framework MultiThreadedTC will run these transactions in 1000 different interleavings thereby
 * pointing out potential concurrency anomalies or deadlocks.
 *
 * As this test is only a template, it is currently disabled.
 *
 * Note: You need to have docker installed on your machine: https://docs.docker.com/get-docker/
 */
@Disabled
@Testcontainers
public class MultithreadedTransactionsTest extends MultithreadedTestCase {

  private static final Logger logger = LoggerFactory.getLogger(MultithreadedTransactionsTest.class);

  // create a database container for testing; The testcontainers project has images for a vast number of different dbs.
  @Container
  private static final MySQLContainer<?> dbContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"))
      .withDatabaseName("suits_accounts")
      .withUsername("root")
      .withPassword("")
      .withLogConsumer(new Slf4jLogConsumer(logger));

  // place your database init script under test resources.
  private static final String DB_INIT_SCRIPT = "/mysql_init.sql";

  private DataSource db;

  // freshly initialize the database prior to each run of concurrent transaction execution.
  @Override
  public void initialize() {
    initializeDataSource();
    initializeDatabaseContents();
  }

  public void thread1() throws InterruptedException {
    // run the first transaction
    // TODO: invoke your transaction
  }

  public void thread2() throws InterruptedException {
    // run the second transaction
    // TODO: invoke your transaction
  }

  @Override
  public void finish() {
    // verify your assertions, e.g. check whether the database state is as expected.
    // TODO: your assertions

    // finally close the data source.
    ((HikariDataSource) this.db).close();
  }

  @Test
  public void testConcurrentTransactionCommits() throws Throwable {
    // MultiThreadedTC will run both transactions in 1000 different interleavings
    TestFramework.runManyTimes(new MultithreadedTransactionsTest(), 1000);
  }

  private void initializeDatabaseContents() {
    Connection connection = null;
    InputStream scriptStream = null;
    Reader reader = null;
    try {

      // run the database schema initialization script and populate database tables.
      connection = db.getConnection();
      scriptStream = this.getClass().getResourceAsStream(DB_INIT_SCRIPT);
      reader = new BufferedReader(new InputStreamReader(scriptStream));
      ScriptRunner sr = new ScriptRunner(connection);
      sr.runScript(reader);

    } catch (SQLException throwables) {
      logger.error("Couldn't get db connection", throwables);

    } finally {
      if(connection != null) {
        try {
          connection.close();
        } catch (SQLException throwables) {
          logger.error("Failed to close db connection", throwables);
        }
      }
      if(reader!= null) {
        try {
          reader.close();
        } catch (IOException e) {
          logger.error("Failed to close file reader.", e);
        }
      }
      if(scriptStream != null) {
        try {
          scriptStream.close();
        } catch (IOException e) {
          logger.error("Failed to close file input stream.", e);
        }
      }
    }
  }

  /**
   * Initializes the datasource and the connection pool.
   */
  private void initializeDataSource() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(dbContainer.getJdbcUrl());
    hikariConfig.setUsername(dbContainer.getUsername());
    hikariConfig.setPassword(dbContainer.getPassword());
    hikariConfig.setDriverClassName(dbContainer.getDriverClassName());
    this.db = new HikariDataSource(hikariConfig);
  }
}
