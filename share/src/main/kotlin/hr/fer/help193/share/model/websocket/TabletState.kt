package hr.fer.help193.share.model.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import hr.fer.help193.share.model.rest.Tablet
import hr.fer.help193.share.model.rest.TabletAssignment
import hr.fer.help193.share.model.rest.TabletPosition
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

@ApiModel(description = "Model for a tablet state.")
data class TabletState(
        @get:ApiModelProperty(required = true, value = "Tablet which state is represented by this object.")
        @get:Valid
        val tablet: Tablet,
        @get:ApiModelProperty(required = false, value = "Last tablet position record received from the tablet.")
        @get:Valid
        val position: TabletPosition? = null,
        @get:ApiModelProperty(required = false, value = "Last assignment record of the tablet.")
        @get:Valid
        val assignment: TabletAssignment? = null,
        @get:ApiModelProperty(required = true, value = "Whether or not tablet is connected to the server.")
        @get:JsonProperty("isConnected")
        val isConnected: Boolean = false
)
