/*
 *  Created by Vinay on 27-1-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.ezeetech.salonme.util

import com.ezeetech.salonme.util.DateUtil.e3
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.absoluteValue

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val compareDate = Calendar.getInstance()
    compareDate.time = this
    return today.get(Calendar.ERA) == compareDate.get(Calendar.ERA) &&
            today.get(Calendar.YEAR) == compareDate.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == compareDate.get(Calendar.DAY_OF_YEAR)
}

fun Long.isToday(): Boolean {
    val today = Calendar.getInstance()
    val compareDate = Calendar.getInstance()
    compareDate.timeInMillis = this
    return today.get(Calendar.ERA) == compareDate.get(Calendar.ERA) &&
            today.get(Calendar.YEAR) == compareDate.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == compareDate.get(Calendar.DAY_OF_YEAR)
}

fun Date.convertToTZ(): Date {
    val c = Calendar.getInstance()
    c.time = this
    return convertTimeZone(c.time)
}

fun Long.toDate(convert: Boolean): Date {
    val c = Calendar.getInstance()
    c.timeInMillis = this
    return if (convert) {
        convertTimeZone(c.time)
    } else {
        c.time
    }
}

fun Date.toCalender(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}

fun Date.setTo0000(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return c.time
}

fun Date.setTo2359(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.set(Calendar.HOUR_OF_DAY, 23)
    c.set(Calendar.MINUTE, 59)
    c.set(Calendar.SECOND, 59)
    c.set(Calendar.MILLISECOND, 0)
    return c.time
}

fun Date.getHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.getMinute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}

fun Long.toDateString(dateFormat: SimpleDateFormat): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    return try {
        dateFormat.format(cal.time)
    } catch (ignored: Exception) {
        ""
    }
}

fun Date.toDateString(dateFormat: SimpleDateFormat): String {
    return try {
        dateFormat.format(this)
    } catch (ignored: Exception) {
        ""
    }
}

fun String.convertToDate(dateFormat: SimpleDateFormat): Date? {
    return try {
        dateFormat.parse(this)
    } catch (ignored: Exception) {
        null
    }
}

fun Date.deltaInMinutes(date: Date): Int {
    val s = Calendar.getInstance()
    s.time = date
    val e = Calendar.getInstance()
    e.time = this
    val delta = e.timeInMillis - s.timeInMillis
    return (delta / (60 * 1000)).toInt()
}

fun Date.absDeltaInMinutes(date: Date): Int {
    val s = Calendar.getInstance()
    s.time = date
    val e = Calendar.getInstance()
    e.time = this
    val delta = e.timeInMillis - s.timeInMillis
    return abs(delta / (60 * 1000)).toInt()
}

fun Calendar.getYear(): Int {
    return this.get(Calendar.YEAR)
}

fun Calendar.getMonth(): Int {
    return this.get(Calendar.MONTH) + 1
}

fun Calendar.getWeekOfDay(): Int {
    this.firstDayOfWeek = Calendar.SUNDAY
    return this.get(Calendar.DAY_OF_WEEK)
}

fun Calendar.getDay(): Int {
    return this.get(Calendar.DATE)
}

fun Calendar.getHour(is24HoursFormat: Boolean): Int {
    return if (is24HoursFormat) {
        this.get(Calendar.HOUR_OF_DAY)
    } else {
        return this.get(Calendar.HOUR)
    }
}

fun Calendar.getHour(): Int {
    return this.get(Calendar.HOUR)
}

fun Calendar.getHour24Format(): Int {
    return this.get(Calendar.HOUR_OF_DAY)
}

fun Calendar.getMinute(): Int {
    return this.get(Calendar.MINUTE)
}

fun Calendar.getSecond(): Int {
    return this.get(Calendar.SECOND)
}

fun Calendar.getAMPM(): Int {
    return this.get(Calendar.AM_PM)
}

fun getTZValue(): Triple<Int, Int, Int> {
    val tz = TimeZone.getDefault().rawOffset / 1000
    val tzEW = if (tz >= 0) 1 else 0
    return Triple(tzEW, (tz.absoluteValue / 60) / 60, (tz.absoluteValue / 60) % 60)
}

fun Date.dayInMonth(): String {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH).toString()
}

fun Date.dayName(): String {
    return e3.format(this)
}

fun Date.monthStartsOn(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DATE, 1)
    return calendar.time
}

fun Date.monthEndsOn(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(
        Calendar.DATE,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    )
    return calendar.time
}

fun Date.weekStartsOn(): Date {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.time = this
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    return calendar.time
}

fun Date.weekEndsOn(): Date {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.SUNDAY
    calendar.time = this
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
    return calendar.time
}

fun Date.isInCurrentWeek(): Boolean {
    return this.isSameDay(Date().weekStartsOn()) ||
            this.isSameDay(Date().weekEndsOn()) ||
            (this.after(Date().weekStartsOn()) && this.before(Date().weekEndsOn()))
}

fun Date.isInCurrentMonth(): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = Date()
    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
}

fun Date.isSameMonth(date: Date): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = date
    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
}

fun Date.isSameDay(date: Date): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.time = this
    val cal2 = Calendar.getInstance()
    cal2.time = date
    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun Date.isInFuture(): Boolean {
    return this.after(Date())
}

fun Date.getDatesInWeek(): ArrayList<Date> {
    val map = ArrayList<Date>()
    val startDayOfWeek = Calendar.getInstance()
    startDayOfWeek.time = this.weekStartsOn()
    val endDayOfWeek = Calendar.getInstance()
    endDayOfWeek.time = this.weekEndsOn()
    do {
        map.add(startDayOfWeek.time)
        startDayOfWeek.add(Calendar.DAY_OF_YEAR, 1)
    } while (!startDayOfWeek.time.after(endDayOfWeek.time))
    map.sort()
    return map
}

fun Date.getDatesInMonth(): ArrayList<Date> {
    val map = ArrayList<Date>()
    val startDayOfMonth = Calendar.getInstance()
    startDayOfMonth.time = this.monthStartsOn()
    val endDayOfMonth = Calendar.getInstance()
    endDayOfMonth.time = this.monthEndsOn()
    do {
        map.add(startDayOfMonth.time)
        startDayOfMonth.add(Calendar.DAY_OF_YEAR, 1)
    } while (!startDayOfMonth.time.after(endDayOfMonth.time))
    map.sort()
    return map
}

private fun convertTimeZone(date: Date): Date {
    val fromTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, TimeZone.getDefault())
    val toTimeZoneOffset = getTimeZoneUTCAndDSTOffset(date, TimeZone.getTimeZone("GMT+5:30"))
    return Date(date.time + (toTimeZoneOffset - fromTimeZoneOffset))
}

private fun getTimeZoneUTCAndDSTOffset(date: Date, timeZone: TimeZone): Long {
    var timeZoneDSTOffset: Long = 0
    if (timeZone.inDaylightTime(date)) {
        timeZoneDSTOffset = timeZone.dstSavings.toLong()
    }

    return timeZone.rawOffset + timeZoneDSTOffset
}

object DateUtil {

    val y4M2d2: SimpleDateFormat
        get() = dateFormat("yyyy-MM-dd")

    val y4M2d2TH2m2s2: SimpleDateFormat
        get() = dateFormat("yyyy-MM-dd'T'HH:mm:ss")

    val h2m2a: SimpleDateFormat
        get() = dateFormat("hh:mm a")

    val H2m2: SimpleDateFormat
        get() = dateFormat("HH:mm")

    val e3: SimpleDateFormat
        get() = dateFormat("EEE")

    val y4M1d1h1m1s1: SimpleDateFormat
        get() = dateFormat("yyyy-M-d H:m:s")

    val hrTimeFormat: SimpleDateFormat
        get() = dateFormat(" hh:mm a")

    val hr24TimeFormat: SimpleDateFormat
        get() = dateFormat(" HH:mm")

    private fun dateFormat(format: String): SimpleDateFormat {
        return SimpleDateFormat(format, Locale.getDefault())
    }

    fun getTheCurrentWeekIsInTwoMonths(): Boolean {
        val startDayOfWeek = Calendar.getInstance()
        startDayOfWeek.time = Date().weekStartsOn()
        val endDayOfWeek = Calendar.getInstance()
        endDayOfWeek.time = Date().weekEndsOn()
        return startDayOfWeek.get(Calendar.MONTH) != endDayOfWeek.get(Calendar.MONTH) && !startDayOfWeek.time.isSameMonth(
            Date()
        )
    }
}
