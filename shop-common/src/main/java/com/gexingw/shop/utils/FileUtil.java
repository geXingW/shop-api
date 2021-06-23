package com.gexingw.shop.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private final static String SYSTEM_TMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String fileName = SYSTEM_TMP_DIR + "job-list.xlsx";
        File file = new File(fileName);

        ExcelWriter writer = ExcelUtil.getBigWriter(file);
        writer.write(list, true);
        SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
        sheet.trackAllColumnsForAutoSizing();
        writer.autoSizeColumnAll();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }
}
