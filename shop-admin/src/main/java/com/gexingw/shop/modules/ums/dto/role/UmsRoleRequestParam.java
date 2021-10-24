package com.gexingw.shop.modules.ums.dto.role;

import com.gexingw.shop.bo.ums.UmsDept;
import com.gexingw.shop.bo.ums.UmsMenu;
import lombok.Data;

import java.util.List;

@Data
public class UmsRoleRequestParam {

    private Long id;

    private String name;

    private String description;

    private int level;

    private List<UmsDept> depts;

    private List<UmsMenu> menus;

    private String dataScope;
}
