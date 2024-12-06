package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("experience")
data class Experience(
    val id: String,
    val name: String,
)