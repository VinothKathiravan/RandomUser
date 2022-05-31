package com.zoho.randomuser.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "random_user")
data class RandomUser (
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val gender     : String?     = null,
    val name       : Name?       = Name(),
    val location   : Location?   = Location(),
    val email      : String?     = null,
    val phone      : String?     = null,
    val picture    : Picture?    = Picture(),
)

//data class Id (
//    var name  : String? = null,
//    var value : String? = null
//
//)

data class Name (
    val title : String? = null,
    val first : String? = null,
    val last  : String? = null
)

data class Location (

    val city        : String?      = null,
    val state       : String?      = null,
    val country     : String?      = null,
    val coordinates : Coordinates? = Coordinates(),

    )

data class Coordinates (

    val latitude  : String? = null,
    val longitude : String? = null

)

//data class Dob (
//
//    @field:SerializedName("date" ) var date : String? = null,
//    @field:SerializedName("age"  ) var age  : Int?    = null
//
//)

data class Picture (
    val large     : String? = null,
    val medium    : String? = null,
    val thumbnail : String? = null
)