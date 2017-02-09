package org.immregistries.dqa.codebase.client.reference;

import java.util.HashMap;
import java.util.Map;

import org.immregistries.dqa.codebase.client.generated.CodeStatus;


public enum CodeStatusValue {

	INVALID("INVALID"), UNRECOGNIZED("UNRECOGNIZED"), DEPRECATED("DEPRECATED"), IGNORED("IGNORED"), VALID("Valid");
	
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
	
	public static CodeStatusValue getBy(String value) {
		CodeStatusValue val = descMap.get(value);
		if (val == null) {
			return CodeStatusValue.UNRECOGNIZED;
		}
		
		return val;
	}
	
	public static CodeStatusValue getBy(CodeStatus cs) {
		return descMap.get(cs.getStatus());
	}
}
