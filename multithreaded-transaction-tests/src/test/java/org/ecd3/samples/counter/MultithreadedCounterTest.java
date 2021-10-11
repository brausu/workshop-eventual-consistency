package org.ecd3.samples.counter;


import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for testing thread-safety of {@link org.ecd3.samples.counter.SimpleCounter}, and
 * {@link ThreadSafeCounter} respectively. This simple test class deomonstrates usage of the
 * MultithreadedTC testing framework: https://www.cs.umd.edu/projects/PL/multithreadedtc/overview.html.
 */
public class MultithreadedCounterTest extends MultithreadedTestCase {

  private static final Logger logger = LoggerFactory.getLogger(MultithreadedCounterTest.class);

  private Counter counterUnderTest;

  @Override
  public void initialize() {
    // uncomment to switch between the thread-safe and the non-thread-safe implementation of the counter.
//    counterUnderTest = new SimpleCounter(0);
    counterUnderTest = new ThreadSafeCounter(0);
  }

  public void thread1() throws InterruptedException {
    counterUnderTest.increment();
  }

  public void thread2() throws InterruptedException {
    counterUnderTest.increment();
  }

  @Override
  public void finish() {
    // as two threads invoke one increment operation each, we expect a total counter value of 2 after
    // terminaiton of both threads.
    assertEquals(2, counterUnderTest.getCount());
  }

  @Test
  public void testConcurrentCounterIncrements() throws Throwable {
    // MultiThreadedTC will run both threads in 1000 different interleavings
    TestFramework.runManyTimes(new MultithreadedCounterTest(), 1000);
  }

}
