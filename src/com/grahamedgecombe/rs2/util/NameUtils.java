package com.grahamedgecombe.rs2.util;

public class NameUtils {
	
	public static boolean isValidName(String s) {
		return formatNameForProtocol(s).matches("[a-z0-9_]+");
	}

	public static long nameToLong(String s) {
		long l = 0L;
		for(int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z') l += (1 + c) - 65;
			else if(c >= 'a' && c <= 'z') l += (1 + c) - 97;
			else if(c >= '0' && c <= '9') l += (27 + c) - 48;
		}
		while(l % 37L == 0L && l != 0L) l /= 37L;
		return l;
	}
	
	public static String formatNameForProtocol(String s) {
		return s.toLowerCase().replace(" ", "_");
	}

	public static String formatName(String s) {
		return fixName(s.replace(" ", "_"));
	}
	
	private static String fixName(final String s) {
		if(s.length() > 0) {
			final char ac[] = s.toCharArray();
			for(int j = 0; j < ac.length; j++)
				if(ac[j] == '_') {
					ac[j] = ' ';
					if((j + 1 < ac.length) && (ac[j + 1] >= 'a')
							&& (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}

			if((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}

}
