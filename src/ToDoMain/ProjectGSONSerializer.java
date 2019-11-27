package ToDoMain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class ProjectGSONSerializer implements JsonSerializer<Project> {

        @Override
        public JsonElement serialize(Project p, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("id", p.getId());
            jsonObject.addProperty("title", p.getTitle());
            jsonObject.addProperty("createdBy", p.getCreatedBy().getId());
            jsonObject.addProperty("createdAt", p.getCreated().toString());

            if(p.getCompleted() != null){
                jsonObject.addProperty("completedBy", p.getCompletedBy().getId());
                jsonObject.addProperty("completedAt", p.getCompleted().toString());
            }

            return jsonObject;
        }
}
