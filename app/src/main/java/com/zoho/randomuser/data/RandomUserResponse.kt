package com.zoho.randomuser.data

import com.google.gson.annotations.SerializedName


data class RandomUserResponse (

  @field:SerializedName("results" ) var results : ArrayList<RandomUser> = arrayListOf(),
  @field:SerializedName("info"    ) var info    : RandomUserResultInfo? = RandomUserResultInfo()

)