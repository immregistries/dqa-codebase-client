package org.immregistries.dqa.codebase.client;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import org.immregistries.dqa.codebase.client.generated.Code;
import org.immregistries.dqa.codebase.client.generated.Codeset;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.immregistries.dqa.codebase.client.reference.Ops;
import org.immregistries.dqa.codebase.client.util.LoggingUtility;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMapTester {

  LoggingUtility logutil = new LoggingUtility();
  private static final Logger logger = LoggerFactory.getLogger(CodeMapTester.class);

  private CodeMapBuilder builder = CodeMapBuilder.INSTANCE;

  private InputStream is = Codeset.class.getResourceAsStream("/Compiled.xml");
  private CodeMap cm = builder.getCodeMap(is);

  /**
   * Test with deprecated language code.
   */
  @Test
  public void testDeprecatedUnmappedCode() {
    String primaryLanguage = "Ar";
    Code c = cm.getCodeForCodeset(CodesetType.PERSON_LANGUAGE, primaryLanguage, Ops.Mapping.MAP);
    assertTrue(c != null);
    assertEquals("Should get the code for Ar, because it doesn't have a map-to value",
        primaryLanguage, c.getValue());
  }

  @Test
  public void ndcCodeTest() {
    String ndcString = "";
    ndcString = "49281-0400-05";
    Code c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should find in unit of sale bucket ", ndcString, c.getValue());

    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should find in the generic NDC bucket ", ndcString, c.getValue());

    ndcString = "asfasfjask;fj2f3";
    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNull(c);

    ndcString = "";
    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNull(c);

    ndcString = null;
    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNull(c);

    ndcString = "51285-0138-50";
    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNotNull(c);

    Code mvx = cm.getRelatedCode(c, CodesetType.VACCINATION_MANUFACTURER_CODE);
    assertNotNull(mvx);
    assertEquals("Barr Laboratories", mvx.getLabel());
  }

  @Test
  public void ndcCodeTestMapping() {
    String ndcString = "";

    ndcString = "49281-400-05";
    Code c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should be a mapped code.  not the same", "49281-0400-05",
        c.getValue());

    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should find in the generic NDC bucket ", "49281-0400-05",
        c.getValue());

    ndcString = "49281-400-10";
    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should be a mapped code.  not the same", "49281-0400-10",
        c.getValue());

    c = cm.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE, ndcString);
    assertNotNull(c);
    assertEquals(ndcString + " should find in the generic NDC bucket ", "49281-0400-10",
        c.getValue());

  }

  @Test
  public void prodCodeTest() {
    Code product = cm.getProductFor("113", "PMC", "20160101");
    assertNotNull("Should have found a DECAVAC product for 20160101", product);
    assertEquals("Should be DECAVAC for 20160101", "DECAVAC", product.getValue());

    product = cm.getProductFor("113", "PMC", "19500101");
    assertNotNull("Should have found a product for 19500101", product);
    assertEquals("Should be TENIVAC for 19500101. It is the closest", "TENIVAC",
        product.getValue());

    product = cm.getProductFor("113", "PMC", "19840101");
    assertNotNull("Should have found a TENVAC product", product);
    assertEquals("Should be TENIVAC for 19840101", "TENIVAC", product.getValue());

    product = cm.getProductFor("113", "PMC", "20030101");
    assertNotNull("Should have found a DECAVAC product for 20030101", product);
    assertEquals("Should be DECAVAC for 20030101", "DECAVAC", product.getValue());
  }

  @Test
  public void testFindCodeEntry() {
    InputStream is = Codeset.class.getResourceAsStream("/DQA_CM_1.0.xml");
    CodeMap cm = builder.getCodeMap(is);
    logger.info(logutil.stringify(cm));

    {
      Code iv = cm.getCodeForCodeset(CodesetType.BODY_ROUTE, "IV");

      logger.info(logutil.stringify(iv));

      assertNotNull(iv);
      assertEquals(iv.getValue(), "IV");
    }

    {
      Code mmr = cm.getCodeForCodeset(CodesetType.VACCINATION_CVX_CODE, "03");
      logger.info(logutil.stringify(mmr));

      assertNotNull(mmr);
      assertEquals(mmr.getLabel(), "MMR");
    }

  }

  @Test
  public void wildTestGetProductCode() {
    InputStream is = Codeset.class.getResourceAsStream("/VaccineProductUseDateGaps.xml");
    CodeMap cm = builder.getCodeMap(is);

    Code product = cm.getProductFor("000", "ZZZ", "19500101");
    assertNotNull("Should have found a product for 19500101", product);
    assertEquals("Should be TEST-PRODUCT-1 for 19500101. It is the closest", "TEST-PRODUCT-1",
        product.getValue());

    product = cm.getProductFor("000", "ZZZ", "19800101");
    assertNotNull("Should have found a product for 19800101", product);
    assertEquals("Should be TEST-PRODUCT-1 for 19800101. It is the closest", "TEST-PRODUCT-1",
        product.getValue());

    product = cm.getProductFor("000", "ZZZ", "20000601");
    assertNotNull("Should have found a product for 20000601", product);
    assertEquals("Should be TEST-PRODUCT-2 for 20000601", "TEST-PRODUCT-2", product.getValue());

    product = cm.getProductFor("000", "ZZZ", "19960101");
    assertNotNull("Should have found a product for 19960101", product);
    assertEquals("Should be TEST-PRODUCT-2 for 19960101", "TEST-PRODUCT-2", product.getValue());

    product = cm.getProductFor("000", "ZZZ", "20030101");
    assertNotNull("Should have found a product for 20030101", product);
    assertEquals("Should be TEST-PRODUCT-3 for 20030101", "TEST-PRODUCT-3", product.getValue());

    product = cm.getProductFor("000", "ZZZ", "20160101");
    assertNotNull("Should have found a product for 20160101", product);
    assertEquals("Should be TEST-PRODUCT-4 for 20160101", "TEST-PRODUCT-4", product.getValue());

    product = cm.getProductFor("000", "ZZZ", "20160601");
    assertNotNull("Should have found a product for 20160601", product);
    assertEquals("Should be TEST-PRODUCT-4 for 20160601", "TEST-PRODUCT-4", product.getValue());

  }


}
