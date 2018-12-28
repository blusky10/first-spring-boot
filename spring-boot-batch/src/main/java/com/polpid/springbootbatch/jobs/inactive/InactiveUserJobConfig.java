package com.polpid.springbootbatch.jobs.inactive;

import com.polpid.springbootbatch.domain.User;
import com.polpid.springbootbatch.domain.enums.UserStatus;
import com.polpid.springbootbatch.jobs.inactive.listener.InactiveJobListener;
import com.polpid.springbootbatch.jobs.inactive.listener.InactiveStepListener;
import com.polpid.springbootbatch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final static int CHUNK_SIZE = 5;

    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory,
                               InactiveJobListener inactiveJobListener,
                               Step inactiveJobStep,
                               Flow inactiveJobFlow){
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .listener(inactiveJobListener)
                .start(inactiveJobFlow)
                .end()

                .build();
    }

    @Bean
    public Flow inactiveJobFlow(Step inactiveJobStep){
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("inactiveJobFlow");

        return flowBuilder
                .start(new InactiveJobExecutionDecider())
                .on(FlowExecutionStatus.FAILED.getName())
                .end()
                .on(FlowExecutionStatus.COMPLETED.getName()).to(inactiveJobStep)
                .end();
    }


    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory,
                                ListItemReader<User> listInactiveUserReader,
                                InactiveStepListener inactiveStepListener){
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User>chunk(CHUNK_SIZE)
                .reader(listInactiveUserReader)
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .listener(inactiveStepListener)
                .build();
    }
//
//
//    @Bean
//    @StepScope
//    public QueueItemReader<User> inactiveUserReader(){
//        List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);
//        return new QueueItemReader<>(oldUsers);
//    }
//
    @Bean
    @StepScope
    public ListItemReader<User> listInactiveUserReader(UserRepository userRepository){
        List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE);

        return new ListItemReader<>(oldUsers);
    }

//    @Bean
//    @StepScope
//    public ListItemReader<User> inactiveUserReaderWithParams(@Value("#{jobParameters[nowDate]}") Date nowDate, UserRepository userRepository){
//
//        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());
//        List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1), UserStatus.ACTIVE);
//
//        return new ListItemReader<>(oldUsers);
//    }
//
//    @Bean(destroyMethod="")
//    @StepScope
//    public JpaPagingItemReader<User> inactiveUserJpaReader() {
//        JpaPagingItemReader<User> jpaPagingItemReader = new JpaPagingItemReader<>();
//        jpaPagingItemReader.setQueryString("select u from User as u where u.createdDate < :createdDate and u.status = :status");
//        Map<String, Object> map = new HashMap<>();
//        LocalDateTime now = LocalDateTime.now();
//        map.put("createdDate", now.minusYears(1));
//        map.put("status", UserStatus.ACTIVE);
//        jpaPagingItemReader.setParameterValues(map);
//        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
//        jpaPagingItemReader.setPageSize(CHUNK_SIZE);
//        return jpaPagingItemReader;
//    }

    public ItemProcessor<User, User> inactiveUserProcessor(){
        return User::setInactive;
    }

//    public ItemWriter<User> inactiveUserWriter(){
//        return ((List<? extends User> users) -> userRepository.saveAll(users));
//    }

    private JpaItemWriter<User> inactiveUserWriter() {
        JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
