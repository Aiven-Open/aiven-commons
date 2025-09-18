
/*
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information    *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the      *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                *
 *   http://www.apache.org/licenses/LICENSE-2.0         *
 *                                *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY     *
 * KIND, either express or implied.  See the License for the  *
 * specific language governing permissions and limitations    *
 * under the License.                       *
 */


package io.aiven.commons.kafka.config;

import org.apache.kafka.common.config.ConfigDef;

public class ExtendedConfigKey extends ConfigDef.ConfigKey {

  /**
   * The deprecation information. May be {@code null}.
   */
  public final DeprecatedInfo deprecated;

  /**
   * The version in which this attribute was added. May be {@code null}.
   */
  public final String since;

  public ExtendedConfigKey(Builder<?> builder) {
    super(builder.name, builder.type, builder.defaultValue, builder.validator, builder.importance, builder.documentation, builder.group,
        builder.orderInGroup, builder.width, builder.displayName, builder.getDependents(), builder.recommender, builder.internalConfig);
    this.deprecated = builder.deprecated;
    this.since = builder.since;
  }

  public final String getDeprecationMessage() {
    return isDeprecated() ? deprecated.getDescription() : "";
  }

  public final String getSince() {
    return since;
  }

  public final boolean isDeprecated() {
    return deprecated != null;
  }

  public static <T extends Builder<?>> Builder<T> builder(String name) {
    return new Builder<>(name);
  }


  public static class Builder<T extends Builder<?>> extends ConfigKeyBuilder<T> {
    private DeprecatedInfo deprecated;
    private String since;

    protected Builder(String name) {
      super(name);
    }

    @Override
    public ExtendedConfigKey build() {
      return new ExtendedConfigKey(this);
    }

    public final T deprecatedInfo(final DeprecatedInfo.Builder deprecatedInfoBuilder) {
      return deprecatedInfo(deprecatedInfoBuilder.get());
    }

    public final T deprecatedInfo(final DeprecatedInfo deprecatedInfo) {
      this.deprecated = deprecatedInfo;
      return self();
    }

    public final T since(final String since) {
      this.since = since;
      return self();
    }
  }

}
