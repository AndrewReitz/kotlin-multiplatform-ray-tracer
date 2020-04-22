package cash.andrew.mntrailconditions.data

import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

import android.app.Application
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import java.util.ArrayDeque
import javax.inject.Inject
import javax.inject.Singleton
import okio.BufferedSink
import okio.buffer
import okio.sink
import org.threeten.bp.LocalDateTime
import timber.log.Timber

private const val BUFFER_SIZE = 200

@Singleton
class LumberYard @Inject constructor(private val app: Application) {

    private val entries = ArrayDeque<Entry>(BUFFER_SIZE + 1)
    private val entrySubject = PublishSubject.create<Entry>()

    fun tree() = object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            addEntry(Entry(priority, tag, message))
        }
    }

    @Synchronized
    private fun addEntry(entry: Entry) {
        entries.addLast(entry)
        if (entries.size > BUFFER_SIZE) {
            entries.removeFirst()
        }

        entrySubject.onNext(entry)
    }

    fun bufferedLogs(): List<Entry> = entries.toList()

    fun logs(): Observable<Entry> = entrySubject

    /** Save the current logs to disk.  */
    fun save(): Observable<File> = Observable.create { emitter ->
        val folder = app.getExternalFilesDir(null)
        if (folder == null) {
            emitter.onError(IOException("External storage is not mounted."))
            return@create
        }

        val fileName = ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())
        val output = File(folder, fileName)

        var sink: BufferedSink? = null
        try {
            sink = output.sink().buffer()
            val entries = bufferedLogs()
            for (entry in entries) {
                sink.writeUtf8(entry.prettyPrint()).writeByte('\n'.toInt())
            }

            emitter.onNext(output)
            emitter.onComplete()
        } catch (e: IOException) {
            emitter.onError(e)
        } finally {
            if (sink != null) {
                try {
                    sink.close()
                } catch (e: IOException) {
                    emitter.onError(e)
                }

            }
        }
    }

    /**
     * Delete all of the log files saved to disk. Be careful not to call this before any intents have
     * finished using the file reference.
     */
    fun cleanUp() {
        Completable.fromAction {
            val folder = app.getExternalFilesDir(null) ?: return@fromAction
            for (file in folder.listFiles()!!) {
                if (file.name.endsWith(".log")) {
                    file.delete()
                }
            }
        }
        .subscribeOn(Schedulers.computation())
        .subscribe()
    }

    class Entry constructor(
            val level: Int,
            val tag: String?,
            val message: String
    ) {

        fun prettyPrint() = String.format(
                "%22s %s %s",
                tag,
                displayLevel(),
                // Indent newlines to match the original indentation.
                message.replace("\\n".toRegex(), "\n                         "))

        fun displayLevel() = when (level) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }
    }
}
