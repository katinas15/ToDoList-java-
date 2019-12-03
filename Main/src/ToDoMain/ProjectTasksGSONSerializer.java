package ToDoMain;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class ProjectTasksGSONSerializer implements JsonSerializer<List<Task>> {
    @Override
    public JsonElement serialize(List<Task> tasks, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonObject = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Task.class, new TaskGSONSerializer());
        Gson parser = gsonBuilder.create();

        for(Task t:tasks){
            jsonObject.add(parser.toJson(t));
        }

        return jsonObject;
    }
}
