package net.atos.ol.listaccounts;

import java.util.HashMap;

public class TestData {

    private HashMap<String,String> responses = new HashMap();


    public TestData()
    {
        super();
        responses.put(
                "1","{\n" + "goalList : [\n" +
                    "{\n" +
                    "\"accountNumber\" : \"12345678\",\n" +
                    "\"authorityType\" : \"001\",\n" +
                    "\"availableBalance\" : 904.0,\n" +
                    "\"bankProduct\": \"0901\",\n" +
                    "\"bankProductWording\": \"Premium Bonds\",\n" +
                    "\"cashAccountClientWording\": \"My remium Bonds\",\n" +
                    "\"currentBalance\": 904.0,\n" +
                    "\"isAccountReadOnly\": false,\n" +
                    "\"isExistNewAccountStatement\": false,\n" +
                    "\"isSavingAccount\": true,\n" +
                    "\"isSightAccount\": false,\n" +
                    "\"valuationCurrency\": \"GBP\",\n" +
                    "\"orderNumber\": 0\n" +
                    "}\n" +
                    "]\n" +
                "   }\n");

        responses.put(
                "2","{\n" + "goalList : [\n" +
                        "{\n" +
                        "\"accountNumber\" : \"12345678\",\n" +
                        "\"authorityType\" : \"001\",\n" +
                        "\"availableBalance\" : 904.0,\n" +
                        "\"bankProduct\": \"0901\",\n" +
                        "\"bankProductWording\": \"Premium Bonds\",\n" +
                        "\"cashAccountClientWording\": \"My remium Bonds\",\n" +
                        "\"currentBalance\": 904.0,\n" +
                        "\"isAccountReadOnly\": false,\n" +
                        "\"isExistNewAccountStatement\": false,\n" +
                        "\"isSavingAccount\": true,\n" +
                        "\"isSightAccount\": false,\n" +
                        "\"valuationCurrency\": \"GBP\",\n" +
                        "\"orderNumber\": 0\n" +
                        "}\n" +
                        "," +
                        "{\n" +
                        "\"accountNumber\" : \"23456789\",\n" +
                        "\"authorityType\" : \"001\",\n" +
                        "\"availableBalance\" : 1234.34,\n" +
                        "\"bankProduct\": \"0901\",\n" +
                        "\"bankProductWording\": \"Premium Bonds\",\n" +
                        "\"cashAccountClientWording\": \"My Premium Bonds\",\n" +
                        "\"currentBalance\": 123.24,\n" +
                        "\"isAccountReadOnly\": false,\n" +
                        "\"isExistNewAccountStatement\": false,\n" +
                        "\"isSavingAccount\": true,\n" +
                        "\"isSightAccount\": false,\n" +
                        "\"valuationCurrency\": \"GBP\",\n" +
                        "\"orderNumber\": 0\n" +
                        "}\n" +
                        "]\n" +
                        "}\n");
    }

    public String getData(String index)
    {
        return responses.get(index);
    }
}
