package com.lorrdi.iqtest.presentation.ui.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region
import com.lorrdi.iqtest.data.paging.VacancyPagingSource.Companion.DEFAULT_AREA
import com.lorrdi.iqtest.domain.entities.FilterSelections
import com.lorrdi.iqtest.presentation.ui.components.CheckboxList

@Composable
fun FilterBottomSheetContent(
    filters: Filters,
    availableFilters: Filters?,
    availableRegions: List<Region>,
    onClose: () -> Unit,
    onApplyFilters: (Filters) -> Unit
) {
    val scrollState = rememberScrollState()

    val filterSelections = rememberSelectedFilters(filters, availableRegions)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        FilterSection(
            label = "Опыт работы",
            selectedItems = filterSelections.selectedExperience,
            availableItems = availableFilters?.experience?.map { it.name } ?: emptyList(),
            onItemSelectionChanged = { id, isChecked ->
                updateSelectionList(filterSelections.selectedExperience, id, isChecked)
            }
        )

        FilterSection(
            label = "Тип занятости",
            selectedItems = filterSelections.selectedEmployment,
            availableItems = availableFilters?.employment?.map { it.name } ?: emptyList(),
            onItemSelectionChanged = { id, isChecked ->
                updateSelectionList(filterSelections.selectedEmployment, id, isChecked)
            }
        )

        FilterSection(
            label = "График работы",
            selectedItems = filterSelections.selectedSchedule,
            availableItems = availableFilters?.schedule?.map { it.name } ?: emptyList(),
            onItemSelectionChanged = { id, isChecked ->
                updateSelectionList(filterSelections.selectedSchedule, id, isChecked)
            }
        )

        FilterSection(
            label = "Регион",
            selectedItems = filterSelections.selectedRegionMap.keys.toList(),
            availableItems = availableRegions.take(20).map { it.name },
            onItemSelectionChanged = { name, isChecked ->
                val regionId = availableRegions.firstOrNull { it.name == name }?.id
                if (regionId != null) {
                    if (isChecked) {
                        filterSelections.selectedRegionMap[name] = regionId
                    } else {
                        filterSelections.selectedRegionMap.remove(name)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onClose) {
                Text("Отмена")
            }

            Button(onClick = { resetFilters(filterSelections) }) {
                Text("Сбросить")
            }

            Button(onClick = {
                onApplyFilters(
                    filters.copy(
                        experience = availableFilters?.experience?.filter {
                            filterSelections.selectedExperience.contains(it.name)
                        },
                        employment = availableFilters?.employment?.filter {
                            filterSelections.selectedEmployment.contains(it.name)
                        },
                        schedule = availableFilters?.schedule?.filter {
                            filterSelections.selectedSchedule.contains(it.name)
                        },
                        area = if (filterSelections.selectedRegionMap.isEmpty()) {
                            listOf(DEFAULT_AREA)
                        } else {
                            filterSelections.selectedRegionMap.values.toList()
                        }
                    )
                )
                onClose()
            }) {
                Text("Применить")
            }
        }
    }
}

@Composable
fun FilterSection(
    label: String,
    selectedItems: List<String>,
    availableItems: List<String>,
    onItemSelectionChanged: (String, Boolean) -> Unit
) {
    CheckboxList(
        label = label,
        selectedItems = selectedItems,
        items = availableItems,
        onItemSelectionChanged = onItemSelectionChanged
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun rememberSelectedFilters(
    filters: Filters,
    availableRegions: List<Region>
): FilterSelections {
    val selectedExperience = remember {
        mutableStateListOf<String>().apply {
            addAll(filters.experience?.map { it.name } ?: emptyList())
        }
    }
    val selectedEmployment = remember {
        mutableStateListOf<String>().apply {
            addAll(filters.employment?.map { it.name } ?: emptyList())
        }
    }
    val selectedSchedule = remember {
        mutableStateListOf<String>().apply {
            addAll(filters.schedule?.map { it.name } ?: emptyList())
        }
    }
    val selectedRegionMap = remember {
        mutableStateMapOf<String, String>().apply {
            filters.area?.forEach { regionId ->
                availableRegions.firstOrNull { it.id == regionId }?.name?.let { regionName ->
                    this[regionName] = regionId
                }
            }
        }
    }
    return FilterSelections(
        selectedExperience = selectedExperience,
        selectedEmployment = selectedEmployment,
        selectedSchedule = selectedSchedule,
        selectedRegionMap = selectedRegionMap
    )
}

fun resetFilters(filterSelections: FilterSelections) {
    filterSelections.selectedExperience.clear()
    filterSelections.selectedEmployment.clear()
    filterSelections.selectedSchedule.clear()
    filterSelections.selectedRegionMap.clear()
}

fun updateSelectionList(selectionList: MutableList<String>, item: String, isChecked: Boolean) {
    if (isChecked) selectionList.add(item) else selectionList.remove(item)
}