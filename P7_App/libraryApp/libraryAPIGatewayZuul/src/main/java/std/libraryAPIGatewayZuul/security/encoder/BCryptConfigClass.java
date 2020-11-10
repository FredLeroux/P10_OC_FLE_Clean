package std.libraryAPIGatewayZuul.security.encoder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("bcrypt")
@PropertySource("bcrypt.properties")
@Getter
@Setter
public class BCryptConfigClass {

	private String version;
	private Integer strength;
	private Boolean randomSecure;

}
