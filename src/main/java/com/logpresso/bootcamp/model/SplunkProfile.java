package com.logpresso.bootcamp.model;

import org.araqne.confdb.CollectionName;

import com.splunk.HttpService;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@CollectionName("splunk_profile")
public class SplunkProfile {
	private String name;
	private String host;
	private Integer port;
	private String loginName;
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Service connect() {
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);

		ServiceArgs loginArgs = new ServiceArgs();
		loginArgs.setUsername(loginName);
		loginArgs.setPassword(password);
		loginArgs.setHost(host);
		loginArgs.setPort(port);

		return Service.connect(loginArgs);
	}

	@Override
	public String toString() {
		return "name=" + name + ", host=" + host + ", port=" + port + ", loginName=" + loginName;
	}

}
