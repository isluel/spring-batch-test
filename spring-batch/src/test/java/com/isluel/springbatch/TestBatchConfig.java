package com.isluel.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestExecutionListeners;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@TestExecutionListeners(StepScopeTestExecutionListener.class)
public class TestBatchConfig {
}
