package cash.andrew.mntrailconditions.ui.debug

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cash.andrew.mntrailconditions.BuildConfig
import cash.andrew.mntrailconditions.MnTrailConditionsApp
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.ApiEndpoint
import cash.andrew.mntrailconditions.data.ApiEndpoints
import cash.andrew.mntrailconditions.data.LumberYard
import cash.andrew.mntrailconditions.ui.logs.LogsDialog
import cash.andrew.mntrailconditions.ui.misc.EnumAdapter
import cash.andrew.mntrailconditions.util.IntentManager
import com.f2prateek.rx.preferences2.Preference
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.readystatesoftware.chuck.Chuck
import com.squareup.leakcanary.internal.DisplayLeakActivity
import io.reactivex.Completable
import okhttp3.OkHttpClient
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DebugView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val endpointView get() = findViewById<Spinner>(R.id.debug_network_endpoint)
    private val endpointEditView get() = findViewById<View>(R.id.debug_network_endpoint_edit)
    private val buildNameView get() = findViewById<TextView>(R.id.debug_build_name)
    private val buildCodeView get() = findViewById<TextView>(R.id.debug_build_code)
    private val deviceMakeView get() = findViewById<TextView>(R.id.debug_device_make)
    private val deviceModelView get() = findViewById<TextView>(R.id.debug_device_model)
    private val deviceResolutionView get() = findViewById<TextView>(R.id.debug_device_resolution)
    private val deviceDensityView get() = findViewById<TextView>(R.id.debug_device_density)
    private val deviceReleaseView get() = findViewById<TextView>(R.id.debug_device_release)
    private val deviceApiView get() = findViewById<TextView>(R.id.debug_device_api)
    private val okHttpCacheMaxSizeView get() = findViewById<TextView>(R.id.debug_okhttp_cache_max_size)
    private val okHttpCacheWriteErrorView get() = findViewById<TextView>(R.id.debug_okhttp_cache_write_error)
    private val okHttpCacheRequestCountView get() = findViewById<TextView>(R.id.debug_okhttp_cache_request_count)
    private val okHttpCacheNetworkCountView get() = findViewById<TextView>(R.id.debug_okhttp_cache_network_count)
    private val okHttpCacheHitCountView get() = findViewById<TextView>(R.id.debug_okhttp_cache_hit_count)
    private val debugShowLogs get() = findViewById<Button>(R.id.debug_logs_show)
    private val debugShowLeaks get() = findViewById<Button>(R.id.debug_leaks_show)

    @Inject lateinit var client: OkHttpClient
    @Inject lateinit var lumberYard: LumberYard
    @Inject @field:ApiEndpoint lateinit var networkEndpoint: Preference<String>
    @Inject lateinit var app: Application
    @Inject lateinit var intentManager: IntentManager

    init {
        (context.applicationContext as MnTrailConditionsApp).component.inject(this)

        // Inflate all of the controls and inject them.
        LayoutInflater.from(context).inflate(R.layout.debug_view_content, this)
        setupNetworkSection()
        setupBuildSection()
        setupDeviceSection()
        setupOkHttpCacheSection()

        endpointEditView.setOnClickListener {
            Timber.d("Prompting to edit custom endpoint URL.")
            // Pass in the currently selected position since we are merely editing.
            showCustomEndpointDialog(endpointView.selectedItemPosition, networkEndpoint.get())
        }

        debugShowLogs.setOnClickListener {
            LogsDialog(
                    ContextThemeWrapper(context, R.style.Theme_MnTrailConditions),
                    lumberYard,
                    intentManager)
                    .show()
        }

        debugShowLeaks.setOnClickListener {
            val intent = Intent(context, DisplayLeakActivity::class.java)
            context.startActivity(intent)
        }

        findViewById<Button>(R.id.debug_network_logs_show).setOnClickListener {
            val intent = Chuck.getLaunchIntent(app)
            app.startActivity(intent)
        }
    }

    fun onDrawerOpened() {
        refreshOkHttpCacheStats()
    }

    @SuppressLint("CheckResult")
    private fun setupNetworkSection() {
        val currentEndpoint = ApiEndpoints.from(networkEndpoint.get())
        val endpointAdapter = EnumAdapter(context, ApiEndpoints::class.java)
        endpointView.adapter = endpointAdapter
        endpointView.setSelection(currentEndpoint.ordinal)

        RxAdapterView.itemSelections<SpinnerAdapter>(endpointView)
                .map { endpointAdapter.getItem(it) }
                .filter { item -> item !== currentEndpoint }
                .subscribe { selected ->
                    if (selected === ApiEndpoints.CUSTOM) {
                        Timber.d("Custom network endpoint selected. Prompting for URL.")
                        showCustomEndpointDialog(currentEndpoint.ordinal, "http://")
                    } else {
                        setEndpointAndRelaunch(selected!!.url)
                    }
                }

        // Only show the endpoint editor when a custom endpoint is in use.
        endpointEditView.visibility = if (currentEndpoint === ApiEndpoints.CUSTOM) View.VISIBLE else View.GONE
    }

    private fun setupBuildSection() {
        buildNameView.text = BuildConfig.VERSION_NAME
        buildCodeView.text = BuildConfig.VERSION_CODE.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun setupDeviceSection() {
        val displayMetrics = context.resources.displayMetrics
        val densityBucket = getDensityString(displayMetrics)
        deviceMakeView.text = Build.MANUFACTURER.take(20)
        deviceModelView.text = Build.MODEL.take(20)
        deviceResolutionView.text = "${displayMetrics.heightPixels}x${displayMetrics.widthPixels}"
        deviceDensityView.text = "${displayMetrics.densityDpi}dpi ($densityBucket)"
        deviceReleaseView.text = Build.VERSION.RELEASE
        deviceApiView.text = Build.VERSION.SDK_INT.toString()
    }

    private fun setupOkHttpCacheSection() {
        val cache = client.cache() // Shares the cache with apiClient, so no need to check both.
        okHttpCacheMaxSizeView.text = getSizeString(cache.maxSize())

        refreshOkHttpCacheStats()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshOkHttpCacheStats() {
        val cache = client.cache() // Shares the cache with apiClient, so no need to check both.
        val writeTotal = cache.writeSuccessCount() + cache.writeAbortCount()
        val percentage = (1f * cache.writeAbortCount() / writeTotal * 100).toInt()
        okHttpCacheWriteErrorView.text = "${cache.writeAbortCount()}/$writeTotal($percentage%)"
        okHttpCacheRequestCountView.text = cache.requestCount().toString()
        okHttpCacheNetworkCountView.text = cache.networkCount().toString()
        okHttpCacheHitCountView.text = cache.hitCount().toString()
    }

    @SuppressLint("InflateParams")
    private fun showCustomEndpointDialog(originalSelection: Int, defaultUrl: String) {
        val view = LayoutInflater.from(app).inflate(R.layout.debug_drawer_network_endpoint, null)
        val url: EditText = findViewById(R.id.debug_drawer_network_endpoint_url)
        url.setText(defaultUrl)
        url.setSelection(url.length())

        AlertDialog.Builder(context) //
                .setTitle("Set Network Endpoint")
                .setView(view)
                .setNegativeButton("Cancel") { dialog, _ ->
                    endpointView.setSelection(originalSelection)
                    dialog.cancel()
                }
                .setPositiveButton("Use") { _, _ ->
                    val theUrl = url.text.toString()
                    if (theUrl.isNotBlank()) {
                        setEndpointAndRelaunch(theUrl)
                    } else {
                        endpointView.setSelection(originalSelection)
                    }
                }
                .setOnCancelListener { _ -> endpointView.setSelection(originalSelection) }
                .show()
    }

    private fun setEndpointAndRelaunch(endpoint: String?) {
        Timber.d("Setting network endpoint to %s", endpoint)
        networkEndpoint.set(endpoint!!)

        Completable.timer(1L, TimeUnit.SECONDS)
                .subscribe { ProcessPhoenix.triggerRebirth(context) }
    }

    companion object {
        private val DATE_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US).withZone(ZoneId.systemDefault())

        private fun getDensityString(displayMetrics: DisplayMetrics): String {
            return when (displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_LOW -> "ldpi"
                DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
                DisplayMetrics.DENSITY_HIGH -> "hdpi"
                DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
                DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
                DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
                DisplayMetrics.DENSITY_TV -> "tvdpi"
                else -> displayMetrics.densityDpi.toString()
            }
        }

        private fun getSizeString(bytes: Long): String {
            var betterBytes = bytes
            val units = arrayOf("B", "KB", "MB", "GB")
            var unit = 0
            while (betterBytes >= 1024) {
                betterBytes /= 1024
                unit += 1
            }
            return betterBytes.toString() + units[unit]
        }
    }
}