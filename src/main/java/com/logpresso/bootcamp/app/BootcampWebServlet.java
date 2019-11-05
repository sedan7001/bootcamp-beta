package com.logpresso.bootcamp.app;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.araqne.httpd.BundleResourceServlet;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootcampWebServlet extends BundleResourceServlet{
	private static final long serialVersionUID = 1L;
	private final Logger slog = LoggerFactory.getLogger(BootcampWebServlet.class);

	public BootcampWebServlet(Bundle bundle, String basePath) {
		super(bundle, basePath);
	}

	@Override
	protected InputStream getInputStream(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();

		if (!pathInfo.equals("/index.html") && !pathInfo.endsWith(".js") && !pathInfo.startsWith("/img/")
				&& !pathInfo.endsWith(".css") && !pathInfo.endsWith(".svg") && !pathInfo.endsWith(".ttf")
				&& !pathInfo.endsWith(".eot") && !pathInfo.endsWith(".woff") && !pathInfo.endsWith(".woff2")
				&& !pathInfo.startsWith("/attachment/")) {
			slog.debug("bootcamp web: request route path info [{}] set path to index.html", pathInfo);
			return super.getInputStream(new Request(req, "/index.html"));
		}
		return super.getInputStream(req);
	}

	private class Request extends HttpServletRequestWrapper {
		private String pathInfo;

		public Request(HttpServletRequest request, String pathInfo) {
			super(request);
			this.pathInfo = pathInfo;
		}

		@Override
		public String getPathInfo() {
			return pathInfo;
		}
	}

	@Override
	protected String getMimeType(String path) {
		if (path == null || path.indexOf('.') < 0)
			return "text/html";

		return super.getMimeType(path);
	}
	
}