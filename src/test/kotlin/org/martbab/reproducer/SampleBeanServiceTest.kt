package org.martbab.reproducer

import org.junit.jupiter.api.Test
import java.time.ZoneId
import kotlin.test.assertEquals

const val SAMPLE_BEAN_XML = """<SampleBean>
    <StringProperty>I am a string!</StringProperty>
    <LocalDateTimeProperty>2023-03-08 15:50:45</LocalDateTimeProperty>
</SampleBean>
"""

class SampleBeanServiceTest {
    private val service = SampleBeanService(
        XmlMapperConfig(LocalDateTimeUtil(ZoneId.systemDefault())).xmlMapperFactory()
    )

    @Test
    fun `try to serialize and deserialize sample bean`() {
        val bean = service.deserializeSampleBean(SAMPLE_BEAN_XML)

        val serializedBean = service.serializeSampleBean(bean)

        assertEquals(
            SAMPLE_BEAN_XML,
            serializedBean
        )
    }
}