package com.securin.converters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class xml_converter implements XMLJSONConverterI{
    ObjectMapper mapper = new ObjectMapper();
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
    public String convert(Object data) {
        StringBuilder sb = new StringBuilder();
        try {
            if (data instanceof String) {
                String str = (String) data;
                str = str.trim();
                if (str.startsWith("{")) {
                    Map<String, Object> map = mapper.readValue(str, new TypeReference<LinkedHashMap<String, Object>>() {
                    });

                    data = map;
                }
                else if (str.startsWith("[")) {
                    List<Object> map = mapper.readValue(str, new TypeReference<ArrayList<Object>>() {
                    });
                }
            }
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(data instanceof Map){
            sb.append("<object>");

            Map<String,Object> obj = (Map<String,Object>)(data);
            for (String key : obj.keySet()) {
                sb.append(inner_data_object(key, obj.get(key)));
            }
            sb.append("</object>");
        }
        else if(data instanceof List){
            sb.append("<array>");
            List<Object> arr = (List<Object>) data;
            for(int i = 0; i <arr.size() ; i++){
                sb.append(inner_data_array(arr.get(i)));
            }
            sb.append("</array>");
        }

        else if(data instanceof Number){
            sb.append("<number>").append(data).append("</number>");
        }
        else if(data instanceof Boolean){
            sb.append("<boolean>").append(data).append("</boolean>");
        }
        else if(data instanceof String){
            sb.append("<string>").append(data).append("</string>");
        }
        else{
            sb.append("<null/>");
        }
        return sb.toString();
    }
    public String inner_data_array(Object value){
        StringBuilder sb = new StringBuilder();
        if(value instanceof Map){
            sb.append(convert(value));
        }
        else if(value instanceof List){
            sb.append(convert(value));
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
            sb.append("<null/>");
        }
        return sb.toString();
    }
    public String inner_data_object(String key , Object value){
        StringBuilder sb = new StringBuilder();
        if(value instanceof Map){
            sb.append("<object name=\"").append(key + "\">");
            Map<String,Object> obj = (Map<String,Object>)(value);
            for (String key1 : obj.keySet()) {
                sb.append(inner_data_object(key1, obj.get(key1)));
            }
            sb.append("</object>");
        }
        else if(value instanceof List){
            sb.append("<array name=\"").append(key + "\">");
            List<Object> arr = (List<Object>) value;
            for(int i = 0; i <arr.size() ; i++){
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
            sb.append("<null name=\"" + key + "\"/>");
        }
        return sb.toString();
    }
}
