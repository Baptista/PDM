package com.pdm.thoth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class HttpLoader extends AsyncTaskLoader<StringBuilder> {

	private StringBuilder _stream;
	private URL _url;

	public HttpLoader(Context context, URL url) {
		super(context);
		_url = url;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (_stream != null)
			deliverResult(_stream);
		else
			forceLoad();
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
		super.onStopLoading();
	}

	@Override
	protected boolean onCancelLoad() {

		return super.onCancelLoad();
	}

	@Override
	public void deliverResult(StringBuilder data) {
		_stream = data;
		super.deliverResult(data);
	}

	@Override
	public StringBuilder loadInBackground() {

		HttpURLConnection urlConnection = null;
		try {

			urlConnection = (HttpURLConnection) _url.openConnection();
			if (urlConnection.getResponseCode() != 200)
				return null;

			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			return sb;

		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

	}

}
