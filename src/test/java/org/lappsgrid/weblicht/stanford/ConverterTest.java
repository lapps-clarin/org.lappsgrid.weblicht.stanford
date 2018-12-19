package org.lappsgrid.weblicht.stanford;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lappsgrid.api.WebService;
import org.lappsgrid.weblicht.Http;
import org.lappsgrid.weblicht.HttpTest;
import org.lappsgrid.weblicht.convert.Lif2TcfWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 *
 */
@Ignore
public class ConverterTest
{
	WebService converter;

	@Before
	public void setup()
	{
		converter = new Lif2TcfWrapper();
	}

	@After
	public void teardown()
	{
		converter = null;
	}

	@Test
	public void karen()
	{
		System.out.println("ConverterTest.karen");
		InputStream stream = this.getClass().getResourceAsStream("/karen.lif");
		assert null != stream;
		String lif = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
		System.out.println(lif);
		String result = converter.execute(lif);
		System.out.println(result);
	}

	@Test
	public void lif2tcf()
	{
		System.out.println("ConverterTest.lif2tcf");
		InputStream stream = this.getClass().getResourceAsStream("/LIF2TCF.lif");
		assert null != stream;
		String lif = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
		String result = converter.execute(lif);
		System.out.println(result);
	}

	@Test
	public void stanfordPos()
	{
		System.out.println("ConverterTest.stanfordPos");
		InputStream stream = this.getClass().getResourceAsStream("/stanford-pos.json");
		assert null != stream;
		String lif = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
		System.out.println("Sending");
		System.out.println(lif);
		String result = converter.execute(lif);
		System.out.println(result);
	}

	@Test
	public void httpTest() throws IOException
	{
		System.out.println("ConverterTest.httpTest");
		String uri = "http://weblicht.sfs.uni-tuebingen.de/rws/service-lapps-converter/con/stream";
		InputStream stream = this.getClass().getResourceAsStream("/stanford-pos.json");
		assert null != stream;
		String lif = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
		HttpTest.Response response = HttpTest.post(uri, lif);
		System.out.println(response.getStatus());
		System.out.println(response.getMessage());
	}

	@Test
	public void httpPost() throws IOException
	{
		System.out.println("ConverterTest.httpPost");
		String uri = "http://weblicht.sfs.uni-tuebingen.de/rws/service-lapps-converter/con/stream";
		InputStream stream = this.getClass().getResourceAsStream("/stanford-pos.json");
		assert null != stream;
		String lif = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
		Http.Response response = Http.post(uri, Http.TCF, lif);
		System.out.println(response.getStatus());
		System.out.println(response.getMessage());
	}
}
