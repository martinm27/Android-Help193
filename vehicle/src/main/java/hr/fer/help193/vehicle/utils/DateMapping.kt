package hr.fer.help193.vehicle.utils

import org.joda.time.DateTime
import java.text.DecimalFormat
import java.util.*

private val df = DecimalFormat("00")

fun toFormattedDate(date: DateTime): String =
        with(date) {
            dayOfMonth().getAsShortText(Locale.getDefault()).plus(".").plus(df.format(monthOfYear().get()).plus("."))
        }

fun toFormattedTime(date: DateTime): String =
        with(date) {
            hourOfDay().getAsShortText(Locale.getDefault()).plus(":").plus(df.format(minuteOfHour().get()))
        }
