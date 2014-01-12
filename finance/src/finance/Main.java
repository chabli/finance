package finance;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Main {

	public static void main(String[] args) {
		try {
			URL url = new URL(
					"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22LBON.PA%22)%20&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");
			// connection.setRequestProperty("Content-Type","application/json");
			// connection.setRequestProperty("Content-Length", "" +
			// Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			// wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			String jsonContent = response.toString();

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonContent);

			JSONObject queryJSONObject = (JSONObject) obj;
			Object queryObj = queryJSONObject.get("query");

			JSONObject resultsJSONObject = (JSONObject) queryObj;
			Object resultsObj = resultsJSONObject.get("results");

			JSONObject quoteJSONObject = (JSONObject) resultsObj;
			Object quoteObj = quoteJSONObject.get("quote");

			JSONObject MarketCapitalizationJSONObject = (JSONObject) quoteObj;

			// Mapping
			String marketCapitalization = MarketCapitalizationJSONObject.get(
					"MarketCapitalization").toString();
			String yearLow = MarketCapitalizationJSONObject.get("YearLow")
					.toString();
			String change = MarketCapitalizationJSONObject.get("Change")
					.toString();
			String volume = MarketCapitalizationJSONObject.get("Volume")
					.toString();
			String daysHigh = MarketCapitalizationJSONObject.get("DaysHigh")
					.toString();
			String daysRange = MarketCapitalizationJSONObject.get("DaysRange")
					.toString();
			String averageDailyVolume = MarketCapitalizationJSONObject.get(
					"AverageDailyVolume").toString();
			String name = MarketCapitalizationJSONObject.get("Name").toString();
			String yearHigh = MarketCapitalizationJSONObject.get("YearHigh")
					.toString();
			String stockExchange = MarketCapitalizationJSONObject.get(
					"StockExchange").toString();
			String symbol = MarketCapitalizationJSONObject.get("Symbol")
					.toString();
			String daysLow = MarketCapitalizationJSONObject.get("DaysLow")
					.toString();
			// TODO : DAO + Factory

			// System.out.println(marketCapitalization);
			// System.out.println(daysLow);

			MongoClient mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("TOTO");
			BasicDBObject doc = new BasicDBObject("name", "MongoDB")
					.append("type", "database")
					.append("count", 1)
					.append("info",
							new BasicDBObject("x", 203).append("y", 102));

			DBCollection coll = db.getCollection("testCollection");;
			coll.insert(doc);

			List<String> dbs = mongo.getDatabaseNames();
			for (String dbStr : dbs) {
				System.out.println(dbStr);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
