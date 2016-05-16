package calculator.ui;

import calculator.AssetValue;
import calculator.StockPrice;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StockRun {
    public static void main(String[] args) {
        System.out.printf("\n%s%15s%25s", "Symbol", "Shares", "Net Asset Value");
        System.out.println("\n----------------------------------------------");
        try{

            String inputLine;
            Map<String, Integer> stocks = new HashMap<>();
            Map<String, String> errors = new HashMap<>();
            Scanner fileInput = new Scanner(new File("./src/main/resources/SampleRunStockInfo.txt"));

            while(fileInput.hasNextLine()) {
                inputLine = fileInput.nextLine();
                try {
                    String words[] = inputLine.split(" ", 2);
                    stocks.put(words[0], Integer.parseInt(words[1]));
                }
                catch(Exception e){
                    errors.put(inputLine, "Invalid input from file");
                }
            }

            AssetValue assetValue = new AssetValue();
            StockPrice stockPrice = new StockPrice();
            assetValue.setStockService(stockPrice);

            Map<String, String> results = assetValue.fetchStockValues(stocks);
            String totalAssets = results.get("TotalAssets");
            results.remove("TotalAssets");
            for(Map.Entry<String, String> entry : results.entrySet()){
                try{
                    String symbol = entry.getKey();
                    double assetWorth = Double.parseDouble(entry.getValue());
                    System.out.printf("%4s%16d%15s%.2f\n", symbol, stocks.get(symbol), "$", assetWorth);

                }catch (Exception e){
                    errors.put(entry.getKey(), entry.getValue());
                }
            }
            System.out.printf("\nTotal Assets: $%.2f\n", Double.parseDouble(totalAssets));

            System.out.println("\nErrors:");
            for(Map.Entry<String, String> entry : errors.entrySet()){
                System.out.printf("%-20s%s\n", entry.getKey(), entry.getValue());
            }
        }
        catch(Exception e) {
            System.out.println("Unable to open input file");
        }
    }
}