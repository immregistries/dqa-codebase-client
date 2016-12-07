package org.openimmunizationsoftware.dqa.codebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openimmunizationsoftware.dqa.codebase.generated.Code;
import org.openimmunizationsoftware.dqa.codebase.generated.Codebase;
import org.openimmunizationsoftware.dqa.codebase.generated.Codeset;
import org.openimmunizationsoftware.dqa.codebase.generated.LinkTo;
import org.openimmunizationsoftware.dqa.codebase.generated.Reference;
import org.openimmunizationsoftware.dqa.codebase.generated.UseDate;
import org.openimmunizationsoftware.dqa.codebase.reference.CodesetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMap {

	
	private static final Logger logger = LoggerFactory.getLogger(CodeMap.class);
	
	private Map<CodesetType, Map<String, Code>> codeBaseMap;
	private Map<String, List<Code>> productMap = new HashMap<String, List<Code>>();
	private static final String MAP_DELIMITER = "::";
	private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd");
	
	public List<Code> getProductsFor(String vaccineCvx, String vaccineMvx) {
		if (StringUtils.isBlank(vaccineCvx) || StringUtils.isBlank(vaccineMvx)) {
			return new ArrayList<Code>();
		}
		List<Code> productCodes = productMap.get(vaccineCvx + MAP_DELIMITER + vaccineMvx);
		if (productCodes == null) {
			return new ArrayList<Code>();
		}
		return productCodes;
	}
	
	public Code getProductFor(String vaccineCvx, String vaccineMvx, String adminDate) {
		List<Code> productCodes = getProductsFor(vaccineCvx, vaccineMvx);
		
		if (productCodes.size() == 1) {
			return productCodes.get(0);
		} else if (productCodes.size() == 0) {
			return null;
		}
		
		//at this point we know we have more than one product in our list.  time to get the hands dirty.
		logger.debug("There is more than one product for cvx[" + vaccineCvx + "] + mvx[" + vaccineMvx + "]");
		//We need to pick the most likely one.  

		if (adminDate != null) {
			logger.debug("Searching for product that is valid for admin date[" + adminDate + "]");
			
			DateTime adminDT = parseDateTime(adminDate);
			
			//At this point, we know there's two or more products. 
			for (Code productCode : productCodes) {
				//Now we need to chose the right one based on the dates.
				UseDate dates = productCode.getUseDate();
				
				String beginning = dates.getNotBefore();
				
				//Parse the dates...
				DateTime bDT = parseDateTime(beginning);

				logger.debug("Must be after getNotBefore["+beginning+"] ");
				
				boolean isAfterBeginning = !isBeforeThis(adminDT, bDT);
				
				logger.debug("isAfterBeginning ? " + isAfterBeginning);

				//We only care about the beginning date. 
				//This is very intentional, and it allows us to be flexible and return products where 
				//the admin date isn't exactly within the range, but its the closest one. 
				//This properly deals with gaps, and with overlapping ranges. 
				if (isAfterBeginning) {
					return productCode;
				}
			}
			
			//If we get here, there are multiple and the admin date is before ALL of the beginning dates
			//so just return the last one in the list. 
			return productCodes.get(productCodes.size()-1);
		}
		
		return null;
	}
	
	protected DateTime parseDateTime(String inDate) {
		if (inDate != null) {
			try {
				return dtf.parseDateTime(inDate);
			} catch (IllegalArgumentException iae) {
				return null;
			}
		}
		return null;
	}
	
	protected boolean isAfterThis(DateTime isThis, DateTime afterThis) {
		if (isThis != null) {
			boolean isAfterThis = (afterThis != null && isThis.isAfter(afterThis));
			return isAfterThis;
		}
		return false;
	}
	
	protected boolean isBeforeThis(DateTime isThis, DateTime beforeThis) {
		if (isThis != null) {
			boolean isBefore = (beforeThis != null && isThis.isBefore(beforeThis));
			return isBefore;
		}
		return false;
	}

	
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
			
			if (CodesetType.VACCINE_PRODUCT == t) {
				for (Code productCode : s.getCode()) {
					Reference ref = productCode.getReference();
					if (ref != null) {
						List<LinkTo> links = ref.getLinkTo();
						String productCvx = null;
						String productMvx = null;
						if (links != null) {
							for (LinkTo link : links) {
								String codeset = link.getCodeset();
								switch (CodesetType.getByTypeCode(codeset)) {
								case VACCINATION_CVX_CODE: 
									productCvx = link.getValue();
									break;
								case VACCINATION_MANUFACTURER_CODE:
									productMvx = link.getValue();
									break;
								default: 
									break;
								}
							}
						}
						if (productCvx != null && productMvx != null) {
							List<Code> products = productMap.get(productCvx + MAP_DELIMITER + productMvx);
							if (products == null) {
								products = new ArrayList<Code>();
								productMap.put(productCvx+MAP_DELIMITER+productMvx, products);
							}
							
							products.add(productCode);
							
							if (products.size() > 1) {
								logger.debug("SORTING PRODUCT: " + productCode.getValue());
								Collections.sort(products, new CustomCodeDateComparator());
							}
						}
					}
				}	
			}
		}
	}
	
	/**
	 * If the products both have a date, they will sort by that date.  if either one doesn't have a date, they 
	 * will be considered equal, and sort in the order they come in. 
	 * @author Josh
	 *
	 */
	public class CustomCodeDateComparator implements Comparator<Code> {
	    public int compare(Code o1, Code o2) {
	    	UseDate dates = o1.getUseDate();
	    	UseDate dates2 = o2.getUseDate();
	    	
	    	if (dates != null && dates2 != null) {
	    		String date1 = dates.getNotBefore();
	    		if (date1 == null) {
	    			date1 = dates.getNotAfter();
	    		}
	    		
				String date2 = dates2.getNotBefore();
				
				if (date2 == null) {
					date2 = dates2.getNotAfter();
				}
				
				if (date2 != null && date1 != null) {
					return date2.compareTo(date1);//Want a descending sort. 
				}
	    	}
    		return 0;
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
	
	/**
	 * This is a shorcut method.  It gives access to the related codes
	 * without first getting a Code object. 
	 * @param ct
	 * @param value
	 * @return
	 */
	public Map<CodesetType, List<Code>> getRelatedCodes(CodesetType ct, String value) {
		Code code = this.getCodeForCodeset(ct,  value);
		Map<CodesetType, List<Code>> links = getRelatedCodes(code);
		return links;
	}
	
	/**
	 * Gets the codes defined as related to the code sent in. 
	 * This method assumes singly related codes.  if there are multiple, you will
	 * end up with the last one put into the map. 
	 * @param c 
	 * @return Map of Types, and the associated codes. 
	 */
	public  Map<CodesetType, List<Code>> getRelatedCodes(Code c) {
		Map<CodesetType, List<Code>> relatedCodes = new HashMap<CodesetType, List<Code>>();
		List<LinkTo> links = c.getReference().getLinkTo();
		
		for (LinkTo link : links) {
			if (link != null) {
				String codesetType = link.getCodeset();
				CodesetType type = CodesetType.getByTypeCode(codesetType);
				List<Code> codeList = relatedCodes.get(type);
				if (codeList == null) {
					codeList = new ArrayList<Code>();
					relatedCodes.put(type,  codeList);
				}
				Code relatedCode = this.getCodeForCodeset(type, link.getValue());
				codeList.add(relatedCode);
			}
		}
		
		return relatedCodes;
	}
	
	public Collection<Code> getCodesForTable(CodesetType c) {
		Map<String, Code> codeSetMap = codeBaseMap.get(c);
		return codeSetMap.values();
	}
	
	public List<Codeset> getCodesets() {
		return base.getCodeset();
	}
	
	/**
	 * A helper method to compare two codes.  The Code object is a generated class, so anything put there
	 * would get erased the next time its generated. 
	 * @param code1
	 * @param code2
	 * @return
	 */
	public boolean codeEquals(Code code1, Code code2) {
		if (code1.getConceptType() == null) {
			if (code2.getConceptType() != null) {
				return false;
			}
		} else if (!code1.getConceptType().equals(code2.getConceptType())) {
			return false;
		}

		if (code1.getValue() == null) {
			if (code2.getValue() != null) {
				return false;
			}
		} else if (!code1.getValue().equals(code2.getValue())) {
			return false;
		}

		return true;
	}
}
