package hr.fer.help193.share.model.rest

import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel(value = "Vehicle", description = "Model for a vehicle.")
data class Vehicle(
        @get:ApiModelProperty(
                required = false,
                value = "Vehicle ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(required = true, value = "Vehicle purpose.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val purpose: String
        ,
        @get:ApiModelProperty(required = true, value = "Vehicle licence plate.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val licencePlate: String
        ,
        @get:ApiModelProperty(required = true, value = "Vehicle GRIC ID.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val gricId: String
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Fire station id ID. " +
                        "ID of fire station that this vehicle belongs to. " +
                        "Must point to a existing fire station."
        )
        @get:NotNull
        val fireStationId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Fire station name. Ignored in POST and PUT requests.")
        val fireStationName: String?
        ,
        @get:ApiModelProperty(required = false, value = "Fire station color. Ignored in POST and PUT requests.")
        val fireStationColor: String?
) : Model<Int?> {

    companion object {
        val EMPTY = Vehicle(-1, "", "", "", -1, "", "")
    }
}
