package com.lorrdi.iqtest.presentation.ui.components.bottomsheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.lorrdi.iqtest.data.dto.Filters
import com.lorrdi.iqtest.data.dto.Region

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filters: Filters,
    availableFilters:  Filters,
    availableRegions: List<Region>,
    onApplyFilters: (Filters) -> Unit,
    onClose: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onClose) {
        FilterBottomSheetContent(
            filters = filters,
            availableFilters = availableFilters,
            availableRegions = availableRegions,
            onApplyFilters = onApplyFilters,
            onClose = onClose
        )
    }
}
