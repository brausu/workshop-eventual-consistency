package org.ecd3.samples.counter;

/**
 * Simple counter implementation. Note that the counter is NOT thread-safe.
 */
public class SimpleCounter implements Counter {

  private int count = 0;

  public SimpleCounter(int initialCount) {
    this.count = initialCount;
  }

  @Override
  public void increment() {
    this.count  = count +1;
  }

  @Override
  public int getCount() {
    return count;
  }
}
