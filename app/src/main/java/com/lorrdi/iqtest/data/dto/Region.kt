package com.lorrdi.iqtest.data.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Region(
    val id: String,
    @JsonProperty("parent_id")
    val parentId: Any?,
    val name: String,
    val areas: List<Area>,
)