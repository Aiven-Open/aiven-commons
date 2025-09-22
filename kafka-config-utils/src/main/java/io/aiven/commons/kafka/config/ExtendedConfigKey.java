
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

/**
 * An extended {@link ConfigDef.ConfigKey} that added deprecation information, and since data.
 */
public class ExtendedConfigKey extends ConfigDef.ConfigKey {

  /**
   * The deprecation information. May be {@code null}.
   */
  public final DeprecatedInfo deprecated;

  /**
   * The version in which this attribute was added. May be {@code null}.
   */
  public final String since;

  /**
   * The constructor called by the builder.
   * @param builder the builder.
   */
  private ExtendedConfigKey(Builder<?> builder) {
    super(builder.name, builder.type, builder.defaultValue, builder.validator, builder.importance, builder.documentation, builder.group,
        builder.orderInGroup, builder.width, builder.displayName, builder.getDependents(), builder.recommender, builder.internalConfig);
    this.deprecated = builder.deprecated;
    this.since = builder.since == null ? "" : builder.since;
  }

  /**
   * Gets the deprecation message.
   * @return If the deprecation was set, returns {@link DeprecatedInfo#getDescription()} otherwise return an empty string.
   */
  public final String getDeprecationMessage() {
    return isDeprecated() ? deprecated.getDescription() : "";
  }

  /**
   * Gets the value of since data element.
   * @return the value of since if it was set, an empty string otherwise.
   */
  public final String getSince() {
    return since;
  }

  /**
   * Get the deprecated flag.
   * @return {@code true} if this key is deprecated, {@code false} otherwise.
   */
  public final boolean isDeprecated() {
    return deprecated != null;
  }

  /**
   * Creates a builder for the ExtendedConfigKey.
   * @param name the name for the resulting key.
   * @return the builder.
   * @param <T> the type of the returned builder.
   */
  public static <T extends Builder<?>> Builder<T> builder(String name) {
    return new Builder<>(name);
  }

  /**
   * The builder for an ExtendedConfigKey.
   * @param <T> the type of the file ConfigKey instance.
   */
  public static class Builder<T extends Builder<?>> extends ConfigKeyBuilder<T> {
    /**
     * The deprecated info.
     */
    private DeprecatedInfo deprecated;
    /**
     * The since value.
     */
    private String since;

    /**
     * The builder.
     * @param name the name of the final ConfigKey
     */
    protected Builder(String name) {
      super(name);
    }

    @Override
    public ExtendedConfigKey build() {
      return new ExtendedConfigKey(this);
    }

    /**
     * Sets the deprecation info.
     * @param deprecatedInfoBuilder the builder for the DeprecatedInfo
     * @return this
     */
    public final T deprecatedInfo(final DeprecatedInfo.Builder deprecatedInfoBuilder) {
      return deprecatedInfo(deprecatedInfoBuilder.get());
    }

    /**
     * Sets the deprecation info.
     * @param deprecatedInfo the Deprecated info to use.
     * @return this
     */
    public final T deprecatedInfo(final DeprecatedInfo deprecatedInfo) {
      this.deprecated = deprecatedInfo;
      return self();
    }

    /**
     * Sets the since value.
     * @param since the since value.
     * @return this.
     */
    public final T since(final String since) {
      this.since = since;
      return self();
    }
  }
}
