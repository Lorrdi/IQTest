package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class LogoUrls(
    @JsonProperty("90")
    val n90: String,
    @JsonProperty("240")
    val n240: String,
    val original: String,
)