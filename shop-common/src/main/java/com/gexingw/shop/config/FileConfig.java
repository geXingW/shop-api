package com.gexingw.shop.config;

import com.gexingw.shop.exception.UploadConfigErrorException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig {
    private String location;


    private Map<String, DiskItem> diskItems = new HashMap<>();

    private String activeDisk;

    private Disk disks;

    @Data
    private static class Disk {
        private DiskItem local;

    }

    @Data
    private static class DiskItem {
        private String location;
        private String host;
    }

    public String getDiskName() {
        return activeDisk;
    }

    public DiskItem getDiskItem(String disk) {
        // 先尝试从实例中获取
        DiskItem diskItem = diskItems.get(disk);
        if (diskItem == null) { // 实例中没有，调用方法获取
            try {
                Class<Disk> aClass = Disk.class;
                disk = disk.substring(0, 1).toUpperCase() + disk.substring(1);

                Method method = aClass.getMethod("get" + disk);

                Disk disks = getDisks();
                diskItem = (DiskItem) method.invoke(disks);

                // 重新写入实例属性
                diskItems.put(disk.toLowerCase(Locale.ROOT), diskItem);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new UploadConfigErrorException("存储实例获取失败！");
            }
        }

        return diskItem;
    }

    public String getDiskLocation(String disk) {
        DiskItem diskItem = getDiskItem(disk);
        if (diskItem == null) {
            return null;
        }

        return diskItem.getLocation();
    }

    public String getDiskHost() {
        DiskItem diskItem = getDiskItem(getDiskName());
        if (diskItem == null) {
            return null;
        }

        return diskItem.getHost();
    }

    public String getDiskHost(String disk) {
        DiskItem diskItem = getDiskItem(disk);
        if (diskItem == null) {
            return null;
        }

        return diskItem.getHost();
    }

    public String getLocation() {
        return getLocation(activeDisk);
    }

    public String getLocation(String disk) {
        DiskItem diskItem = getDiskItem(disk);
        if (diskItem == null) {
            return null;
        }

        return diskItem.getLocation();
    }
}




