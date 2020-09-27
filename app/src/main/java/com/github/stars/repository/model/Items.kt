package com.github.stars.repository.model

import com.google.gson.annotations.SerializedName

data class Items (
    @SerializedName("id") val id : Int,
    @SerializedName("full_name") val fullName : String,
    @SerializedName("stargazers_count") val stargazersCount : Int,
    @SerializedName("forks_count") val forksCount : Int,
    @SerializedName("owner") val owner : Owner,
)
