package ng.max.binger.data;

import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
