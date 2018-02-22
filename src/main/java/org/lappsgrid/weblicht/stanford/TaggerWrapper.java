package org.lappsgrid.weblicht.stanford;

import static org.lappsgrid.discriminator.Discriminators.*;
import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.weblicht.AbstractWrapper;

/**
 * @author Keith Suderman
 */
public class TaggerWrapper extends AbstractWrapper
{
	public TaggerWrapper()
	{
		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-stanford-tagger/annotate/stream?model=left3words");
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
		builder.name("Stanford Tagger (Weblicht)")
				.description("LAPPS Grid wrapper around the Weblicht Stanford POS Tagger.")
				.produce(Uri.POS)
				.requires(Uri.TOKEN, Uri.SENTENCE);
		return builder;
	}
}
