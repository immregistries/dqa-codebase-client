package org.openimmunizationsoftware.dqa.codebase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.openimmunizationsoftware.dqa.codebase.generated.Codebase;

public enum CodeMapBuilder {
	INSTANCE;

	public CodeMap getCodeMap(InputStream is) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Codebase.class);
			Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();
			Codebase hcp = (Codebase) jaxbUM.unmarshal(is);
			return new CodeMap(hcp);
		} catch (JAXBException e) {
			throw new RuntimeException("Could not marshall the codemap", e);
		}
	}

	public CodeMap getCodeMap(String codebaseXml) {
		InputStream is = new ByteArrayInputStream(codebaseXml.getBytes());
		return getCodeMap(is);
	}
}
