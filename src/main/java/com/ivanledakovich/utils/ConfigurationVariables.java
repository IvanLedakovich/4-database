package com.ivanledakovich.utils;

import com.ivanledakovich.models.DatabaseDao;

public class ConfigurationVariables {

    public static DatabaseDao getEnvironmentVariables() {
        DatabaseDao databaseDao = new DatabaseDao();
        databaseDao.setUrl(System.getenv().get("DB_URL"));
        databaseDao.setUsername(System.getenv().get("DB_USERNAME"));
        databaseDao.setPassword(System.getenv().get("DB_PASSWORD"));
        databaseDao.setDriver(System.getenv().get("DB_DRIVER"));
        return databaseDao;
    }
}
