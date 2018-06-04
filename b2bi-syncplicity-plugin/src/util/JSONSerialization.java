package util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * For this sample code we are using google's open source JSON serialize/deserialize 
 * library.  Any method of serializing/deserializing of the JSON request/response
 * can be used in your own application.
 * 
 * NOTE: this is not a complete instantiation of all the data types that may need to 
 *       be serialized/deserialized when interacting with other Syncplicity APIs,
 *       this example covers what is needed for the few APIs invoked in 
 *       this application.
 */
public class JSONSerialization {
	
	/**
	 * Serialize objects to JSON string.
	 * 
	 * @param entity The serialization object.
	 * 
	 * @return JSON string which represents object.
	 */
	public static <T> String serizalize(T entity) {
		
		if (entity != null) {
			Gson gson = new GsonBuilder()
					// .setPrettyPrinting()  // uncomment this line to turn on debugging
					.registerTypeAdapterFactory(new EnumAdapterFactory())
					.registerTypeAdapter(Date.class, new JSONDateTypeAdapter())
					.create();

			return gson.toJson(entity);
		}

		return null;
	}

	/**
	 * De-serialize the input JSON string into the object.
	 * 
	 * @param serialized The input JSON string.
	 * @param type  The type of object.
	 * 
	 * @return The instance of object of type Class<T>.
	 */
	public static <T> T deserizalize(String serialized, Class<T> classType) {
		
		if( !StringUtils.isEmpty(serialized) && !StringUtils.isWhitespace(serialized) ) {
			
			Gson gson = new GsonBuilder()
					.registerTypeAdapterFactory(new EnumAdapterFactory())
					.registerTypeAdapter(Date.class, new JSONDateTypeAdapter())
					.create();

			return gson.fromJson(serialized, classType);
		}

		return null;
	}
}