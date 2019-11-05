package com.logpresso.bootcamp.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.araqne.log.api.AbstractLogParserFactory;
import org.araqne.log.api.LogParser;

@Component(name = "logpresso-splunk-event-parser-factory")
@Provides
public class SplunkEventParserFactory extends AbstractLogParserFactory {

	@Override
	public String getName() {
		return "splunk-event";
	}

	@Override
	public Collection<Locale> getDisplayNameLocales() {
		return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
	}

	@Override
	public String getDisplayName(Locale locale) {
		return "Splunk Event";
	}

	@Override
	public Collection<Locale> getDescriptionLocales() {
		return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
	}

	@Override
	public String getDescription(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "Splunk 이벤트 로그를 파싱합니다.";
		return "Parse Splunk event logs.";
	}

	@Override
	public String getDisplayGroup(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "부트캠프";
		return "Bootcamp";
	}

	@Override
	public LogParser createParser(Map<String, String> configs) {
		return new SplunkEventParser();
	}

}
