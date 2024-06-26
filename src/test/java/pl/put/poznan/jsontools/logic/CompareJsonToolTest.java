package pl.put.poznan.jsontools.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompareJsonToolTest {
    private IJsonTool basicJsonTool;
    private String jsonString;

    @BeforeEach
    public void setUp() {
        basicJsonTool = mock(IJsonTool.class);
    }
    @AfterEach
    public void tearDown(){
        jsonString = null;
        basicJsonTool = null;
    }

    //Basic test
    @Test
    public void testCompare1(){
        jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        String toCompare = "{\"name\":\"Adam\",\"age\":30,\"city\":\"New Jersey\",\"surname\":\"Smith\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[1, 3]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing two identical strings
    @Test
    public void testCompare2(){
        jsonString = "{\"name\":\"John\", \"age\":30}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, "{\"name\":\"John\", \"age\":30}");
        String result = compareTool.getJsonString();
        String expectedResult = "[]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing two empty strings
    @Test
    public void testCompare3(){
        jsonString = "{}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        String toCompare = "{}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing some string with empty string
    @Test
    public void testCompare4(){
        jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        String toCompare = "{}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[1, 2, 3, 4]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing string with varying number of arguments
    @Test
    public void testCompare5(){
        jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\", " +
                "\"phone number\":\"123456789\", \"sex\":\"male\", \"color of eyes\":\"brown\", " +
                "\"car\":\"Ferrari\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);

        String toCompare = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[5, 6, 7, 8]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing strings with different keys, same values
    @Test
    public void testCompare6() {
        jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"surname\":\"Smith\", " +
                "\"phone number\":\"123456789\", \"sex\":\"male\", \"color of eyes\":\"brown\", " +
                "\"car\":\"Ferrari\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);
        String toCompare = "{\"Name\":\"John\",\"Age\":30,\"City\":\"New York\",\"Surname\":\"Smith\", " +
                "\"PhoneNumber\":\"123456789\", \"Sex\":\"male\", \"Color of eyes\":\"brown\", " +
                "\"Car\":\"Ferrari\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[1, 2, 3, 4, 5, 6, 7, 8]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Comparing strings with same keys, different values
    @Test
    public void testCompare7() {
        jsonString = "{\"name\":\"Emma\",\"age\":30,\"city\":\"New York\",\"surname\":\"Williams\", " +
                "\"phone number\":\"123456789\", \"sex\":\"female\", \"color of eyes\":\"brown\", " +
                "\"car\":\"Mercedes\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);
        String toCompare = "{\"name\":\"Michael\",\"age\":25,\"city\":\"Monaco\",\"surname\":\"Smith\", " +
                "\"phone number\":\"987654321\", \"sex\":\"male\", \"color of eyes\":\"blue\", " +
                "\"car\":\"Ferrari\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[1, 2, 3, 4, 5, 6, 7, 8]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Same keys and values, different order
    @Test
    public void testCompare8(){
        jsonString = "{\"English\":\"Hello World!\",\"German\":\"Hallo Welt!\",\"Spanish\":\"Hola Mundo!\",\"Polish\":\"Witaj Swiecie!\", " +
                "\"French\":\"Bonjuor Monde!\", \"Italian\":\"Ciao mondo!\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);
        String toCompare = "{\"Polish\":\"Witaj Swiecie!\",\"Italian\":\"Ciao mondo!\",\"Spanish\":\"Hola Mundo!\",\"English\":\"Hello World!\", " +
                "\"French\":\"Bonjuor Monde!\", \"German\":\"Hallo Welt!\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[1, 2, 4, 6]";

        assertFalse(result == "[]");
        assertFalse(result == "[1, 2, 3, 4, 5, 6]");
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }

    //Combine previous tests - some keys are different, some values are different, different lenghts
    @Test
    public void testCompare9(){
        jsonString = "{\"name\":\"Michael\",\"Age\":30,\"city\":\"New York\",\"surname\":\"Smith\", " +
                "\"phone number\":\"123456789\", \"sex\":\"female\", \"color of eyes\":\"brown\", " +
                "\"car\":\"Mercedes\"}";
        when(basicJsonTool.getJsonString()).thenReturn(jsonString);
        String toCompare = "{\"name\":\"Michael\",\"age\":25,\"city\":\"New York\",\"surname\":\"Jordan\", " +
                "\"fax number\":\"123456789\", \"Number of children\":\"2\", \"color of eyes\":\"brown\"}";
        IJsonTool compareTool = new CompareJsonTool(basicJsonTool, toCompare);
        String result = compareTool.getJsonString();
        String expectedResult = "[2, 4, 5, 6, 8]";
        assertEquals(result, expectedResult);
        verify(basicJsonTool, times(1)).getJsonString();
    }
}