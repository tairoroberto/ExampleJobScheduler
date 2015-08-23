package br.com.thiengo.examplejobscheduler;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.thiengo.examplejobscheduler.domain.MessageEB;
import br.com.thiengo.examplejobscheduler.service.JobSchedulerService;
import de.greenrobot.event.EventBus;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;


public class MainActivity extends ActionBarActivity {
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register( this );

        tv = (TextView) findViewById(R.id.tv_job_scheduler_answer);
    }


    public void onEvent(MessageEB m){
        tv.setText( m.getResult() );
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onJobExecute(View view){
        ComponentName cp = new ComponentName(this, JobSchedulerService.class);

        PersistableBundle b = new PersistableBundle();
        b.putString("string", "Qualquer coisa");

        JobInfo jb = new JobInfo.Builder(1, cp)
                .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setExtras(b)
                //.setMinimumLatency(2000)
                //.setOverrideDeadline(2000)
                .setPersisted(true)
                .setPeriodic(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build();

        //JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobScheduler js = JobScheduler.getInstance(this);
        js.schedule(jb);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onCancelAll(View view){
        //JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobScheduler js = JobScheduler.getInstance(this);
        js.cancel(1);
    }
}
