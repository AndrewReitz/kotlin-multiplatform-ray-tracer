package cash.andrew.mntrailconditions.ui.logs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;
import android.widget.Toast;
import cash.andrew.mntrailconditions.data.LumberYard;
import cash.andrew.mntrailconditions.util.IntentManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;

public final class LogsDialog extends AlertDialog {
  private final LumberYard lumberYard;
  private final LogAdapter adapter;
  private final IntentManager intentManager;

  private CompositeDisposable subscriptions;

  public LogsDialog(Context context, LumberYard lumberYard, IntentManager intentManager) {
    super(context);
    this.lumberYard = lumberYard;
    this.intentManager = intentManager;

    adapter = new LogAdapter(context);

    ListView listView = new ListView(context);
    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
    listView.setAdapter(adapter);

    setTitle("Logs");
    setView(listView);
    setButton(
        BUTTON_NEGATIVE,
        "Close",
        (dialog, which) -> {
          // NO-OP.
        });
    setButton(
        BUTTON_POSITIVE,
        "Share",
        (dialog, which) -> {
          share();
        });
  }

  @Override
  protected void onStart() {
    super.onStart();

    adapter.setLogs(lumberYard.bufferedLogs());

    subscriptions = new CompositeDisposable();
    subscriptions.add(
        lumberYard
            .logs() //
            .observeOn(AndroidSchedulers.mainThread()) //
            .subscribe(adapter));
  }

  @Override
  protected void onStop() {
    super.onStop();
    subscriptions.clear();
  }

  private void share() {
    lumberYard
        .save() //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(
            new DefaultObserver<File>() {
              @Override
              public void onComplete() {
                // NO-OP.
              }

              @Override
              public void onError(Throwable e) {
                Toast.makeText(
                        getContext(), "Couldn't save the logs for sharing.", Toast.LENGTH_SHORT)
                    .show();
              }

              @Override
              public void onNext(File file) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intentManager.maybeStartChooser(sendIntent);
              }
            });
  }
}
