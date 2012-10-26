package br.net.woodstock.freetsa.web.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.cert.CertificateRequest;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.PrivateKeyHolder;
import br.net.woodstock.rockframework.security.cert.impl.BouncyCastleCertificateBuilder;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.StoreEntryType;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;

public class CreateServerCertificateTest extends TestCase {

	public CreateServerCertificateTest() {
		super();
	}

	public void testCreateSelfSignedCert() throws Exception {
		CertificateRequest request = new CertificateRequest("Lourival Sabino");
		request.withEmail("junior@woodstock.net.br");
		request.withIssuer("Woodstock Tecnologia");
		request.withKeyUsage(KeyUsageType.DIGITAL_SIGNATURE, KeyUsageType.NON_REPUDIATION, KeyUsageType.KEY_AGREEMENT);
		request.withExtendedKeyUsage(ExtendedKeyUsageType.TIMESTAMPING);
		PrivateKeyHolder holder = BouncyCastleCertificateBuilder.getInstance().build(request);

		Store store = new JCAStore(KeyStoreType.PKCS12);
		Alias alias = new PasswordAlias("tsaserver", "tsaserver");
		PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry(alias, holder.getPrivateKey(), holder.getChain());
		store.add(privateKeyEntry);
		store.write(new FileOutputStream("/tmp/tsaserver.pfx"), "tsaserver");
	}

	public void xtestCreateCA() throws Exception {
		CertificateRequest request = new CertificateRequest("Woodstock Tecnologia CA");
		request.withCa(true);
		request.withComment("Woodstock Tecnologia CA");
		request.withEmail("ca@woodstock.net.br");

		PrivateKeyHolder holder = BouncyCastleCertificateBuilder.getInstance().build(request);

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("woodstock", "woodstock"), holder.getPrivateKey(), holder.getChain()));
		store.write(new FileOutputStream("/tmp/woodstock.pfx"), "woodstock");
	}

	public void xtestCreateLocalCACert() throws Exception {
		FileInputStream inputStream = new FileInputStream("/tmp/woodstock.pfx");
		Store caStore = new JCAStore(KeyStoreType.PKCS12);
		caStore.read(inputStream, "woodstock");
		PrivateKeyEntry entry = (PrivateKeyEntry) caStore.get(new PasswordAlias("woodstock", "woodstock"), StoreEntryType.PRIVATE_KEY);

		CertificateRequest request = new CertificateRequest("Lourival Sabino");
		request.withEmail("junior@woodstock.net.br");
		request.withKeyUsage(KeyUsageType.DIGITAL_SIGNATURE, KeyUsageType.NON_REPUDIATION, KeyUsageType.KEY_AGREEMENT);
		request.withExtendedKeyUsage(ExtendedKeyUsageType.TIMESTAMPING);
		request.withIssuerKeyHolder(new PrivateKeyHolder(entry.getValue(), entry.getChain()));

		PrivateKeyHolder holder = BouncyCastleCertificateBuilder.getInstance().build(request);

		Store store = new JCAStore(KeyStoreType.PKCS12);
		Alias alias = new PasswordAlias("tsaserver", "tsaserver");
		PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry(alias, holder.getPrivateKey(), holder.getChain());
		store.add(privateKeyEntry);
		store.write(new FileOutputStream("/tmp/tsaserver.pfx"), "tsaserver");
	}

}
