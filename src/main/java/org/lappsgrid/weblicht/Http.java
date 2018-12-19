package org.lappsgrid.weblicht;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Keith Suderman
 */
public class Http
{
	public static final String TCF = "text/tcf+xml";
	public static final String JSON_MIME_TYPE = "application/json";

	public static Response post(String uri, String body) throws IOException
	{
		return post(uri, TCF, body);
	}

	public static Response post(String uri, String type, String body) throws IOException
	{
//		System.out.println("Posting " + type);
//		return httpPost(uri, type, body);
//	}
//
//	public static Response okPost(String uri, String type, String body) throws IOException
//	{
//		OkHttp.Reply reply = OkHttp.post(uri, type, body);
//		return new Response(reply.getCode(), reply.getBody());
//	}
//
//	public static Response httpPost(String uri, String type, String body) throws IOException
//	{
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept", TCF);
		connection.setRequestProperty("Content-type", type);
		connection.setDoOutput(true);
		PrintStream out = new PrintStream(connection.getOutputStream());
		System.out.println(body.getClass().getName());
		out.print(body);
		out.flush();
		out.close();

		int status = connection.getResponseCode();
		String message;
		if (status < 300)
		{
			message = read(connection.getInputStream());
		}
		else
		{
			message = "ERROR: " + connection.getResponseMessage() + "\n" + read(connection.getErrorStream());
		}
		return new Response(status, message);
	}

	protected static String read(InputStream in) throws IOException
	{
		System.out.println("available: " + in.available());
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;

		while ((line = reader.readLine()) != null)
		{
			buffer.append(line);
		}
		return buffer.toString();
	}

	public static class Response {
		private int status;
		private String message;

		public Response(int status, String message)
		{
			this.status = status;
			this.message = message;
		}

		public int getStatus()
		{
			return status;
		}

		public String getMessage()
		{
			return message;
		}
	}

	/*
	private HttpBuilder http;

	public Http()
	{
		String[] accept = { TCF };
		http =	HttpBuilder.configure( config -> {
			HttpConfig.Request request = config.getRequest();
			request.setAccept(accept);
			request.setContentType(TCF);
			request.encoder(TCF, NativeHandlers.Encoders::xml);
		});
	}

	public String post(String uri, String tcf)
	{
		Data data = new Data(Discriminators.Uri.TCF, tcf);
		Data result = (Data) http.post(config -> {
			HttpConfig.Request request = config.getRequest();
			request.setUri(uri);
			request.setBody(data.getPayload());
			HttpConfig.Response response = config.getResponse();
			response.exception( throwable -> {
//				System.out.println("HTTP POST: exception");
				throwable.printStackTrace();
				Data d = new Data();
				d.setDiscriminator(Discriminators.Uri.ERROR);
				d.setPayload(throwable.toString());
				return d;
			});
			response.failure((fromServer, object) -> {
//				System.out.println("HTTP POST: failure");
//				System.out.println(fromServer.getMessage());
//				System.out.println("HEADERS");
//				for (FromServer.Header header : fromServer.getHeaders())
//				{
//					System.out.println(header.getKey() + " = " + header.getValue());
//				}
				Data d = new Data();
				d.setDiscriminator(Discriminators.Uri.ERROR);
				d.setPayload(object.toString());
				return d;
			});
			response.success((fromServer, object) -> {
//				System.out.println("object is a " + object.getClass().getName());
//				System.out.println("HTTP POST: success!");
				Data d = new Data();
//				if (object == null && fromServer.getHasBody())
//				{
//					System.out.println("Reading body from server");
//					d.setPayload(readString(fromServer.getReader()));
//				}
				if (object instanceof byte[])
				{
					System.out.println("Creating String from byte[]");
					d.setPayload(new String((byte[])object));
				}
				else
				{
					System.out.println("Setting payload to the object " + object.getClass().getName());
					d.setPayload(object);
				}
				d.setDiscriminator(Discriminators.Uri.TCF);
				return d;
			});
		});

		return result.asPrettyJson();
	}
	*/

}
