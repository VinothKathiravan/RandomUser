package com.zoho.randomuser.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun userNameToString(userName: Name) : String {
        val type = object : TypeToken<Name>(){}.type
        return Gson().toJson(userName,type)
    }

    @TypeConverter
    fun stringToUserName(userName: String) : Name {
        val type=object :TypeToken<Name>(){}.type
        return Gson().fromJson(userName,type)
    }

    @TypeConverter
    fun userPictureToString(userPicture: Picture) : String {
        val type = object : TypeToken<Picture>(){}.type
        return Gson().toJson(userPicture,type)
    }

    @TypeConverter
    fun stringToUserPicture(userPicture: String) : Picture {
        val type=object :TypeToken<Picture>(){}.type
        return Gson().fromJson(userPicture,type)
    }

    @TypeConverter
    fun userLocationToString(location: Location) : String {
        val type = object : TypeToken<Location>(){}.type
        return Gson().toJson(location,type)
    }

    @TypeConverter
    fun stringToUserLocation(location: String) : Location {
        val type=object :TypeToken<Location>(){}.type
        return Gson().fromJson(location,type)
    }

    @TypeConverter
    fun userLocationCoordinatesToString(location: Coordinates) : String {
        val type = object : TypeToken<Coordinates>(){}.type
        return Gson().toJson(location,type)
    }

    @TypeConverter
    fun stringToUserLocationCoordinates(location: String) : Coordinates {
        val type=object :TypeToken<Coordinates>(){}.type
        return Gson().fromJson(location,type)
    }
}