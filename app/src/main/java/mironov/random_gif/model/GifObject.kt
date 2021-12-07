package mironov.random_gif.model

import com.google.gson.annotations.SerializedName;

class GifObject {
    @SerializedName("description")
    private var desription: String? = null

    @SerializedName("gifURL")
    private var uri: String? = null


    @SerializedName("previewURL")
    private var previewUri: String? = null

    fun getDesription(): String? {
        return desription
    }

    fun setDesription(desription: String?) {
        this.desription = desription
    }

    fun getUri(): String? {
        return uri
    }

    fun setUri(uri: String?) {
        this.uri = uri
    }

    fun getPreviewUri(): String? {
        return previewUri
    }

    fun setPreviewUri(previewUri: String?) {
        this.previewUri = previewUri
    }

    override fun toString(): String {
        return "GifObject{" +
                "desription='" + desription + '\'' +
                ", uri='" + uri + '\'' +
                ", previewUri='" + previewUri + '\'' +
                '}'
    }
}
