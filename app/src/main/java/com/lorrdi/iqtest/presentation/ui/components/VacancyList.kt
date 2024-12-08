package com.lorrdi.iqtest.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.lorrdi.iqtest.data.dto.Vacancy

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VacancyList(
    vacancies: LazyPagingItems<Vacancy>,
    contentPadding: PaddingValues,
    refreshState: PullRefreshState,
    refreshing: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .pullRefresh(refreshState)
    ) {
        LazyColumn(contentPadding = contentPadding) {
            if (vacancies.itemCount == 0 && vacancies.loadState.refresh !is LoadState.Loading) {
                item {
                    Text(
                        text = "Результатов нет",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            items(vacancies.itemCount) { index ->
                vacancies[index]?.let { vacancy ->
                    VacancyItem(vacancy = vacancy)
                }
            }

            when (val appendState = vacancies.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text(
                            text = "Ошибка при подгрузке: ${appendState.error.message}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> Unit
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}