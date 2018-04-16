package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.DirectlyConnectedDevice;
import oracle.iot.client.device.VirtualDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.security.GeneralSecurityException;

@Service("MockCommandsService")
public class MockCommandsService {
    @Resource(name="CSVReaderService")
    private CSVReaderService csvReaderService;

    @Value("${iot.filefolder.mockcommands}")
    private String fileDir;

    @Value("${iot.client.remote_host_name}")
    private String remote_host_name;

    // The URN marked with number 2.
    private  final String URN = "urn:fawiotpoc:waterpump";

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
                    virtualDevice.set("uuid", "M" );

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
