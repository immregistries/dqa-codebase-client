package org.openimmunizationsoftware.dqa.codebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import org.junit.Test;
import org.openimmunizationsoftware.dqa.codebase.generated.Code;
import org.openimmunizationsoftware.dqa.codebase.reference.CodesetType;

public class CodeMapBuilderTester {

	CodeMapBuilder builder = CodeMapBuilder.INSTANCE;
	@Test
	public final void testGetCodeMap() throws FileNotFoundException {
		InputStream is = Object.class.getResourceAsStream("/DQA_CM_1.0.xml");
		
		
		CodeMap cm = builder.getCodeMap(is);
		assertNotNull(cm);
		
		Collection<Code> codes = cm.getCodesForTable(CodesetType.EVIDENCE_OF_IMMUNITY);
		
		assertNotNull(codes);
		
		assertEquals("There should be a bunch of codes", 32, codes.size());
		
		Code immunity = cm.getCodeForCodeset(CodesetType.EVIDENCE_OF_IMMUNITY, "38907003");
		assertEquals("Should have 38907003", "38907003", immunity.getValue());
		
	}

}
