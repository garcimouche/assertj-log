/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p>
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.log;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 * @param <A> Class of appender.
 * @param <L> Class of logger.
 *
 * @author Fabien DUMINY
 */
@RunWith(Theories.class)
abstract public class AbstractListAppenderTest<A, L> {
    private A appender;
    private Appender logs;

    private final LogLevelMap<LogMethod<L>> LOG_METHODS = createLogMethodMap();

    protected abstract LogLevelMap<LogMethod<L>> createLogMethodMap();

    protected abstract A createAppender(Appender logs);

    protected abstract void removeAppender(A appender);

    protected abstract L getLogger(String loggerName);

    @SuppressWarnings("unchecked")
    @Before
    public final void setUp() {
        logs = mock(Appender.class);
        appender = createAppender(logs);
    }

    @After
    public final void tearDown() {
        removeAppender(appender);
    }

    @Theory
    public final void testLog(LogLevel level, Messages message, Throwables throwableEnum) throws Exception {
        System.out.println("testLog(" + level + ", " + message + ", " + throwableEnum + ")");
        final String loggerName = getClass().getName();

        LOG_METHODS.get(level).log(getLogger(loggerName), message.getMessage(), throwableEnum.getThrowable());

        verify(logs).append(eq(loggerName), eq(level), eq(message.getMessage()), eq(throwableEnum.getThrowable()));
        verifyNoMoreInteractions(logs);
    }
}