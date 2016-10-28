package org.openimmunizationsoftware.dqa.codebase.values;

import java.util.HashMap;
import java.util.Map;


public enum CodeStatusValue {

	INVALID("INVALID"), UNRECOGNIZED("UNRECOGNIZED"), DEPRECATED("DEPRECATED"), IGNORED("IGNORED");
	
		private static final Map<String, CodeStatusValue> descMap = new HashMap<String, CodeStatusValue>();
	
	static {
		for (CodeStatusValue t : CodeStatusValue.values()) {
			descMap.put(t.value, t);
		}
	}
	
	private String value;
	
	private CodeStatusValue(String valueIn) {
		this.value = valueIn;
	}
	
	public static CodeStatusValue getByValue(String value) {
		return descMap.get(value);
	}
}
