package org.lappsgrid.weblicht.stanford;

import org.lappsgrid.metadata.ServiceMetadataBuilder;
import org.lappsgrid.weblicht.AbstractWrapper;

import static org.lappsgrid.discriminator.Discriminators.*;

/**
 * @author Keith Suderman
 */
public class NameEntityRecognizerWrapper extends AbstractWrapper
{
	public NameEntityRecognizerWrapper()
	{
		super("http://weblicht.sfs.uni-tuebingen.de/rws/service-stanford-ner/annotate");
	}

	@Override
	public ServiceMetadataBuilder configure(ServiceMetadataBuilder builder)
	{
		builder.name("Stanford Named Entity Recognizer (Weblicht)")
				.description("LAPPS Grid wrapper around the Weblicht Stanford Entity Recognizer.")
				.produce(Uri.NE)
				.requires(Uri.TOKEN, Uri.SENTENCE);
		return builder;
	}
}
