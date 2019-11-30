package com.mz.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.matchers.JsonPathMatchers;
import com.mz.example.ApplicationBaseTest;
import com.mz.example.api.model.ProduceMessageRequest;
import com.mz.example.api.model.ProduceMessageResponse;
import com.mz.example.service.customization.MessageType;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import com.mz.example.service.customization.descriptor.GenerationType;
import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;
import com.mz.example.TestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.matchers.CompareMatcher;
import org.xmlunit.matchers.EvaluateXPathMatcher;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Arrays;

public class RestControllerTest extends ApplicationBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProduceJSONMessageWithNoCustomization() throws Exception {
        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.JSON,
                "message.json");

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        JSONAssert.assertEquals(produceMessageRequest.getMsg(), response.getSentMsg(), JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void testCustomizeStringJSONValue() throws Exception {
        String path = "address.street";

        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.JSON,
                "message.json",
                new CustomizationDescriptor<>(ValueTypeDescriptor.STRING, GenerationType.UNIQUE, path));

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        JSONAssert.assertEquals(produceMessageRequest.getMsg(), response.getSentMsg(),
                new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
                        new Customization(path, (v1, v2) -> true)));
        Assert.assertThat(response.getSentMsg(),
                JsonPathMatchers.hasJsonPath(path, TestUtil.isUUID()));
    }

    @Test
    public void testCustomizeNumericJSONValue() throws Exception {
        String path = "age";

        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.JSON,
                "message.json",
                new CustomizationDescriptor<>(ValueTypeDescriptor.NUMBER, GenerationType.UNIQUE, path));

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        JSONAssert.assertEquals(produceMessageRequest.getMsg(), response.getSentMsg(),
                new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
                        new Customization(path, (v1, v2) -> true)));
        Assert.assertThat(response.getSentMsg(),
                JsonPathMatchers.hasJsonPath(path, Matchers.isA(Long.class)));
    }

    @Test
    public void testProduceXMLMessageWithNoCustomization() throws Exception {
        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.XML,
                "message.xml");

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        Assert.assertThat(response.getSentMsg(), CompareMatcher.isSimilarTo(produceMessageRequest.getMsg())
                .ignoreWhitespace()
                .normalizeWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)));
    }

    @Test
    public void testCustomizeStringXMLValue() throws Exception {
        String path = "/person/address/street";

        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.XML,
                "message.xml",
                new CustomizationDescriptor<>(ValueTypeDescriptor.STRING, GenerationType.UNIQUE, path));

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        Assert.assertThat(response.getSentMsg(), CompareMatcher.isSimilarTo(produceMessageRequest.getMsg())
                .ignoreWhitespace()
                .normalizeWhitespace()
                .withNodeFilter(node -> node.isSameNode(findNodeUnderXpath(node.getOwnerDocument(), path)))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)));
        Assert.assertThat(response.getSentMsg(),
                EvaluateXPathMatcher.hasXPath(path, TestUtil.isUUID()));
    }

    @Test
    public void testCustomizeNumericXMLValue() throws Exception {
        String path = "/person/age";

        ProduceMessageRequest produceMessageRequest = createTestProduceMessageRequestWith(
                MessageType.XML,
                "message.xml",
                new CustomizationDescriptor<>(ValueTypeDescriptor.NUMBER, GenerationType.UNIQUE, path));

        ProduceMessageResponse response = sendProduceMessageRequest(produceMessageRequest);

        Assert.assertEquals(produceMessageRequest.getTopic(), response.getTopic());
        Assert.assertThat(response.getSentMsg(), CompareMatcher.isSimilarTo(produceMessageRequest.getMsg())
                .ignoreWhitespace()
                .normalizeWhitespace()
                .withNodeFilter(node -> node.isSameNode(findNodeUnderXpath(node.getOwnerDocument(), path)))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)));
        Assert.assertThat(response.getSentMsg(),
                EvaluateXPathMatcher.hasXPath(path, TestUtil.isLong()));
    }

    private Node findNodeUnderXpath(Document document, String xPath) {
        try {
            return (Node) XPathFactory.newInstance().newXPath().compile(xPath).evaluate(document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new AssertionError("Couldn't find xPath in xml document. XPath: "+xPath);
        }
    }

    private ProduceMessageRequest createTestProduceMessageRequestWith(MessageType msgType,
                                                                      String messageTemplate,
                                                                      CustomizationDescriptor<?> ... customizationDescriptors) {
        String topic = "my-test-topic";

        ProduceMessageRequest produceMessageRequest = new ProduceMessageRequest();
        produceMessageRequest.setTopic(topic);
        produceMessageRequest.setMsgType(msgType);
        produceMessageRequest.setMsg(TestUtil.loadMessageTemplate(msgType, messageTemplate));
        produceMessageRequest.setCustomizationDescriptors(Arrays.asList(customizationDescriptors));

        return produceMessageRequest;
    }

    private ProduceMessageResponse sendProduceMessageRequest(ProduceMessageRequest produceMessageRequest) throws Exception{
        String produceMessageRequestStr = objectMapper.writeValueAsString(produceMessageRequest);
        return RestAssured.with()
                    .port(getServerPort())
                    .contentType(ContentType.JSON.withCharset(TestUtil.utf8()))
                    .body(produceMessageRequestStr)
                .when()
                    .post(RestController.RestControllerMapping.PRODUCE_METHOD)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                    .contentType(ContentType.JSON.withCharset(TestUtil.utf8()))
                .extract().response().getBody().as(ProduceMessageResponse.class);
    }
}
