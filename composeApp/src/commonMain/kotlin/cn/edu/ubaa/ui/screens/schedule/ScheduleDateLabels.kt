package cn.edu.ubaa.ui.screens.schedule

import cn.edu.ubaa.model.dto.Week
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal data class ScheduleHeaderDayLabel(
    val weekdayLabel: String,
    val dateLabel: String? = null,
)

internal fun defaultScheduleHeaderDayLabels(): List<ScheduleHeaderDayLabel> =
    listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日").map {
      ScheduleHeaderDayLabel(weekdayLabel = it)
    }

internal fun Week.headerDayLabels(): List<ScheduleHeaderDayLabel> {
  val parsedStart = runCatching { LocalDate.parse(startDate.trim()) }.getOrNull()
  val defaults = defaultScheduleHeaderDayLabels()
  if (parsedStart == null) return defaults

  return defaults.mapIndexed { index, default ->
    ScheduleHeaderDayLabel(
        weekdayLabel = default.weekdayLabel,
        dateLabel = parsedStart.plus(DatePeriod(days = index)).toMonthDayLabel(),
    )
  }
}

private fun LocalDate.toMonthDayLabel(): String = "${month.ordinal + 1}月${day}日"
