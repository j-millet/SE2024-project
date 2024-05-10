package pl.put.poznan.jsontools.logic;

import java.util.ArrayList;

public class IdentityJsonTool extends JsonTool {

    public IdentityJsonTool(IJsonTool wrappee) {
        super(wrappee,"identity");
    }
    @Override
    public String getJsonString() {
        return wrappee.getJsonString();
    }
}
