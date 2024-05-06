package pl.put.poznan.jsontools.logic;

public class JsonContainer implements IJsonTool{
    private String json;

    public JsonContainer(String json) {
        this.json = json;
    }

    @Override
    public String getJsonString() {
        return json;
    }
}
