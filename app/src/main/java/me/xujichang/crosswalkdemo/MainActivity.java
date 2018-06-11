package me.xujichang.crosswalkdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import me.xujichang.crosswalksdk.view.XWebViewActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUrl = findViewById(R.id.et_url_input);
    }

    public void toWebView(View view) {
        Intent intent = new Intent(this, XWebViewActivity.class);
        String url = etUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            url = "http://192.168.1.116:3000";
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
