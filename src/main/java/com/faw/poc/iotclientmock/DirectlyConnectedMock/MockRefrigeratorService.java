package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import oracle.iot.client.device.VirtualDevice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("MockRefrigeratorService")
public class MockRefrigeratorService extends MockAbsService{
    @Resource(name="CSVReaderService")
    private CSVReaderService csvReaderService;

    @Value("${iot.filefolder.mockrefrigerator}")
    private String fileDir;

    @Value("${iot.client.remote_host_name}")
    private String remote_host_name;

    // The URN marked with number 2.
    private  final String URN = "urn:fawiotpoc:refrigerator";

    public void startWork(String activation_ID,String file_Protection_Password) {
        try {
            this.handleWork(remote_host_name,fileDir,URN,activation_ID,file_Protection_Password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(VirtualDevice virtualDevice, String[] contents) {
        virtualDevice.set("uuid", contents[0].replace("\"",""));
        virtualDevice.set("current", Double.valueOf(contents[1].replace("\"","")));
        virtualDevice.set("power", Double.valueOf(contents[2].replace("\"","")));
        virtualDevice.set("rtc", contents[3].replace("\"",""));
    }
}
