package util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serialization {
	/**
	 * Serizalize object to JSON string.
	 * 
	 * @param entity The serialization object.
	 * @return JSON string which represents object.
	 */
	public static <T> String serizalize(T entity) {
		if (entity != null) {
			Gson gson = new GsonBuilder()
					// .setPrettyPrinting()
					.registerTypeAdapterFactory(new EnumAdapterFactory())
					.registerTypeAdapter(Date.class, new JSONDateTypeAdapter())
					.create();

			return gson.toJson(entity);
		}

		return null;
	}

	/**
	 * Deserialize the input JSON string into the object.
	 * 
	 * @param serialized The input JSON string.
	 * @param type The type of object.
	 * 
	 * @return The instance of object of type Class<T>.
	 */
	public static <T> T deserizalize(String serialized, Class<T> type) {
		
		if (serialized != null && !StringUtils.isEmpty(serialized)) {
			Gson gson = new GsonBuilder()
					.registerTypeAdapterFactory(new EnumAdapterFactory())
					.registerTypeAdapter(Date.class, new JSONDateTypeAdapter())
					.create();

			return gson.fromJson(serialized, type);
		}

		return null;
	}

}
