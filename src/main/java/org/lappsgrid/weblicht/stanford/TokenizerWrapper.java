package org.lappsgrid.weblicht.stanford;

import static org.lappsgrid.discriminator.Discriminators.*;
import org.lappsgrid.metadata.ServiceMetadataBuilder;

/**
 * @author Keith Suderman
 */
public class TokenizerWrapper extends AbstractWrapper
{
	public TokenizerWrapper()
	{
		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-stanford-tokenizer/annotate/stream");
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
//		String[] types = { Uri.TOKEN, Uri.SENTENCE };
		System.out.println("Tokenizer: configuring builder");
		builder.name("Weblicht Stanford Tokenizer")
				.produces(Uri.TOKEN, Uri.SENTENCE)
				.description("LAPPS Grid wrapper around the Weblicht Stanford Tokenizer.");
		return builder;
	}
}
