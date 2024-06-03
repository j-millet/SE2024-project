package pl.put.poznan.jsontools.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IncludeKeysJsonToolTest {
    private IJsonTool basicJsonTool;
    private String jsonString;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        objectMapper = new ObjectMapper();
        jsonString = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\", \"surname\":\"Smith\"}";
        basicJsonTool = new IJsonTool() {
            @Override
            public String getJsonString() {
                return jsonString;
            }
        };
    }

    @AfterEach
    public void tearDown() {
        objectMapper = null;
        basicJsonTool = null;
        jsonString = null;
    }

    //Basic test
    @Test
    public void testIncludeKeys1() throws Exception{
        String[] keysToInclude = {"name", "city"};
        IJsonTool includeKeysTool = new IncludeKeysJsonTool(basicJsonTool, keysToInclude);

        String result = includeKeysTool.getJsonString();
        String expectedJson = "{\"name\":\"John\",\"city\":\"New York\"}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        assertEquals(trueMap, expectedMap);
    }

    //Empty string
    @Test
    public void testIncludeKeys2() throws Exception{
        String[] keysToInclude = {};
        IJsonTool includeKeysTool = new IncludeKeysJsonTool(basicJsonTool, keysToInclude);

        String result = includeKeysTool.getJsonString();
        String expectedJson = "{}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        assertEquals(trueMap, expectedMap);
    }

    //All parameters
    @Test
    public void testIncludeKeys3() throws Exception{
        String[] keysToInclude = {"name", "age", "city", "surname"};
        IJsonTool includeKeysTool = new IncludeKeysJsonTool(basicJsonTool, keysToInclude);

        String result = includeKeysTool.getJsonString();
        String expectedJson = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\", \"surname\":\"Smith\"}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        assertEquals(trueMap, expectedMap);
    }

    //Non-existing keys
    @Test
    public void testIncludeKeys4() throws Exception{
        String[] keysToInclude = {"name", "phone number", "city", "address"};
        IJsonTool includeKeysTool = new IncludeKeysJsonTool(basicJsonTool, keysToInclude);

        String result = includeKeysTool.getJsonString();
        String expectedJson = "{\"name\":\"John\",\"city\":\"New York\"}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        assertEquals(trueMap, expectedMap);
    }

    //Different order of keys
    @Test
    public void testIncludeKeys5() throws Exception{
        String[] keysToInclude = {"age", "surname", "city", "name"};
        IJsonTool includeKeysTool = new IncludeKeysJsonTool(basicJsonTool, keysToInclude);

        String result = includeKeysTool.getJsonString();
        String expectedJson = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\", \"surname\":\"Smith\"}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});

        assertEquals(trueMap, expectedMap);
    }
}