package org.immregistries.dqa.codebase.client.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.immregistries.dqa.codebase.client.CodeMap;
import org.immregistries.dqa.codebase.client.CodeMapBuilder;
import org.immregistries.dqa.codebase.client.RelatedCode;
import org.immregistries.dqa.codebase.client.generated.Code;
import org.immregistries.dqa.codebase.client.generated.LinkTo;
import org.junit.Test;

public class RelatedCodesTester {

  private CodeMap codeMapper = CodeMapBuilder.INSTANCE.getDefaultCodeMap();
  private RelatedCode rc = new RelatedCode(codeMapper);

  @Test
  public void testGettingRelatedCodes() {
    String ndcCode = "00006-4047-20";
    Code code = codeMapper
        .getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcCode);
    assertNotNull("You should find the NDC Code!!!", code);

    Map<CodesetType, List<Code>> relatedCodes = codeMapper
        .getRelatedCodes(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcCode);

    assertNotNull(relatedCodes);
    assertEquals("should have... three", 3, relatedCodes.size());

    List<LinkTo> linkList = code.getReference().getLinkTo();
    String cvx = "";
    for (LinkTo link : linkList) {
      if (CodesetType.VACCINATION_CVX_CODE.equals(CodesetType.getByTypeCode(link.getCodeset()))) {
        cvx = link.getValue();
        break;
      }
    }
    assertEquals("Should have a CVX Represented", "116", cvx);

  }

  @Test
  public void relatedValueTestNdc() {
    String ndcCode = "00006-4047-20";
    Code code = codeMapper
        .getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE_UNIT_OF_SALE, ndcCode);
    assertNotNull("You should find the NDC Code!!!", code);

    String cvx = codeMapper.getRelatedValue(code, CodesetType.VACCINATION_CVX_CODE);
    assertNotNull("CVX Should have a value", cvx);

    assertEquals("should be 116", "116", cvx);
  }

  @Test
  public void getRelatedValue() {
    String ndcCode = "xxxxx-4047-20";
    String cvx = rc.getCvxValueFromNdcString(ndcCode);
    assertEquals("Should be empty", "", cvx);

    ndcCode = "00006-4047-20";
    cvx = rc.getCvxValueFromNdcString(ndcCode);
    assertEquals("should STILL be 116", "116", cvx);

    List<String> vaccineGroup = rc.getVaccineGroupLabelsFromCvx(cvx);
    assertEquals("Should only have one vaccine group for this CVX", 1, vaccineGroup.size());
    assertEquals("should get a proper vaccine group", "Rotavirus", vaccineGroup.get(0));

    cvx = "146";
    vaccineGroup = rc.getVaccineGroupLabelsFromCvx(cvx);
    assertEquals(cvx + "Should have a bunch of vaccine groups for this CVX", 4,
        vaccineGroup.size());
		/*
		 <link-to codeset="VACCINE_GROUP">DTAP</link-to>
     <link-to codeset="VACCINE_GROUP">HepB</link-to>
     <link-to codeset="VACCINE_GROUP">HIB</link-to>
     <link-to codeset="VACCINE_GROUP">POLIO</link-to>
		 */
    String[] groups = {"DTaP", "Hep B", "Hib", "Polio"};
    List<String> expectedList = Arrays.asList(groups);
    assertEquals(cvx + " - should get a proper vaccine group", expectedList, vaccineGroup);

  }

}
