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

    public void setThresholdBottomValue(String newValue){
        videoServerRestClient.put("/gui/threshold/bottom", newValue);
    }

    public void setThresholdTopValue(String newValue){
        videoServerRestClient.put("/gui/threshold/bottom", newValue);
    }

}
