package hr.fer.help193.share.model.websocket.vehicle

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hr.fer.help193.share.model.websocket.commandcenter.MessageFromCommandCenter
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.locationtech.jts.geom.Point
import org.locationtech.spatial4j.io.jackson.GeometryAsGeoJSONSerializer
import org.locationtech.spatial4j.io.jackson.GeometryDeserializer
import javax.validation.Valid
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotNull

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = MessageFromTablet.PositionUpdate::class, name = "PositionUpdate"),
        JsonSubTypes.Type(value = MessageFromTablet.Assignment::class, name = "Assignment"),
        JsonSubTypes.Type(value = MessageFromTablet.Pong::class, name = "Pong")
)
sealed class MessageFromTablet {
    @ApiModel(description = "WebSocket message that notifies the server about tablet position change.")
    data class PositionUpdate(
            @get:ApiModelProperty(
                    required = true,
                    value = "Geolocation where the tablet was at the specified timestamp."
            )
            @get:JsonSerialize(using = GeometryAsGeoJSONSerializer::class)
            @get:JsonDeserialize(using = GeometryDeserializer::class)
            @get:NotNull
            val location: Point,
            @get:ApiModelProperty(
                    required = true,
                    value = "Timestamp of when the position was captured. " +
                            "The value should be taken from GPS clock."
            )
            @get:NotNull
            val timestamp: DateTime,
            @get:ApiModelProperty(
                    required = false,
                    value = "Direction that the tablet was looking towards at the specified timestamp. " +
                            "The value indicates how far off from heading true north the device is in degrees clockwise."
            )
            @get:DecimalMin(value = "0.0")
            @get:DecimalMax(value = "360.0", inclusive = false)
            val heading: Double?,
            @get:ApiModelProperty(
                    required = false,
                    value = "Speed with which the tablet was traveling at the specified timestamp. " +
                            "The value is in meters per second."
            )
            @get:DecimalMin(value = "0.0")
            val speed: Double?
    ) : MessageFromTablet()
    @ApiModel(description = "WebSocket message that assigns the message sending tablet to the specified intervention.")
    data class Assignment(
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
    ) : MessageFromTablet()
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
    ) : MessageFromTablet()
}
