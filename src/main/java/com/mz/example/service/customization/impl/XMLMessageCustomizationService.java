package com.mz.example.service.customization.impl;

import com.mz.example.service.customization.MessageType;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import com.mz.example.service.customization.exception.MessageCustomizationException;
import com.mz.example.service.customization.MessageCustomizationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XMLMessageCustomizationService implements MessageCustomizationService {

    @Override
    public String customizeMessage(String input, List<CustomizationDescriptor<?>> descriptors) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(input)));

            descriptors.forEach(desc -> customizeMessageInternal(document, desc));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = prettyPrintConfiguration(tf.newTransformer());
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
            throw produceCustomizationError(ex);
        }
    }
    
    private Transformer prettyPrintConfiguration(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        return transformer;
    }

    private void customizeMessageInternal(Document document, CustomizationDescriptor desc) {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().compile(desc.getValuePath()).evaluate(document, XPathConstants.NODE);
            node.setTextContent(String.valueOf(desc.generateValue()));
        } catch (XPathExpressionException ex) {
            throw produceCustomizationError(ex);
        }
    }

    private MessageCustomizationException produceCustomizationError(Throwable cause){
        return new MessageCustomizationException("Error when modifying xml message content", cause);
    }

    @Override
    public Set<MessageType> getSupportedMessageTypes() {
        return Collections.singleton(MessageType.XML);
    }
}
