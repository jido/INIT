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
        return s.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
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
                    System.out.println("SET PROPERTY " + skey);
                    Object svalu = current.get(skey);
                    String text = (String) svalu;
                    String prefix = "";
                    int refEndi = -1;
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
                                prefix = prefix + text.substring(refEndi + 1, refi);
                                refEndi = text.indexOf(']', refi);
                                String ref;
                                if (refEndi == -1)
                                {
                                    System.err.println("INVALID REFERENCE " + text.substring(refi));
                                }
                                else
                                {
                                    ref = strip(text.substring(refi + 2, refEndi));
                                    Object pvalue = doc.get(ref);
                                    if (pvalue == null)
                                    {
                                        System.err.println("UNKNOWN REFERENCE " + ref);
                                    }
                                    else
                                    {
                                        System.out.println("REFERENCE " + ref);
                                        if (svalu instanceof String && pvalue instanceof String)
                                        {
                                            System.out.println("Appending " + pvalue);
                                            prefix = prefix + pvalue;
                                        }
                                        if (svalu instanceof String && pvalue instanceof HashMap)
                                        {
                                            svalu = pvalue;
                                        }
                                        if (svalu instanceof HashMap && pvalue instanceof HashMap)
                                        {
                                            HashMap<String, Object> copy = (HashMap<String, Object>) ((HashMap) svalu).clone();
                                            copy.putAll((HashMap<String, Object>) pvalue);
                                            svalu = copy;
                                        }
                                    }
                                }
                            }
                            else if (svalu instanceof String)
                            {
                                prefix = prefix + text.substring(refEndi + 1, refi + 2);
                                refEndi = refi + 2;
                            }
                        }
                        refi = text.indexOf("%[", refi + 1);
                    }
                    if (svalu instanceof String)
                    {
                        svalu = prefix + (refEndi + 1 < text.length() ? text.substring(refEndi + 1) : "");
                        System.out.println("     value=" + svalu);
                    }
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