package pl.put.poznan.jsontools.logic;

public class JsonTool implements IJsonTool{
    protected IJsonTool wrappee;
    private String name;

    public JsonTool(IJsonTool wrappee,String name) {
        this.wrappee = wrappee;
        this.name = name;
    }

    public JsonTool(IJsonTool wrappee) {
        this.wrappee = wrappee;
        this.name = "None";
    }

    @Override
    public String getJsonString(){
        return wrappee.getJsonString();
    }

    public String getName() {
        return name;
    }
}
