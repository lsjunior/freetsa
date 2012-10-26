package br.net.woodstock.freetsa.web.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.timestamp.TimeStamp;
import br.net.woodstock.rockframework.security.timestamp.TimeStampClient;
import br.net.woodstock.rockframework.security.timestamp.impl.URLTimeStampClient;
import br.net.woodstock.rockframework.utils.IOUtils;

public class TimeStampServerTest extends TestCase {

	public TimeStampServerTest() {
		super();
	}

	public void test1() throws Exception {
		TimeStampClient client = new URLTimeStampClient("http://localhost:8080/freetsa-web/timestamp");
		FileInputStream inputStream = new FileInputStream("/home/lourival/Documentos/curriculum.pdf");
		byte[] input = IOUtils.toByteArray(inputStream);
		TimeStamp timeStamp = client.getTimeStamp(input);
		byte[] bytes = timeStamp.getEncoded();
		FileOutputStream outputStream = new FileOutputStream("/tmp/test-server.tsr");
		outputStream.write(bytes);
		outputStream.close();
	}

}
