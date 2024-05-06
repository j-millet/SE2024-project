package pl.put.poznan.jsontools.logic;

import java.util.ArrayList;

public class JsonToolFactory {
    public static IJsonTool createJsonTool(IJsonTool wrappee, String transformationType, ArrayList<String> wrapperParams) throws Exception{
        if (transformationType == null){
            return new IdentityJsonTool(wrappee);
        }
        switch (transformationType) {
            case "identity":
                return new IdentityJsonTool(wrappee);
            default:
                return new IdentityJsonTool(wrappee);
        }
    }
}
