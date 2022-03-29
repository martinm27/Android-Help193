package hr.fer.help193.share.model.rest

import com.fasterxml.jackson.annotation.JsonIgnore
import hr.fer.help193.share.model.rest.generic.Model
import hr.fer.help193.share.model.rest.generic.TwoComponentKey
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.joda.time.DateTime
import java.io.Serializable
import javax.validation.constraints.*
import javax.persistence.*

@ApiModel(description = "Model for a tablet assignment entry.")
data class TabletAssignment(
        @get:ApiModelProperty(required = true, value = "Tablet ID of the tablet assignment entry.")
        @get:NotNull
        val tabletId: Int
        ,
        @get:ApiModelProperty(required = false, value = "Name of the tablet. Ignored in POST and PUT requests.")
        val tabletName: String?
        ,
        @get:ApiModelProperty(required = true, value = "Timestamp of the tablet assignment entry.")
        @get:NotNull
        val timestamp: DateTime
        ,
        @get:ApiModelProperty(
                required = false,
                value = "ID of the intervention that the tablet was assigned to respond to at the specified timestamp." +
                        "Must point to a existing intervention."
        )
        val interventionId: Int?
        ,
        @get:ApiModelProperty(required = false, value = "Event of the intervention. Ignored in POST and PUT requests.")
        val interventionEvent: String?
        ,
        @get:ApiModelProperty(
                required = false,
                value = "ID of the vehicle that the tablet was inside of at the specified timestamp. " +
                        "Must point to a existing vehicle."
        )
        val vehicleId: Int?
        ,
        @get:ApiModelProperty(required = false, value = "Gric id of the vehicle. Ignored in POST and PUT requests.")
        val vehicleGricId: String?
) : Model<TabletAssignmentKey> {
    @get:ApiModelProperty(hidden = true)
    @get:JsonIgnore
    override val id: TabletAssignmentKey
        get() = TabletAssignmentKey(
                tabletId = tabletId,
                timestamp = timestamp
        )
}

data class TabletAssignmentKey(
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
