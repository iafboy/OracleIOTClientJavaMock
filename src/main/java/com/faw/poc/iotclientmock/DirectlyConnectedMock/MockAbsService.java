package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.DeviceModel;
import oracle.iot.client.device.DirectlyConnectedDevice;
import oracle.iot.client.device.VirtualDevice;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.util.List;

@Component
public abstract class MockAbsService {
    @Resource(name="CSVReaderService")
    private CSVReaderService csvReaderService;
    protected void handleWork(String remote_host_name,String fileDir,String URN,String activation_ID,String file_Protection_Password) throws Exception{
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals(remote_host_name));
            try {
                List<File> files = csvReaderService.getFileList(fileDir);
                for (File file : files) {
                    if (!file.exists()) continue;
                    List<String[]> contentsLs = csvReaderService.csvReaderHandler(file);
                    for (String[] contents : contentsLs) {
                        DirectlyConnectedDevice dcd = null;
                        try {
                            dcd = new DirectlyConnectedDevice(activation_ID, file_Protection_Password);
                        } catch (Exception e) {
                           throw e;
                        }
                        if (dcd == null) return;
                        // Activate the device if it not activated (so that you can run the code more than once!)
                        if (!dcd.isActivated()) {
                            try {
                                dcd.activate(URN);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        // Set up a virtual device based on your device model
                        DeviceModel dcdModel = dcd.getDeviceModel(URN);
                        VirtualDevice virtualDevice = dcd.createVirtualDevice(dcd.getEndpointId(), dcdModel);
                        //Triggers  messages to the Cloud Service.
                        sendMessage(virtualDevice,contents);
                        try {
                            dcd.close();
                        } catch (Exception e) {
                            throw e;
                        }
                    }
                    try {
                        Thread.sleep(3 * 1000);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {;
                throw ex;
            }
        }
        public abstract void sendMessage(VirtualDevice virtualDevice,String[] contents);
}
