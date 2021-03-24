package rs.ac.uns.ftn.bsep.pki.keystore;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.bsep.pki.data.IssuerData;


@Component
public class KeyStoreReader {

	//KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    //Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreReader() throws Exception {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            throw new Exception(e.getMessage());
        } catch (NoSuchProviderException e) {
        	throw new Exception(e.getMessage());
        } catch (Exception e) {
        	throw new Exception(e.getMessage());
		}
    }
    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     *
     * @param keyStoreFile - datoteka odakle se citaju podaci
     * @param alias - alias putem kog se identifikuje sertifikat izdavaoca
     * @param password - lozinka koja je neophodna da se otvori key store
     * @param keyPass - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     * @throws Exception 
     */
    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) throws Exception {
        try {
            //Datoteka se ucitava
        	BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
        	keyStore.load(in, password);
            //Iscitava se sertifikat koji ima dati alias
        	Certificate[] chain = keyStore.getCertificateChain(alias);
            Certificate cert = chain[chain.length-1];
            //Certificate cert = keyStore.getCertificate(alias);
            //Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);
            
            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            
            String issuerAltName = null;
            try {
            	issuerAltName = (String) ((X509Certificate) cert).getIssuerAlternativeNames().stream().findFirst().get().get(0);
            }
            catch (Exception e) {}
              
            IssuerData issuerData = new IssuerData(privKey, issuerName);
            issuerData.setIssuerAltName(issuerAltName);
            
            return issuerData;

        } catch (Exception e) {
			throw new Exception(e);
		}
    }

    /**
     * Ucitava sertifikat is KS fajla
     * @throws Exception 
     */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) throws Exception {
        try {
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate[] chain = ks.getCertificateChain(alias);
                Certificate cert = chain[chain.length-1];
                //Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (Exception e) {
			throw new Exception(e);
		}
        return null;
    }


    /**
     * Ucitava privatni kljuc is KS fajla
     * @throws Exception 
     */
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) throws Exception {
        try {
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));

            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (Exception e) {
			throw new Exception(e);
		}
        return null;
    }

    public ArrayList<Certificate> readAllCertificates(String keyStoreFile, String keyStorePass) throws Exception {
        KeyStore ks = null;
        ArrayList<Certificate> certs = new ArrayList<>(50);
        try {
            ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));

            ks.load(in, keyStorePass.toCharArray());
            Enumeration<String> es = ks.aliases();
            String alias = "";
            while (es.hasMoreElements()) {
                alias = (String) es.nextElement();
                Certificate[] chain = ks.getCertificateChain(alias);
                Certificate c = chain[chain.length-1];
                //Certificate c = ks.getCertificate(alias);
                certs.add(c);
            }
        } catch (Exception e) {
			throw new Exception(e);
		}
        return certs;
    }

    public ArrayList<Certificate> readCertificateChain(String keyStoreFile, String keyStorePass, String alias) throws Exception {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate[] chain = ks.getCertificateChain(alias);
               return new ArrayList<Certificate>(Arrays.asList(chain));
            }
        } catch (Exception e) {
			throw new Exception(e);
		}
        return null;
    }
}
