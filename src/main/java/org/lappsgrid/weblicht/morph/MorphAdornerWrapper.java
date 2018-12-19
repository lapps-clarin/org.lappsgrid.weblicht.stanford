package org.lappsgrid.weblicht.morph;

import static org.lappsgrid.discriminator.Discriminators.*;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.weblicht.AbstractWrapper;

/**
 *
 */
public class MorphAdornerWrapper extends AbstractWrapper
{
	public MorphAdornerWrapper()
	{
		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-morphadorner/annotate/morph/stream");
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
		builder.name("Weblicht MorphAdorner")
				.requires(Uri.TOKEN, Uri.SENTENCE)
				.produces("http://vocab.lappsgrid.org/Morphology")
				.description("LAPPS Grid wrapper around the Weblicht Stanford Tokenizer.");
		return builder;
	}
}
