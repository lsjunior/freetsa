package br.net.woodstock.freetsa.web.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.Identity;
import br.net.woodstock.rockframework.security.cert.CertificateRequest;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.impl.BouncyCastleCertificateGenerator;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;

public class CreateServerCertificateTest extends TestCase {

	public CreateServerCertificateTest() {
		super();
	}

	public void testCreateSelfSignedCert() throws Exception {
		CertificateRequest request = new CertificateRequest("Lourival Sabino");
		request.setEmail("junior@woodstock.net.br");
		request.setIssuerName("Woodstock Tecnologia");
		request.getKeyUsage().add(KeyUsageType.DIGITAL_SIGNATURE);
		request.getKeyUsage().add(KeyUsageType.NON_REPUDIATION);
		request.getKeyUsage().add(KeyUsageType.KEY_AGREEMENT);
		request.getExtendedKeyUsage().add(ExtendedKeyUsageType.TIMESTAMPING);
		Identity identity = BouncyCastleCertificateGenerator.getInstance().generate(request).getIdentity();

		Store store = new JCAStore(KeyStoreType.PKCS12);
		Alias alias = new PasswordAlias("tsaserver", "tsaserver");
		PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry(alias, identity);
		store.add(privateKeyEntry);
		store.write(new FileOutputStream("/tmp/tsaserver.pfx"), "tsaserver");
	}

	public void xtestCreateCA() throws Exception {
		CertificateRequest request = new CertificateRequest("Woodstock Tecnologia CA");
		request.setCa(true);
		request.setComment("Woodstock Tecnologia CA");
		request.setEmail("ca@woodstock.net.br");

		Identity identity = BouncyCastleCertificateGenerator.getInstance().generate(request).getIdentity();

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("woodstock", "woodstock"), identity));
		store.write(new FileOutputStream("/tmp/woodstock.pfx"), "woodstock");
	}

	public void xtestCreateLocalCACert() throws Exception {
		FileInputStream inputStream = new FileInputStream("/tmp/woodstock.pfx");
		Store caStore = new JCAStore(KeyStoreType.PKCS12);
		caStore.read(inputStream, "woodstock");
		PrivateKeyEntry entry = (PrivateKeyEntry) caStore.get(new PasswordAlias("woodstock", "woodstock"));

		CertificateRequest request = new CertificateRequest("Lourival Sabino");
		request.setEmail("junior@woodstock.net.br");
		request.getKeyUsage().add(KeyUsageType.DIGITAL_SIGNATURE);
		request.getKeyUsage().add(KeyUsageType.NON_REPUDIATION);
		request.getKeyUsage().add(KeyUsageType.KEY_AGREEMENT);
		request.getExtendedKeyUsage().add(ExtendedKeyUsageType.TIMESTAMPING);
		request.setIssuerIdentity(entry.toIdentity());

		Identity identity = BouncyCastleCertificateGenerator.getInstance().generate(request).getIdentity();

		Store store = new JCAStore(KeyStoreType.PKCS12);
		Alias alias = new PasswordAlias("tsaserver", "tsaserver");
		PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry(alias, identity);
		store.add(privateKeyEntry);
		store.write(new FileOutputStream("/tmp/tsaserver.pfx"), "tsaserver");
	}

}
