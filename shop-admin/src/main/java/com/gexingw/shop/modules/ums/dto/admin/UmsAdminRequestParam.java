package com.gexingw.shop.modules.ums.dto.admin;

import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.bo.ums.UmsJob;
import com.gexingw.shop.bo.ums.UmsRole;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UmsAdminRequestParam {

    private long id;

    @NotEmpty
    @Max(value = 10, message = " 管理员名称不能超过10个字符")
    private String username;

    @NotEmpty
    private String email;

    @NotNull
    private boolean enabled;

    @NotEmpty
    private String gender;

    @NotNull
    private UmsDept dept;

    @NotNull
    private List<UmsJob> jobs;

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String phone;

    @NotEmpty
    private List<UmsRole> roles;

    private String newPass;
    private String oldPass;

//    private void setGender(String v){
//        gender = "男".equals(v) ? UmsAdminSexType.M: UmsAdminSexType.F;
//    }

//    public UmsAdminSexType getGender(){
//        if(UmsAdminSexType.M.name().equals(gender)){
//            return UmsAdminSexType.M;
//        }
//
//        return UmsAdminSexType.F;
//    }

    public List<Long> getRoleIds(){
        return roles.stream().map(UmsRole::getId).collect(Collectors.toList());
    }

    public List<Long> getJobIds(){
        return jobs.stream().map(UmsJob::getId).collect(Collectors.toList());
    }

    public long getDeptId(){
        return dept.getId();
    }
}
