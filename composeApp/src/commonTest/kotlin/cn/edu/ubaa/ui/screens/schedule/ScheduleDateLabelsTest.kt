package cn.edu.ubaa.ui.screens.schedule

import cn.edu.ubaa.model.dto.Week
import kotlin.test.Test
import kotlin.test.assertEquals

class ScheduleDateLabelsTest {

  @Test
  fun headerDayLabelsReturnsWeekdayAndMonthDayForValidStartDate() {
    assertEquals(
        listOf(
            ScheduleHeaderDayLabel("周一", "4月13日"),
            ScheduleHeaderDayLabel("周二", "4月14日"),
            ScheduleHeaderDayLabel("周三", "4月15日"),
            ScheduleHeaderDayLabel("周四", "4月16日"),
            ScheduleHeaderDayLabel("周五", "4月17日"),
            ScheduleHeaderDayLabel("周六", "4月18日"),
            ScheduleHeaderDayLabel("周日", "4月19日"),
        ),
        week(startDate = "2026-04-13").headerDayLabels(),
    )
  }

  @Test
  fun headerDayLabelsDoesNotIncludeYearWhenWeekCrossesYears() {
    assertEquals(
        listOf(
            ScheduleHeaderDayLabel("周一", "12月29日"),
            ScheduleHeaderDayLabel("周二", "12月30日"),
            ScheduleHeaderDayLabel("周三", "12月31日"),
            ScheduleHeaderDayLabel("周四", "1月1日"),
            ScheduleHeaderDayLabel("周五", "1月2日"),
            ScheduleHeaderDayLabel("周六", "1月3日"),
            ScheduleHeaderDayLabel("周日", "1月4日"),
        ),
        week(startDate = "2025-12-29").headerDayLabels(),
    )
  }

  @Test
  fun headerDayLabelsFallsBackToWeekdaysWhenDateParsingFails() {
    assertEquals(defaultScheduleHeaderDayLabels(), week(startDate = "2026/04/13").headerDayLabels())
  }
}

private fun week(startDate: String) =
    Week(
        startDate = startDate,
        endDate = "2026-04-19",
        term = "2025-2026-2",
        curWeek = false,
        serialNumber = 7,
        name = "第7周",
    )
