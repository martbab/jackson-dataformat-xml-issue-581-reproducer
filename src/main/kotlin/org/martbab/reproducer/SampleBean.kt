package org.martbab.reproducer

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.LocalDateTime

@JacksonXmlRootElement(localName = "SampleBean")
data class SampleBean(
    @JacksonXmlProperty(localName = "StringProperty")
    val stringProperty: String,

    @JacksonXmlProperty(localName = "LocalDateTimeProperty")
    val localDateTimeProperty: LocalDateTime
)