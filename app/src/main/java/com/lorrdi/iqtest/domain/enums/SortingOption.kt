package com.lorrdi.iqtest.domain.enums

enum class SortingOption(val apiValue: String, val displayName: String) {
    RELEVANCE("relevance", "По релевантности"),
    PUBLICATION_TIME("publication_time", "По дате");
}