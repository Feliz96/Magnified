package com.example.feliz.magnified_pictures;

/**
 * Created by Feliz on 2018/01/31.
 */

public class Api

{

    private static final String ROOT_URL = "http://192.168.56.1/MagnifiedApi/v1/Api.php?apicall=";

    public static final String URL_CREATE_USER = ROOT_URL+"createuser";
    public static final String URL_READ_USER = ROOT_URL+"getuser";
    public static final String URL_UPDATE_USER = ROOT_URL+"updateuser";
    public static final String URL_DELETE_USER = ROOT_URL+"deleteuser&id=";
}
