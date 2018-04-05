package org.immregistries.dqa.codebase.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import org.immregistries.dqa.codebase.client.generated.Code;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.junit.Test;

public class CodeMapBuilderTester {

  CodeMapBuilder builder = CodeMapBuilder.INSTANCE;

  @Test
  public final void testGetCodeMap() throws FileNotFoundException {
    InputStream is = Object.class.getResourceAsStream("/Compiled.xml");

    CodeMap cm = builder.getCodeMap(is);
    assertNotNull(cm);

    Collection<Code> codes = cm.getCodesForTable(CodesetType.EVIDENCE_OF_IMMUNITY);

    assertNotNull(codes);

    assertEquals("There should be a bunch of codes", 32, codes.size());

    Code immunity = cm.getCodeForCodeset(CodesetType.EVIDENCE_OF_IMMUNITY, "38907003");
    assertEquals("Should have 38907003", "38907003", immunity.getValue());

  }

  public void verifyCodeMap(CodeMap cm) {
    assertNotNull(cm);

    Collection<Code> codes = cm.getCodesForTable(CodesetType.EVIDENCE_OF_IMMUNITY);

    assertNotNull(codes);

    assertEquals("There should be a bunch of codes", 32, codes.size());

    Code immunity = cm.getCodeForCodeset(CodesetType.EVIDENCE_OF_IMMUNITY, "38907003");
    assertEquals("Should have 38907003", "38907003", immunity.getValue());
  }


  @Test
  public final void testGetCodeMapFromDir() throws FileNotFoundException {
    InputStream is = builder.getCodeMapFromSameDirAsJar("Compiled-test.xml");
    CodeMap cm = builder.getCodeMap(is);
    verifyCodeMap(cm);
  }


  @Test
  public final void testGetDefaultCodeMap()  {
    CodeMap cm = builder.getCompiledCodeMap();
    verifyCodeMap(cm);
  }

}
