package com.gexingw.shop.dto.admin;

import com.gexingw.shop.bean.ums.UmsDept;
import com.gexingw.shop.bean.ums.UmsJob;
import com.gexingw.shop.bean.ums.UmsRole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class UmsAdminRequestParam {

    private long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    private boolean enabled;

    @NotEmpty
    private String gender;

    @NotEmpty
    private UmsDept dept;

    @NotEmpty
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
