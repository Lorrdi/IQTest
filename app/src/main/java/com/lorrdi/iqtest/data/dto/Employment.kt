package com.lorrdi.iqtest.data.dto

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("employment")
data class Employment(
    val id: String,
    val name: String,
)