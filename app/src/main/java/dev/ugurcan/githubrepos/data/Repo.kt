package dev.ugurcan.githubrepos.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Repo(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("stargazers_count") val starCount: Int
) : Serializable
