package cn.edu.ubaa.ui.screens.schedule

import cn.edu.ubaa.model.dto.Week
import kotlinx.datetime.LocalDate

internal fun Week.dateRangeLabel(): String? = formatWeekDateRange(startDate, endDate)

internal fun formatWeekDateRange(startDate: String?, endDate: String?): String? {
  val safeStart = startDate?.trim().orEmpty()
  val safeEnd = endDate?.trim().orEmpty()
  if (safeStart.isEmpty() || safeEnd.isEmpty()) return null

  val parsedStart = runCatching { LocalDate.parse(safeStart) }.getOrNull()
  val parsedEnd = runCatching { LocalDate.parse(safeEnd) }.getOrNull()

  return when {
    parsedStart != null && parsedEnd != null && parsedStart.year == parsedEnd.year ->
        "${parsedStart.monthValueText()}月${parsedStart.day}日 - ${parsedEnd.monthValueText()}月${parsedEnd.day}日"
    parsedStart != null && parsedEnd != null ->
        "${parsedStart.year}年${parsedStart.monthValueText()}月${parsedStart.day}日 - ${parsedEnd.year}年${parsedEnd.monthValueText()}月${parsedEnd.day}日"
    else -> "$safeStart - $safeEnd"
  }
}

private fun LocalDate.monthValueText(): Int = month.ordinal + 1
