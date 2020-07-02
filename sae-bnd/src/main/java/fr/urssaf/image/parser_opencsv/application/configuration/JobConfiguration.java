package fr.urssaf.image.parser_opencsv.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class JobConfiguration {

   @Value("${pool.max.size}")
   private String nbreThread;

   @Bean(name = "bnd_thread_pool")
   public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
      final int max = Integer.parseInt(nbreThread);
      final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(max);
      executor.setMaxPoolSize(max);
      executor.setThreadNamePrefix("Bnd_task_executor_thread_");
      executor.initialize();

      return executor;
   }
}
