package com.lorrdi.iqtest.data.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Vacancy(
    val id: String,
    val premium: Boolean,
    val name: String,
    val department: Department?,
    @JsonProperty("has_test")
    val hasTest: Boolean,
    @JsonProperty("response_letter_required")
    val responseLetterRequired: Boolean,
    val area: Area,
    val salary: Salary?,
    val type: Type,
    val address: Address?,
    @JsonProperty("response_url")
    val responseUrl: Any?,
    @JsonProperty("sort_point_distance")
    val sortPointDistance: Any?,
    @JsonProperty("published_at")
    val publishedAt: String,
    @JsonProperty("created_at")
    val createdAt: String,
    val archived: Boolean,
    @JsonProperty("apply_alternate_url")
    val applyAlternateUrl: String,
    @JsonProperty("show_logo_in_search")
    val showLogoInSearch: Boolean?,
    @JsonProperty("insider_interview")
    val insiderInterview: Any?,
    val url: String,
    @JsonProperty("alternate_url")
    val alternateUrl: String,
    val relations: List<Any?>,
    val employer: Employer,
    val snippet: Snippet,
    val contacts: Any?,
    val schedule: Schedule,
    @JsonProperty("working_days")
    val workingDays: List<Any?>,
    @JsonProperty("working_time_intervals")
    val workingTimeIntervals: List<WorkingTimeInterval>,
    @JsonProperty("working_time_modes")
    val workingTimeModes: List<Any?>,
    @JsonProperty("accept_temporary")
    val acceptTemporary: Boolean,
    @JsonProperty("professional_roles")
    val professionalRoles: List<ProfessionalRole>,
    @JsonProperty("accept_incomplete_resumes")
    val acceptIncompleteResumes: Boolean,
    val experience: Experience,
    val employment: Employment,
    @JsonProperty("adv_response_url")
    val advResponseUrl: Any?,
    @JsonProperty("is_adv_vacancy")
    val isAdvVacancy: Boolean,
    @JsonProperty("adv_context")
    val advContext: Any?,
    val branding: Branding?,
)