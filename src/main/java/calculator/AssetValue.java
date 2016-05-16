package calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssetValue {

    private StockService stockService;

    public double calculateNetAssetValue(double price, int quantity) {
        return (price > 0 && quantity > 0) ? price * quantity : 0;
    }

    public double totalNetAssets(ArrayList<Double> assets) {
        return assets.stream()
                     .reduce(0.0, Double::sum);
    }

    public Map<String, String> fetchStockValues(Map<String, Integer> ownedShares) {

        Map<String, String> assetValues = new HashMap<>();
        ArrayList<Double> valuesToBeTotaled = new ArrayList<>();

        if(!ownedShares.isEmpty()){

            for(Map.Entry<String, Integer> entry : ownedShares.entrySet()){
                String symbol = entry.getKey();
                int quantity = entry.getValue();
                String price = stockService.getPrice(symbol);

                try {
                    double assetValue = Double.parseDouble(price) * quantity;
                    valuesToBeTotaled.add(assetValue);
                    assetValues.put(symbol, String.valueOf(assetValue));
                }
                catch (Exception e){
                    assetValues.put(symbol, price);
                }
            }
        }
        double totalAssets = totalNetAssets(valuesToBeTotaled);
        assetValues.put("TotalAssets", String.valueOf(totalAssets));
        return assetValues;
    }

    public void setStockService(StockService service) {
        stockService = service;
    }

}