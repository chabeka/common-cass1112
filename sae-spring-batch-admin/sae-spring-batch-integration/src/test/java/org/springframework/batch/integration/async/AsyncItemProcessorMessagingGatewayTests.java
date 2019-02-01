/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.integration.async;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Configuration
public class AsyncItemProcessorMessagingGatewayTests {

  private final AsyncItemProcessor<String, String> processor = new AsyncItemProcessor<>();

  private final StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(new JobParametersBuilder().addLong("factor", 2L).toJobParameters());;

  @Rule
  public MethodRule rule = new MethodRule() {
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          StepScopeTestUtils.doInStepScope(stepExecution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
              try {
                base.evaluate();
              }
              catch (final Exception e) {
                throw e;
              }
              catch (final Throwable e) {
                throw new Error(e);
              }
              return null;
            }
          });
        };
      };
    }
  };

  @Autowired
  private ItemProcessor<String, String> delegate;

  @Test
  public void testMultiExecution() throws Exception {
    processor.setDelegate(delegate);
    processor.setTaskExecutor(new SimpleAsyncTaskExecutor());
    final List<Future<String>> list = new ArrayList<>();
    for (int count = 0; count < 10; count++) {
      list.add(processor.process("foo" + count));
    }
    for (final Future<String> future : list) {
      final String value = future.get();
      /**
       * This delegate is a Spring Integration MessagingGateway. It can
       * easily return null because of a timeout, but that will be treated
       * by Batch as a filtered item, whereas it is really more like a
       * skip. So we have to throw an exception in the processor if an
       * unexpected null value comes back.
       */
      assertNotNull(value);
      assertTrue(value.matches("foo.*foo.*"));
    }
  }

  @Bean
  public StepScope stepScope() {
    StepSynchronizationManager.register(stepExecution);
    final StepScope stepScope = new StepScope();
    stepScope.setProxyTargetClass(true);
    return stepScope;
  }

  @Component(value = "doubler")
  @Scope(value = "step", proxyMode = ScopedProxyMode.DEFAULT)
  public static class Doubler {
    private int factor = 1;

    @Value("#{jobParameters['factor']}")
    public void setFactor(final int factor) {
      this.factor = factor;
    }

    public String cat(String value) {
      for (int i = 1; i < factor; i++) {
        value += value;
      }
      return value;
    }
  }

}
