package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is the class that returns json string only with keys provided by the user (filtering)
 * @author Kuba Czech
 * @author Jakub Jagla
 * @author Lukasz Borak
 */
public class IncludeKeysJsonTool extends JsonTool {
    private String[] keysToInclude;
    private ObjectMapper objectMapper;

    /**
     * Constructor for class IncludeKeysJsonTool
     * @param wrappee json format (input) string
     * @param keysToInclude array of strings that contains keys to be included
     */
    public IncludeKeysJsonTool(IJsonTool wrappee, String[] keysToInclude) {
        super(wrappee, "include");
        this.keysToInclude = keysToInclude;
        this.objectMapper = new ObjectMapper();
    }

    /**
     *
     * @param jsonString string to be filtered
     * @return json string with only certain keys included
     * @throws Exception
     */
    private String includeKeys(String jsonString) throws Exception {
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });
        Set<String> keysToIncludeSet = Set.of(keysToInclude);
        Map<String, Object> filteredMap = map.entrySet().stream()
                .filter(entry -> keysToIncludeSet.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return objectMapper.writeValueAsString(filteredMap);
    }

    /**
     * Inherited method (from Json Tool Class) that returns string after transformation
     * @return string in json format that is result of the filtering
     */
    @Override
    public String getJsonString() {
        try {
            return includeKeys(wrappee.getJsonString());
        } catch (Exception e) {
            return wrappee.getJsonString();
        }
    }
}