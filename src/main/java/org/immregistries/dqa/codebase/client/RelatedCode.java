package org.immregistries.dqa.codebase.client;

import org.immregistries.dqa.codebase.client.generated.Code;
import org.immregistries.dqa.codebase.client.reference.CodesetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RelatedCode {

    private static final Logger logger = LoggerFactory.getLogger(RelatedCode.class);

    private final CodeMap map;

    public RelatedCode(CodeMap cm) {
        this.map = cm;
    }

    public List<String> getVaccineGroupLabelsFromCvx(String cvx) {
        List<String> grouplabels = new ArrayList<>();
        List<Code> cvxVaccineGroups = this.map.getRelatedCodesForCodeIn(
            CodesetType.VACCINATION_CVX_CODE, cvx,
            CodesetType.VACCINE_GROUP);
        if (cvxVaccineGroups != null) {
            for (Code c : cvxVaccineGroups) {
                grouplabels.add(c.getLabel());
            }
        }
        return grouplabels;
    }

    public String getCvxValueFromNdcString(String ndcStringIn) {
        String cvxValue = "";
        Code ndc = this.map.getCodeForCodeset(CodesetType.VACCINATION_NDC_CODE, ndcStringIn);
        cvxValue = this.map.getRelatedValue(ndc,  CodesetType.VACCINATION_CVX_CODE);
        return cvxValue;
    }

    public String getCvxFromCptString(String cptIn) {
        Code cpt = this.map.getCodeForCodeset(CodesetType.VACCINATION_CPT_CODE, cptIn);
        String cvxValue = this.map.getRelatedValue(cpt,  CodesetType.VACCINATION_CVX_CODE);
        return cvxValue;
    }
}
