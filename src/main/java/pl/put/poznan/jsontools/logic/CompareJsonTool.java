package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;

public class CompareJsonTool extends JsonTool{
    private String toCompare;
    private ObjectMapper objectMapper;

    public CompareJsonTool(IJsonTool wrappee, String stringToCompare) {
        super(wrappee,"compare");
        this.toCompare = stringToCompare;
        this.objectMapper = new ObjectMapper();
    }

    private String compareTexts(String jsonString) throws Exception{
        Map<String,Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
        Map<String,Object> map2 = objectMapper.readValue(this.toCompare, new TypeReference<Map<String, Object>>() {});

        Iterator<Map.Entry<String, Object>> iterator1 = map.entrySet().iterator();
        Iterator<Map.Entry<String, Object>> iterator2 = map2.entrySet().iterator();

        List<Integer> linesWithDifference = new ArrayList<>();
        int lineNumber = 0;
        while(iterator1.hasNext() && iterator2.hasNext()){
            lineNumber++;

            Map.Entry<String, Object> entry1 = iterator1.next();
            Map.Entry<String, Object> entry2 = iterator2.next();

            if (
                    entry1.getKey() != null &&
                    entry2.getKey() != null &&
                    entry1.getKey().equals(entry2.getKey()) &&
                    entry1.getValue().equals(entry2.getValue())
            ){
                continue;
            }
            else {
                linesWithDifference.add(lineNumber);
            }
        }

        int diff = Math.abs(map.size() - map2.size());
        for (int i=1; i<=diff;i++){
            linesWithDifference.add(lineNumber+i);
        }
        return linesWithDifference.toString();
    }

    @Override
    public String getJsonString(){
        try {
            return compareTexts(wrappee.getJsonString());
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
}