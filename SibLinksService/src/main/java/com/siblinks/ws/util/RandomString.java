package com.siblinks.ws.util;

import java.util.Random;

public class RandomString {
	
	public String random() {
		String alphabet= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String s = "";
        Random random = new Random();
        int randomLen = 1 + random.nextInt(19);
        for (int i = 0; i < randomLen; i++) {
            char c = alphabet.charAt(random.nextInt(62));
            s += c;
        }
        return s;
	}
}
