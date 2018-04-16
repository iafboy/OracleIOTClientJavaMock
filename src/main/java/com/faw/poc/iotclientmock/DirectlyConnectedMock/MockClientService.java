package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.DeviceModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.DirectlyConnectedDevice;
import oracle.iot.client.device.VirtualDevice;
import org.springframework.stereotype.Service;

@Service("MockClientService")
public class MockClientService {

    @Value("${iot.client.remote_host_name}")
    private String remote_host_name;

    // The URN marked with number 2.
    private  final String URN = "urn:fawiotpoc:aircondition";

    // The attribute name marked with number 3.
    private final String GREETING_ATTRIBUTE = "message";

    public void startWork(String activation_ID,String file_Protection_Password) {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals(remote_host_name));
        DirectlyConnectedDevice dcd = null;
        try {
            dcd = new DirectlyConnectedDevice(activation_ID,file_Protection_Password);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if(dcd==null) return;

        // Activate the device if it not activated (so that you can run the code more than once!)
        if (!dcd.isActivated()) {
            try {
                dcd.activate(URN);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            try{
                // Set up a virtual device based on your device model
                DeviceModel dcdModel = dcd.getDeviceModel(URN);
                VirtualDevice virtualDevice = dcd.createVirtualDevice(dcd.getEndpointId(), dcdModel);

                //Triggers  messages to the Cloud Service.
                int i = 0;
                while (i < 100) {
                    Random rand = new Random();
                    int cnt = (int) (Math.random() * 100);
                    virtualDevice.set(GREETING_ATTRIBUTE, "M" + cnt);
                    i++;
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        try {
                dcd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
