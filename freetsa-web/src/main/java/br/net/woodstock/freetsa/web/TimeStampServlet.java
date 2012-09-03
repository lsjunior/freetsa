package br.net.woodstock.freetsa.web;

import java.io.IOException;

import br.net.woodstock.rockframework.security.timestamp.web.BouncyCastleTimeStampConfig;
import br.net.woodstock.rockframework.security.timestamp.web.BouncyCastleTimeStampServlet;
import br.net.woodstock.rockframework.security.timestamp.web.DefaultBouncyCastleTimeStampConfig;

public class TimeStampServlet extends BouncyCastleTimeStampServlet {

	private static final long	serialVersionUID	= -7125952867120963318L;

	public static final String	PROPERTIES_FILE		= "freetsa.properties";

	@Override
	protected BouncyCastleTimeStampConfig getConfig() throws IOException {
		return new DefaultBouncyCastleTimeStampConfig(TimeStampServlet.PROPERTIES_FILE);
	}

}
