package GSONSerializers;

import Objektai.Project;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class UserProjectGSONSerializer implements JsonSerializer<List<Project>> {

    @Override
    public JsonElement serialize(List<Project> projects, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonObject = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
        Gson parser = gsonBuilder.create();

        for(Project p:projects){
            jsonObject.add(parser.toJson(p));
        }

        return jsonObject;
    }

}
