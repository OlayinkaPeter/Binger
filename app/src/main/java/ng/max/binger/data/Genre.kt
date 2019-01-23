package ng.max.binger.data

import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.SerializedName

class Genre(@field:PrimaryKey
            @field:SerializedName("id")
            var id: Int?, @field:SerializedName("name")
            var name: String?)
