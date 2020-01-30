package dev.ugurcan.githubrepos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Repo(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @SerializedName("stargazers_count") val starCount: Int,
    var isBookmarked: Boolean
) : Serializable
