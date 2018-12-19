package org.lappsgrid.weblicht;

import org.lappsgrid.api.WebService;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.Serializer;
import org.lappsgrid.weblicht.Http;
import org.lappsgrid.weblicht.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import static org.lappsgrid.discriminator.Discriminators.*;

/**
 * @author Keith Suderman
 */
public abstract class AbstractWrapper implements WebService
{
	public static final String TCF = "text/tcf+xml";

	protected String metadata;
	protected String url;
	protected String format;

	private AbstractWrapper()
	{

	}

	protected AbstractWrapper(String url)
	{
		this(url, Uri.TCF);
	}

	protected AbstractWrapper(String url, String format)
	{
		this.url = url;
		this.format = format;
	}
//	public abstract String getUrl();

	public abstract ServiceMetadataBuilder configure(ServiceMetadataBuilder builder);

	/**
	 * The content-type this service will POST to the Weblicht service.
	 *
	 * @return
	 */
	public String contentType()
	{
		return TCF;
	}

	@Override
	public String execute(String input)
	{
		Data data = Serializer.parse(input, Data.class);
		String discriminator = data.getDiscriminator();
		if (Uri.ERROR.equals(discriminator)) {
			return input;
		}
		if (!this.format.equals(discriminator)) {
			data.setDiscriminator(Uri.ERROR);
			data.setPayload("Invalid discriminator type: " + discriminator);
			return data.asPrettyJson();
		}

		try
		{
//			String json = Serializer.toJson(data.getPayload());
//			System.out.println("POSTing");
//			System.out.println(json);
			Http.Response response = Http.post(url, contentType(), data.getPayload().toString());
			data.setPayload(response.getMessage());
			if (response.getStatus() < 300) {
				data.setDiscriminator(Uri.TCF);
			}
			else
			{
				data.setDiscriminator(Uri.ERROR);
			}
		}
		catch (IOException e)
		{
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			data.setDiscriminator(Uri.ERROR);
			data.setPayload(writer.toString());
		}
		return data.asPrettyJson();

		/*
		final String[] accept = { TCF };
		Data result = (Data) HttpBuilder.configure( config -> {
			HttpConfig.Request request = config.getRequest();
			request.setAccept(accept);
			request.setUri(url);
			request.setContentType(TCF);
			request.encoder(TCF, NativeHandlers.Encoders::xml);
		}).post(config -> {
			config.getRequest().setBody(data.getPayload());
			HttpConfig.Response response = config.getResponse();
			response.exception( throwable -> {
				System.out.println("HTTP POST: exception");
				throwable.printStackTrace();
				Data d = new Data();
				d.setDiscriminator(Uri.ERROR);
				d.setPayload(throwable.toString());
				return d;
			});
			response.failure((fromServer, object) -> {
				System.out.println("HTTP POST: failure");
				System.out.println(fromServer.getMessage());
				System.out.println("HEADERS");
				for (FromServer.Header header : fromServer.getHeaders())
				{
					System.out.println(header.getKey() + " = " + header.getValue());
				}
				Data d = new Data();
				d.setDiscriminator(Uri.ERROR);
				d.setPayload(object.toString());
				return d;
			});
			response.success((fromServer, object) -> {
				System.out.println("object is a " + object.getClass().getName());
				System.out.println("HTTP POST: success!");
				Data d = new Data();
				if (object == null && fromServer.getHasBody())
				{
					System.out.println("Reading body from server");
					d.setPayload(readString(fromServer.getReader()));
				}
				else if (object instanceof byte[])
				{
					System.out.println("Creating String from byte[]");
					d.setPayload(new String((byte[])object));
				}
				else
				{
					System.out.println("Setting payload to the object " + object.getClass().getName());
					d.setPayload(object);
				}
				d.setDiscriminator(Uri.TCF);
				return d;
			});
		});

		return result.asPrettyJson();
		*/
	}

	@Override
	public String getMetadata()
	{
		if (metadata == null) {
			//String licenseText = "Stanford NLP is released under the `GNU General Public License v3 (or later) <https://stanfordnlp.github.io/CoreNLP/#license>`_.";
			String licenseText = Uri.GPL3;
			ServiceMetadata md = configure(new ServiceMetadataBuilder())
					.allow(Uri.ALL)
					.vendor("http://weblicht.sfs.uni-tuebingen.de/")
					.version(Version.getVersion())
					.license(licenseText)
					.produceEncoding("UTF-8")
					.produceFormat(Uri.TCF)
					.produceLanguage("en")
					.requireEncoding("UTF-8")
					.requireLanguage("en")
					.requireFormat(this.format)
					.build();
			Data data = new Data();
			data.setDiscriminator(Uri.META);
			data.setPayload(md);
			metadata = data.asPrettyJson();
		}
		return metadata;
	}

	private String readString(Reader reader)
	{
		BufferedReader bufferedReader;
		if (reader instanceof BufferedReader) {
			bufferedReader = (BufferedReader) reader;
		}
		else {
			bufferedReader = new BufferedReader(reader);
		}
		StringBuilder builder = new StringBuilder();
		String line = null;
		try
		{
			line = bufferedReader.readLine();
			while (line != null)
			{
				builder.append(line);
				builder.append('\n');
				line = bufferedReader.readLine();
			}
			bufferedReader.close();;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bufferedReader.close();
			}
			catch (IOException e) { }
		}
		return builder.toString();
	}
}
