package org.openimmunizationsoftware.dqa.codebase;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openimmunizationsoftware.dqa.codebase.generated.Code;
import org.openimmunizationsoftware.dqa.codebase.generated.Codebase;
import org.openimmunizationsoftware.dqa.codebase.generated.Codeset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMap {

	
	private static final Logger logger = LoggerFactory.getLogger(CodeMap.class);
	
	private Map<CodesetType, Map<String, Code>> codeBaseMap;
	private Codebase base;
	
	public CodeMap(Codebase mapthis) {
		remap(mapthis);
	}
	
	public CodeMap() {
		//default constructor. 
	}
	
	public void remap(Codebase mapthis) {
		this.base = mapthis;
		logger.info("mapping!");
		this.codeBaseMap = new HashMap<CodesetType, Map<String, Code>>();
		for (Codeset s : mapthis.getCodeset()) {
			logger.info("Mapping " + s.getLabel());
			Map<String, Code> codeMap = new HashMap<String, Code>();
			
			for (Code c : s.getCode()) {
				codeMap.put(c.getValue(), c);
			}
			logger.info("        type: " + s.getType());
			CodesetType t = CodesetType.getByTypeCode(s.getType());
			logger.info("Codeset: " + t);
			this.codeBaseMap.put(t, codeMap);
		}
	}
	
	public Code getCodeForCodeset(CodesetType c, String value) {
		Code code = null;
//		1. Get the codeset
		Map<String, Code> codeSetMap = codeBaseMap.get(c);
		if (codeSetMap != null) {
//		2. get the code
			code = codeSetMap.get(value);
		}
		
		return code;
	}
	
	public Collection<Code> getCodesForTable(CodesetType c) {
		Map<String, Code> codeSetMap = codeBaseMap.get(c);
		return codeSetMap.values();
	}
	
	public List<Codeset> getCodesets() {
		return base.getCodeset();
	}
}
