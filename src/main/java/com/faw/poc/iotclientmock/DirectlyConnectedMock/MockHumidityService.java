package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.device.VirtualDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("MockHumidityService")
public class MockHumidityService extends MockAbsService{
    @Resource(name="CSVReaderService")
    private CSVReaderService csvReaderService;

    @Value("${iot.filefolder.mockhumidity}")
    private String fileDir;

    @Value("${iot.client.remote_host_name}")
    private String remote_host_name;

    // The URN marked with number 2.
    private  final String URN = "urn:fawiotpoc:humidity";

    public void startWork(String activation_ID,String file_Protection_Password) {
        try {
            this.handleWork(remote_host_name,fileDir,URN,activation_ID,file_Protection_Password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(VirtualDevice virtualDevice, String[] contents) {
        virtualDevice.update().set("uuid", contents[0].replace("\"", ""))
                .set("temperature", Double.valueOf(contents[1].replace("\"", "")))
                .set("humidity", Double.valueOf(contents[2].replace("\"", "")))
                .set("rtc", contents[3].replace("\"", "")).finish();
    }
}
