package org.wongws.hichat.util;

import java.util.HashMap;
import java.util.Map;

public class ChatEnum {
	public enum EnuPicType {
		online(1), offline(2);

		public static final int SIZE = Integer.SIZE;
		private int intValue;
		private static Map<Integer, EnuPicType> mappings;

		private static Map<Integer, EnuPicType> getMappings() {
			if (mappings == null) {
				synchronized (EnuPicType.class) {
					if (mappings == null)
						mappings = new HashMap<>();
				}
			}
			return mappings;
		}

		private EnuPicType(int value) {
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue() {
			return intValue;
		}

		public static EnuPicType forValue(int value) {
			return getMappings().get(value);
		}
	}

	public enum EnuRoleType {
		ROLE_ADMIN(1), ROLE_USER(2);

		public static final int SIZE = Integer.SIZE;
		private int intValue;
		private static Map<Integer, EnuRoleType> mappings;

		private static Map<Integer, EnuRoleType> getMappings() {
			if (mappings == null) {
				synchronized (EnuRoleType.class) {
					if (mappings == null)
						mappings = new HashMap<>();
				}
			}
			return mappings;
		}

		private EnuRoleType(int value) {
			intValue = value;
			getMappings().put(value, this);
		}

		public int getValue() {
			return intValue;
		}

		public static EnuRoleType forValue(int value) {
			return getMappings().get(value);
		}
	}
}
