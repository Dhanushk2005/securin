package com.securin.converters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class xml_converter implements XMLJSONConverterI{
    public String readFile(File file){
        try{
            return Files.readString(file.toPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void convertJSONtoXML(File json, File xml) throws IOException {

        try {
            // read input JSON
            String file = readFile(json);

            String js = convert(file);
            // write to XML file
            Files.writeString(xml.toPath(), js);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to XML", e);
        }
    }
    public String convert(Object data){
        StringBuilder sb = new StringBuilder();
        if(data instanceof String){
            String str = (String) data;
            str = str.trim();
            if(str.startsWith("{")){
                data = new JSONObject(str);
            }
            else if(str.startsWith("[")){
                data = new JSONArray(str);
            }
        }
        if(data instanceof JSONObject){
            sb.append("<object>");
            JSONObject obj = (JSONObject) data;
            for(String key : obj.keySet()){
                sb.append(inner_data_object(key , obj.get(key)));
            }
            sb.append("</object>");
        }
        else if(data instanceof JSONArray){
            sb.append("<array>");
            JSONArray arr = (JSONArray) data;
            for(int i = 0; i <arr.length() ; i++){
                sb.append(inner_data_array(arr.get(i)));
            }
            sb.append("</array>");
        }
        else if(data == null){
            sb.append("<null name=\"").append(data + "\"/>");
        }
        else if(data instanceof Number){
            sb.append("<number>").append(data).append("</number>");
        }
        else if(data instanceof Boolean){
            sb.append("<boolean>").append(data).append("</boolean>");
        }
        return sb.toString();
    }
    public String inner_data_array(Object value){
        StringBuilder sb = new StringBuilder();
        if(value instanceof JSONObject){
            sb.append("<object>").append(convert(value)).append("</object>");
        }
        else if(value instanceof JSONArray){
            sb.append("<array>").append(convert(value)).append("</array>");
        }
        else if(value instanceof Number){
            sb.append("<number>").append(value).append("</number>");
        }
        else if(value instanceof Boolean){
            sb.append("<boolean>").append(value).append("</boolean>");
        }
        else if(value instanceof String){
            sb.append("<string>").append(value).append("</string>");
        }
        else{
            sb.append("<null name=\"").append(value + "\"/>");
        }
        return sb.toString();
    }
    public String inner_data_object(String key , Object value){
        StringBuilder sb = new StringBuilder();
        if(value instanceof JSONObject){
            sb.append("<object name=\"").append(key + "\">").append(convert(value)).append("</object>");
        }
        else if(value instanceof JSONArray){
            sb.append("<array name=\"").append(key + "\">");
            JSONArray arr = (JSONArray) value;
            for(int i = 0; i <arr.length() ; i++){
                sb.append(inner_data_array(arr.get(i)));
            }
            sb.append("</array>");
        }
        else if(value instanceof Number){
            sb.append("<number name=\"").append(key + "\">").append(value).append("</number>");
        }
        else if(value instanceof Boolean){
            sb.append("<boolean name=\"").append(key + "\">").append(value).append("</boolean>");
        }
        else if(value instanceof String){
            sb.append("<string name=\"").append(key + "\">").append(value).append("</string>");
        }
        else{
            sb.append("<null>").append("</null>");
        }
        return sb.toString();
    }
}
