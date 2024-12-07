package com.lorrdi.iqtest.data.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Vacancy(
    val id: String,
    val premium: Boolean,
    val name: String,
    val area: Area,
    val salary: Salary?,
    @JsonProperty("published_at")
    val publishedAt: String,
    @JsonProperty("created_at")
    val createdAt: String,
    val url: String,
    @JsonProperty("alternate_url")
    val alternateUrl: String,
    val employer: Employer,
    val schedule: Schedule,
    val experience: Experience,
    val employment: Employment
)