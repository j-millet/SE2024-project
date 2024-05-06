package pl.put.poznan.jsontools.rest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.put.poznan.jsontools.logic.IJsonTool;
import pl.put.poznan.jsontools.logic.JsonContainer;
import pl.put.poznan.jsontools.logic.JsonToolFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Math.min;

@RestController
@RequestMapping("/api/json-tools/")
public class JsonToolsController {

    private static final Logger logger = LoggerFactory.getLogger(JsonToolsController.class);

    private ObjectMapper objectMapper;
    private Map<String,Object> requestBodyMap;

    private String jsonString;
    private ArrayList<LinkedHashMap<String,Object>> transforms;



    public JsonToolsController(){
        objectMapper = new ObjectMapper();
    }

    private void loadRequestParams(String requestBody) throws Exception{
        try{
            requestBodyMap = objectMapper.readValue(requestBody, new TypeReference<Map<String,Object>>(){});
            jsonString = (String) requestBodyMap.get("json-string");
            transforms = (ArrayList<LinkedHashMap<String,Object>>) requestBodyMap.get("transforms");
        }catch (Exception e){
            throw new Exception("Invalid request body -> Parsing error.");
        }
        if (jsonString == null){
            throw new Exception("Invalid request body -> JSON string not found.");
        }
        try {
            objectMapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});
        }catch (Exception e){
            throw new Exception("Invalid JSON for transformation -> Parsing error.");
        }
        if (transforms == null){
            transforms = new ArrayList<LinkedHashMap<String,Object>>();
        }
    }

    private IJsonTool parseTransform(IJsonTool wrappee, LinkedHashMap<String, Object> transform) throws Exception{
        String transformationType;
        ArrayList<String> wrapperParams;

        if(!transform.containsKey("type")){
           throw new Exception("Invalid transforms -> No type for transform.");
        }
        try {
            transformationType = (String) transform.get("type");
            wrapperParams = (ArrayList<String>) transform.get("params");
        }catch (Exception e){
            throw new Exception("Invalid transforms -> Wrong structure.");
        }

        return JsonToolFactory.createJsonTool(wrappee, transformationType, wrapperParams);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity post(@RequestBody String requestBody) {
        try {
            loadRequestParams(requestBody);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{\"error\":\"%s\"}", e.getMessage()));
        }

        logger.debug(
                String.format(
                        "Received JSON head: %s\nTransforms: %s",
                        jsonString.substring(0,min(jsonString.length(),100)),
                        transforms.toString()
                )
        );

        IJsonTool jsonTool = new JsonContainer(jsonString);

        //apply transformation wrappers
        for(LinkedHashMap<String,Object> transform : transforms){

            try {
                jsonTool = parseTransform(jsonTool, transform);
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{\"error\":\"%s\"}", e.getMessage()));
            }
        }

        try {
            logger.debug(String.format("{\"json-string\":\"%s\"}", jsonTool.getJsonString()));
            return ResponseEntity.status(HttpStatus.OK).body(String.format("{\"json-string\":\"%s\"}", jsonTool.getJsonString()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Oops :( Something something server monkeys.\"}");
        }
    }
}


