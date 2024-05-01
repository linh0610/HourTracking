package persistence;

import org.json.JSONArray;
import org.json.JSONObject;

public interface WritableArray {
    // EFFECTS: returns this as JSON array
    JSONArray toJson();
}
