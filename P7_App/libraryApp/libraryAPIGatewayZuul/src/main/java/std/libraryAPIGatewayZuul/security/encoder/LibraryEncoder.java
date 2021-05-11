package std.libraryAPIGatewayZuul.security.encoder;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

@Service
public class LibraryEncoder {

    @Autowired
    private BCryptConfigClass bCryptConfigClass;

    private Integer defaultStrength = 10;
    private BCryptVersion defaultVersion = BCryptVersion.$2A;
    private SecureRandom random = new SecureRandom();

    public BCryptPasswordEncoder bCryptEncoder() {
	if (isSecureRandom(bCryptConfigClass.getRandomSecure())) {
	    return new BCryptPasswordEncoder(version(bCryptConfigClass.getVersion()),
		    strength(bCryptConfigClass.getStrength()), random);
	}
	return new BCryptPasswordEncoder(version(bCryptConfigClass.getVersion()),
		strength(bCryptConfigClass.getStrength()));

    }

    private Integer strength(Integer strength) {
	if (strength != null) {
	    if (strength >= 4 && strength <= 31) {
		return strength;
	    }
	}
	return defaultStrength;
    }

    private BCryptVersion version(String version) {
	if (version != null) {
	    if (version.toLowerCase().equals("b")) {
		return BCryptVersion.$2B;
	    } else if (version.toLowerCase().equals("y")) {
		return BCryptVersion.$2Y;
	    } else {
		return defaultVersion;
	    }
	} else {
	    return defaultVersion;
	}
    }

    private boolean isSecureRandom(Boolean bool) {
	Boolean defaultBool = false;
	if (bool != null) {
	    return bool;
	}
	return defaultBool;

    }

    public void check() {
	System.out.println("check");
    }

}
