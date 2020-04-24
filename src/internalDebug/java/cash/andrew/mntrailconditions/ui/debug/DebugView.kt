package cash.andrew.mntrailconditions.ui.debug

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import cash.andrew.mntrailconditions.BuildConfig
import cash.andrew.mntrailconditions.MnTrailConditionsApp
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.ApiEndpoint
import cash.andrew.mntrailconditions.data.ApiEndpoints
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.DebugDrawerNetworkEndpointBinding
import cash.andrew.mntrailconditions.databinding.DebugViewContentBinding
import cash.andrew.mntrailconditions.ui.misc.EnumAdapter
import cash.andrew.mntrailconditions.util.IntentManager
import com.jakewharton.processphoenix.ProcessPhoenix
import com.readystatesoftware.chuck.Chuck
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class DebugView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : GridLayout(context, attrs) {

    private val endpointView get() = binding.debugNetworkEndpoint
    private val endpointEditView get() = binding.debugNetworkEndpointEdit
    private val buildNameView get() = binding.debugBuildName
    private val buildCodeView get() = binding.debugBuildCode
    private val deviceMakeView get() = binding.debugDeviceMake
    private val deviceModelView get() = binding.debugDeviceModel
    private val deviceResolutionView get() = binding.debugDeviceResolution
    private val deviceDensityView get() = binding.debugDeviceDensity
    private val deviceReleaseView get() = binding.debugDeviceRelease
    private val deviceApiView get() = binding.debugDeviceApi
    private val okHttpCacheMaxSizeView get() = binding.debugOkhttpCacheMaxSize
    private val okHttpCacheWriteErrorView get() = binding.debugOkhttpCacheWriteError
    private val okHttpCacheRequestCountView get() = binding.debugOkhttpCacheRequestCount
    private val okHttpCacheNetworkCountView get() = binding.debugOkhttpCacheNetworkCount
    private val okHttpCacheHitCountView get() = binding.debugOkhttpCacheHitCount

    @Inject lateinit var client: OkHttpClient
    @Inject @field:ApiEndpoint lateinit var networkEndpoint: Preference<String>
    @Inject lateinit var app: Application
    @Inject lateinit var intentManager: IntentManager

    private val binding: DebugViewContentBinding

    init {
        (context.applicationContext as MnTrailConditionsApp).component.inject(this)
        View.inflate(context, R.layout.debug_view_content, this)

        // Inflate all of the controls and inject them.
        binding = DebugViewContentBinding.bind(this)

        setupNetworkSection()
        setupBuildSection()
        setupDeviceSection()
        setupOkHttpCacheSection()

        endpointEditView.setOnClickListener {
            Timber.d("Prompting to edit custom endpoint URL.")
            // Pass in the currently selected position since we are merely editing.
            showCustomEndpointDialog(endpointView.selectedItemPosition, networkEndpoint.get())
        }

        binding.debugNetworkLogsShow.setOnClickListener {
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

        endpointView.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = endpointAdapter.getItem(position)
                if (selected == currentEndpoint) return

                if (selected === ApiEndpoints.CUSTOM) {
                    Timber.d("Custom network endpoint selected. Prompting for URL.")
                    showCustomEndpointDialog(currentEndpoint.ordinal, "http://")
                } else {
                    setEndpointAndRelaunch(selected!!.url)
                }
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
        val cache = client.cache!! // Shares the cache with apiClient, so no need to check both.
        okHttpCacheMaxSizeView.text = getSizeString(cache.maxSize())

        refreshOkHttpCacheStats()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshOkHttpCacheStats() {
        val cache = client.cache!! // Shares the cache with apiClient, so no need to check both.
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
        val viewBinding = DebugDrawerNetworkEndpointBinding.bind(view)
        val url: EditText = viewBinding.debugDrawerNetworkEndpointUrl
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

    @SuppressLint("CheckResult")
    private fun setEndpointAndRelaunch(endpoint: String?) {
        Timber.d("Setting network endpoint to %s", endpoint)
        networkEndpoint.set(endpoint!!)

        postDelayed({
            ProcessPhoenix.triggerRebirth(context)
        }, 1000)
    }

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
