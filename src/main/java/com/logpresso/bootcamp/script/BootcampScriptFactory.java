package com.logpresso.bootcamp.script;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.araqne.api.Script;
import org.araqne.api.ScriptFactory;
import org.araqne.confdb.ConfigService;

@Component(name = "logpresso-bootcamp-script-factory")
@Provides
public class BootcampScriptFactory implements ScriptFactory {
	@ServiceProperty(name = "alias", value = "bootcamp")
	private String alias;

	@Requires
	private ConfigService conf;

	@Override
	public Script createScript() {
		return new BootcampScript(conf);
	}
}