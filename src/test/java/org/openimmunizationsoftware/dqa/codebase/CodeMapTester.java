package org.openimmunizationsoftware.dqa.codebase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.junit.Test;
import org.openimmunizationsoftware.dqa.codebase.generated.Code;
import org.openimmunizationsoftware.dqa.codebase.generated.Codebase;
import org.openimmunizationsoftware.dqa.codebase.generated.Codeset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMapTester extends TestCase {
	LoggingUtility logutil = new LoggingUtility();
	
	private static final Logger logger = LoggerFactory
			.getLogger(CodeMapTester.class);
	
	@Test
	public void testFindCodeEntry() throws JAXBException, IOException {
		
		InputStream is = Codeset.class.getResourceAsStream("/DQA_CM_1.0.xml");
//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
//		String line = br.readLine();
//		while(line != null) {
//			System.out.println(line);
//			line = br.readLine();
//		}
//		
//		is = Codeset.class.getResourceAsStream("/DQA_CM_1.0.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Codebase.class);
		Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();
		Codebase hcp = (Codebase) jaxbUM.unmarshal(is);
		
		logger.info(logutil.stringify(hcp));
		
		CodeMap cm = new CodeMap(hcp);
		logger.info(logutil.stringify(cm));
		
		Code iv = cm.getCodeForCodeset(CodesetType.BODY_ROUTE, "IV");
		
		logger.info(logutil.stringify(iv));
		
		assertNotNull(iv);
		assertEquals(iv.getValue(),  "IV");
		
	}
}
