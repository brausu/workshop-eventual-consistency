package org.ecd3.samples.counter;

public class ThreadSafeCounter implements Counter {

  private int count = 0;

  public ThreadSafeCounter(int initialCount) {
    this.count = initialCount;
  }

  public synchronized void increment() {
    this.count  = count +1;
  }

  public int getCount() {
    return count;
  }

}
