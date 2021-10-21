package com.example.springbatch.batch.config.jobParameter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job JobParameter() {
        return jobBuilderFactory.get("JobParameter")
                .start(jobParameterStep1())
                .next(jobParameterStep2())
                .build();
    }

    @Bean
    public Step jobParameterStep2() {
        return stepBuilderFactory.get("jobParameterStep2")
                .tasklet(new Tasklet() {

                    // StepContribution, ChunkContext 에서 JobParameters 를 참조할 수 있다.
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

                        JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
                        String name = jobParameters.getString("name");
                        Long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");
                        Double age = jobParameters.getDouble("age");

                        System.out.println("=======================");
                        System.out.println("Step 2 run !!");
                        System.out.print(name + " " + seq + " " + date + " " + age);
                        System.out.println("=======================");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step jobParameterStep1() {
        return stepBuilderFactory.get("jobParameterStep1")
                .tasklet(new Tasklet() {

                    // StepContribution, ChunkContext 에서 JobParameters 를 참조할 수 있다.
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

                        // chunkContext 는 map 으로 반환됨
                        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

                        Object name = jobParameters.get("name");

                        System.out.println("=======================");
                        System.out.println("Hello Spring Batch !!");
                        System.out.print("name : " + name);
                        System.out.println("=======================");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
