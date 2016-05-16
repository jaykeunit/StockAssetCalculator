package calculator;

import junit.framework.TestCase;
import org.junit.Before;;
import org.mockito.Mockito;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import static org.mockito.Mockito.when;

public class StockPriceTest extends TestCase {

    public StockPrice stockPrice;

    @Before
    public void setUp() {
        stockPrice = new StockPrice();
    }

    public void testGetPriceWithInvalidRequest() {
        StockPrice stockPrice = Mockito.mock(StockPrice.class);
        when(stockPrice.requestStockPrice("ABCD")).thenReturn("Invalid Symbol");
        when(stockPrice.parseStockPrice("Invalid Symbol")).thenCallRealMethod();
        when(stockPrice.getPrice("ABCD")).thenCallRealMethod();

        assertEquals("Invalid Symbol", stockPrice.getPrice("ABCD"));
    }

    public void testGetPriceWithValidRequest() {
        StockPrice stockPrice = Mockito.mock(StockPrice.class);
        when(stockPrice.requestStockPrice("JPM")).thenReturn("2015-10-12,100.0041,555.48,987.63245,200.5149,400501,200.1563");
        when(stockPrice.parseStockPrice("2015-10-12,100.0041,555.48,987.63245,200.5149,400501,200.1563")).thenCallRealMethod();
        when(stockPrice.getPrice("JPM")).thenCallRealMethod();

        assertEquals("200.5149", stockPrice.getPrice("JPM"));
    }

    public void testParseStockPriceForValidString() {
        assertEquals("646.669983", stockPrice.parseStockPrice("2015-10-12,642.090027,648.50,639.01001,646.669983,1275200,646.669983"));
    }

    public void testParseStockPriceForInvalidSymbol() {
        assertEquals("Invalid Symbol", stockPrice.parseStockPrice("Invalid Symbol"));
    }

    public void testParseStockPriceForNetworkError() {
        assertEquals("Network Error", stockPrice.parseStockPrice("Network Error"));
    }

    public void testParseStockPriceForUnknownError() {
        assertEquals("Unknown Error Occurred", stockPrice.parseStockPrice("Unknown Error Occurred"));
    }

    public void testRequestStockPriceWithNoNetworkConnection() {
        try{
            StockPrice stockPrice = Mockito.mock(StockPrice.class);
            when(stockPrice.makeConnection("GOOG")).thenThrow(new UnknownHostException());
            when(stockPrice.requestStockPrice("GOOG")).thenCallRealMethod();
            when(stockPrice.parseStockPrice("Network Error")).thenCallRealMethod();
            when(stockPrice.getPrice("GOOG")).thenCallRealMethod();

            assertEquals("Network Error", stockPrice.getPrice("GOOG"));
        }
        catch(Exception e){
            assertTrue(false);
        }
    }

    public void testRequestStockPriceWithInvalidURL() {
        try{
            StockPrice stockPrice = Mockito.mock(StockPrice.class);
            when(stockPrice.makeConnection("GOOG")).thenThrow(new FileNotFoundException());
            when(stockPrice.requestStockPrice("GOOG")).thenCallRealMethod();
            when(stockPrice.parseStockPrice("Invalid Symbol")).thenCallRealMethod();
            when(stockPrice.getPrice("GOOG")).thenCallRealMethod();

            assertEquals("Invalid Symbol", stockPrice.getPrice("GOOG"));
        }
        catch(Exception e){
            assertTrue(false);
        }
    }

    public void testRequestStockPrice() {
        try{
            StockPrice stockPrice = Mockito.mock(StockPrice.class);
            when(stockPrice.makeConnection("GOOG")).thenThrow(new IOException());
            when(stockPrice.requestStockPrice("GOOG")).thenCallRealMethod();
            when(stockPrice.parseStockPrice("Unknown Error Occurred")).thenCallRealMethod();
            when(stockPrice.getPrice("GOOG")).thenCallRealMethod();

            assertEquals("Unknown Error Occurred", stockPrice.getPrice("GOOG"));
        }
        catch(Exception e){
            assertTrue(false);
        }
    }

    public void testRequestStockPriceGetInputLine() {
        try {
            String inputStream = "Opening, Closing\n3005-10-12,100.0041,555.48,987.63245,200.5149,400501,200.1563\n";
            URLConnection url = Mockito.mock(URLConnection.class);
            StockPrice stockPrice = Mockito.mock(StockPrice.class);
            when(stockPrice.makeConnection("JPM")).thenReturn(url);
            when(stockPrice.requestStockPrice("JPM")).thenCallRealMethod();
            when(url.getInputStream()).thenReturn(new ByteArrayInputStream(inputStream.getBytes()));
            assertEquals("3005-10-12,100.0041,555.48,987.63245,200.5149,400501,200.1563", stockPrice.requestStockPrice("JPM"));
        }
        catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testMakeConnection () {
        try {
            URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=GOOG");
            assertEquals(url.openConnection().toString(),stockPrice.makeConnection("GOOG").toString());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}