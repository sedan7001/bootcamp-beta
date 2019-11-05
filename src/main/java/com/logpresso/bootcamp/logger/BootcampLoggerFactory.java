package com.logpresso.bootcamp.logger;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.araqne.confdb.ConfigService;
import org.araqne.log.api.AbstractLoggerFactory;
import org.araqne.log.api.Logger;
import org.araqne.log.api.LoggerConfigOption;
import org.araqne.log.api.LoggerSpecification;
import org.araqne.log.api.MutableSourceLoggerConfigType;

@Component(name = "logpresso-bootcamp-logger-factory")
@Provides
public class BootcampLoggerFactory extends AbstractLoggerFactory {

	@Requires
	private ConfigService conf;

	@Override
	public String getName() {
		return "bootcamp";
	}

	@Override
	public String getDisplayName(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "부트캠프 수집";
		return "Bootcamp Logger";
	}

	@Override
	public String getDescription(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "부트캠프 로그를 수집합니다.";
		return "Collect Bootcamp log.";
	}

	@Override
	public Collection<LoggerConfigOption> getConfigOptions() {
		LoggerConfigOption profile = new MutableSourceLoggerConfigType("profile", t("프로파일"), t("프로파일"), true);
		LoggerConfigOption query = new MutableSourceLoggerConfigType("query", t("쿼리"), t("수집을 위한 쿼리"), true);
		LoggerConfigOption keyField = new MutableSourceLoggerConfigType("key_field", t("키 필드"),
				t("마지막 수집 지점을 확인할 필드(default: _cd)"), false);

		return Arrays.asList(profile, query, keyField);
	}

	private Map<Locale, String> t(String text) {
		Map<Locale, String> m = new HashMap<Locale, String>();
		m.put(Locale.KOREAN, text);

		return m;
	}

	@Override
	protected Logger createLogger(LoggerSpecification spec) {
		return new BootcampLogger(spec, this, conf);
	}

	@Override
	public void deleteLogger(String namespace, String name) {
		super.deleteLogger(namespace, name);

		File dataDir = new File(System.getProperty("araqne.data.dir"), "araqne-log-api");
		File f = new File(dataDir, "bootcamp-" + name + ".lastlog");
		f.delete();
	}

}
