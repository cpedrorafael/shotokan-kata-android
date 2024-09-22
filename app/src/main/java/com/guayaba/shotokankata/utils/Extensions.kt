package com.guayaba.shotokankata.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toEpochMilli(): Long =
    this.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli()

fun LocalDateTime.toEpochMilli(): Long =
    this.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()