package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class IncludeKeysJsonTool extends JsonTool {
    private String[] keysToInclude;
    private ObjectMapper objectMapper;

    public IncludeKeysJsonTool(IJsonTool wrappee, String[] keysToInclude) {
        super(wrappee, "include");
        this.keysToInclude = keysToInclude;
        this.objectMapper = new ObjectMapper();
    }

    private String includeKeys(String jsonString) throws Exception {
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });
        Set<String> keysToIncludeSet = Set.of(keysToInclude);
        Map<String, Object> filteredMap = map.entrySet().stream()
                .filter(entry -> keysToIncludeSet.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return objectMapper.writeValueAsString(filteredMap);
    }

    @Override
    public String getJsonString() {
        try {
            return includeKeys(wrappee.getJsonString());
        } catch (Exception e) {
            return wrappee.getJsonString();
        }
    }
}