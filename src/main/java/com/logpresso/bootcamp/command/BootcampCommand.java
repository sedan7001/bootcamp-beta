package com.logpresso.bootcamp.command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.araqne.logdb.DriverQueryCommand;
import org.araqne.logdb.QueryCancelReason;
import org.araqne.logdb.Row;

import com.logpresso.bootcamp.model.SplunkProfile;
import com.splunk.CollectionArgs;
import com.splunk.Job;
import com.splunk.JobArgs;
import com.splunk.ResultsReaderXml;
import com.splunk.Service;

public class BootcampCommand extends DriverQueryCommand {
	private final org.slf4j.Logger slog = org.slf4j.LoggerFactory.getLogger(BootcampCommand.class);

	private SplunkProfile profile;
	private String query;
	private Service svc;
	private Job job;

	public BootcampCommand(SplunkProfile profile, String query) {
		this.profile = profile;
		this.query = query;
	}

	@Override
	public String getName() {
		return "bootcamp";
	}

	@Override
	public void onStart() {
		svc = profile.connect();
	}

	@Override
	protected void onClose() {
		if (job != null)
			job.finish();

		if (svc != null)
			svc.logout();
	}

	@Override
	protected void onCancel(QueryCancelReason reason) {
		if (job != null) {
			job.finish();
			job = null;
		}

		if (svc != null)
			svc.logout();
	}

	@Override
	public void run() {
		job = null;
		try {
			JobArgs ja = new JobArgs();
			ja.setExecutionMode(JobArgs.ExecutionMode.NORMAL);

			job = svc.getJobs().create(query, ja);
			slog.info("logpresso bootcamp: bootcamp command query[{}] start", query);

			while (!job.isDone()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}

			int resultCount = job.getResultCount();
			int offset = 0;
			int count = 500;

			InputStream is = null;
			ResultsReaderXml reader = null;

			while (offset < resultCount && !isCancelRequested()) {
				try {
					CollectionArgs outputArgs = new CollectionArgs();
					outputArgs.setCount(count);
					outputArgs.setOffset(offset);

					is = job.getResults(outputArgs);
					reader = new ResultsReaderXml(is);

					HashMap<String, String> event = null;
					while ((event = reader.getNextEvent()) != null) {
						pushPipe(new Row(new HashMap<String, Object>(event)));
					}

					offset = offset + count;
				} catch (Throwable t) {
					if (reader != null)
						try {
							reader.close();
						} catch (IOException e) {
						}
					ensureClose(is);
				}
			}
		} finally {
			if (svc != null)
				svc.logout();
		}
	}

	private void ensureClose(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (IOException e) {
			}
	}
}
