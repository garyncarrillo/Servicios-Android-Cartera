package com.example.blanquita.cm_wallets;

public class Web_Services {
    private String Ip_Server;
    private String Puerto;
    private boolean Login;
    public Web_Services(){
        Ip_Server="172.20.10.3";
        Puerto ="3000";
        Login = false;
    }
    public void setLogin(boolean status){
        this.Login=status;
    }
    public boolean IsLogin(){
        return Login;
    }
    public String getIp_Server(){
        return Ip_Server;
    }
    public String getPuerto(){
        return  Puerto;
    }
}
