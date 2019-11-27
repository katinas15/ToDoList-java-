package ToDoMain;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserGSONSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("login", user.getLogin());
        jsonObject.addProperty("pass", user.getPass());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("surname", user.getSurname());

        return jsonObject;
    }
}
