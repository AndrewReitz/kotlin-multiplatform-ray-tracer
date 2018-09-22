package cash.andrew.mntrailconditions.ui.logs

import cash.andrew.mntrailconditions.data.LumberYard.Entry

import android.content.Context
import androidx.annotation.DrawableRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.LumberYard
import cash.andrew.mntrailconditions.ui.misc.BindableAdapter
import io.reactivex.functions.Consumer

import java.util.ArrayList

internal class LogAdapter(context: Context) : BindableAdapter<Entry>(context), Consumer<Entry> {

    private var logs: MutableList<LumberYard.Entry> = mutableListOf()

    fun setLogs(logs: List<LumberYard.Entry>) {
        this.logs = ArrayList(logs)
    }

    override fun getCount(): Int {
        return logs.size
    }

    override fun getItem(i: Int): LumberYard.Entry {
        return logs[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun newView(inflater: LayoutInflater, position: Int, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.debug_logs_list_item, container, false)
        val viewHolder = LogItemViewHolder(view)
        view.tag = viewHolder
        return view
    }

    override fun bindView(item: LumberYard.Entry?, position: Int, view: View) {
        val viewHolder = view.tag as LogItemViewHolder
        viewHolder.setEntry(item!!)
    }

    override fun accept(entry: Entry) {
        logs.add(entry)
        notifyDataSetChanged()
    }

    class LogItemViewHolder(private val rootView: View) {

        private val levelView: TextView = rootView.findViewById(R.id.debug_log_level)
        private val tagView: TextView = rootView.findViewById(R.id.debug_log_tag)
        private val messageView: TextView = rootView.findViewById(R.id.debug_log_message)

        fun setEntry(entry: LumberYard.Entry) {
            rootView.setBackgroundResource(backgroundForLevel(entry.level))
            levelView.text = entry.displayLevel()
            tagView.text = entry.tag
            messageView.text = entry.message
        }
    }

    companion object {
        @DrawableRes fun backgroundForLevel(level: Int): Int {
            return when (level) {
                Log.VERBOSE, Log.DEBUG -> R.color.debug_log_accent_debug
                Log.INFO -> R.color.debug_log_accent_info
                Log.WARN -> R.color.debug_log_accent_warn
                Log.ERROR, Log.ASSERT -> R.color.debug_log_accent_error
                else -> R.color.debug_log_accent_unknown
            }
        }
    }
}
