package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Metro(
    @JsonProperty("station_name")
    val stationName: String,
    @JsonProperty("line_name")
    val lineName: String,
    @JsonProperty("station_id")
    val stationId: String,
    @JsonProperty("line_id")
    val lineId: String,
    val lat: Double,
    val lng: Double,
)