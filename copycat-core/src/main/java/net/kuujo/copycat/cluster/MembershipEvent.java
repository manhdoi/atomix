/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.cluster;

import net.kuujo.copycat.Event;

/**
 * Cluster membership event.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class MembershipEvent implements Event {
  private final Cluster cluster;
  private final Member member;

  public MembershipEvent(Cluster cluster, Member member) {
    this.cluster = cluster;
    this.member = member;
  }

  /**
   * Returns the cluster instance.
   *
   * @return The cluster instance.
   */
  public Cluster cluster() {
    return cluster;
  }

  /**
   * Returns the member instance.
   *
   * @return The member instance.
   */
  public Member member() {
    return member;
  }

}
