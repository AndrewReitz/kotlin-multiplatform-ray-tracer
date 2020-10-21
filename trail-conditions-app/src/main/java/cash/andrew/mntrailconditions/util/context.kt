package cash.andrew.mntrailconditions.util

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

fun Context.openUri(uri: Uri) {
  val intent = Intent(ACTION_VIEW, uri).apply {
    addCategory(CATEGORY_BROWSABLE)
    flags = FLAG_ACTIVITY_NEW_TASK
  }
  startActivity(intent)
}
