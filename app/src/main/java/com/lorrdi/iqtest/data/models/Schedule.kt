package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonRootName


@JsonRootName("schedule")
data class Schedule(
    val uid: String?,
    val id: String,
    val name: String,
)