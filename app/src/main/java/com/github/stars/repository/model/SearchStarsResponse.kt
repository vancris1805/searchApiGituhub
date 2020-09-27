package com.github.stars.repository.model

import com.google.gson.annotations.SerializedName

data class SearchStarsResponse(
    @SerializedName("items") val items : List<Items>
)