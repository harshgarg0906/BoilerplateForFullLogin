package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepo userRepo;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String jwtSigningKey = "santander-induction";

	@Override
	public User createUser(User user) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String encodedPass = bCrypt.encode(user.getPassword());
		user.setPassword(encodedPass);
		return userRepo.save(user);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}

	public User searchByPsid(String psid) {
		// TODO Auto-generated method stub
		return userRepo.findByPsid(psid);
	}

	public User loadByUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("JWT-TOKEN")) {
					String user;
					try {
						user = Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(cookie.getValue()).getBody()
								.get("ps", String.class);
					} catch (ExpiredJwtException exception) {
						logger.error("In loadByUsername method" + LocalDateTime.now() + " " + exception.getMessage());
						return null;
					}
					if (user != null) {
						return this.userRepo.findByPsid(user);
					}
				}
			}
		}
		return null;
	}

/*########################################### decryption using AES algorithm ###########################################################*/
	 public String service(String ciphertext) throws UnsupportedEncodingException, GeneralSecurityException {
		 System.out.println("inside user service;;-=-");
//	        String ciphertext = "U2FsdGVkX19g4ZaeTbGbLxjyBr3WHxJJ7IdU2/1TH/k=";
	        String password = "santander_key_quiz_pass";
	        int keySize = 256;
	        int ivSize = 128;
	        // var wordArray = WordArray.create([0x53616c74, 0x65645f5f]).concat(salt).concat(ciphertext);
	        byte[] ctBytes = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
	        byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
	        byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
	        byte[] key = new byte[keySize/8];
	        byte[] iv = new byte[ivSize/8];
	        EvpKDF(password.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
	        byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
	        String recoveredPlaintext = new String(recoveredPlaintextBytes);
	        System.out.println("Recovered Plaintext: " + recoveredPlaintext);
//	        System.out.println("Expected Plaintext: " + plaintext);
	        return recoveredPlaintext;
	    }
	    public static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
	        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
	    }
	    public static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
	        keySize = keySize / 32;
	        ivSize = ivSize / 32;
	        int targetKeySize = keySize + ivSize;
	        byte[] derivedBytes = new byte[targetKeySize * 4];
	        int numberOfDerivedWords = 0;
	        byte[] block = null;
	        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
	        while (numberOfDerivedWords < targetKeySize) {
	            if (block != null) {
	                hasher.update(block);
	            }
	            hasher.update(password);
	            block = hasher.digest(salt);
	            hasher.reset();
	            // Iterations
	            for (int i = 1; i < iterations; i++) {
	                block = hasher.digest(block);
	                hasher.reset();
	            }
	            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
	                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
	            numberOfDerivedWords += block.length/4;
	        }
	        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
	        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
	        return derivedBytes; // key + iv
	    }

		public void updateUserStatus(String psid, boolean userStatus) {
			// TODO Auto-generated method stub
			User user = userRepo.findByPsid(psid);
			user.setUserActive(userStatus);
			userRepo.save(user);
		}
}
