package com.logpresso.bootcamp.logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.araqne.confdb.Config;
import org.araqne.confdb.ConfigDatabase;
import org.araqne.confdb.ConfigService;
import org.araqne.confdb.Predicates;
import org.araqne.log.api.AbstractLogger;
import org.araqne.log.api.LastPosition;
import org.araqne.log.api.LastPositionHelper;
import org.araqne.log.api.LoggerFactory;
import org.araqne.log.api.LoggerSpecification;
import org.araqne.log.api.LoggerStartReason;
import org.araqne.log.api.LoggerStopReason;
import org.araqne.log.api.Reconfigurable;
import org.araqne.log.api.SimpleLog;

import com.logpresso.bootcamp.model.SplunkProfile;
import com.splunk.CollectionArgs;
import com.splunk.Event;
import com.splunk.Job;
import com.splunk.JobArgs;
import com.splunk.ResultsReaderXml;
import com.splunk.Service;

public class BootcampLogger extends AbstractLogger implements Reconfigurable {
	private final org.slf4j.Logger slog = org.slf4j.LoggerFactory.getLogger(BootcampLogger.class);

	private ConfigService conf;
	private SplunkProfile profile;
	private String query;
	private String keyField;
	private boolean onStop = false;

	public BootcampLogger(LoggerSpecification spec, LoggerFactory factory, ConfigService conf) {
		super(spec, factory);
		File dataDir = new File(System.getProperty("araqne.data.dir"), "araqne-log-api");
		dataDir.mkdirs();

		// try migration at boot
		File oldLastFile = getLastLogFile(dataDir);
		if (oldLastFile.exists()) {
			Map<String, LastPosition> lastPositions = LastPositionHelper.readLastPositions(oldLastFile);
			setStates(LastPositionHelper.serialize(lastPositions));
			oldLastFile.renameTo(new File(oldLastFile.getAbsolutePath() + ".migrated"));
		}

		this.conf = conf;
		applyConfig();
	}

	@Override
	protected void onStop(LoggerStopReason reason) {
		this.onStop = true;
	}

	@Override
	protected void onStart(LoggerStartReason reason) {
		this.onStop = false;
	}

	private void applyConfig() {
		Map<String, String> configs = getConfigs();

		String profileName = configs.get("profile");
		ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
		Config c = db.findOne(SplunkProfile.class, Predicates.field("name", profileName));
		if (c == null)
			throw new IllegalStateException("profile-not-found");

		this.profile = c.getDocument(SplunkProfile.class);

		this.query = configs.get("query");

		this.keyField = configs.get("key_field");
		if (keyField == null || keyField.isEmpty())
			keyField = "_cd";
	}

	protected File getLastLogFile(File dataDir) {
		return new File(dataDir, "bootcamp-" + getName() + ".lastlog");
	}

	@Override
	public void onConfigChange(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
		if (!oldConfigs.get("profile").equals(newConfigs.get("profile"))
				|| !oldConfigs.get("query").equals(newConfigs.get("query"))
				|| !oldConfigs.get("key_field").equals(newConfigs.get("key_field")))
			setStates(new HashMap<String, Object>());

		applyConfig();
	}

	@Override
	protected void runOnce() {
		String lastKey = loadLastKey();
		String newLastKey = null;
		Service svc = null;
		Job job = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		try {
			String q = this.query + " | sort 0 " + keyField;
			slog.debug("logpresso bootcamp: bootcamp logger load last key [{}]", lastKey);
			if (lastKey != null)
				q += "| search " + keyField + " > \"" + lastKey + "\"";

			svc = profile.connect();

			JobArgs ja = new JobArgs();
			ja.setExecutionMode(JobArgs.ExecutionMode.NORMAL);

			job = svc.getJobs().create(q, ja);
			slog.debug("logpresso bootcamp: bootcamp logger query[{}] start", q);

			while (!job.isDone()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}

			int resultCount = job.getResultCount();
			int offset = 0;
			int count = 1000;

			InputStream is = null;
			ResultsReaderXml reader = null;

			while (offset < resultCount && !onStop) {
				try {
					CollectionArgs outputArgs = new CollectionArgs();
					outputArgs.setCount(count);
					outputArgs.setOffset(offset);

					is = job.getResults(outputArgs);
					reader = new ResultsReaderXml(is);

					Event event = null;
					while ((event = reader.getNextEvent()) != null) {
						newLastKey = event.get(keyField);
						Map<String, Object> raw = new HashMap<String, Object>();
						raw.putAll(event);
						write(new SimpleLog(sdf.parse(event.get("_time")), getFullName(), raw));
					}

					offset = offset + count;
				} catch (Throwable t) {
					if (reader != null)
						try {
							reader.close();
						} catch (IOException e) {
						}

					if (is != null)
						try {
							is.close();
						} catch (IOException e) {
						}

					if (job != null) {
						job.finish();
						job = null;
					}

					if (svc != null)
						svc.logout();
				}
			}
		} finally {
			slog.debug("logpresso bootcamp: bootcamp logger save last key [{}]", newLastKey);
			if (newLastKey != null) {
				Map<String, Object> state = new HashMap<String, Object>();
				state.put("last_key", newLastKey);

				setStates(state);
			}
		}
	}

	private String loadLastKey() {
		Map<String, Object> states = getStates();
		if (states == null || states.isEmpty())
			return null;

		return (String) states.get("last_key");
	}

}
