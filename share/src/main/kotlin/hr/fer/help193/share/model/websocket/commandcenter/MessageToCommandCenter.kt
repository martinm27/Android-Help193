package hr.fer.help193.share.model.websocket.commandcenter

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import hr.fer.help193.share.model.rest.Intervention
import hr.fer.help193.share.model.websocket.TabletState
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
        JsonSubTypes.Type(value = MessageToCommandCenter.TabletStatesBatchUpdate::class, name = "TabletStatesBatchUpdate"),
        JsonSubTypes.Type(value = MessageToCommandCenter.TabletStateUpdate::class, name = "TabletStateUpdate"),
        JsonSubTypes.Type(value = MessageToCommandCenter.NewActiveIntervention::class, name = "NewActiveIntervention"),
        JsonSubTypes.Type(value = MessageToCommandCenter.InterventionClosed::class, name = "InterventionClosed"),
        JsonSubTypes.Type(value = MessageToCommandCenter.Ping::class, name = "Ping")
)
sealed class MessageToCommandCenter{
    @ApiModel(description = "WebSocket message that updates the state of all tablets.")
    data class TabletStatesBatchUpdate(
            @get:ApiModelProperty(required = true, value = "States of all the tablets that need to be updated.")
            @get:NotNull
            @get:Valid
            val states: List<TabletState>
    ) : MessageToCommandCenter()
    @ApiModel(description = "WebSocket message that updates the state of one tablet.")
    data class TabletStateUpdate(
            @get:ApiModelProperty(required = true, value = "State of all the tablet that needs to be updated.")
            @get:NotNull
            @get:Valid
            val state: TabletState
    ) : MessageToCommandCenter()
    @ApiModel(description = "WebSocket message that notifies that a new active intervention in the system.")
    data class NewActiveIntervention(
            @get:ApiModelProperty(required = true, value = "New active intervention.")
            @get:NotNull
            @get:Valid
            val intervention: Intervention
    ) : MessageToCommandCenter()
    @ApiModel(description = "WebSocket message that notifies that a intervention has been closed.")
    data class InterventionClosed(
            @get:ApiModelProperty(required = true, value = "Closed intervention.")
            @get:NotNull
            @get:Valid
            val intervention: Intervention
    ) : MessageToCommandCenter()
    @ApiModel(description = "WebSocket message sent periodically used to keep the connection alive.")
    data class Ping(
            @get:ApiModelProperty(
                    required = true,
                    value = "Time when the server sent the ping message measured with server's system clock."
            )
            @get:NotNull
            @get:Valid
            val time: Long
    ) : MessageToCommandCenter()
}
