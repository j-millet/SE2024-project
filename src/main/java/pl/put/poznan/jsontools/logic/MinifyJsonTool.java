package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MinifyJsonTool implements IJsonTool{
    private IJsonTool wrappee;
    private ObjectMapper objectMapper;

    public MinifyJsonTool(IJsonTool wrappee) {
        this.wrappee = wrappee;
        this.objectMapper = new ObjectMapper();
    }

    private String minifyJson() throws Exception {
        Map<String, Object> jsonMap = objectMapper.readValue(wrappee.getJsonString(), new TypeReference<Map<String, Object>>(){});
        return objectMapper.writeValueAsString(jsonMap);
    }

    @Override
    public String getJsonString() {
        try {
            return minifyJson();
        } catch (Exception e) {
            return wrappee.getJsonString();
        }

    }
}
