package org.openimmunizationsoftware.dqa.codebase;

import static org.junit.Assert.*;

import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.junit.Test;

public class CodesetTypeTester {

	@Test
	public final void testGetDescription() {
		String desc = CodesetType.ADDRESS_TYPE.getDescription();
		assertEquals("Address Type", desc);
	}

	@Test
	public final void testGetByDesc() {
		CodesetType t = CodesetType.getByDesc("Address Type");
		assertEquals(CodesetType.ADDRESS_TYPE, t);
	}

	@Test
	public final void testGetByType() {
		CodesetType t = CodesetType.getByTypeCode("ADDRESS_TYPE");
		assertEquals(CodesetType.ADDRESS_TYPE, t);
	}

}
