/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.counter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

import io.atomix.core.impl.Metadata;

/**
 * Counter service.
 */
public class CounterService extends AbstractCounterService {
  private final AtomicLong counter = new AtomicLong();

  @Override
  public SetResponse set(SetRequest request) {
    return SetResponse.newBuilder()
        .setMetadata(Metadata.newBuilder()
            .setIndex(getCurrentIndex())
            .build())
        .setPreviousValue(counter.getAndSet(request.getValue()))
        .build();
  }

  @Override
  public GetResponse get(GetRequest request) {
    return GetResponse.newBuilder()
        .setMetadata(Metadata.newBuilder()
            .setIndex(getCurrentIndex())
            .build())
        .setValue(counter.get())
        .build();
  }

  @Override
  public CheckAndSetResponse checkAndSet(CheckAndSetRequest request) {
    return CheckAndSetResponse.newBuilder()
        .setMetadata(Metadata.newBuilder()
            .setIndex(getCurrentIndex())
            .build())
        .setSucceeded(counter.compareAndSet(request.getExpect(), request.getUpdate()))
        .build();
  }

  @Override
  public IncrementResponse increment(IncrementRequest request) {
    long previousValue;
    if (request.getDelta() == 0) {
      previousValue = counter.getAndIncrement();
    } else {
      previousValue = counter.getAndAdd(request.getDelta());
    }
    return IncrementResponse.newBuilder()
        .setMetadata(Metadata.newBuilder()
            .setIndex(getCurrentIndex())
            .build())
        .setPreviousValue(previousValue)
        .setNextValue(counter.get())
        .build();
  }

  @Override
  public DecrementResponse decrement(DecrementRequest request) {
    long previousValue;
    if (request.getDelta() == 0) {
      previousValue = counter.getAndDecrement();
    } else {
      previousValue = counter.getAndAdd(-request.getDelta());
    }
    return DecrementResponse.newBuilder()
        .setMetadata(Metadata.newBuilder()
            .setIndex(getCurrentIndex())
            .build())
        .setPreviousValue(previousValue)
        .setNextValue(counter.get())
        .build();
  }

  @Override
  public void snapshot(OutputStream output) throws IOException {
    AtomicCounterSnapshot.newBuilder()
        .setCounter(counter.get())
        .build()
        .writeTo(output);
  }

  @Override
  public void install(InputStream input) throws IOException {
    AtomicCounterSnapshot snapshot = AtomicCounterSnapshot.parseFrom(input);
    counter.set(snapshot.getCounter());
  }
}