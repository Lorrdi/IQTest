package com.lorrdi.iqtest.data.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class VacanciesResponse @JsonCreator constructor(
    val items: List<Vacancy>,
    val found: Long,
    val pages: Long,
    val page: Long,
    @JsonProperty("per_page")
    val perPage: Long,
    val clusters: Any?,
    val arguments: Any?,
    val fixes: Any?,
    val suggests: Any?,
    @JsonProperty("alternate_url")
    val alternateUrl: String,
)