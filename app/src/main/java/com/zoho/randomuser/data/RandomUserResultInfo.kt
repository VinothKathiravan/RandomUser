package com.zoho.randomuser.data

import com.google.gson.annotations.SerializedName

data class RandomUserResultInfo (
    @field:SerializedName("seed"    ) var seed    : String? = null,
    @field:SerializedName("results" ) var results : Int?    = null,
    @field:SerializedName("page"    ) var page    : Int?    = null,
    @field:SerializedName("version" ) var version : String? = null
)