package calculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class StockPrice implements StockService {

    public String getPrice(String symbol) {
        String results = requestStockPrice(symbol);
        return parseStockPrice(results);
    }

    public String requestStockPrice(String symbol) {
        String inputLine;
        try{
            URLConnection yahooConnection = makeConnection(symbol);
            BufferedReader in = new BufferedReader(
                                new InputStreamReader(yahooConnection.getInputStream()));

            in.readLine();
            inputLine = in.readLine();
            in.close();

            return inputLine;
        }
        catch(Exception e){
            if(e instanceof FileNotFoundException)
                return "Invalid Symbol";
            else if (e instanceof UnknownHostException)
                return "Network Error";
            else
                return "Unknown Error Occurred";
        }
    }

    public URLConnection makeConnection(String symbol) throws IOException{
        URL yahoo = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol);
        return yahoo.openConnection();
    }

    public String parseStockPrice(String stockPriceInfo) {
        String returnPrice;

        if(stockPriceInfo.equals("Invalid Symbol") || stockPriceInfo.equals("Network Error") || stockPriceInfo.equals("Unknown Error Occurred"))
            returnPrice = stockPriceInfo;
        else{
            String[] stockPrices =  stockPriceInfo.split(",");
            returnPrice = stockPrices[4];
        }
        return returnPrice;
    }

}