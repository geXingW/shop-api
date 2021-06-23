package com.gexingw.shop.constant;

public interface AdminConstant {
    // 用户菜单
    public static final String REDIS_ADMIN_MENUS_FORMAT = "admin:%d:menus";

    // 用户权限
    public static final String REDIS_ADMIN_PERMISSIONS_FORMAT = "admin:%d:permissions";

    // 用户角色
    public static final String REDIS_ADMIN_ROLES_FORMAT = "admin:%d:roles";

    // 数据权限
    public static final String REDIS_ADMIN_DATA_SCOPE_FORMAT = "admin:%d:data-scope";

    // 用户岗位
    public static final String REDIS_ADMIN_JOBS_FORMAT = "admin:%d:jobs";

    // 根据用户名存储的用户详情
    public static final String REDIS_ADMIN_NAME_DETAILS_FORMAT = "admin:name:%s:details";

    // 根据用户ID存储的用户详情
    public static final String REDIS_ADMIN_ID_DETAILS_FORMAT = "admin:id:%d:details";
}
