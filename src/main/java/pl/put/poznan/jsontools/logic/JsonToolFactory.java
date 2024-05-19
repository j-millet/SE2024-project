package pl.put.poznan.jsontools.logic;

import java.util.ArrayList;

public class JsonToolFactory {
    public static JsonTool createJsonTool(IJsonTool wrappee, String transformationType, ArrayList<String> wrapperParams){
        if (transformationType == null){
            return new IdentityJsonTool(wrappee);
        }
        switch (transformationType) {
            case "identity":
                return new IdentityJsonTool(wrappee);
            case "compare":
                return new CompareJsonTool(wrappee, wrapperParams.get(0));
            case "include":
                return new IncludeKeysJsonTool(wrappee, wrapperParams.toArray(new String[0]));
            case "minify":
                return new MinifyJsonTool(wrappee);
            case "full":
                return new FullStructureJsonTool(wrappee);
            case "skip":
                return new SkipKeysJsonTool(wrappee, wrapperParams.toArray(new String[0]));
            default:
                return new IdentityJsonTool(wrappee);
        }
    }
}
