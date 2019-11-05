package com.logpresso.bootcamp.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.araqne.log.api.AbstractLogParserFactory;
import org.araqne.log.api.LogParser;

@Component(name = "logpresso-bootcamp-parser-factory")
@Provides
public class BootcampParserFactory extends AbstractLogParserFactory {
	@Override
	public String getName() {
		return "bootcamp";
	}

	@Override
	public Collection<Locale> getDisplayNameLocales() {
		return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
	}

	@Override
	public String getDisplayName(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "부트캠프";
		return "Bootcamp";
	}

	@Override
	public Collection<Locale> getDescriptionLocales() {
		return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
	}

	@Override
	public String getDescription(Locale locale) {
		if (locale != null && locale.equals(Locale.KOREAN))
			return "Bootcamp 로그를 파싱합니다.";
		return "Parse Bootcamp logs.";
	}

	@Override
	public LogParser createParser(Map<String, String> configs) {
		return null;
	}
}
