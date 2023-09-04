package br.com.agenda.simonebraga.view.view;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.agenda.simonebraga.R;

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        WebView webView = findViewById(R.id.web);
        webView.loadUrl("file:///android_asset/Privacy Policy.html");
    }
}
