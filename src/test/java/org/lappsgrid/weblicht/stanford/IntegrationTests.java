package org.lappsgrid.weblicht.stanford;

import org.anc.io.UTF8Reader;
import org.junit.Ignore;
import org.junit.Test;
import org.lappsgrid.api.WebService;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.Serializer;

import static org.lappsgrid.discriminator.Discriminators.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Keith Suderman
 */
@Ignore
public class IntegrationTests
{
	public IntegrationTests()
	{

	}

	@Test
	public void tokenizerTest() throws IOException
	{
		WebService tokenizer = new TokenizerWrapper();

		ClassLoader loader = this.getClass().getClassLoader();
		InputStream istream = loader.getResourceAsStream("karen.tcf");
		assert null != istream;
		UTF8Reader reader = new UTF8Reader(istream);
		String tcf = reader.readString();

		assert null != tcf;

		Data data = new Data();
		data.setDiscriminator(Uri.TCF);
		data.setPayload(tcf);

		String json = tokenizer.execute(data.asJson());

		data = Serializer.parse(json, Data.class);
		assert null != data;
		if (Uri.ERROR.equals(data.getDiscriminator()))
		{
			System.out.println(data.getPayload());
			assert false;
		}
		assert !Uri.ERROR.equals(data.getDiscriminator());
		assert Uri.TCF.equals(data.getDiscriminator());
		System.out.println(data.getPayload().toString());
	}

	@Test
	public void taggerTest() throws IOException
	{
		WebService tagger = new TaggerWrapper();

		ClassLoader loader = this.getClass().getClassLoader();
		InputStream istream = loader.getResourceAsStream("karen-tokens.tcf");
		assert null != istream;
		UTF8Reader reader = new UTF8Reader(istream);
		String tcf = reader.readString();

		assert null != tcf;

		Data data = new Data();
		data.setDiscriminator(Uri.TCF);
		data.setPayload(tcf);

		String json = tagger.execute(data.asJson());

		data = Serializer.parse(json, Data.class);
		assert null != data;
		if (Uri.ERROR.equals(data.getDiscriminator()))
		{
			System.out.println(data.getPayload());
			assert false;
		}
		assert !Uri.ERROR.equals(data.getDiscriminator());
		assert Uri.TCF.equals(data.getDiscriminator());
		System.out.println(data.getPayload().toString());
	}

	@Test
	public void parserTest() throws IOException
	{
		WebService parser = new ParserWrapper();

		ClassLoader loader = this.getClass().getClassLoader();
		InputStream istream = loader.getResourceAsStream("karen-tokens.tcf");
		assert null != istream;
		UTF8Reader reader = new UTF8Reader(istream);
		String tcf = reader.readString();

		assert null != tcf;

		Data data = new Data();
		data.setDiscriminator(Uri.TCF);
		data.setPayload(tcf);

		String json = parser.execute(data.asJson());

		data = Serializer.parse(json, Data.class);
		assert null != data;
		if (Uri.ERROR.equals(data.getDiscriminator()))
		{
			System.out.println(data.getPayload());
			assert false;
		}
		assert !Uri.ERROR.equals(data.getDiscriminator());
		assert Uri.TCF.equals(data.getDiscriminator());
		System.out.println(data.getPayload().toString());
	}

	@Test
	public void namedEntityRecognizerTest() throws IOException
	{
		WebService ner = new NameEntityRecognizerWrapper();

		ClassLoader loader = this.getClass().getClassLoader();
		InputStream istream = loader.getResourceAsStream("karen-tokens.tcf");
		assert null != istream;
		UTF8Reader reader = new UTF8Reader(istream);
		String tcf = reader.readString();

		assert null != tcf;

		Data data = new Data();
		data.setDiscriminator(Uri.TCF);
		data.setPayload(tcf);

		String json = ner.execute(data.asJson());

		data = Serializer.parse(json, Data.class);
		assert null != data;
		if (Uri.ERROR.equals(data.getDiscriminator()))
		{
			System.out.println(data.getPayload());
			assert false;
		}
		assert !Uri.ERROR.equals(data.getDiscriminator());
		assert Uri.TCF.equals(data.getDiscriminator());

		System.out.println("Printing Result");
		System.out.println(data.asPrettyJson());
	}

}
