package com.lorrdi.iqtest.presentation.util

fun formatSalary(from: Long?, to: Long?, currency: String): String {
    return buildString {
        if (from != null) {
            append(
                if (from > 1000)
                    "от ${from / 1000} тыс."
                else
                    "от $from"
            )
        }
        if (to != null) {
            if (from != null) append(" до ")
            append(
                if (to > 1000)
                    "${to / 1000} тыс."
                else
                    "$to"
            )
        }
        if (currency.isNotBlank()) {
            append(" $currency")
        }
        append(" на руки")
    }
}