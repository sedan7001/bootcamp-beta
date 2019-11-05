package com.logpresso.bootcamp.script;

import org.araqne.api.Script;
import org.araqne.api.ScriptArgument;
import org.araqne.api.ScriptContext;
import org.araqne.api.ScriptUsage;
import org.araqne.confdb.Config;
import org.araqne.confdb.ConfigDatabase;
import org.araqne.confdb.ConfigIterator;
import org.araqne.confdb.ConfigService;
import org.araqne.confdb.Predicates;

import com.logpresso.bootcamp.model.SplunkProfile;

public class BootcampScript implements Script {
	private ScriptContext context;
	private ConfigService conf;

	public BootcampScript(ConfigService conf) {
		this.conf = conf;
	}

	@Override
	public void setScriptContext(ScriptContext context) {
		this.context = context;
	}

	public void getSplunkProfiles(String[] args) {
		context.println("Profiles");
		context.println("----------");

		ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
		ConfigIterator it = null;
		try {
			it = db.findAll(SplunkProfile.class);
			while (it.hasNext()) {
				SplunkProfile profile = it.next().getDocument(SplunkProfile.class);
				context.println(profile.toString());
			}
		} finally {
			if (it != null)
				it.close();
		}
	}

	public void createSplunkProfile(String[] args) {
		SplunkProfile profile = new SplunkProfile();
		try {
			context.println("name? ");
			profile.setName(context.readLine().trim());

			context.println("host? ");
			profile.setHost(context.readLine().trim());

			context.println("port? ");
			profile.setPort(Integer.parseInt(context.readLine().trim()));

			context.println("user? ");
			profile.setLoginName(context.readLine().trim());

			context.println("password? ");
			profile.setPassword(context.readPassword().trim());

			ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
			db.add(profile);

			context.println("created");
		} catch (InterruptedException e) {
			context.println("cannot create profile. cause: " + e.getMessage());
		}
	}

	@ScriptUsage(description = "update splunk profile", arguments = {
			@ScriptArgument(name = "name", type = "string", description = "profile name") })
	public void updateSplunkProfile(String[] args) {
		ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
		Config c = db.findOne(SplunkProfile.class, Predicates.field("name", args[0]));
		if (c == null) {
			context.println("not found profile");
			return;
		}

		SplunkProfile profile = c.getDocument(SplunkProfile.class);
		try {
			context.println("host? ");
			profile.setHost(context.readLine(profile.getHost()).trim());

			context.println("port? ");
			profile.setPort(Integer.parseInt(context.readLine(profile.getPort().toString()).trim()));

			context.println("user? ");
			profile.setLoginName(context.readLine(profile.getLoginName()).trim());

			context.println("password? ");
			profile.setPassword(context.readPassword().trim());

			db.update(c, profile);
			context.println("updated");
		} catch (InterruptedException e) {
			context.println("cannot create profile. cause: " + e.getMessage());
		}
	}

	@ScriptUsage(description = "remove splunk profile", arguments = {
			@ScriptArgument(name = "name", type = "string", description = "profile name") })
	public void removeSplunkProfile(String[] args) {
		ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
		Config c = db.findOne(SplunkProfile.class, Predicates.field("name", args[0]));
		if (c == null) {
			context.println("not found profile");
			return;
		}

		db.remove(c);
		context.println("removed");
	}
}
