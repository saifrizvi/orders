package com.sbm.lob.utils;

import java.util.concurrent.atomic.AtomicLong;

public class Generator {
   private final static AtomicLong counter = new AtomicLong();

   public static long getId() {
      return counter.incrementAndGet();
   }
}