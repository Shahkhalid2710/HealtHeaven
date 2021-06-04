import com.applocum.connecttomyhealth.ui.exemptions.models.UserExemption
import com.google.gson.annotations.SerializedName

data class ExemptionResponse(

    @SerializedName("user_exemption") val userExemption: UserExemption
)