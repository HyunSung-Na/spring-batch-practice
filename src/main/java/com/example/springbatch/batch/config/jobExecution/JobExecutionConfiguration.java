package com.example.springbatch.batch.config.jobExecution;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// JobExecution 은 JobInstance 와 부모 자식 관계
// JobExecution 에서 Status 가 complete 면 2번째 실행할 때는 에러가 발생하게 된다.
// 만약 Job 에서 에러가 난다면 Status 가 Failed 로 되고
// 다시 실행해도 에러가 발생하지 않고 새로운 Execution 을 생성합니다

@Configuration
@RequiredArgsConstructor
public class JobExecutionConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job JobParameter() {
        return jobBuilderFactory.get("JobParameter")
                .start(jobExecutionStep1())
                .next(jobExecutionStep2())
                .build();
    }

    @Bean
    public Step jobExecutionStep2() {
        return stepBuilderFactory.get("jobExecutionStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("=======================");
                        System.out.println("Step 2 run !!");
                        System.out.println("=======================");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step jobExecutionStep1() {
        return stepBuilderFactory.get("jobExecutionStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("=======================");
                        System.out.println("Hello Spring Batch !!");
                        System.out.println("=======================");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
