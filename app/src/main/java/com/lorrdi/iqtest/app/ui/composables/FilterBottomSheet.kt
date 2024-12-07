package com.lorrdi.iqtest.app.ui.composables

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lorrdi.iqtest.data.models.FiltersResponse
import com.lorrdi.iqtest.data.models.Region

@Composable
fun FilterBottomSheet(
    filters: FiltersResponse,
    availableFilters: FiltersResponse?,
    availableRegions: List<Region>,
    onClose: () -> Unit,
    onApplyFilters: (FiltersResponse) -> Unit
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
            selectedItems = selectedRegionMap.keys.toList(), // Pass region names (keys)
            items = availableRegions.take(20).map { it.name }, // Map to region names
            onItemSelectionChanged = { name, isChecked ->
                val regionId = availableRegions.firstOrNull { it.name == name }?.id
                if (regionId != null) {
                    if (isChecked) {
                        selectedRegionMap[name] = regionId // Add name and ID
                    } else {
                        selectedRegionMap.remove(name) // Remove name
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
                        area = selectedRegionMap.values.toList()
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
fun CheckboxList(
    label: String,
    selectedItems: List<String>,
    items: List<String>,
    onItemSelectionChanged: (String, Boolean) -> Unit
) {
    Text(label)
    Column {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedItems.contains(item),
                    onCheckedChange = { isChecked ->
                        onItemSelectionChanged(item, isChecked)
                    }
                )
                Text(item)
            }
        }
    }
}
