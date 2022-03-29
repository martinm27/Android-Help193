package hr.fer.help193.share.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.locationtech.spatial4j.io.jackson.ShapesAsGeoJSONModule

fun ObjectMapper.applyCustomConfiguration(): ObjectMapper {
    registerModules(
            KotlinModule(),
            JodaModule(),
            ShapesAsGeoJSONModule()
    )
    configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
    configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
    configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
    return this
}
