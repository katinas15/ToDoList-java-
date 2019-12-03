package ToDoMain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CompanyGSONSerializer implements JsonSerializer<Company> {

    @Override
    public JsonElement serialize(Company c, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", c.getId());
        jsonObject.addProperty("login", c.getLogin());
        jsonObject.addProperty("pass", c.getPass());
        jsonObject.addProperty("companyName", c.getCompanyName());

        return jsonObject;
    }
}
