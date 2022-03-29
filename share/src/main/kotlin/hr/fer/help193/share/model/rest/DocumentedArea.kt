package hr.fer.help193.share.model.rest

import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ApiModel(
        description = "Model for a documented area. " +
                "Documented area is a part of a street that has the same route description from each fire station."
)
data class DocumentedArea(
        @get:ApiModelProperty(
                required = false,
                value = "Documented area ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(
                required = true,
                value = "ID that the external intervention notification system uses to identify the documented area."
        )
        @get:NotNull
        val externalSystemId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Settlement that the documented area is in.")
        val settlement: String?
        ,
        @get:ApiModelProperty(required = false, value = "Neighborhood that the documented area is in.")
        val neighborhood: String?
        ,
        @get:ApiModelProperty(required = false, value = "District office that the documented area is in.")
        val districtOffice: String?
        ,
        @get:ApiModelProperty(required = true, value = "Street that the documented area is in.")
        @get:NotEmpty
        val street: String
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Street with details that describe the part of the street that the documented area is in."
        )
        @get:NotEmpty
        val streetWithDetails: String
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Additional information about the hydrants that are in the documented area."
        )
        val hydrantInfo: String?
): Model<Int?>
