/* See numbertext.org
 * 2009-2010 (c) László Németh
 * License: LGPL/BSD dual license */

package org.numbertext;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.numbertext.Soros;
import java.net.URL;
import java.util.HashMap;

public class Numbertext {
  static HashMap<String, Soros> modules = new HashMap<String, Soros>();

  private static Soros load(String lang) {
    try {
	URL url = Numbertext.class.getResource("data/" + lang + ".sor");
	BufferedReader f = new BufferedReader(new InputStreamReader(url.openStream()));
	StringBuffer st = new StringBuffer();
	String line = null;
	while (( line = f.readLine()) != null) {
	    st.append(line);
	    st.append(System.getProperty("line.separator"));
	}
	Soros s = new Soros(new String(st));
	if (modules != null && lang != null) modules.put(lang, s);
	return s;
    } catch(Exception e) {
	System.out.println("Problem: " + e);
    }
    return null;
  }

  public static String numbertext(String input, String lang) {
    Soros s = (Soros) modules.get(lang);
    if (s == null) s = load(lang);
    if (s == null) return null;
    return s.run(input);
  }

  public static String moneytext(String input, String money, String lang) {
    return numbertext(money + " " + input, lang);
  }

  public static void main (String[] args) {
	String lang = "en_US";
	if (args.length == 0) {
	    System.out.println("Usage: java soros [-l lang] [-p prefix] par1 [par2...]");
	    System.out.println("Example: java soros -l en_US -p ord: 1-10 500 1000-1010");
	    return;
	}
	int state = 0;
	String prefix = "";
	for (int i = 0; i < args.length; i++) {
	    if (state != 0) {
		if (state == 1) lang = args[i];
		else prefix = args[i] + " ";
		state = 0;
		continue;
	    }
	    if (args[i].equals("-l")) {
		state = 1;
	    } else if (args[i].equals("-p")) {
		state = 2;
	    } else {
		int idx = args[i].indexOf('-', 1);
		if (idx == -1) System.out.println(numbertext(prefix + args[i], lang));
		else {
		    for (int j = Integer.parseInt(args[i].substring(0, idx)); j <= Integer.parseInt(args[i].substring(idx + 1)); j++) {
			System.out.println(numbertext(prefix + j, lang));
		    }
		}
	    }
	}
  }
}
