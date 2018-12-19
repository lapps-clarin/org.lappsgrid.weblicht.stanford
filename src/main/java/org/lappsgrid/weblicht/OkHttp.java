package org.lappsgrid.weblicht;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static org.lappsgrid.discriminator.Discriminators.*;
//import com.sq

/**
 *
 */
public class OkHttp
{
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType TEXT = MediaType.parse("text/plain");
	public static final MediaType TCF = MediaType.parse("text/tcf+xml");

	private static OkHttpClient client = new OkHttpClient();

	public OkHttp()
	{

	}

	public static Reply post(String url, String body) throws IOException
	{
		return post(url, Uri.TCF, body);
	}

	public static Reply post(String url, String type, String json) throws IOException
	{
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
//				.header("Accept", "text/xml")
				.url(url)
				.post(body)
				.build();
		Response response = client.newCall(request).execute();
		Headers headers = response.headers();
		System.out.println(String.format("%d %s", response.code(), response.message()));
		for (String name: headers.names())
		{
			String value = headers.get(name);
			System.out.println(String.format("%s = %s", name, value));
		}
		Reply reply = new Reply();
		reply.setCode(response.code());
		reply.setBody(response.body().string());
		return reply;
	}

	public static class Reply
	{
		private int code;
		private String body;

		public Reply()
		{
		}

		public Reply(int code, String body)
		{
			this.code = code;
			this.body = body;
		}

		public int getCode()
		{
			return code;
		}

		public void setCode(int code)
		{
			this.code = code;
		}

		public String getBody()
		{
			return body;
		}

		public void setBody(String body)
		{
			this.body = body;
		}
	}
}
