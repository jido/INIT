//package com.bredelet.init.format;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class SimpleProperty {
    List<String> parts = new ArrayList<String>();
    
    public SimpleProperty(String text) {
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
                    parts.add(text.substring(refEndi + 1, refi));
                    refEndi = text.indexOf(']', refi);
                    String ref;
                    if (refEndi == -1)
                    {
                        System.err.println("UNENDED REFERENCE " + text.substring(refi));
                    }
                    else
                    {
                        ref = INITFileReader.strip(text.substring(refi + 2, refEndi));
                        System.out.println("REFERENCE " + ref);
                        parts.add(ref);
                    }
                }
                else
                {
                    refEndi = refi + 2;
                }
            }
            refi = text.indexOf("%[", refi + 2);
        }
        parts.add(refEndi + 1 < text.length() ? text.substring(refEndi + 1) : "");
        System.out.println("     value=" + text);
    }
    
    @Override
    public String toString() {
        return String.join("$", parts);
    }
    
    public int referenceCount() {
        return parts.size() / 2;
    }
    
    public String getReference(int n) {
        return parts.get(1 + 2 * n);
    }
    
    public String getText(int n) {
        return parts.get(2 * n);
    }
}

class PropertySet extends HashMap<String, SimpleProperty> {
    List<String> keys = new ArrayList<String>();
    String name;
    
    public PropertySet(String name) {
        this.name = name;
    }
    
    @Override
    public SimpleProperty put(String key, SimpleProperty value) {
        if (containsKey(key))
        {
            throw new IllegalArgumentException("Found duplicate property name: " + key + " in set: " + name);
        }
        super.put(key, value);
        keys.add(key);
        return null;
    }
}

class ArrayNode {
    String key;
    List<ArrayNode> children;
    
    public ArrayNode(String key) {
        this.key = key;
    }
    
    public ArrayNode(String key, String childKey) {
        this.key = key;
        
        children = new ArrayList<ArrayNode>();
        int dot = childKey.indexOf('.') + 1;
        if (dot == 0)
        {
            children.add(new ArrayNode(childKey));     // leaf
        }
        else
        {
            children.add(new ArrayNode(childKey.substring(0, dot - 1), childKey.substring(dot)));   // subarray
        }
    }
    
    public boolean isLeaf() {
        return children == null;
    }
    
    public int index() {
        try {
            return Integer.parseInt(key);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    } 
    
    public String add(String index) {
        if (isLeaf())
        {
            throw new RuntimeException("Unexpected error: adding child with index '" + index + "' in leaf: " + this.key);
        }
        else if (".".equals(index))
        {
            ArrayNode last = children.get(children.size() - 1);
            if (last.isLeaf())
            {
                int num = last.index() + 1;
                children.add(new ArrayNode("" + num));
                return "" + num;
            }
            else
            {
                return last.key + "." + last.add(index);
            }
        }
        else if (!INITFileReader.indices.matcher(index).matches())
        {
            throw new IllegalArgumentException("Invalid array indices: " + index);
        }
        else
        {
            int dot = index.indexOf('.') + 1;
            String key = index.substring(0, dot - 1);
            String rest = index.substring(dot);
            ArrayNode last = children.get(children.size() - 1);
            
            if (last.key.equals(key))
            {
                last.add(rest);         // add to existing child node
            }
            else
            {
                ArrayNode newChild;
                if (dot == 0)
                {
                    newChild = new ArrayNode(index);        // leaf
                }
                else
                {
                    newChild = new ArrayNode(key, rest);    // subarray
                }
                children.add(newChild);
            }
            return index;
        }
    }
}

class PropertyArray extends HashMap<String, SimpleProperty> {
    ArrayNode indices;
    
    public PropertyArray(String name, String key, SimpleProperty  value) {
        indices = new ArrayNode(name, key);
        super.put(key, value);
    }
    
    @Override
    public SimpleProperty put(String key, SimpleProperty value) {
        if (!".".equals(key) && containsKey(key))
        {
            throw new IllegalArgumentException("Found duplicate index: " + key + " in array: " + indices.key);
        }
        key = indices.add(key);
        super.put(key, value);
        return null;
    }
}

public class INITFileReader {

    final static Pattern nonspace = Pattern.compile("\\S\\[?");
    final static Pattern indices = Pattern.compile("\\d[\\d.]*");
    
    static String strip(String s) {
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
        Map<String, Map<String, SimpleProperty> > doc = new HashMap<String, Map<String, SimpleProperty> >();
        Map<String, SimpleProperty> root, current;
        root = new PropertySet("[root]");
        current = root;
        doc.put("", root);
        String setName = null;
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
                        case ";[":
                        case ";":
                            System.out.println("COMMENT " + line.substring(macha.end()));
                            break;
                     
                        case "[[":
                            int closingBracket = line.indexOf("]]", macha.end());
                            System.out.println("CONFIG " + line.substring(macha.end(), closingBracket));
                            break;
                     
                        case "[":
                            closingBracket = line.indexOf(']', macha.end());
                            setName = strip(line.substring(macha.end(), closingBracket));
                            current = new PropertySet(setName);
                            doc.put(setName, current);
                            break;
                     
                        default:
                            int equals = line.indexOf('=');
                            if (equals == -1)
                            {
                                System.err.println("Syntax error: missing '=' in: " + line);
                                break;
                            }
                            String propertyName = strip(line.substring(0, equals));
                            String value = line.substring(equals + 1);
                            if (setName != null && current.size() == 0 && indices.matcher(propertyName).matches())
                            {
                                current = new PropertyArray(setName, propertyName, new SimpleProperty(value));
                                doc.put(setName, current);
                            }
                            else
                            {
                                current.put(propertyName, new SimpleProperty(value));
                            }
                    }
                }
            }
        } while (line != null);
        for (String key: doc.keySet())
        {
            System.out.println("\nSET " + key);                
            current = doc.get(key);
            if (current instanceof PropertySet)
            {
                PropertySet value = (PropertySet) current;
                for (String skey: value.keys)
                {
                    System.out.println("SET PROPERTY " + skey);
                    SimpleProperty svalue = current.get(skey);
                    String text = svalue.getText(0);
                    for (int ref = 0; ref < svalue.referenceCount(); ++ref)
                    {
                        String name = svalue.getReference(ref);
                        SimpleProperty prop = root.get(name);
                        if (prop == null)
                        {
                            Map<String, SimpleProperty> other = doc.get(name);
                            if (other == null)
                            {
                                System.err.println("UNKNOWN REFERENCE " + name);
                            }
                            else
                            {
                                System.out.println("REFERENCE " + name);
                                text += "%[" + name + "]";
                            }
                        }
                        else
                        {
                            text += prop.toString();
                        }
                        text += svalue.getText(ref + 1);
                    }
                    System.out.println("     value=" + text);
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