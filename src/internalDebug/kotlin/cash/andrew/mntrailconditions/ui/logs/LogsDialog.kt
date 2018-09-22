package cash.andrew.mntrailconditions.ui.logs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cash.andrew.mntrailconditions.data.LumberYard
import cash.andrew.mntrailconditions.util.IntentManager
import cash.andrew.mntrailconditions.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LogsDialog(
        context: Context,
        private val lumberYard: LumberYard,
        private val intentManager: IntentManager
) : AlertDialog(context) {

    private val adapter: LogAdapter = LogAdapter(context)
    private val subscriptions = CompositeDisposable()

    init {

        val listView = ListView(context)
        listView.transcriptMode = ListView.TRANSCRIPT_MODE_NORMAL
        listView.adapter = adapter

        setTitle("Logs")
        setView(listView)
        setButton(BUTTON_NEGATIVE, "Close") { _, _ -> /* NO-OP.*/ }
        setButton(BUTTON_POSITIVE,"Share") { _, _ -> share() }
    }

    override fun onStart() {
        super.onStart()

        adapter.setLogs(lumberYard.bufferedLogs())

        subscriptions += lumberYard.logs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter)
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }


    private fun share() {
        subscriptions += lumberYard.save()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ file ->
                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.type = "text/plain"
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                    intentManager.maybeStartChooser(sendIntent)
                }, {
                    Toast.makeText(
                            context, "Couldn't save the logs for sharing.", Toast.LENGTH_SHORT)
                            .show()
                })
    }
}
