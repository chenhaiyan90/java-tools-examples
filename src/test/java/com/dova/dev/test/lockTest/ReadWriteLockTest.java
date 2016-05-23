package com.dova.dev.test.lockTest;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by liuzhendong on 16/5/22.
 */
public class ReadWriteLockTest {

    ReadWriteLock lock = new ReentrantReadWriteLock();
}
