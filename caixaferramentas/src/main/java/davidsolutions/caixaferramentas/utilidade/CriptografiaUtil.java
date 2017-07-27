package davidsolutions.caixaferramentas.utilidade;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CriptografiaUtil {

	private static final Key CHAVE = new SecretKeySpec("qP2x$ad9X#29iQod".getBytes(), "AES");

	private CriptografiaUtil() {
	}

	public static String encriptarSemVolta(String valor) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			return new BigInteger(1, messageDigest.digest(valor.getBytes())).toString(16);
		}
		catch (NoSuchAlgorithmException excecao) {
			throw new RuntimeException(excecao);
		}
	}

	public static String encriptar(String valor) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, CHAVE);
			byte[] bytes = cipher.doFinal(valor.getBytes());
			return DatatypeConverter.printBase64Binary(bytes).replaceAll("\\+", "-").replaceAll("/", "_");
		}
		catch (Exception excecao) {
			throw new RuntimeException(excecao);
		}
	}

	public static String decriptar(String valor) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, CHAVE);
			byte[] bytes = cipher.doFinal(DatatypeConverter.parseBase64Binary(valor.replaceAll("-", "+").replaceAll("_", "/")));
			return new String(bytes);
		}
		catch (Exception excecao) {
			throw new RuntimeException(excecao);
		}
	}
}