package pl.put.poznan.jsontools.logic;

import java.util.ArrayList;

public class IdentityJsonTool implements IJsonTool {
    private IJsonTool wrappee;

    public IdentityJsonTool(IJsonTool wrappee) throws Exception {
        this.wrappee = wrappee;
    }
    @Override
    public String getJsonString() {
        return wrappee.getJsonString();
    }
}
