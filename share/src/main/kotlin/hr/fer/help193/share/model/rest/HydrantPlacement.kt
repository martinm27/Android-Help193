package hr.fer.help193.share.model.rest

import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.*
import javax.validation.constraints.*

@ApiModel(description = "Model for a hydrant placement.")
data class HydrantPlacement(
        @get:ApiModelProperty(
                required = false,
                value = "Hydrant placement ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(required = true, value = "Hydrant placement name.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val name: String
) : Model<Int?>
