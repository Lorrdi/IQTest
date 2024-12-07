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
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        val selectedExperience = remember { mutableStateListOf<String>() }
        val selectedEmployment = remember { mutableStateListOf<String>() }
        val selectedSchedule = remember { mutableStateListOf<String>() }
        val selectedRegionMap =
            remember { mutableStateMapOf<String, String>() }

        CheckboxList(
            label = "Опыт работы",
            selectedItems = selectedExperience,
            items = availableFilters?.experience?.map { it.name } ?: listOf(),
            onItemSelectionChanged = { id, isChecked ->
                if (isChecked) selectedExperience.add(id) else selectedExperience.remove(id)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CheckboxList(
            label = "Тип занятости",
            selectedItems = selectedEmployment,
            items = availableFilters?.employment?.map { it.name } ?: listOf(),
            onItemSelectionChanged = { id, isChecked ->
                if (isChecked) selectedEmployment.add(id) else selectedEmployment.remove(id)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CheckboxList(
            label = "График работы",
            selectedItems = selectedSchedule,
            items = availableFilters?.schedule?.map { it.name } ?: listOf(),
            onItemSelectionChanged = { id, isChecked ->
                if (isChecked) selectedSchedule.add(id) else selectedSchedule.remove(id)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CheckboxList(
            label = "Регион",
            selectedItems = selectedRegionMap.keys.toList(),
            items = availableRegions.take(20).map { it.name },
            onItemSelectionChanged = { name, isChecked ->
                val regionId = availableRegions.firstOrNull { it.name == name }?.id
                if (regionId != null) {
                    if (isChecked) {
                        selectedRegionMap[name] = regionId
                    } else {
                        selectedRegionMap.remove(name)
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
            Button(onClick = {
                onApplyFilters(
                    filters.copy(
                        experience = availableFilters?.experience?.filter {
                            selectedExperience.contains(it.name)
                        },
                        employment = availableFilters?.employment?.filter {
                            selectedEmployment.contains(it.name)
                        },
                        schedule = availableFilters?.schedule?.filter { selectedSchedule.contains(it.name) },
                        area = if (selectedRegionMap.isEmpty()) listOf(DEFAULT_AREA) else selectedRegionMap.values.toList()
                    )
                )
                onClose()
            }) {
                Text("Применить")
            }
        }
    }
}
