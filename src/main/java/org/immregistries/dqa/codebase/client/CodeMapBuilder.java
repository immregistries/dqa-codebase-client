package org.immregistries.dqa.codebase.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.immregistries.dqa.codebase.client.generated.Codebase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CodeMapBuilder {
  INSTANCE;

  private CodeMap preBuilt;

  private static final Logger logger = LoggerFactory
      .getLogger(CodeMapBuilder.class);

  public CodeMap getCodeMap(InputStream inputStream) {
    logger.trace("input stream: " + inputStream);
    if (inputStream == null) {
      throw new IllegalArgumentException(
          "You cannot build a CodeMap if the input stream is null.  Verify that you are building an input stream from a file that exists. ");
    }

    JAXBContext jaxbContext;
    try {

      jaxbContext = JAXBContext.newInstance(Codebase.class);
      Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();
      Codebase hcp = (Codebase) jaxbUM.unmarshal(inputStream);
      CodeMap cm = new CodeMap(hcp);
      this.preBuilt = cm;
      return cm;
    } catch (JAXBException e) {
      throw new RuntimeException("Could not marshall the codemap", e);
    }
  }

  public CodeMap getCodeMap(String codebaseXml) {
    InputStream is = new ByteArrayInputStream(codebaseXml.getBytes());
    return getCodeMap(is);
  }

  public CodeMap getCompiledCodeMap() {
    if (preBuilt == null) {
      preBuilt = getCodeMapFromClasspathResource("/Compiled.xml");
    }
    return preBuilt;
  }

  public CodeMap getCodeMapFromClasspathResource(String resourcePath) {
    InputStream is = Object.class.getResourceAsStream(resourcePath);
    return getCodeMap(is);
  }
}
