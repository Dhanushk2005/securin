package com.securin;

import com.securin.converters.*;

import java.io.File;
import java.io.IOException;

public class json_to_xml {
    public static void main(String args[]) throws IOException {
        XMLJSONConverterI convert = ConverterFactory.createXMLJSONConverter();
        File in = new File("securin-main/src/src/main/java/com/securin/in.json");
        File out = new File("securin-main/src/src/main/java/com/securin/out.xml");
        convert.convertJSONtoXML(in , out);
    }
}
