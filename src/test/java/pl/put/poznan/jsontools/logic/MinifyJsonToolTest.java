package pl.put.poznan.jsontools.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MinifyJsonToolTest {
    private IJsonTool basicJsonTool;
    private String jsonString;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        basicJsonTool = mock(IJsonTool.class);
    }

    @AfterEach
    public void tearDown() {
        objectMapper = null;
        basicJsonTool = null;
        jsonString = null;
    }

    //Basic test - some spaces
    @Test
    public void testMinify1() throws Exception {
        jsonString = "{\"name\" :  \"John\", \"age\"  : 30    }";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool = new MinifyJsonTool(basicJsonTool);
        String result = minifyTool.getJsonString();
        String expectedJson = "{\"name\":\"John\",\"age\":30}";
        assertEquals(expectedJson, result);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Check that spaces in keys or values are not 'cancelled'
    @Test
    public void testMinify2() throws Exception {
        jsonString = "{\"n a   m" +
                "e    \" :  \"Jo h" +
                "n\", \"ag   e\"  : 3" +
                "0    }";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool = new MinifyJsonTool(basicJsonTool);
        String result = minifyTool.getJsonString();

        String expectedJson = "{\"n a   m" +
                "e\":\"Jo h" +
                "n\",\"ag   e\":30}";
        assertNotEquals(expectedJson, result);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Minify from full
    @Test
    public void testMinify3() throws Exception {
        jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool = new MinifyJsonTool(basicJsonTool);
        String result = minifyTool.getJsonString();

        String expectedJson = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        assertEquals(expectedJson, result);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Minify lots of spaces
    @Test
    public void testMinify4() throws Exception {
        jsonString = "{\"name\"   :   \"John\"      ," +
                " \"age\"                 : 30    , \"city\" " +
                "" +
                "" +
                "" +
                "" +
                "" +
                ":\"New York\",    " +
                " \"surname\"                             :\"Smith\"   }";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool = new MinifyJsonTool(basicJsonTool);
        String result = minifyTool.getJsonString();
        String expectedJson = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        assertEquals(expectedJson, result);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Minify empty
    @Test
    public void testMinify5() throws Exception {
        jsonString = "{}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool = new MinifyJsonTool(basicJsonTool);

        String result = minifyTool.getJsonString();
        String expectedJson = "{}";

        Map<String, Object> expectedMap = objectMapper.readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> trueMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        assertEquals(expectedMap, trueMap);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Minify from minified
    @Test
    public void testMinify6() throws Exception {
        jsonString = "{\"name\" :  \"John\", \"age\"  : 30    }";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool minifyTool1 = new MinifyJsonTool(basicJsonTool);
        String result = minifyTool1.getJsonString();

        when(basicJsonTool.getJsonString()).thenReturn(result);
        IJsonTool minifyTool2 = new MinifyJsonTool(basicJsonTool);
        result = minifyTool2.getJsonString();

        String expectedJson = "{\"name\":\"John\",\"age\":30}";
        assertEquals(expectedJson, result);
        verify(basicJsonTool, times(2)).getJsonString();
    }
}
