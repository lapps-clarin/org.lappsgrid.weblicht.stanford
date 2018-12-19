package org.lappsgrid.weblicht.convert;

import org.lappsgrid.discriminator.Discriminators;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.weblicht.AbstractWrapper;
import org.lappsgrid.weblicht.Http;

import java.io.IOException;

/**
 *
 */
public class Lif2TcfWrapper extends AbstractWrapper
{
	public Lif2TcfWrapper()
	{
//		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-lapps-converter/con/stream", Discriminators.Uri.LIF);
		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-lapps-converter/con/stream", Discriminators.Uri.LIF);
	}

	@Override
	public String contentType()
	{
		return "application/json";
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
		builder.name("LIF to TCF Converter (Weblicht)")
				.description("LAPPS Grid wrapper around the Weblicht converter service.");
		return builder;
	}

	@Override
	public String execute(String input) {
		Data data = null;
		try
		{
			Http.Response response = Http.post(this.url, contentType(), input);
			if (response.getStatus() < 300) {
				data = new Data(Discriminators.Uri.TCF, response.getMessage());
			}
			else {
				data = new Data(Discriminators.Uri.ERROR, response.getMessage());
			}
		}
		catch (IOException e)
		{
			data = new Data(Discriminators.Uri.ERROR, e.getMessage());
		}
		return data.asPrettyJson();
	}
}
