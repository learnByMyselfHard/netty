/*
 * Copyright 2022 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty5.util.concurrent;

import java.util.concurrent.Executor;

/**
 * {@link Executor} which is aware of the {@link Thread} that does the execution of the submitted work.
 */
public interface ThreadAwareExecutor extends Executor {

    /**
     * Returns {@code true} if the calling {@link Thread} is the same as the {@link Thread} that is used by the
     * {@link Executor} for execution the submitted work.
     *
     * @return  {@code true} if the calling {@link Thread} is the same as the {@link Thread} of the {@link Executor},
     *          {@code false} otherwise.
     */
    boolean inExecutorThread();
}
