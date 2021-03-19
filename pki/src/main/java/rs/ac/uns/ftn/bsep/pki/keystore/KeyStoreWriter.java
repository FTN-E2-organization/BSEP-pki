package rs.ac.uns.ftn.bsep.pki.keystore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class KeyStoreWriter {

	//KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    //Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreWriter(KeyStore keyStore) {
        super();
        this.keyStore = keyStore;
    }

    public KeyStoreWriter() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(Paths.get(ResourceUtils.getFile("classpath:")+"\\..\\..\\src\\main\\resources").toRealPath().toString()+"\\"+fileName), password);
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(Paths.get(ResourceUtils.getFile("classpath:")+"\\..\\..\\src\\main\\resources").toRealPath().toString()+"\\"+fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

    public Certificate[] getChain(String alias) {
        try {
            return keyStore.getCertificateChain(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;       
    }
}
