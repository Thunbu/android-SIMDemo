package com.sammbo.imdemo.sdk;

/**
 * @Description: sdk环境配置, 具体在SDKManager.envService
 * @author: Yang.Yang33
 * @Date: 2021/4/20 20:16
 */
public enum EnvService {
    DEV("https://sdkdev-gateway.sammbo.com:8777/", "wss://sdkdev-websocket.sammbo.com", 8326, "http://10.86.78.55:8367/demo/", "1000000217"),
    TEST("https://sdktest-gateway.sammbo.com:18777/", "wss://sdktest-websocket.sammbo.com", 18326, "https://developer.sammbo.com/ServerController/", "1000000217"),
//    TEST("http://ht.oa8000.com.cn:8777/", "ws://ht.oa8000.com.cn", 8326, "http://ht.oa8000.com.cn:8367/demo/", "1000000217"),
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
