package org.lappsgrid.weblicht;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTest
{
    public static final String TCF = "text/tcf+xml";
    public static final String JSON_MIME_TYPE = "application/json";

    public static Response post(String uri, String body) throws IOException
    {
        System.out.println(body);
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", TCF);
        connection.setRequestProperty("Content-type", JSON_MIME_TYPE);
        connection.setDoOutput(true);
        OutputStream ostream = connection.getOutputStream();
        PrintStream out = new PrintStream(connection.getOutputStream());
        out.print(body);
        out.close();

        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        int status = connection.getResponseCode();
        String message;
        if (status >= 200 && status < 300)
        {
            message = read(connection.getInputStream());
        }
        else if (status >= 400)
        {
            message = read(connection.getErrorStream());
        }
        else
        {
            message = String.format("Unexcected status code: " + status);
            status = 500;
        }
        return new Response(status, message);
    }

    protected static String read(InputStream in) throws IOException
    {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static class Response {
        private int status;
        private String message;

        public Response(int status, String message)
        {
            this.status = status;
            this.message = message;
        }

        public int getStatus()
        {
            return status;
        }

        public String getMessage()
        {
            return message;
        }
    }


    public static void main(String[] args) throws Exception {
        String uri = "http://weblicht.sfs.uni-tuebingen.de/rws/service-lapps-converter/con/stream";
//        FileInputStream jsonfilein = new FileInputStream("lapps-test.json");
        FileInputStream jsonfilein = new FileInputStream("src/test/resources/stanford-pos.json");
        String jsonContent = read(jsonfilein);
        Response r = HttpTest.post(uri, jsonContent);
        System.out.println(r.message);
    }
}
