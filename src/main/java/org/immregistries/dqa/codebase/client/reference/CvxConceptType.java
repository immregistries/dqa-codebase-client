package org.immregistries.dqa.codebase.client.reference;

import java.util.HashMap;
import java.util.Map;

import org.immregistries.dqa.codebase.client.generated.CodeStatus;

public enum CvxConceptType {
	  NON_VACCINE("non vaccine")
	, VACCINE("vaccine")
	, UNSPECIFIED("unspecified")
	, NEVER_ACTIVE("never active")
	, FOREIGN_VACCINE("foreign vaccine");
	
	private static final Map<String, CvxConceptType> codeMap = new HashMap<String, CvxConceptType>();

	static {
		for (CvxConceptType t : CvxConceptType.values()) {
			codeMap.put(t.value, t);
		}
	}

	private String value;

	private CvxConceptType(String valueIn) {
		this.value = valueIn;
	}

	public static CvxConceptType getBy(String value) {
		return codeMap.get(value);
	}

}
