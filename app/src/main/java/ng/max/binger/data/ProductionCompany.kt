package ng.max.binger.data

import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.SerializedName

class ProductionCompany(@field:PrimaryKey
                        @field:SerializedName("id")
                        var id: Int?, @field:SerializedName("logo_path")
                        var logoPath: String?, @field:SerializedName("name")
                        var name: String?, @field:SerializedName("origin_country")
                        var originCountry: String?)
