//package com.bredelet.init.format;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.TreeSet;

public class INITFileReader {

    final static Pattern nonspace = Pattern.compile("\\S\\[?");
    
    String strip(String s) {
        return s.replace("^\\s*", "").replace("\\s*$", "");
    }
    
    
    public INITFileReader(Reader reader) {
        BufferedReader source;
        if (reader instanceof BufferedReader)
        {
            source = (BufferedReader) reader;
        }
        else
        {
            source = new BufferedReader(reader);
        }
        String line;
        HashMap<String, Object> doc = new HashMap<String, Object>();
        HashMap<String, Object> current = doc;
        do
        {
            try {
                line = source.readLine();
            }
            catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                line = null;
            }
            if (line != null)
            {
                Matcher macha = nonspace.matcher(line);
                if (macha.find())
                {
                    switch (macha.group()) {
                        case ";":
                        System.out.println("COMMENT " + line.substring(macha.end()));
                        break;
                     
                        case "[[":
                        int closingBracket = line.indexOf("]]", macha.end());
                        System.out.println("CONFIG " + line.substring(macha.end(), closingBracket));
                        break;
                     
                        case "[":
                        closingBracket = line.indexOf(']', macha.end());
                        String setName = strip(line.substring(macha.end(), closingBracket));
                        current = new HashMap<String, Object>();
                        doc.put(setName, current);
                        break;
                     
                        default:
                        int equals = line.indexOf('=');
                        String propertyName = strip(line.substring(0, equals));
                        String value = line.substring(equals + 1);
                        current.put(propertyName, value);
                    }
                }
            }
        } while (line != null);
        for (String key: new TreeSet<String>(doc.keySet()))
        {
            Object valu = doc.get(key);
            if (valu instanceof String)
            {
                System.out.println("\nPROPERTY " + key);
                System.out.println(" value=" + valu);
            }
            else
            {
                System.out.println("\nSET " + key);
                current = (HashMap<String, Object>) valu;
                for (String skey: new TreeSet<String>(current.keySet()))
                {
                    Object svalu = current.get(skey);
                    String text = (String) svalu;
                    int refi = text.indexOf("%[");
                    while (refi >= 0)
                    {
                        if (text.length() < refi + 3)
                        {
                            System.err.println("Truncated value - did you forget a ';'? " + text);
                        }
                        else
                        {
                            if (text.charAt(refi + 2) != ';')
                            {
                                int refEndi = text.indexOf(']', refi);
                                if (refEndi == -1)
                                {
                                    System.err.println("INVALID REFERENCE " + text.substring(refi));
                                }
                                else
                                {
                                    String ref = strip(text.substring(refi + 2, refEndi));
                                    System.out.println("REFERENCE " + ref);
                                }
                            }
                            else
                            {
                                svalu = text.substring(0, refi + 2) + text.substring(refi + 3);
                            }
                        }
                        refi = text.indexOf("%[", refi + 1);
                    }
                    System.out.println("SET PROPERTY " + skey);
                    System.out.println("     value=" + svalu);
                }                
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String filename = "xml-to-init.ini";
        if (args.length > 0)
        {
            filename = args[0];
        }
        new INITFileReader(new FileReader(filename));
    }
}