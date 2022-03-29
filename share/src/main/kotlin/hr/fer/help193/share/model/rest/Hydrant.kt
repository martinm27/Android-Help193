package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.*
import org.locationtech.jts.geom.Point
import org.locationtech.spatial4j.io.jackson.GeometryAsGeoJSONSerializer
import org.locationtech.spatial4j.io.jackson.GeometryDeserializer
import javax.validation.constraints.*

@ApiModel(description = "Model for a hydrant.")
data class Hydrant(
        @get:ApiModelProperty(
                required = false,
                value = "Hydrant ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(required = false, value = "Hydrant inventory mark.")
        @get:Size(min = 1, max = 256)
        val inventoryMark: String?
        ,
        @get:ApiModelProperty(required = false, value = "Hydrant type.")
        @get:Size(min = 1, max = 256)
        val type: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Hydrant placement ID. Must point to a existing hydrant placement."
        )
        val placementId: Int?
        ,
        @get:ApiModelProperty(required = false, value = "Hydrant placement name. Ignored in POST and PUT requests.")
        val placementName: String?
        ,
        @get:ApiModelProperty(required = false, value = "Hydrant's additional function.")
        @get:Size(min = 1, max = 256)
        val additionalFunction: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Whether or not hydrant is functional/operational. " +
                        "Null value represents uncertainty in hydrants operational status."
        )
        @get:JsonProperty("isFunctional")
        val isFunctional: Boolean?
        ,
        @get:ApiModelProperty(required = true, value = "Hydrant's geolocation.")
        @get:JsonSerialize(using = GeometryAsGeoJSONSerializer::class)
        @get:JsonDeserialize(using = GeometryDeserializer::class)
        @get:NotNull
        val location: Point
) : Model<Int?>
