package com.logpresso.bootcamp.msgbus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.httpd.BundleResourceServlet;
import org.araqne.httpd.HttpContext;
import org.araqne.httpd.HttpService;
import org.araqne.webconsole.AppManifest;
import org.araqne.webconsole.AppProgram;
import org.araqne.webconsole.AppProvider;
import org.araqne.webconsole.AppRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = "logpresso-bootcamp-ui-plugin")
public class UiPlugin implements AppProvider {
	private final Logger slog = LoggerFactory.getLogger(UiPlugin.class);

	@Requires
	private AppRegistry appRegistry;

	@Requires
	private HttpService httpd;

	private BundleContext bc;

	public UiPlugin(BundleContext bc) {
		this.bc = bc;
	}

	@Override
	public AppManifest getManifest() {
		AppManifest manifest = new AppManifest();
		manifest.setId("bootcamp");
		manifest.setVersion("1.0");
		manifest.setDisplayNames(setLocaleTexts("bootcamp", "부트캠프"));
		manifest.setDescriptions(setLocaleTexts("bootcamp DEMO", "부트캠프 데모"));

		AppProgram program = new AppProgram();
		program.setId("bootcamp");
		program.setDisplayNames(setLocaleTexts("BOOTCAMP", "부트캠프"));
		manifest.getPrograms().add(program);

		return manifest;
	}

	private Map<Locale, String> setLocaleTexts(String en, String ko) {
		Map<Locale, String> m = new HashMap<Locale, String>();
		m.put(Locale.ENGLISH, en);
		m.put(Locale.KOREAN, ko);
		return m;
	}

	@Validate
	public void start() {
		appRegistry.register(this);

		HttpContext ctx = httpd.ensureContext("webconsole");
		Bundle bundle = bc.getBundle();

		AppManifest manifest = getManifest();
		String appId = manifest.getId();
		ctx.addServlet(appId, new BundleResourceServlet(bundle, "/WEB-INF"), "/" + appId + "/*");
		slog.info("bootcamp ui: loading app [{}] servlet of bundle [{}]", appId, bc.getBundle().getBundleId());
	}

	@Invalidate
	public void stop() {
		Bundle bundle = bc.getBundle();
		
		AppManifest manifest = getManifest();
		String appId = manifest.getId();

		if (httpd != null) {
			HttpContext ctx = httpd.ensureContext("webconsole");
			ctx.removeServlet(appId);
			slog.info("bootcamp ui: unloading app [{}] servlet of bundle [{}]", appId, bundle.getBundleId());
		}

		if (appRegistry != null)
			appRegistry.unregister(this);
	}

}
