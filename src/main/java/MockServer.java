import java.util.HashMap;

import org.apache.axis2.context.*;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.*;
import org.apache.axis2.receivers.RawXMLINOutMessageReceiver;
import org.apache.axis2.transport.http.SimpleHTTPServer;

public class MockServer {
    public static void main(String[] args) throws Exception {
        final int port = 8080;
        final ConfigurationContext configuration = ConfigurationContextFactory.createDefaultConfigurationContext();

        AxisService service = createService(configuration);
        configuration.deployService(service);
        SimpleHTTPServer receiver = new SimpleHTTPServer(configuration, port);

        receiver.start();
        Thread.sleep(1000);
        if (receiver.isRunning()){
            System.out.println("SERVER IS RUNNING");
        }
        Thread.sleep(1000);
        receiver.stop();
    }

    private static AxisService createService(ConfigurationContext configuration) throws Exception {
        AxisConfiguration axisConfig = configuration.getAxisConfiguration();

        HashMap<String, MessageReceiver> messageReceiverMap = new HashMap<>();
        messageReceiverMap.put(WSDL2Constants.MEP_URI_IN_OUT, new RawXMLINOutMessageReceiver());

        final AxisService service = AxisService.createService(
                MyService.class.getName(),
                axisConfig,
                messageReceiverMap,
                null,
                null,
                MockServer.class.getClassLoader()
        );

        service.setName("serviceName");

        return service;
    }
}