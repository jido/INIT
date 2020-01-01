//package com.bredelet.init.format;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;

public class INITFileReader {

    final static Pattern nonspace = Pattern.compile("\\S\\[?");
    
    // pour les qu'ont un vieux Java (moi!)
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
        HashMap doc = new HashMap();
        HashMap current = doc;
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
                        int closingBracket = line.indexOf(']', macha.end());
                        System.out.println("CONFIG " + line.substring(macha.end(), closingBracket));
                        break;
                     
                        case "[":
                        closingBracket = line.indexOf(']', macha.end());
                        String setName = strip(line.substring(macha.end(), closingBracket));
                        current = new HashMap();
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
        for (Object key: doc.keySet())
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
                current = (HashMap) valu;
                for (Object skey: current.keySet())
                {
                    Object svalu = current.get(skey);
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