/*
 * Copyright 2009-2010 the original author or authors.
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
/**
 * 
 */
package org.springframework.batch.admin.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.support.PropertiesConverter;

public class JobExecutionInfo {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");

	private Long id;

	private int stepExecutionCount;

	private Long jobId;

	private String jobName;

	private String startDate = "";

	private String startTime = "";

	private String duration = "";

	private JobExecution jobExecution;

	private String jobParameters;

	private boolean restartable = false;

	private boolean abandonable = false;

	private boolean stoppable = false;

	private JobParametersConverter converter = new DefaultJobParametersConverter();

	private final TimeZone timeZone;
	
	private String executionContext;

	public JobExecutionInfo(JobExecution jobExecution, TimeZone timeZone) {

		this.jobExecution = jobExecution;
		this.timeZone = timeZone;
		this.id = jobExecution.getId();
		this.jobId = jobExecution.getJobId();
		this.stepExecutionCount = jobExecution.getStepExecutions().size();

		JobInstance jobInstance = jobExecution.getJobInstance();
		if (jobInstance != null) {
			this.jobName = jobInstance.getJobName();
			Properties properties = converter.getProperties(jobInstance.getJobParameters());
			this.jobParameters = PropertiesConverter.propertiesToString(properties);
			BatchStatus status = jobExecution.getStatus();
			this.restartable = status.isGreaterThan(BatchStatus.STOPPING) && status.isLessThan(BatchStatus.ABANDONED);
			this.abandonable = status.isGreaterThan(BatchStatus.STARTED) && status!=BatchStatus.ABANDONED;
			this.stoppable  = status.isLessThan(BatchStatus.STOPPING);
		}
		else {
			this.jobName = "?";
			this.jobParameters = null;
		}

		// Duration is always in GMT
		durationFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		// The others can be localized
		timeFormat.setTimeZone(timeZone);
		dateFormat.setTimeZone(timeZone);
		if (jobExecution.getStartTime() != null) {
			this.startDate = dateFormat.format(jobExecution.getStartTime());
			this.startTime = timeFormat.format(jobExecution.getStartTime());
			Date endTime = jobExecution.getEndTime() != null ? jobExecution.getEndTime() : new Date();
			this.duration = durationFormat.format(new Date(endTime.getTime() - jobExecution.getStartTime().getTime()));
		}
		
		if (jobExecution.getExecutionContext() != null) {
		   this.executionContext = jobExecution.getExecutionContext().toString();
		   
		} else {
		   this.executionContext = "NONE"; 
		}

	}
	
	public String getServer() {
	   ExecutionContext context = jobExecution.getExecutionContext();
	   if (context.containsKey("serveur")) return context.getString("serveur");
	   return "";
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getName() {
		return jobName;
	}

	public Long getId() {
		return id;
	}

	public int getStepExecutionCount() {
		return stepExecutionCount;
	}
	
	public Long getJobId() {
		return jobId;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getDuration() {
		return duration;
	}

	public JobExecution getJobExecution() {
		return jobExecution;
	}

	public boolean isRestartable() {
		return restartable;
	}
	
	public boolean isAbandonable() {
		return abandonable;
	}
	
	public boolean isStoppable() {
		return stoppable;
	}

	public String getJobParameters() {
		return jobParameters;
	}

	public String getExecutionContext() {
	   return executionContext;
	}
}