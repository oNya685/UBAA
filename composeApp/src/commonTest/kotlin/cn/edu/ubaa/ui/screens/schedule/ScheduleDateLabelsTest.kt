package cn.edu.ubaa.ui.screens.schedule

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ScheduleDateLabelsTest {

  @Test
  fun formatWeekDateRangeReturnsLocalizedMonthDayForSameYear() {
    assertEquals("4月14日 - 4月20日", formatWeekDateRange("2026-04-14", "2026-04-20"))
  }

  @Test
  fun formatWeekDateRangeIncludesYearWhenRangeCrossesYears() {
    assertEquals(
        "2026年12月29日 - 2027年1月4日",
        formatWeekDateRange("2026-12-29", "2027-01-04"),
    )
  }

  @Test
  fun formatWeekDateRangeFallsBackToRawValuesWhenDateParsingFails() {
    assertEquals("2026/04/14 - 2026/04/20", formatWeekDateRange("2026/04/14", "2026/04/20"))
  }

  @Test
  fun formatWeekDateRangeReturnsNullWhenDateIsMissing() {
    assertNull(formatWeekDateRange("2026-04-14", null))
  }
}
