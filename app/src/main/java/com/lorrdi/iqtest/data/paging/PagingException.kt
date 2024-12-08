package com.lorrdi.iqtest.data.paging

import com.lorrdi.iqtest.data.dto.ErrorState


class PagingException(val errorState: ErrorState) : Throwable()