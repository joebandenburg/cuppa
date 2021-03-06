/*
 * Copyright 2015-2016 ForgeRock AS.
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

package org.forgerock.cuppa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.forgerock.cuppa.model.TestBlock;

/**
 * Holds configuration settings for Cuppa.
 */
public final class Configuration {
    List<Function<TestBlock, TestBlock>> testTransforms = new ArrayList<>();
    TestInstantiator testInstantiator = Class::newInstance;

    Configuration() {
    }

    /**
     * Sets the class that will be used to instantiate test classes. Use this function if your test classes require
     * additional steps to instantiate, such as automatic dependency injection.
     *
     * @param testInstantiator A class that instantiates test classes.
     */
    public void setTestInstantiator(TestInstantiator testInstantiator) {
        Objects.requireNonNull(testInstantiator, "Test instantiator must not be null");
        this.testInstantiator = testInstantiator;
    }

    /**
     * Register a transform. This will be called with the root test block after instantiating all test classes on the
     * classpath. Use this function to manipulate the tests and their hooks before they are executed.
     *
     * @param transform The transform. Must not be null. Must not return null.
     */
    public void registerTestTreeTransform(Function<TestBlock, TestBlock> transform) {
        Objects.requireNonNull(transform, "Transform must not be null");
        testTransforms.add(transform);
    }
}
