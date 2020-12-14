package cn.edu.xmu.other.impl;

public interface InLoginService
{
    /**
     * 登录 返回object（包含用户token）
     * @param userName
     * @param password
     * @return
     */
    public String login(String userName, String password);
}
