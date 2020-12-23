package com.sammbo.imdemo.entity.busevent;

import com.geely.imsdk.client.listener.SIMConnectListener;
import com.geely.imsdk.client.result.SIMResult;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventConnectState {
    private SIMConnectListener.ConnectState state;
    private SIMResult result;
}
