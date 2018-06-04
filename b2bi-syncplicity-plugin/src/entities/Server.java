package entities;

import java.io.Serializable;

public class Server {
	public enum Status implements Serializable {
		Unknown(0), Online(1), Offline(2);

		private int intValue;
		private static java.util.HashMap<Integer, Status> mappings;

		private static java.util.HashMap<Integer, Status> getMappings() {
			if (mappings == null) {
				synchronized (Status.class) {
					if (mappings == null) {
						mappings = new java.util.HashMap<Integer, Status>();
					}
				}
			}
			return mappings;
		}

		private Status(int value) {
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue() {
			return intValue;
		}

		public static Status forValue(int value) {
			return getMappings().get(value);
		}
	}
}