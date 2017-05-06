package com.acdprd.echoclientandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private EditText sndText;
    private Button sndButton;
    private TextView text;
    private EchoClient echoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        sndText = (EditText) findViewById(R.id.sendingText);
        sndButton = (Button) findViewById(R.id.sendButton);
        text = (TextView) findViewById(R.id.textView);
        try {
            echoClient = new NewEchoClientTask().execute(text).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        sndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (echoClient.sendMsg(sndText.getText().toString()))sndText.getText().clear();
                else Toast.makeText(getApplicationContext(), "sending error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class NewEchoClientTask extends AsyncTask<TextView, Void, EchoClient> {

        @Override
        protected EchoClient doInBackground(TextView... params) {
            return new EchoClient(params[0]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (echoClient != null) echoClient.close();
    }
}
