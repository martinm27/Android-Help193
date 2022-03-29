package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.locationtech.jts.geom.Point
import org.locationtech.spatial4j.io.jackson.GeometryAsGeoJSONSerializer
import org.locationtech.spatial4j.io.jackson.GeometryDeserializer
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@ApiModel(description = "Model for a fire station.")
data class FireStation(
        @get:ApiModelProperty(
                required = false,
                value = "Fire station ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(required = true, value = "Fire station name.")
        @get:NotEmpty
        @get:Size(min = 1, max = 256)
        val name: String
        ,
        @get:ApiModelProperty(required = true, value = "Fire station's geolocation.")
        @get:JsonSerialize(using = GeometryAsGeoJSONSerializer::class)
        @get:JsonDeserialize(using = GeometryDeserializer::class)
        @get:NotNull
        val location: Point
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Color that represents this fire station's assets on interactive maps. " +
                        "Value is expressed in #RRGGBB format. (RGB without alpha channel)"
        )
        @get:NotEmpty
        @get:Pattern(regexp = "#[A-Fa-f0-9]{6}")
        val color: String
) : Model<Int?>
