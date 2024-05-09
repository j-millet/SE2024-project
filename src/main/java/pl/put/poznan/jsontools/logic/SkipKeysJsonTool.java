package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SkipKeysJsonTool implements IJsonTool{
    private IJsonTool wrappee;
    private String[] keysToSkip;
    private ObjectMapper objectMapper;

    public SkipKeysJsonTool(IJsonTool wrappee, String[] keysToSkip) {
        this.wrappee = wrappee;
        this.keysToSkip = keysToSkip;
        this.objectMapper = new ObjectMapper();
    }

    private String skipKeys(String jsonString) throws Exception{
        Map<String,Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
        for (String key : keysToSkip) {
            map.remove(key);
        }
        return objectMapper.writeValueAsString(map);
    }

    @Override
    public String getJsonString() {
        try {
            return skipKeys(wrappee.getJsonString());
        } catch (Exception e) {
            return wrappee.getJsonString();
        }
    }
}
