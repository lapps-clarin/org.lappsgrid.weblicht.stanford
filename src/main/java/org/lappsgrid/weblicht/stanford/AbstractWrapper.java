package org.lappsgrid.weblicht.stanford;

import groovyx.net.http.FromServer;
import groovyx.net.http.HttpBuilder;
import groovyx.net.http.HttpConfig;
import groovyx.net.http.NativeHandlers;
import org.lappsgrid.api.WebService;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.stream.Collectors;

import static org.lappsgrid.discriminator.Discriminators.*;

/**
 * @author Keith Suderman
 */
public abstract class AbstractWrapper implements WebService
{
	public static final String TCF = "text/tcf+xml";

	private String metadata;
	private String url;

	private AbstractWrapper()
	{

	}

	protected AbstractWrapper(String url)
	{
		this.url = url;
	}

//	public abstract String getUrl();

	public abstract ServiceMetadataBuilder configure(ServiceMetadataBuilder builder);

	@Override
	public String execute(String input)
	{
		Data data = Serializer.parse(input, Data.class);
		String discriminator = data.getDiscriminator();
		if (Uri.ERROR.equals(discriminator)) {
			return input;
		}
		if (!Uri.TCF.equals(discriminator)) {
			data.setDiscriminator(Uri.ERROR);
			data.setPayload("Invalid discriminator type: " + discriminator);
			return data.asPrettyJson();
		}

//		Http http = new Http();
//		return http.post(url, data.getPayload().toString());

		try
		{
			Http.Response response = Http.post(url, data.getPayload().toString());
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
			String licenseText = "Stanford NLP is released under the `GNU General Public License <https://stanfordnlp.github.io/CoreNLP/#license>`_.";
			ServiceMetadata md = configure(new ServiceMetadataBuilder())
					.allow(Uri.ALL)
					.vendor("http://weblicht.sfs.uni-tuebingen.de/")
					.version(Version.getVersion())
					.license(licenseText)
					.produceEncoding("UTF-8")
					.produceFormat(Uri.TCF)
					.produceLanguage("en")
					.requireEncoding("UTF-8")
					.requireFormat(Uri.TCF)
					.requireLanguage("en")
					.build();
			metadata = Serializer.toPrettyJson(md);
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
