package com.ganesh.transformer;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Main {

  public static void main(String[] args) throws Exception {

    String sourceXML = FileManager.getStringFromFile("SourceOrder.xml");

    InputSource inputSource = new InputSource(new ByteArrayInputStream(sourceXML.getBytes("utf-8")));
    Document sourceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);

    DynamicXMLTransformer xmlTransformer = new DynamicXMLTransformer();

    Map<String, String> mappings = FileManager.readPropertiesFileAsMap("mappings.properties");

    Map<String, String> completeMappings = xmlTransformer.generateCompleteXpaths(mappings, sourceDoc);

    Document targetDoc = xmlTransformer.transform(completeMappings, sourceDoc);

    DOMSource source = new DOMSource(targetDoc);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = null;

    StringWriter sw = new StringWriter();
    try {
      transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.transform(source, new StreamResult(sw));
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(sw.toString());

  }
}
