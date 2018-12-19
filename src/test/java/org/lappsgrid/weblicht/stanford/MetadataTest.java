package org.lappsgrid.weblicht.stanford;

import org.junit.Test;
import static org.junit.Assert.*;

import org.lappsgrid.api.WebService;
import org.lappsgrid.metadata.IOSpecification;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.Serializer;
import org.lappsgrid.weblicht.Version;

import java.util.List;
import java.util.Map;

import static org.lappsgrid.discriminator.Discriminators.*;

/**
 * @author Keith Suderman
 */
public class MetadataTest
{
	public MetadataTest()
	{

	}

	@Test
	public void testTokenizerMetadata()
	{
		WebService service = new TokenizerWrapper();
		String json = service.getMetadata();
		Data<Map> data = Serializer.parse(json, Data.class);
		assertEquals(Uri.META, data.getDiscriminator());

		ServiceMetadata md = new ServiceMetadata(data.getPayload());
		assert Uri.ALL.equals(md.getAllow());
		assert "http://weblicht.sfs.uni-tuebingen.de/".equals(md.getVendor());
		assert Version.getVersion().equals(md.getVersion());
		IOSpecification io = md.getProduces();
		List<String> annotations = io.getAnnotations();
		assert 2 == annotations.size();
		assert annotations.contains(Uri.TOKEN);
		assert annotations.contains(Uri.SENTENCE);
		List<String> formats = io.getFormat();
		assert 1 == formats.size();
		assert Uri.TCF.equals(formats.get(0));
		List<String> langs = io.getLanguage();
		assert 1 == langs.size();
		assert "en".equals(langs.get(0));
		assert "UTF-8".equals(io.getEncoding());

		io = md.getRequires();
		formats = io.getFormat();
		assert 1 == formats.size();
		assert Uri.TCF.equals(formats.get(0));
		langs = io.getLanguage();
		assert 1 == langs.size();
		assert "en".equals(langs.get(0));
		assert "UTF-8".equals(io.getEncoding());
	}

	@Test
	public void testTaggerMetadata()
	{
		WebService service = new TaggerWrapper();
		String json = service.getMetadata();
		Data<Map> data = Serializer.parse(json, Data.class);
		assertEquals(Uri.META, data.getDiscriminator());

		ServiceMetadata md = new ServiceMetadata(data.getPayload());
		assert Uri.ALL.equals(md.getAllow());
		assert "http://weblicht.sfs.uni-tuebingen.de/".equals(md.getVendor());
		assert Version.getVersion().equals(md.getVersion());
		IOSpecification io = md.getProduces();
		List<String> annotations = io.getAnnotations();
		assert 1 == annotations.size();
		assert Uri.POS.equals(annotations.get(0));
		List<String> formats = io.getFormat();
		assert 1 == formats.size();
		assert Uri.TCF.equals(formats.get(0));
		List<String> langs = io.getLanguage();
		assert 1 == langs.size();
		assert "en".equals(langs.get(0));
		assert "UTF-8".equals(io.getEncoding());

		io = md.getRequires();
		annotations = io.getAnnotations();
		assert 2 == annotations.size();
		assert annotations.contains(Uri.TOKEN);
		assert annotations.contains(Uri.SENTENCE);
		formats = io.getFormat();
		assert 1 == formats.size();
		assert Uri.TCF.equals(formats.get(0));
		langs = io.getLanguage();
		assert 1 == langs.size();
		assert "en".equals(langs.get(0));
		assert "UTF-8".equals(io.getEncoding());
	}

	@Test
	public void testParserMetadata()
	{
		WebService service = new ParserWrapper();
		String json = service.getMetadata();
		Data<Map> data = Serializer.parse(json, Data.class);
		assertEquals(Uri.META, data.getDiscriminator());

		ServiceMetadata md = new ServiceMetadata(data.getPayload());

		IOSpecification requires = md.getRequires();
		List<String> annotations = requires.getAnnotations();
		assert 2 == annotations.size();
		assert annotations.contains(Uri.TOKEN);
		assert annotations.contains(Uri.SENTENCE);

		annotations = md.getProduces().getAnnotations();
		assert 3 == annotations.size();
		assert annotations.contains(Uri.POS);
		assert annotations.contains(Uri.CONSTITUENT);
		assert annotations.contains(Uri.DEPENDENCY);
	}

	@Test
	public void testNamedEntityRecognizer()
	{
		WebService service = new NameEntityRecognizerWrapper();
		String json = service.getMetadata();
		Data<Map> data = Serializer.parse(json, Data.class);
		assertEquals(Uri.META, data.getDiscriminator());

		ServiceMetadata md = new ServiceMetadata(data.getPayload());

		IOSpecification requires = md.getRequires();
		List<String> annotations = requires.getAnnotations();
		assert 2 == annotations.size();
		assert annotations.contains(Uri.TOKEN);
		assert annotations.contains(Uri.SENTENCE);

		annotations = md.getProduces().getAnnotations();
		assert 1 == annotations.size();
		assert annotations.contains(Uri.NE);
	}
}
