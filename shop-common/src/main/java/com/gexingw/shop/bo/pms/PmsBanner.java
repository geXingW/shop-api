package com.gexingw.shop.bo.pms;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.constant.SystemConstant;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.mapper.sys.SysUploadMapper;
import com.gexingw.shop.utils.SpringContextUtil;
import lombok.Data;

import java.util.Date;

@Data
public class PmsBanner {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer sort;

    private String link;

    private String showStatus;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    private Date endTime;

    @JsonFormat(pattern = SystemConstant.DATETIME_STRING_FORMAT, timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public String getPic() {
        SysUploadMapper sysUploadMapper = SpringContextUtil.getBean(SysUploadMapper.class);
        SysUpload upload = sysUploadMapper.selectOne(new QueryWrapper<SysUpload>().eq("upload_id", id)
                .eq("upload_type", UploadConstant.UPLOAD_TYPE_BANNER));

        return upload != null ? upload.getFullUrl() : "";
    }
}
