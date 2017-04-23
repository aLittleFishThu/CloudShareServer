package server;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIDGenerator {
	
	private static SecureRandom random = new SecureRandom();

	public static String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	}
}
