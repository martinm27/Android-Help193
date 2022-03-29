package hr.fer.help193.share.model.websocket.commandcenter

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
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
        JsonSubTypes.Type(value = MessageFromCommandCenter.Assignment::class, name = "Assignment"),
        JsonSubTypes.Type(value = MessageFromCommandCenter.Pong::class, name = "Pong")
)
sealed class MessageFromCommandCenter {
    @ApiModel(description = "WebSocket message that assigns the specified tablet to the specified intervention.")
    data class Assignment(
            @get:ApiModelProperty(
                    required = true,
                    value = "ID of the tablet that is to be assigned to a intervention."
            )
            @get:NotNull
            val tabletId: Int,
            @get:ApiModelProperty(
                    required = false,
                    value = "ID of the intervention that the tablet is to be assigned to. " +
                            "If null the tablet is relieved of previous intervention assignment. " +
                            "Must point to a existing tablet."
            )
            val interventionId: Int?,
            @get:ApiModelProperty(
                    required = false,
                    value = "ID of the vehicle that the tablet is to be in. " +
                            "If null the tablet is to be taken out of a vehicle. " +
                            "Must point to a existing vehicle."
            )
            val vehicleId: Int?
    ) : MessageFromCommandCenter()
    @ApiModel(description = "WebSocket message response to the received ping message.")
    data class Pong(
            @get:ApiModelProperty(
                    required = true,
                    value = "Time when the server sent the ping message measured with server's system clock. " +
                            "The value should be copied from the ping message."
            )
            @get:NotNull
            @get:Valid
            val time: Long
    ) : MessageFromCommandCenter()
}
