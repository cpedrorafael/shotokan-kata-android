package com.guayaba.shotokankata.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toLong(): Long =
    this.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli()