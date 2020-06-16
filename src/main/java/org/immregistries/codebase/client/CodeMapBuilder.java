package org.immregistries.codebase.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.immregistries.codebase.client.generated.Codebase;
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
          "No file provided for CodeMap:  Verify that you are building a CodeMap from a file that exists.");
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

  public CodeMap getDefaultCodeMap() {
    CodeMap cm;
    String file = "Compiled.xml";
    InputStream is;
    try {
      is = getCodeMapFromSameDirAsJar(file);
      logger.warn("Using Compiled.xml from directory");
    } catch (FileNotFoundException e) {
      logger.warn("Compiled.xml not found in directory with jar.  checking classpath");
      is = getCodeMapFromClasspathResource("/" + file);
      if (is != null) {
        logger.warn("Using Compiled.xml from classpath (resources folder in jar)");
      }
    }
    
    if (is != null) {
      cm = getCodeMap(is);
    } else {
    	cm = null;
    	logger.error("Can't Find Compiled.xml file.");
    	System.exit(78);
    	//throw new IllegalArgumentException("You cannot build a CodeMap if the input stream is null.  Verify that you are building an input stream from a file that exists. ");
    }
    
    return cm;
  }

  public CodeMap getCompiledCodeMap() {
    if (preBuilt == null) {
      this.preBuilt = getDefaultCodeMap();
    }
    return preBuilt;
  }

  public InputStream getCodeMapFromClasspathResource(String resourcePath) {
    logger.warn("Getting resource [" + resourcePath + "]" );
    InputStream is = Object.class.getResourceAsStream(resourcePath);
    if (is == null) {
      is = getClass().getClassLoader().getResourceAsStream(resourcePath);
    }
    return is;
  }

  public InputStream getCodeMapFromSameDirAsJar(String resourcePath) throws FileNotFoundException {
    logger.warn("Current dir: " + new File("").getAbsolutePath());
    File f = new File(resourcePath);
    logger.warn("Looking in: " + f.getAbsolutePath() + " for file");
    return new FileInputStream(f);
  }
}
