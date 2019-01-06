package com.myapp.guiapp.services;

import com.myapp.guiapp.communication.rest.VideoServerRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServerClient {

    private final VideoServerRestClient videoServerRestClient;

    @Autowired
    public VideoServerClient(VideoServerRestClient videoServerRestClient) {
        this.videoServerRestClient = videoServerRestClient;
    }

    public void setThresholdValue(String newValue){
        videoServerRestClient.put("/gui/threshold", newValue);
    }

}
