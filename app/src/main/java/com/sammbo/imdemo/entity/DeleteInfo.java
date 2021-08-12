package com.sammbo.imdemo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class DeleteInfo {
    List<String> messageIds;
    @SerializedName("receiver")
    transient String sessionId;
    transient String deleter;
}
