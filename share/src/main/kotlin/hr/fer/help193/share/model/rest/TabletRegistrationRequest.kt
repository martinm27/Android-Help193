package hr.fer.help193.share.model.rest

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.*

@ApiModel(description = "Model for a tablet registration request.")
data class TabletRegistrationRequest(
        @get:ApiModelProperty(
                required = true,
                value = "PIN that was sent to the tablet when it connected to the registration socket."
        )
        @get:NotEmpty
        val pin: String
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Tablet ID that the tablet is to be registered as."
        )
        @get:NotNull
        val tabletId: Int
)
