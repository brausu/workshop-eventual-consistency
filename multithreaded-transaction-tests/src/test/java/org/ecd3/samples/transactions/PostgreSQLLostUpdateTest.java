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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Runs two concurrent withdraw transactions in 1000 different interleavings against a PostgreSQL database.
 * Use the private field <code>ISOLATION_LEVEL_UNDER_TEST</code> to configure the isolation level to use.
 *
 * At the end of each run it is verified whether the current balance value reflects both withdraw operations.
 *
 * You need to set a timeout parameter of MultiThreadedTC: <code>-Dtunit.runLimit=20</code> to successfully execute
 * this test. This test might run for several minutes until it fails either because the test assertion fails, or
 * because of a non-serializable access exception. Therefore, you have to enable it manually (it is currently disabled).
 *
 * Note: You need to have docker installed on your machine: https://docs.docker.com/get-docker/
 */
@Disabled
@Testcontainers
public class PostgreSQLLostUpdateTest extends MultithreadedTestCase {

  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLLostUpdateTest.class);

//  private static final int ISOLATION_LEVEL_UNDER_TEST = Connection.TRANSACTION_READ_UNCOMMITTED;

//  private static final int ISOLATION_LEVEL_UNDER_TEST = Connection.TRANSACTION_READ_COMMITTED;

  private static final int ISOLATION_LEVEL_UNDER_TEST = Connection.TRANSACTION_REPEATABLE_READ;

//  private static final int ISOLATION_LEVEL_UNDER_TEST = Connection.TRANSACTION_SERIALIZABLE;


  @Container
  private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:9.6.12"))
      .withDatabaseName("suits_accounts")
      .withUsername("root")
      .withPassword("")
      .withLogConsumer(new Slf4jLogConsumer(logger));

  private static final String DB_INIT_SCRIPT = "/postgresql_init.sql";

  private DataSource db;

  private static final String ACCOUNT_ID = "67152732-8cc9-45d8-adc2-d5f835daac6d";

  @Override
  public void initialize() {
    initializeDataSource();
    initializeDatabaseContents();
  }

  public void thread1() throws InterruptedException, SQLException {
    withdraw(ACCOUNT_ID, 2_000_000.00);
  }

  public void thread2() throws InterruptedException, SQLException {
    withdraw(ACCOUNT_ID, 3_000_000.00);
  }

  @Override
  public void finish() {
    // The balance of the account is $ 15_000_000.98
    // thread 1 will withdraw $ 2_000_000
    // thread 2 will withdraw $ 3_000_000
    // thus, the balance is expected to be $ 10_000_000.98 after both transactions complete

    Double balance = getCurrentBalance();
    Assertions.assertEquals(10_000_000.98, balance);
    ((HikariDataSource) this.db).close();
  }

  @Test
  public void testConcurrentTransactionCommits() throws Throwable {
    TestFramework.runManyTimes(new PostgreSQLLostUpdateTest(), 1000);
  }



  private void withdraw(String accountId, Double amount) throws SQLException {
    Connection connection = null;
    PreparedStatement update = null;

    try {
      connection = db.getConnection();
      connection.setTransactionIsolation(ISOLATION_LEVEL_UNDER_TEST);
      connection.setAutoCommit(false);

      Double balance = queryCurrentBalance(connection, accountId);

      // calculate updated balance
      balance = balance - amount;

      update = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
      update.setDouble(1, balance);
      update.setString(2, accountId);
      update.executeUpdate();

      connection.commit();

    } finally {
      if(update != null) {
        update.close();
      }
      if(connection != null) {
        connection.close();
      }
    }
  }

  private Double getCurrentBalance() {
    Double balance = null;
    Connection connection = null;
    try {

      connection = db.getConnection();
      connection.setTransactionIsolation(ISOLATION_LEVEL_UNDER_TEST);
      balance = queryCurrentBalance(connection, ACCOUNT_ID);

    } catch (SQLException throwables) {
      logger.error("Couldn't get db connection", throwables);

    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException throwables) {
          logger.error("Failed to close connection", throwables);
        }
      }
    }

    return balance;
  }

  private Double queryCurrentBalance(Connection connection, String accountId) throws SQLException {
    PreparedStatement query = null;
    ResultSet resultSet = null;
    try {
      query = connection.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
      query.setString(1, accountId);
      resultSet = query.executeQuery();
      resultSet.next();
      Double balance = resultSet.getDouble(1);

      return balance;
    } finally {
      if(resultSet != null) {
        resultSet.close();
      }
      if(query != null) {
        query.close();
      }
    }
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
      sr.setAutoCommit(true);
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

  private void initializeDataSource() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
    hikariConfig.setUsername(postgreSQLContainer.getUsername());
    hikariConfig.setPassword(postgreSQLContainer.getPassword());
    hikariConfig.setDriverClassName(postgreSQLContainer.getDriverClassName());
    this.db = new HikariDataSource(hikariConfig);
  }

}

