package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Employer(
    val id: String,
    val name: String,
    val url: String,
    @JsonProperty("alternate_url")
    val alternateUrl: String,
    @JsonProperty("logo_urls")
    val logoUrls: LogoUrls?,
    @JsonProperty("vacancies_url")
    val vacanciesUrl: String,
    @JsonProperty("accredited_it_employer")
    val accreditedItEmployer: Boolean,
    val trusted: Boolean,
)