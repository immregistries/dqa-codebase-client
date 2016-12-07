package org.openimmunizationsoftware.dqa.codebase;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.junit.Test;
import org.openimmunizationsoftware.dqa.codebase.generated.Code;
import org.openimmunizationsoftware.dqa.codebase.generated.Codeset;
import org.openimmunizationsoftware.dqa.codebase.reference.CodesetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMapTester {
	LoggingUtility logutil = new LoggingUtility();
	
	private static final Logger logger = LoggerFactory
			.getLogger(CodeMapTester.class);
	
	private CodeMapBuilder builder = CodeMapBuilder.INSTANCE;
	
	@Test
	public void prodCodeTest() {
		InputStream is = Codeset.class.getResourceAsStream("/Compiled.xml");
		CodeMap cm = builder.getCodeMap(is);
		
//		String exampleXML = "";
//		InputStream stream = new ByteArrayInputStream(exampleXML.getBytes("UTF-8"));
		
		Code product = cm.getProductFor("113","PMC", "20160101");
		assertNotNull("Should have found a DECAVAC product for 20160101", product);
		assertEquals("Should be DECAVAC for 20160101", "DECAVAC", product.getValue());
		
		product = cm.getProductFor("113","PMC", "19500101");
		assertNotNull("Should have found a product for 19500101", product);
		assertEquals("Should be TENIVAC for 19500101. It is the closest", "TENIVAC", product.getValue());
		
		product = cm.getProductFor("113","PMC", "19840101");
		assertNotNull("Should have found a TENVAC product", product);
		assertEquals("Should be TENIVAC for 19840101", "TENIVAC", product.getValue());
		
		product = cm.getProductFor("113","PMC", "20030101");
		assertNotNull("Should have found a DECAVAC product for 20030101", product);
		assertEquals("Should be DECAVAC for 20030101", "DECAVAC", product.getValue());
	}
	
	@Test
	public void testFindCodeEntry() {
		InputStream is = Codeset.class.getResourceAsStream("/DQA_CM_1.0.xml");
		CodeMap cm = builder.getCodeMap(is);
		logger.info(logutil.stringify(cm));
		
		Code iv = cm.getCodeForCodeset(CodesetType.BODY_ROUTE, "IV");
		
		logger.info(logutil.stringify(iv));
		
		assertNotNull(iv);
		assertEquals(iv.getValue(),  "IV");
	}
	
	@Test
	public void wildTestGetProductCode() {
		InputStream is = Codeset.class.getResourceAsStream("/VaccineProductUseDateGaps.xml");
		CodeMap cm = builder.getCodeMap(is);
		
		Code product = cm.getProductFor("000","ZZZ", "19500101");
		assertNotNull("Should have found a product for 19500101", product);
		assertEquals("Should be TEST-PRODUCT-1 for 19500101. It is the closest", "TEST-PRODUCT-1", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "19800101");
		assertNotNull("Should have found a product for 19800101", product);
		assertEquals("Should be TEST-PRODUCT-1 for 19800101. It is the closest", "TEST-PRODUCT-1", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "20000601");
		assertNotNull("Should have found a product for 20000601", product);
		assertEquals("Should be TEST-PRODUCT-2 for 20000601", "TEST-PRODUCT-2", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "19960101");
		assertNotNull("Should have found a product for 19960101", product);
		assertEquals("Should be TEST-PRODUCT-2 for 19960101", "TEST-PRODUCT-2", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "20030101");
		assertNotNull("Should have found a product for 20030101", product);
		assertEquals("Should be TEST-PRODUCT-3 for 20030101", "TEST-PRODUCT-3", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "20160101");
		assertNotNull("Should have found a product for 20160101", product);
		assertEquals("Should be TEST-PRODUCT-4 for 20160101", "TEST-PRODUCT-4", product.getValue());
		
		product = cm.getProductFor("000","ZZZ", "20160601");
		assertNotNull("Should have found a product for 20160601", product);
		assertEquals("Should be TEST-PRODUCT-4 for 20160601", "TEST-PRODUCT-4", product.getValue());
		
	}
	
	
}
