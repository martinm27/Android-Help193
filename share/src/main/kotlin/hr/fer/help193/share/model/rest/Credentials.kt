package hr.fer.help193.share.model.rest

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.*

@ApiModel(description = "Model used for sending credentials in a sign in request. (Used mainly by web clients.)")
data class Credentials(
        @get:ApiModelProperty(required = true, value = "Username to identify as.")
        @get:NotEmpty
        val username: String
        ,
        @get:ApiModelProperty(required = true, value = "Password that proves the identity.")
        @get:NotEmpty
        val password: String
)
