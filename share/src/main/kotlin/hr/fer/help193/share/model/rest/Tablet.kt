package hr.fer.help193.share.model.rest

import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.*

@ApiModel(description = "Model for a tablet.")
data class Tablet(
        @get:ApiModelProperty(
                required = false,
                value = "Tablet ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(required = true, value = "Name that tablet uses to identify itself to the system.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val name: String
        ,
        @get:ApiModelProperty(required = true, value = "ID of the fire station that this tablet belongs to.")
        @get:NotNull
        val fireStationId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Fire station name. Ignored in POST and PUT requests.")
        val fireStationName: String?
        ,
        @get:ApiModelProperty(required = false, value = "Fire station color. Ignored in POST and PUT requests.")
        val fireStationColor: String?
        ,
        @get:ApiModelProperty(required = false, value = "ID of vehicle that this tablet is expected to be in.")
        val expectedVehicleId: Int?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Gric id of the expected vehicle. Ignored in POST and PUT requests."
        )
        val expectedVehicleGricId: String?
) : Model<Int?>
