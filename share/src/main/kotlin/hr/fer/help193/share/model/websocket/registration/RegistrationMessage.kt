package hr.fer.help193.share.model.websocket.registration

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import hr.fer.help193.share.model.websocket.commandcenter.MessageFromCommandCenter
import hr.fer.help193.share.model.websocket.commandcenter.MessageToCommandCenter
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = RegistrationMessage.PinMessage::class, name = "PinMessage"),
        JsonSubTypes.Type(value = RegistrationMessage.SuccessMessage::class, name = "SuccessMessage"),
        JsonSubTypes.Type(value = RegistrationMessage.Ping::class, name = "Ping")
)
sealed class RegistrationMessage {
    @ApiModel(description = "WebSocket message that contains the PIN that is to be used in the registration process.")
    data class PinMessage (
            @get:ApiModelProperty(required = true, value = "PIN that is to be used in the registration process.")
            @get:NotEmpty
            val pin: String
    ) : RegistrationMessage()
    @ApiModel(description = "WebSocket message that signifies that the registration process is successful.")
    data class SuccessMessage (
            @get:ApiModelProperty(required = true, value = "Tablet ID that the tablet is registered as.")
            val tabletId: Int
            ,
            @get:ApiModelProperty(
                    required = true,
                    value = "Username that is to be used as principal in future authentication."
            )
            @get:NotEmpty
            val username: String
            ,
            @get:ApiModelProperty(
                    required = true,
                    value = "Key that is to be used as credentials in future authentication."
            )
            @get:NotEmpty
            val key: String
    ) : RegistrationMessage()
    @ApiModel(description = "WebSocket message sent periodically used to keep the connection alive.")
    data class Ping(
            @get:ApiModelProperty(
                    required = true,
                    value = "Time when the server sent the ping message measured with server's system clock."
            )
            @get:NotNull
            @get:Valid
            val time: Long
    ) : RegistrationMessage()
}

@ApiModel(description = "WebSocket message response to the received ping message.")
data class RegistrationMessagePong(
        @get:ApiModelProperty(
                required = true,
                value = "Time when the server sent the ping message measured with server's system clock. " +
                        "The value should be copied from the ping message."
        )
        @get:NotNull
        @get:Valid
        val time: Long
)
