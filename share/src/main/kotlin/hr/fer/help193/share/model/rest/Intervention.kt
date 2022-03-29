package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import hr.fer.help193.share.model.rest.generic.Model
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import org.locationtech.jts.geom.Point
import org.locationtech.spatial4j.io.jackson.GeometryAsGeoJSONSerializer
import org.locationtech.spatial4j.io.jackson.GeometryDeserializer
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ApiModel(description = "Model for a intervention.")
data class Intervention(
        @get:ApiModelProperty(
                required = false,
                value = "Intervention ID. " +
                        "Must be set to null when doing a POST request. " +
                        "Must be set to same value as the ID in the path when sending a PUT request."
        )
        override val id: Int? = null
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Id of the documented area that the intervention is located in. " +
                        "Must point to a existing documented area."
        )
        @get:NotNull
        var documentedAreaId: Int
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Whether or not the documented area is referenced by internal or external id. " +
                        "External id makes it easier for the external intervention notification system to reference " +
                        "a documented area."
        )
        var isUsingExternalDocumentedAreaId: Boolean = false
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's settlement. Ignored in POST and PUT requests."
        )
        var settlement: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's neighborhood. Ignored in POST and PUT requests."
        )
        var neighborhood: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's district office. Ignored in POST and PUT requests."
        )
        var districtOffice: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's street office. Ignored in POST and PUT requests."
        )
        var street: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's street street with details. Ignored in POST and PUT requests."
        )
        var streetWithDetails: String?
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Street's house number that the intervention is located in."
        )
        @get:NotEmpty
        var houseNumber: String
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Documented area's hydrant info. Ignored in POST and PUT requests."
        )
        var hydrantInfo: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Time-date when alarm was raised for the intervention. " +
                        "If null the attribute automatically gets sets it to the current server time."
        )
        val alarmTimestamp: DateTime?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Time-date when the first responder arrived at the intervention location. " +
                        "Set to null if no responders arrived at the time of sending the request."
        )
        val arrivalTimestamp: DateTime?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Time-date when the intervention was closed. " +
                        "Set to null if intervention is not closed (if it is still active)."
        )
        val closeTimestamp: DateTime?
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Id of the intervention type. Must point to a existing intervention type."
        )
        @get:NotNull
        var interventionTypeId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Name of intervention type. Ignored in POST and PUT requests.")
        val interventionTypeName: String?
        ,
        @get:ApiModelProperty(required = true, value = "Intervention event.")
        @get:NotEmpty
        val event: String
        ,
        @get:ApiModelProperty(required = true, value = "Intervention event type.")
        @get:NotEmpty
        val eventType: String
        ,
        @get:ApiModelProperty(required = false, value = "Intervention description.")
        val description: String?
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Id of the intervention importance. Must point to a existing intervention importance."
        )
        @get:NotNull
        var interventionImportanceId: Int
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Name of intervention importance. Ignored in POST and PUT requests."
        )
        val interventionImportanceName: String?
        ,
        @get:ApiModelProperty(
                required = true,
                value = "Whether or not intervention is still active (being dealt with). " +
                        "True value represents that the intervention is active. " +
                        "False value represents that the intervention is closed."
        )
        @get:NotNull
        @get:JsonProperty("isActive")
        val isActive: Boolean
        ,
        @get:ApiModelProperty(
                required = false,
                value = "Intervention's geolocation. " +
                        "If set to null a geocoding service will be used to figure out the coordinates from the " +
                        "referenced documented area and the specified house number."
        )
        @get:JsonSerialize(using = GeometryAsGeoJSONSerializer::class)
        @get:JsonDeserialize(using = GeometryDeserializer::class)
        val location: Point?
) : Model<Int?> {

    companion object {
        val EMPTY = Intervention(-1, -1,
                false, null,
                null, null, null,
                null, "", null,
                DateTime.now(), null, null,
                -1, null, "",
                "", null, -1,
                null, false, null)
    }
}
