package com.logpresso.bootcamp.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.httpd.BundleResourceServlet;
import org.araqne.httpd.HttpContext;
import org.araqne.httpd.HttpContextRegistry;
import org.araqne.httpd.HttpService;
import org.araqne.msgbus.MessageBus;
import org.araqne.webconsole.AppManifest;
import org.araqne.webconsole.AppProgram;
import org.araqne.webconsole.AppProvider;
import org.araqne.webconsole.AppRegistry;
import org.araqne.webconsole.WebConsole;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = "logpresso-bootcamp-ui-bootstrap")
public class BootcampUiBootstrap implements AppProvider {
	private final Logger slog = LoggerFactory.getLogger(BootcampUiBootstrap.class);

	private BundleContext bc;

	@Requires
	private HttpService httpd;

	@Requires
	private MessageBus msgbus;

	@Requires
	private WebConsole webconsole;

	@Requires
	private AppRegistry appRegistry;

	public BootcampUiBootstrap(BundleContext bc) {
		this.bc = bc;
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

	@Override
	public AppManifest getManifest() {
		AppManifest manifest = new AppManifest();
		manifest.setId("bootcamp-demo");
		manifest.setVersion("1.0");
		manifest.setRequiredVersion("4.0");
		manifest.setDisplayNames(setLocaleTexts("bootcamp-demo", "부트캠프"));
		manifest.setDescriptions(setLocaleTexts("bootcamp-demo", "부트캠프"));

		AppProgram program = new AppProgram();
		program.setId("bootcamp");
		program.setDisplayNames(setLocaleTexts("BOOTCAMP-DEMO", "부트캠프"));
		program.setScriptFiles(Arrays.asList("main.js"));
		program.setHtmlFile("index.html");

		manifest.getPrograms().add(program);

		return manifest;
	}

	private Map<Locale, String> setLocaleTexts(String en, String ko) {
		Map<Locale, String> m = new HashMap<Locale, String>();
		m.put(Locale.ENGLISH, en);
		m.put(Locale.KOREAN, ko);
		return m;
	}

}
