package dev.u9g.minecraftdatagenerator.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.u9g.minecraftdatagenerator.util.DGU;
import net.minecraft.class_4323;

public class CommandsDataGenerator implements IDataGenerator {
    private static final JsonArray parsers = new JsonArray();

    @Override
    public String getDataName() {
        return "commands";
    }

    private static JsonObject transform(JsonObject input, String name, JsonObject root) {
        JsonObject output = new JsonObject();

        output.addProperty("type", input.get("type").getAsString());
        output.addProperty("name", name);
        output.addProperty("executable", input.has("executable") && input.get("executable").getAsBoolean());

        JsonArray childrenArray = new JsonArray();
        JsonArray redirectPath = new JsonArray();

        if (input.has("redirect")) {
            var redirectName = input.get("redirect").getAsJsonArray().get(0).getAsString();
            if (!redirectName.equals("execute")) {
                JsonObject redirect = root.getAsJsonObject("children").getAsJsonObject(redirectName);
                for (var entry : redirect.getAsJsonObject("children").entrySet()) {
                    childrenArray.add(transform(entry.getValue().getAsJsonObject(), entry.getKey(), root));
                }
            } else {
                redirectPath.add(redirectName);
            }
        }

        output.add("redirects", redirectPath);

        if (input.has("children")) {
            JsonObject children = input.getAsJsonObject("children");

            for (String childName : children.keySet()) {
                childrenArray.add(transform(children.getAsJsonObject(childName), childName, root));
            }
        }

        output.add("children", childrenArray);

        if (input.has("parser")) {
            JsonObject parser = new JsonObject();

            parser.addProperty("parser", input.get("parser").getAsString());
            parser.add("modifier", input.has("properties") ? input.getAsJsonObject("properties") : null);
            parser.add("examples", input.getAsJsonArray("examples"));

            if (!parsers.contains(parser)) {
                parsers.add(parser.deepCopy());
            }

            parser.remove("examples");

            output.add("parser", parser);
        }

        return output;
    }

    @Override
    public JsonElement generateDataJson() {
        JsonObject resultObject = new JsonObject();

        var dispatcher = DGU.getCurrentlyRunningServer().method_2971().method_17518();
        var json = class_4323.method_19894(dispatcher, dispatcher.getRoot());

        resultObject.add("root", transform(json, "root", json));
        resultObject.add("parsers", parsers);

        return resultObject;
    }
}
