package com.lorrdi.iqtest.domain.entities

data class FilterSelections(
    val selectedExperience: MutableList<String>,
    val selectedEmployment: MutableList<String>,
    val selectedSchedule: MutableList<String>,
    val selectedRegionMap: MutableMap<String, String>
)
