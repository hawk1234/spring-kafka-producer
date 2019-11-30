package com.mz.example.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.example.api.model.ProduceMessageRequest;
import com.mz.example.ApplicationBaseTest;
import com.mz.example.TestUtil;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import com.mz.example.service.customization.descriptor.GenerationType;
import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestSerializationTest extends ApplicationBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProduceMessageRequestDeserialization() throws Exception{
        String produceMessageRequest = TestUtil.loadRequestTemplate("produceMessageRequest.json");

        ProduceMessageRequest request = objectMapper.readValue(produceMessageRequest, ProduceMessageRequest.class);

        Assert.assertEquals(1, request.getCustomizationDescriptors().size());
        CustomizationDescriptor<?> desc = request.getCustomizationDescriptors().iterator().next();
        Assert.assertEquals(GenerationType.UNIQUE, desc.getGenerationType());
        Assert.assertEquals("$.att", desc.getValuePath());
        Assert.assertEquals(ValueTypeDescriptor.ValueType.STRING, desc.getValueType().getValueType());
        Assert.assertSame(String.class, desc.getValueType().getValueTypeClass());
        Assert.assertSame(ValueTypeDescriptor.STRING, desc.getValueType());
    }

    @Test
    public void testProduceMessageRequestSerialization() throws Exception {
        performTestProduceMessageRequestSerialization("produceMessageRequest.json");
    }

    @Test
    public void testProduceMessageRequestDeserializationEmptyRequestBody() throws Exception{
        String produceMessageRequest = "{}";

        ProduceMessageRequest request = objectMapper.readValue(produceMessageRequest, ProduceMessageRequest.class);

        Assert.assertNull(request.getMsg());
        Assert.assertNull(request.getTopic());
        Assert.assertNull(request.getMsgType());
        Assert.assertEquals(1, request.getAmount());
        Assert.assertEquals(0, request.getCustomizationDescriptors().size());
    }

    @Test
    public void testProduceMessageRequestDeserializationNullValueType() throws Exception{
        String produceMessageRequest = TestUtil.loadRequestTemplate("nullValueTypeRequest.json");

        ProduceMessageRequest request = objectMapper.readValue(produceMessageRequest, ProduceMessageRequest.class);

        Assert.assertEquals(1, request.getCustomizationDescriptors().size());
        CustomizationDescriptor<?> desc = request.getCustomizationDescriptors().iterator().next();
        Assert.assertEquals(GenerationType.UNIQUE, desc.getGenerationType());
        Assert.assertEquals("$.att", desc.getValuePath());
        Assert.assertNull(desc.getValueType());
    }

    @Test
    public void testProduceMessageRequestSerializationNullValueType() throws Exception{
        performTestProduceMessageRequestSerialization("nullValueTypeRequest.json");
    }

    private void performTestProduceMessageRequestSerialization(String requestTemplateName) throws Exception{
        String produceMessageRequest = TestUtil.loadRequestTemplate(requestTemplateName);

        ProduceMessageRequest request = objectMapper.readValue(produceMessageRequest, ProduceMessageRequest.class);
        String produceMessageRequestSerialized = objectMapper.writeValueAsString(request);

        JSONAssert.assertEquals(produceMessageRequest, produceMessageRequestSerialized, JSONCompareMode.NON_EXTENSIBLE);
    }
}
