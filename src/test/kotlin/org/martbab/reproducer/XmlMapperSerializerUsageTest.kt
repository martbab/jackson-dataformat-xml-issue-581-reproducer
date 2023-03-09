package org.martbab.reproducer

import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class XmlMapperSerializerUsageTest {
    private val localDateTimeUtil = mockk<LocalDateTimeUtil>()

    private val xmlMapper = XmlMapperConfig(localDateTimeUtil).xmlMapperFactory()
    private val beanService = SampleBeanService(xmlMapper)

    @BeforeEach
    fun setUp() {
        every { localDateTimeUtil.fromTimeZoneToUtc(any()) } returns LocalDateTime.now()
        every { localDateTimeUtil.fromUtcToTimeZone(any()) } returns LocalDateTime.now()
    }

    /**
     * this test passes, the custom LocalDateTime deserializer is called as indicated by called mock util
     */
    @Test
    fun `given xml mapper with overriden LocalDateTime deserializer, the util is called`() {
        beanService.deserializeSampleBean(SAMPLE_BEAN_XML)
        verify { localDateTimeUtil.fromTimeZoneToUtc(any()) }
        verify { localDateTimeUtil.fromUtcToTimeZone(any()) wasNot called }
    }

    /**
     * this test fails, the custom LocalDateTime serializer is *not* called, the default JSR310 one is called instead
     */
    @Test
    fun `given xml mapper with overriden LocalDateTime serializer, the util is called`() {
        beanService.serializeSampleBean(SampleBean("I am a string!", LocalDateTime.now()))
        verify { localDateTimeUtil.fromTimeZoneToUtc(any()) wasNot called }
        verify { localDateTimeUtil.fromUtcToTimeZone(any()) }
    }
}