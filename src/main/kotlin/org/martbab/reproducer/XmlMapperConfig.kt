package org.martbab.reproducer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class XmlMapperConfig(
    private val localDateTimeUtil: LocalDateTimeUtil
) {
    private val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private val localDateTimeDeserializer = object : LocalDateTimeDeserializer(
        localDateTimeFormatter
    ) {

        override fun deserialize(parser: JsonParser?, context: DeserializationContext?): LocalDateTime {
            // the values in the xml file are at $timezoneString timezone, so we convert them to UTC
            return localDateTimeUtil.fromTimeZoneToUtc(super.deserialize(parser, context))
        }
    }

    private val localDateTimeSerializer = object : LocalDateTimeSerializer(
        localDateTimeFormatter
    ) {

        override fun serialize(value: LocalDateTime?, g: JsonGenerator?, provider: SerializerProvider?) {
            super.serialize(value?.let { localDateTimeUtil.fromUtcToTimeZone(it) }, g, provider)
        }
    }

    fun xmlMapperFactory(): XmlMapper {
        return XmlMapper().apply {
            registerModule(JacksonXmlModule())
            registerModule(
                KotlinModule.Builder()
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .configure(KotlinFeature.StrictNullChecks, false)
                    .build()
            )
            registerModule(
                JavaTimeModule().apply {
                    addDeserializer(
                        LocalDateTime::class.java,
                        localDateTimeDeserializer
                    )
                    addSerializer(
                        LocalDateTime::class.java,
                        localDateTimeSerializer
                    )
                }
            )
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}