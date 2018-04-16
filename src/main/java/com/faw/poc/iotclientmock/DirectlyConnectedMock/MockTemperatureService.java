package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.DirectlyConnectedDevice;
import oracle.iot.client.device.VirtualDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;

@Service("MockTemperatureService")
public class MockTemperatureService extends MockAbsService{
    @Resource(name="CSVReaderService")
    private CSVReaderService csvReaderService;

    @Value("${iot.filefolder.mocktemperature}")
    private String fileDir;

    @Value("${iot.client.remote_host_name}")
    private String remote_host_name;

    // The URN marked with number 2.
    private  final String URN = "urn:fawiotpoc:temperature";

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
                List<File> files=csvReaderService.getFileList(fileDir);
                for(File file:files) {
                    if (!file.exists()) continue;
                    List<String[]> contentsLs = csvReaderService.csvReaderHandler(file);
                    for (String[] contents : contentsLs) {
                        //Triggers  messages to the Cloud Service.
                        virtualDevice.set("uuid", contents[0].replace("\"",""));
                        virtualDevice.set("temperature", Double.valueOf(contents[1].replace("\"","")));
                        virtualDevice.set("rtc", contents[2].replace("\"",""));
                    }
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

    @Override
    public void sendMessage(VirtualDevice virtualDevice, String[] contents) {
        virtualDevice.update().set("uuid", contents[0].replace("\"",""))
                .set("temperature", Double.valueOf(contents[1].replace("\"","")))
                .set("rtc", contents[2].replace("\"","")).finish();
    }
}
