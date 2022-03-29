package hr.fer.help193.vehicle.app.features.map.map

import android.content.Context
import android.content.res.Resources
import com.mapbox.mapboxsdk.offline.OfflineManager
import com.mapbox.mapboxsdk.offline.OfflineRegion
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition
import com.mapbox.mapboxsdk.plugins.offline.model.NotificationOptions
import com.mapbox.mapboxsdk.plugins.offline.model.OfflineDownloadOptions
import com.mapbox.mapboxsdk.plugins.offline.offline.OfflinePlugin
import com.mapbox.mapboxsdk.plugins.offline.utils.OfflineUtils
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.activity.main.MainActivity
import hr.fer.help193.vehicle.core.MAX_ZOOM_LEVEL
import hr.fer.help193.vehicle.core.MIN_ZOOM_LEVEL
import hr.fer.help193.vehicle.core.RESTRICTED_BOUNDS_AREA
import hr.fer.help193.vehicle.utils.isNotNull

private const val REGION_ZG = "Zagreb"

class OfflineMapManager(private val context: Context, private val resources: Resources) {

    fun downloadOfflineMap(url: String) {
        OfflineManager.getInstance(context).listOfflineRegions(
                object : OfflineManager.ListOfflineRegionsCallback {
                    override fun onList(offlineRegions: Array<out OfflineRegion>?) {
                        val isOfflineRegionPresent = offlineRegions?.find {
                            it.metadata.isNotNull() && it.metadata!!.contentEquals(OfflineUtils.convertRegionName(REGION_ZG))
                        }

                        if (isOfflineRegionPresent == null) {
                            val definition = OfflineTilePyramidRegionDefinition(
                                    url,
                                    RESTRICTED_BOUNDS_AREA,
                                    MIN_ZOOM_LEVEL,
                                    MAX_ZOOM_LEVEL,
                                    resources.displayMetrics.density
                            )
                            // Customize the download notification's appearance
                            val notificationOptions = NotificationOptions.builder(context)
                                    .smallIconRes(R.drawable.mapbox_logo_icon)
                                    .returnActivity(MainActivity::class.java.name)
                                    .build()

                            // Start downloading the map tiles for offline use
                            OfflinePlugin.getInstance(context).startDownload(
                                    OfflineDownloadOptions.builder()
                                            .definition(definition)
                                            .metadata(OfflineUtils.convertRegionName(REGION_ZG))
                                            .notificationOptions(notificationOptions)
                                            .build()
                            )
                        }
                    }

                    override fun onError(error: String?) {}
                }
        )
    }
}
