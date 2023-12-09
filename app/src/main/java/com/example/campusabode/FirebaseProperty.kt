
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FirebaseProperty(

    @SerializedName("primaryID"   ) var primaryID   : String? = null,
    @SerializedName("id"          ) var id          : String? = null,
    @SerializedName("address"        ) var address        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("images") var images: List<String>,
    @SerializedName("url"         ) var url         : String? = null,
    @SerializedName("overview"         ) var overview         : String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("video") var video: Boolean? = false

    ): Serializable
