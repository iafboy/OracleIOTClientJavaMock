package com.faw.poc.iotclientmock.DirectlyConnectedMock;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("CSVReaderService")
public class CSVReaderService {
    public List<File> getFileList(String dirPath){
        List<File> wjList=null;
        File file = new File(dirPath);
        if(file.isDirectory()) {
            File[] fileList = file.listFiles();
            wjList = new ArrayList<File>();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()&&fileList[i].getName().contains(".csv")) {
                    wjList.add(fileList[i]);
                }
            }
        }
        return wjList;
    }
    public List<String[]> csvReaderHandler(File csvFile){
        List<String[]> result=null;
        File csv = csvFile;
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String line = "";
        try {
            result = new ArrayList<>();
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {
                String everyLine = line;
                result.add(everyLine.split(","));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
