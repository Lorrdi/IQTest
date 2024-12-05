package com.lorrdi.iqtest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.lorrdi.iqtest.app.viewmodel.VacanciesViewModel
import com.lorrdi.iqtest.data.models.Vacancy

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VacanciesScreen(
    viewModel: VacanciesViewModel = hiltViewModel(),
    contentPadding: PaddingValues
) {
    val vacancies = viewModel.pagedVacancies.collectAsLazyPagingItems()

    val refreshing = vacancies.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        vacancies.refresh()
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {
        LazyColumn(
            contentPadding = contentPadding
        ) {
            items(vacancies.itemCount) { index ->
                vacancies[index]?.let { vacancy ->
                    VacancyItem(vacancy = vacancy)
                }
            }

            vacancies.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .align(Alignment.Center) // Центрирование по горизонтали
                            )
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        item {
                            Text(
                                text = "Error: ${(loadState.refresh as LoadState.Error).error.message}",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
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

@Composable
fun VacancyItem(vacancy: Vacancy) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(5.dp, shape = RoundedCornerShape(16.dp))
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vacancy.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                vacancy.employer.logoUrls?.let { logoUrls ->
                    AsyncImage(
                        model = logoUrls.n240,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Employer Logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = vacancy.employer.name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = vacancy.salary?.let { "${it.from}-${it.to ?: "N/A"} ${it.currency}" }
                    ?: "Salary not specified",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            vacancy.area.name.let {
                Text(
                    text = "Location: $it",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "Published on: ${vacancy.publishedAt}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

