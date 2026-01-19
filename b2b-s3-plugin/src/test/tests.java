package test;

import java.util.HashMap;
import java.util.Map;

import com.axway.gps.PluggableS3Transport;
import com.cyclonecommerce.tradingengine.transport.TransportInitializationException;
import com.cyclonecommerce.tradingengine.transport.UnableToConnectException;
import com.cyclonecommerce.tradingengine.transport.UnableToConsumeException;
import com.cyclonecommerce.tradingengine.transport.pluggable.PluggableSettingsImpl;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableClient;

public class tests {


    public static void main(String[] args) throws TransportInitializationException {
        Map<String, String> constantSettings = new HashMap<String, String>();
        Map<String, String> settings = new HashMap<String, String>();

        //default settings
        constantSettings.put("Exchange Type","pickup");
        settings.put("Access Key", "<Access Key>");
        settings.put("Secret Key", "<Secret Key>");
        settings.put("AWS Region", "eu-central-1");
        settings.put("Bucket", "tln-s3-mft-dvp");
        settings.put("Folder", "/");
        settings.put("Filter Type", "glob");
        settings.put("Filter", "*");
        settings.put("ACL", "private");
        settings.put("Use Proxy", "");
        settings.put("Proxy Host", "");
        settings.put("Proxy Port", "");
        settings.put("Proxy Username", "");
        settings.put("Proxy Password", "");
        settings.put("Include Subfolders", "false");
        settings.put("Delete After Download", "false");
        settings.put("Download Order", "Latest First");
        settings.put("Max Files", "0");

        /*test1(constantSettings, settings);
        test2(constantSettings, settings);*/
        test3(constantSettings, settings);
        test4(constantSettings, settings);
        test5(constantSettings, settings);
        
    }

    
    private static PluggableClient getTransport(Map<String, String> constantSettings, Map<String, String> settings)
            throws TransportInitializationException {
        PluggableSettingsImpl pluggableSettings = new PluggableSettingsImpl();
        pluggableSettings.setConstantSettings(constantSettings);
        pluggableSettings.setSettings(settings);
        PluggableClient transport = new PluggableS3Transport();
        transport.init(pluggableSettings);
        return transport;
    }

    private static void test1(Map<String, String> constantSettings, Map<String, String> settings) throws TransportInitializationException {

        settings.put("Folder", "/");
        settings.put("Include Subfolders", "false");

        PluggableClient transport = getTransport(constantSettings, settings);

        
        try {
            transport.connect();
            String[] result = transport.list();

            if(result.length == 1)
                System.out.println("Test1 passed, nr. of files found: " + result.length);
            else
                System.out.println("Test1 Failed, Incorrect Nr of files found: " + result.length);
        } catch (UnableToConsumeException | UnableToConnectException e) {
            System.err.println("Test1 failed with Exception.");
            e.printStackTrace();
        }

    }


    private static void test2(Map<String, String> constantSettings, Map<String, String> settings) throws TransportInitializationException {

        settings.put("Folder", "testFolder2");
        settings.put("Include Subfolders", "false");

        PluggableClient transport = getTransport(constantSettings, settings);
        
        try {
            transport.connect();
            String[] result = transport.list();

            if(result.length == 2)
                System.out.println("Test2 passed, nr. of files found: " + result.length);
            else
                System.out.println("Test2 Failed, Incorrect Nr of files found: " + result.length);
        } catch (UnableToConsumeException | UnableToConnectException e) {
            System.err.println("Test2 failed with Exception.");
            e.printStackTrace();
        }

    }

    private static void test3(Map<String, String> constantSettings, Map<String, String> settings) throws TransportInitializationException {

        settings.put("Folder", "testFolder2");
        settings.put("Include Subfolders", "true");

        PluggableClient transport = getTransport(constantSettings, settings);
        
        try {
            transport.connect();
            String[] result = transport.list();

            if(result.length == 3)
                System.out.println("Test3 passed, nr. of files found: " + result.length);
            else
                System.out.println("Test3 Failed, Incorrect Nr of files found: " + result.length);
        } catch (UnableToConsumeException | UnableToConnectException e) {
            System.err.println("Test3 failed with Exception.");
            e.printStackTrace();
        }

    }

    private static void test4(Map<String, String> constantSettings, Map<String, String> settings) throws TransportInitializationException {

        settings.put("Folder", "");
        settings.put("Include Subfolders", "true");

        PluggableClient transport = getTransport(constantSettings, settings);
        
        try {
            transport.connect();
            String[] result = transport.list();

            if(result.length > 100)
                System.out.println("Test4 passed, nr. of files found: " + result.length);
            else
                System.out.println("Test4 Failed, Incorrect Nr of files found: " + result.length);
        } catch (UnableToConsumeException | UnableToConnectException e) {
            System.err.println("Test4 failed with Exception.");
            e.printStackTrace();
        }

    }

    private static void test5(Map<String, String> constantSettings, Map<String, String> settings) throws TransportInitializationException {

        settings.put("Folder", "testFolder2");
        settings.put("Include Subfolders", "false");
        settings.put("Delete After Download", "false");
        settings.put("Download Order", "Latest First");
        settings.put("Max Files", "2");

        PluggableClient transport = getTransport(constantSettings, settings);
        
        try {
            transport.connect();
            String[] result = transport.list();

            if(result.length == 3)
                System.out.println("Test5 passed, nr. of files found: " + result.length);
            else
                System.out.println("Test5 Failed, Incorrect Nr of files found: " + result.length);
        } catch (UnableToConsumeException | UnableToConnectException e) {
            System.err.println("Test5 failed with Exception.");
            e.printStackTrace();
        }

    }

    

}
