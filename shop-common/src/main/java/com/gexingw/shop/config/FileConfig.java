package com.gexingw.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig {
    private String location;

    private Map<String, String> diskLocation = new HashMap<>();

    private String activeDisk;

    private Disk disks;

    @Data
    private static class Disk {
        private DiskItem local;

    }

    @Data
    private static class DiskItem {
        private String location;
    }

    public String getDiskName() {
        return activeDisk;
    }

    public String getDiskLocation(String diskName) {
        try {
            Class<Disk> aClass = Disk.class;

//            String diskName = getDiskName();
            diskName = diskName.substring(0, 1).toUpperCase() + diskName.substring(1);

            Method method = aClass.getMethod("get" + diskName);

            Disk disks = getDisks();
            DiskItem diskItem = (DiskItem) method.invoke(disks);
            return diskItem.getLocation();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    public String getLocation() {
        return getLocation(activeDisk);
    }

    public String getLocation(String disk) {
        String location = diskLocation.get(disk);
        if (location == null) {
            // 获取disk的location
            location = getDiskLocation(disk);

            // 重新写进去
            diskLocation.put(disk, location);
        }

        return location;
    }
}




