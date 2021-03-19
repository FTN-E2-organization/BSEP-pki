package rs.ac.uns.ftn.bsep.pki.keystore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
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

    public KeyStoreWriter() throws Exception {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (Exception e) {
			throw new Exception(e);
		}
    }

    public void loadKeyStore(String fileName, char[] password) throws Exception {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(Paths.get(ResourceUtils.getFile("classpath:")+"\\..\\..\\src\\main\\resources").toRealPath().toString()+"\\"+fileName), password);
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (Exception e) {
			throw new Exception(e);
		}
    }

    public void saveKeyStore(String fileName, char[] password) throws Exception {
        try {
            keyStore.store(new FileOutputStream(Paths.get(ResourceUtils.getFile("classpath:")+"\\..\\..\\src\\main\\resources").toRealPath().toString()+"\\"+fileName), password);
        } catch (Exception e) {
			throw new Exception(e);
		}
    }

    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) throws Exception {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

    public Certificate[] getChain(String alias) throws Exception {
        try {
            return keyStore.getCertificateChain(alias);
        } catch (Exception e) {
			throw new Exception(e);
		}      
    }
}
