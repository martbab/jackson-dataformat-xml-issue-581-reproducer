package org.martbab.reproducer

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateTimeUtil(
    private val timeZone: ZoneId = ZoneId.systemDefault()
) {
    fun fromTimeZoneToUtc(localDateTime: LocalDateTime) = localDateTime.atZone(timeZone)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime()

    fun fromUtcToTimeZone(localDateTime: LocalDateTime) = localDateTime.toInstant(ZoneOffset.UTC).atZone(timeZone)
        .toLocalDateTime()

}