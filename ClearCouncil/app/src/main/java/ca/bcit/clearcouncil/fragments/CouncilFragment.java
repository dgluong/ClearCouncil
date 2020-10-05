package ca.bcit.clearcouncil.fragments;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.bcit.clearcouncil.R;

/**
 * COUNCIL FRAGMENT -- HOLDS WEBPAGE OF THE CITY COUNCIL MEMBERS
 * FINISHED.
 */
public class CouncilFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Get's the view and assigns it to webview
        View view = inflater.inflate(R.layout.fragment_council, null);
        WebView webView = (WebView) view.findViewById(R.id.webview);

        //load the link in the view
        webView.loadUrl("https://vancouver.ca/your-government/city-councillors.aspx");

        //Allows javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Allows opening links to stay within the view
        webView.setWebViewClient(new WebViewClient());


        return view;
    }
}
