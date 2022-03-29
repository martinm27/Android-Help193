package hr.fer.help193.share.model.websocket.vehicle

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.share.model.rest.Vehicle
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = MessageToTablet.IncomingAssignment::class, name = "IncomingAssignment"),
        JsonSubTypes.Type(value = MessageToTablet.Ping::class, name = "Ping")
)
sealed class MessageToTablet {
    @ApiModel(description = "WebSocket message that notifies the tablet about a new assignment.")
    data class IncomingAssignment (
            @get:ApiModelProperty(
                    required = true,
                    value = "Intervention that is assigned to the tablet. " +
                            "If null the tablet is relieved of previous intervention assignment."
            )
            @get:Valid
            val intervention: Intervention?
            ,
            @get:ApiModelProperty(
                    required = true,
                    value = "Vehicle that the tablet is assigned to be in. " +
                            "If null the tablet is to be taken out of a vehicle."
            )
            @get:Valid
            val vehicle: Vehicle?
    ) : MessageToTablet()
    @ApiModel(description = "WebSocket message sent periodically used to keep the connection alive.")
    data class Ping(
            @get:ApiModelProperty(
                    required = true,
                    value = "Time when the server sent the ping message measured with server's system clock."
            )
            @get:NotNull
            @get:Valid
            val time: Long
    ) : MessageToTablet()
}
