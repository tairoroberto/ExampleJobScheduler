package br.com.thiengo.examplejobscheduler.service;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import br.com.thiengo.examplejobscheduler.domain.MessageEB;
import br.com.thiengo.examplejobscheduler.network.HttpConnection;
import de.greenrobot.event.EventBus;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by viniciusthiengo on 3/8/15.
 *
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    public static final String TAG = "LOG";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob("+params.getExtras().getString("string")+")");
        new MyAsyncTask(this).execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob()");
        return true;
    }


    // INNER CLASS
        private static class MyAsyncTask extends AsyncTask<JobParameters, Void, String>{
            private JobSchedulerService jss;

            public MyAsyncTask(JobSchedulerService j){
                jss = j;
            }
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected String doInBackground(JobParameters... params) {
                Log.i(TAG, "doInBackground()");

                SystemClock.sleep(5000);

                String answer = HttpConnection
                                    .getSetDataWeb("http://trmasolucoes.com.br/teste-jobschedule",
                                            "method");

                jss.jobFinished(params[0], true);

                return answer;
            }
            @Override
            protected void onPostExecute(String s) {
                Log.i(TAG, "onPostExecute()");

                EventBus.getDefault().post( new MessageEB( s ));
            }
        }
}
