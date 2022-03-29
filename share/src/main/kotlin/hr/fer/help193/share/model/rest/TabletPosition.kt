package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hr.fer.help193.share.model.rest.generic.Model
import hr.fer.help193.share.model.rest.generic.TwoComponentKey
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.locationtech.jts.geom.Point
import org.locationtech.spatial4j.io.jackson.GeometryAsGeoJSONSerializer
import org.locationtech.spatial4j.io.jackson.GeometryDeserializer
import java.io.Serializable
import javax.validation.constraints.*
import javax.persistence.*

@ApiModel(description = "Model for a tablet position entry.")
data class TabletPosition(
        @get:ApiModelProperty(required = true, value = "Tablet ID of the tablet position entry.")
        @get:NotNull
        val tabletId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Name of the tablet. Ignored in POST and PUT requests.")
        val tabletName: String?
        ,
        @get:ApiModelProperty(required = true, value = "Timestamp of the tablet position entry.")
        @get:NotNull
        val timestamp: DateTime
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Geolocation where the specified tablet was at the specified timestamp."
        )
        @get:JsonSerialize(using = GeometryAsGeoJSONSerializer::class)
        @get:JsonDeserialize(using = GeometryDeserializer::class)
        @get:NotNull
        val location: Point
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Direction that the specified tablet was looking towards at the specified timestamp." +
                        "The number indicates how far off from heading true north the device is in degrees clockwise."
        )
        @get:DecimalMin(value = "0.0")
        @get:DecimalMax(value = "360.0", inclusive = false)
        val heading: Double?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Speed with which the specified tablet was traveling at the specified timestamp." +
                        "The value is in meters per second."
        )
        @get:DecimalMin(value = "0.0")
        val speed: Double?
) : Model<TabletPositionKey> {
    @get:ApiModelProperty(hidden = true)
    @get:JsonIgnore
    override val id: TabletPositionKey
        get() = TabletPositionKey(
                tabletId = tabletId,
                timestamp = timestamp
        )
}

data class TabletPositionKey(
        var tabletId: Int = 0,
        var timestamp: DateTime = DateTime.now()
) : TwoComponentKey<Int, DateTime>, Serializable {
    @get:Transient
    override val component1: Int
        get() = tabletId

    @get:Transient
    override val component2: DateTime
        get() = timestamp
}
