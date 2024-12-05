package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Address(
    val city: String?,
    val street: String?,
    val building: String?,
    val lat: Double,
    val lng: Double,
    val description: Any?,
    val raw: String?,
    val metro: Metro?,
    @JsonProperty("metro_stations")
    val metroStations: List<MetroStation>,
    val id: String,
)
