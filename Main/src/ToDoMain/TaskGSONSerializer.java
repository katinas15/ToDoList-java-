package ToDoMain;

import ToDoMain.Project;
import ToDoMain.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TaskGSONSerializer implements JsonSerializer<Task> {
    @Override
    public JsonElement serialize(Task t, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", t.getId());
        jsonObject.addProperty("title", t.getTitle());
        if(t.getParentTask() != null){
            jsonObject.addProperty("parentTask", t.getParentTask().getId());
            int indent = 0;
            Task parent = t.getParentTask();
            while(parent != null){
                indent++;
                parent = parent.getParentTask();
            }
            jsonObject.addProperty("indent", indent);
        }
        jsonObject.addProperty("createdBy", t.getCreatedBy().getId());
        jsonObject.addProperty("createdByName", t.getCreatedBy().getName());
        jsonObject.addProperty("createdAt", t.getCreated().toString());
        if(t.getCompletedBy() != null){
            jsonObject.addProperty("completedBy", t.getCompletedBy().getId());
            jsonObject.addProperty("completedByName", t.getCompletedBy().getName());
            jsonObject.addProperty("completedAt", t.getCompleted().toString());
        }

        return jsonObject;
    }
}
