package calculator;

import junit.framework.TestCase;
import org.junit.Before;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class AssetValueTest extends TestCase {
    public AssetValue assetCalculator;
    double DELTA = 0.05;

    @Before
    public void setUp() {
        assetCalculator = new AssetValue();
    }

    public void testCanary() {
         assertTrue(true);
    }

    public void testCalculateNetAssetValueForValidQuantityAndValidPrice() {
        int quantity = 500;
        double price = 3;
        assertEquals(1500.00, assetCalculator.calculateNetAssetValue(price, quantity), DELTA);
    }

    public void testCalculateNetAssetValueForInvalidQuantityAndValidPrice() {
        int quantity = -500;
        int price = 3;
        assertEquals(0.0, assetCalculator.calculateNetAssetValue(price, quantity), DELTA);
    }

    public void testCalculateNetAssetValueForValidQuantityAndInvalidPrice() {
        int quantity = 500;
        int price = -3;
        assertEquals(0.0, assetCalculator.calculateNetAssetValue(price, quantity), DELTA);
    }

    public void testCalculateNetAssetValueForInvalidQuantityAndInvalidPrice() {
        int quantity = 0;
        int price = 0;
        assertEquals(0.0, assetCalculator.calculateNetAssetValue(price, quantity), DELTA);
    }

    public void testTotalNetAssetsWithZeroAssets() {
        ArrayList<Double> assets = new ArrayList<>();
        assertEquals(0, assetCalculator.totalNetAssets(assets), DELTA);
    }

    public void testTotalNetAssetsWithTwoAssets() {
        ArrayList<Double> assets = new ArrayList<>(Arrays.asList(100.00, 200.00));
        assertEquals(300.00, assetCalculator.totalNetAssets(assets), DELTA);
    }

    public void testTotalNetAssetsWithFiveAssets() {
        ArrayList<Double> assets = new ArrayList<>(Arrays.asList(100.00, 200.00, 300.0, 400.0, 500.00));
        assertEquals(1500.00, assetCalculator.totalNetAssets(assets), DELTA);
    }

    public void testFetchStockValuesForZeroSymbols () {
        Map<String, Integer> ownedShares = new HashMap<>();
        Map<String, String> returnedAssets = new HashMap<>();
        returnedAssets.put("TotalAssets", "0.0");

        StockService stockService = Mockito.mock(StockService.class);
        assetCalculator.setStockService(stockService);

        assertEquals(returnedAssets, assetCalculator.fetchStockValues(ownedShares));
    }

    public void testFetchStockValuesForOneSymbol () {
        StockService stockService = Mockito.mock(StockService.class);
        when(stockService.getPrice("GOOG")).thenReturn("1.0");

        assetCalculator.setStockService(stockService);

        Map<String, Integer> ownedShares = new HashMap<>();
        ownedShares.put("GOOG", 1000);

        Map<String, String> returnedAssets = new HashMap<>();
        returnedAssets.put("GOOG", "1000.0");
        returnedAssets.put("TotalAssets", "1000.0");

        assertEquals(returnedAssets, assetCalculator.fetchStockValues(ownedShares));
    }

    public void testFetchStockValuesForThreeSymbols () {
        StockService stockService = Mockito.mock(StockService.class);
        when(stockService.getPrice("GOOG")).thenReturn("1.0");
        when(stockService.getPrice("IBM")).thenReturn("2.0");
        when(stockService.getPrice("JPM")).thenReturn("3.0");

        assetCalculator.setStockService(stockService);

        Map<String, Integer> ownedShares = new HashMap<>();
        ownedShares.put("GOOG", 1000);
        ownedShares.put("IBM", 2000);
        ownedShares.put("JPM", 3000);

        Map<String, String> returnedAssets = new HashMap<>();
        returnedAssets.put("GOOG", "1000.0");
        returnedAssets.put("IBM", "4000.0");
        returnedAssets.put("JPM", "9000.0");
        returnedAssets.put("TotalAssets", "14000.0");

        assertEquals(returnedAssets, assetCalculator.fetchStockValues(ownedShares));
    }

    public void testFetchStockValuesForTwoSymbolsWhereOneIsInvalid () {
        StockService stockService = Mockito.mock(StockService.class);
        when(stockService.getPrice("GOOG")).thenReturn("1.0");
        when(stockService.getPrice("AFDS")).thenReturn("Invalid Symbol");
        assetCalculator.setStockService(stockService);

        Map<String, Integer> ownedShares = new HashMap<>();
        ownedShares.put("GOOG", 1000);
        ownedShares.put("AFDS", 2000);

        Map<String, String> returnedAssets = new HashMap<>();
        returnedAssets.put("GOOG", "1000.0");
        returnedAssets.put("AFDS", "Invalid Symbol");
        returnedAssets.put("TotalAssets", "1000.0");

        assertEquals(returnedAssets, assetCalculator.fetchStockValues(ownedShares));
    }
}
