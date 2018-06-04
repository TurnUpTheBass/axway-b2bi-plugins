package util;

import java.io.IOException;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class JSONDateTypeAdapter extends TypeAdapter<Date> {
	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		String date = in.nextString();
		if (date.startsWith("/Date(")) {
			try {
				date = date.substring(date.indexOf('(') + 1,
						date.indexOf(')') - 1);
			} catch (Exception e) {
				return null;
			}
		}
		try {
			long milis = Long.parseLong(date);
			return new Date(milis);
		} catch (NumberFormatException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		String date = null;
		if (value != null) {
			date = String.format("/Date(%d)/", value.getTime());
		}
		out.value(date);
	}

}
