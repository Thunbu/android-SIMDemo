package com.sammbo.imdemo.sdk;

/**
 * @Description: sdk环境配置, 具体在SDKManager.envService
 * @author: Yang.Yang33
 * @Date: 2021/4/20 20:16
 */
public enum EnvService {
    PRD("https://sdk-gateway.sammbo.com:8777", "wss://sdk-websocket.sammbo.com", 8326, "https://developer.sammbo.com/DLSP-PROD/", "1000000217");
    private final String httpServer;
    private final String tcpServerHost;
    private final int tcpServerPort;
    private final String appServer;
    private final String appId;

    EnvService(String httpServer, String tcpServerHost, int tcpServerPort, String appServer, String appId) {
        this.httpServer = httpServer;
        this.tcpServerHost = tcpServerHost;
        this.tcpServerPort = tcpServerPort;
        this.appServer = appServer;
        this.appId = appId;
    }

    public String getHttpServer() {
        return httpServer;
    }

    public String getTcpServerHost() {
        return tcpServerHost;
    }

    public int getTcpServerPort() {
        return tcpServerPort;
    }

    public String getAppServer() {
        return appServer;
    }

    public String getAppId() {
        return appId;
    }
}
