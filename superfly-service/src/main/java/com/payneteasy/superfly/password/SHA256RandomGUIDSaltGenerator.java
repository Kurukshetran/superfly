package com.payneteasy.superfly.password;

import com.payneteasy.superfly.common.utils.CryptoHelper;
import com.payneteasy.superfly.utils.RandomGUID;

/**
 * Salt generator which uses SHA-256 from secure RandomGUID values.
 *
 * @author Roman Puchkovskiy
 */
public class SHA256RandomGUIDSaltGenerator implements SaltGenerator {

	public String generate() {
		RandomGUID guid=new RandomGUID(true);
        String salt=CryptoHelper.SHA256(guid.toString());
		return salt;
	}
	
}
