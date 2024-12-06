package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("area")
data class Area(
    val id: String,
    val name: String,
    val url: String? = null,
    val areas: List<City>? = null
)