package hr.fer.help193.share.model.rest

import hr.fer.help193.share.model.rest.generic.Model
import hr.fer.help193.share.model.rest.generic.TwoComponentKey
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.Transient
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ApiModel(
        description = "Model for a route description. " +
                "Route description describes the fastest way to get from a fire station to a documented area."
)
data class RouteDescription(
        @get:ApiModelProperty(
                required = true,
                value = "ID of the documented area that is the destination of the described route."
        )
        @get:NotNull
        val documentedAreaId: Int
        ,
        @get:ApiModelProperty(
                required = true,
                value = "ID of the fire station that is the starting point of the described route."
        )
        @get:NotNull
        val fireStationId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Fire station name. Ignored in POST and PUT requests.")
        val fireStationName: String?
        ,
        @get:ApiModelProperty(required = false, value = "Fire station color. Ignored in POST and PUT requests.")
        val fireStationColor: String?
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Priority that the fire station has in responding to a intervention on the documented area. " +
                        "Lower number represents higher priority."
        )
        @get:NotNull
        val priority: Int
        ,
        @get:ApiModelProperty(required = true, value = "Actual text that describes the travel route.")
        @get:NotEmpty
        val text: String
        ,
        @get:ApiModelProperty(required = false, value = "Warning text of something to be aware of while traveling.")
        val notice: String?
        ,
        @get:ApiModelProperty(required = false, value = "TODO figure out what this is.") // TODO figure out what this is
        val notice15Minutes: String?
): Model<RouteDescriptionKey> {
    override val id: RouteDescriptionKey
        get() = RouteDescriptionKey(
                documentedAreaId = documentedAreaId,
                fireStationId = fireStationId
        )
}

data class RouteDescriptionKey(
        var documentedAreaId: Int = 0,
        var fireStationId: Int = 0
) : TwoComponentKey<Int, Int>, Serializable {
    @get:Transient
    override val component1: Int
        get() = documentedAreaId

    @get:Transient
    override val component2: Int
        get() = fireStationId
}
