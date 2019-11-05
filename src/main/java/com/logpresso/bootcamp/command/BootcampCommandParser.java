package com.logpresso.bootcamp.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.confdb.Config;
import org.araqne.confdb.ConfigDatabase;
import org.araqne.confdb.ConfigService;
import org.araqne.confdb.Predicates;
import org.araqne.logdb.AbstractQueryCommandParser;
import org.araqne.logdb.QueryCommand;
import org.araqne.logdb.QueryContext;
import org.araqne.logdb.QueryParseException;
import org.araqne.logdb.QueryParserService;
import org.araqne.logdb.query.parser.ParseResult;
import org.araqne.logdb.query.parser.QueryTokenizer;

import com.logpresso.bootcamp.model.SplunkProfile;

@Component(name = "logpresso-bootcamp-command-parser")
public class BootcampCommandParser extends AbstractQueryCommandParser {

	@Requires
	private QueryParserService parserService;

	@Requires
	private ConfigService conf;

	@Validate
	public void start() {
		parserService.addCommandParser(this);
	}

	@Invalidate
	public void stop() {
		if (parserService != null)
			parserService.removeCommandParser(this);
	}

	@Override
	public String getCommandName() {
		return "bootcamp";
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryCommand parse(QueryContext context, String commandString) {
		ParseResult r = QueryTokenizer.parseOptions(context, commandString, getCommandName().length(),
				Arrays.asList("name", "query"), getFunctionRegistry());

		Map<String, Object> options = (Map<String, Object>) r.value;
		String name = (String) options.get("name");
		String query = options.get("query").toString().trim();

		ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
		Config c = db.findOne(SplunkProfile.class, Predicates.field("name", name));
		if (c == null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", name);
			throw new QueryParseException("909090", -1, -1, params);
		}

		SplunkProfile profile = c.getDocument(SplunkProfile.class);

		return new BootcampCommand(profile, query);
	}

}
