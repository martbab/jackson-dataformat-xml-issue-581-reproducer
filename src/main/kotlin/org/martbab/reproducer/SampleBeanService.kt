package org.martbab.reproducer

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.InputStream

class SampleBeanService(
    private val xmlMapper: XmlMapper
) {
    fun deserializeSampleBean(xmlString: String): SampleBean {
        return xmlMapper.readValue(xmlString, SampleBean::class.java)
    }

    fun serializeSampleBean(bean: SampleBean): String {
        return xmlMapper.writeValueAsString(bean)
    }
}