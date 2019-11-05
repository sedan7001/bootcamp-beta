package com.logpresso.bootcamp.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.araqne.log.api.V1LogParser;

public class SplunkEventParser extends V1LogParser {
	private final org.slf4j.Logger slog = org.slf4j.LoggerFactory.getLogger(SplunkEventParser.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

	@Override
	public Map<String, Object> parse(Map<String, Object> params) {
		Map<String, Object> m = new HashMap<String, Object>();

		String raw = (String) params.get("_raw");
		String data = raw.split("\"")[1];
		String[] items = data.split("<<LF>>\t- ");

		String date = items[0];
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			slog.error("parser cannot parse date ", e.getMessage());
		}
		m.put("_time", d);

		for (int i = 1; i < items.length; ++i) {
			int idx = items[i].indexOf('=');
			if (idx < 0) {
				slog.error("parser cannot parse ", items[i]);
				continue;
			}
			String field = items[i].substring(0, idx);
			String value = items[i].substring(idx + 1);

			if (field.equals("_id") || field.equals("repo_id") || field.equals("user_id")) {
				m.put(field, Long.parseLong(value));
			} else if (field.equals("public")) {
				m.put(field, Boolean.parseBoolean(value));
			} else {
				m.put(field, value);
			}
		}
		return m;
	}
}
