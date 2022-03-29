package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.*

@ApiModel(description = "Model for a tablet registration.")
data class TabletRegistration(
        @get:ApiModelProperty(required = true, value = "ID of the tablet.")
        @get:NotNull
        val tabletId: Int
        ,
        @get:ApiModelProperty(required = true, value = "Name of the tablet.")
        @get:NotEmpty
        val tabletName: String
        ,
        @get:ApiModelProperty(required = true, value = "Whether or not the tablet is registered.")
        @get:JsonProperty("isRegistered")
        @get:NotNull
        val isRegistered: Boolean
)
