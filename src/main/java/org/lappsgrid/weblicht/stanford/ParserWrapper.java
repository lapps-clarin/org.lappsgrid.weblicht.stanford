package org.lappsgrid.weblicht.stanford;

import static org.lappsgrid.discriminator.Discriminators.*;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.weblicht.AbstractWrapper;

/**
 * @author Keith Suderman
 */
public class ParserWrapper extends AbstractWrapper
{
	public ParserWrapper()
	{
		super("http://weblicht.sfs.uni-tuebingen.de/rws/parsers/service-stanford/annotate/parse");
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
		builder.name("Stanford Parser (Weblicht)")
				.description("LAPPS Grid wrapper around the Weblicht Stanford Parser.")
				.produces(Uri.POS, Uri.CONSTITUENT, Uri.DEPENDENCY)
				.requires(Uri.TOKEN, Uri.SENTENCE);
		return builder;
	}
}
