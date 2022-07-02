package com.gexingw.shop.enums;

import lombok.Getter;

/**
 * @author GeXingW
 * <p>
 * 100	Continue	继续。客户端应继续其请求
 * 101	Switching Protocols	切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议
 * 200	OK	请求成功。一般用于GET与POST请求
 * 201	Created	已创建。成功请求并创建了新的资源
 * 202	Accepted	已接受。已经接受请求，但未处理完成
 * 203	Non-Authoritative Information	非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
 * 204	No Content	无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
 * 205	Reset Content	重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
 * 206	Partial Content	部分内容。服务器成功处理了部分GET请求
 * 300	Multiple Choices	多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
 * 301	Moved Permanently	永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
 * 302	Found	临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
 * 303	See Other	查看其它地址。与301类似。使用GET和POST请求查看
 * 304	Not Modified	未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
 * 305	Use Proxy	使用代理。所请求的资源必须通过代理访问
 * 306	Unused	已经被废弃的HTTP状态码
 * 307	Temporary Redirect	临时重定向。与302类似。使用GET请求重定向
 * 400	Bad Request	客户端请求的语法错误，服务器无法理解
 * 401	Unauthorized	请求要求用户的身份认证
 * 402	Payment Required	保留，将来使用
 * 403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
 * 404	Not Found	服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
 * 405	Method Not Allowed	客户端请求中的方法被禁止
 * 406	Not Acceptable	服务器无法根据客户端请求的内容特性完成请求
 * 407	Proxy Authentication Required	请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
 * 408	Request Time-out	服务器等待客户端发送的请求时间过长，超时
 * 409	Conflict	服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突
 * 410	Gone	客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
 * 411	Length Required	服务器无法处理客户端发送的不带Content-Length的请求信息
 * 412	Precondition Failed	客户端请求信息的先决条件错误
 * 413	Request Entity Too Large	由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
 * 414	Request-URI Too Large	请求的URI过长（URI通常为网址），服务器无法处理
 * 415	Unsupported Media Type	服务器无法处理请求附带的媒体格式
 * 416	Requested range not satisfiable	客户端请求的范围无效
 * 417	Expectation Failed	服务器无法满足Expect的请求头信息
 * 500	Internal Server Error	服务器内部错误，无法完成请求
 * 501	Not Implemented	服务器不支持请求的功能，无法完成请求
 * 502	Bad Gateway	作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应
 * 503	Service Unavailable	由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
 * 504	Gateway Time-out	充当网关或代理的服务器，未及时从远端服务器获取请求
 * 505	HTTP Version not supported	服务器不支持请求的HTTP协议的版本，无法完成处理
 */

@Getter
public enum RespCode {
    // 正常响应
    SUCCESS(200000, "success"),

    // 创建
    PRODUCT_CREATED(201001, "商品已创建！"),
    CART_ADDED(201002, "加入购物车成功！"),
    USER_CREATED(201003, "用户已创建！"),
    ORDER_CREATED(201004, "订单已创建！"),
    PRODUCT_ATTRIBUTE_CREATED(201005, "商品属性已创建！"),
    PRODUCT_ATTRIBUTE_GROUP_CREATED(201005, "商品属性组已创建！"),
    PRODUCT_CATEGORY_CREATED(201006, "商品分类已创建！"),
    UPLOADED(201007, "上传成功！"),
    BANNER_CREATED(201008, "轮播图添加成功！"),
    SYSTEM_DICT_CREATED(201009, "数据字典添加成功！"),
    SYSTEM_CITY_CREATED(201010, "城市添加成功！"),
    ADMIN_CREATED(201011, "管理员添加成功！"),
    DEPT_CREATED(201012, "部门添加成功！"),
    ADMIN_JOB_CREATED(201013, "岗位添加成功！"),
    MENU_CREATED(201014, "菜单添加成功！"),
    ADMIN_ROLE_CREATED(201015, "角色添加成功！"),

    // 更新
    PRODUCT_UPDATED(202001, "商品已更新！"),
    CART_UPDATED(202002, "购物车已更新！"),
    USER_UPDATED(202003, "用户信息已更新！"),
    ORDER_UPDATED(202004, "订单已更新！"),
    PRODUCT_ATTRIBUTE_UPDATED(202005, "订单已更新！"),
    PRODUCT_ATTRIBUTE_GROUP_UPDATED(202005, "商品属性组已更新！"),
    PRODUCT_CATEGORY_UPDATED(202006, "商品分类已更新！"),
    BANNER_UPDATED(202007, "轮播图更新成功！"),
    SYSTEM_DICT_UPDATED(202008, "数据字典已更新！"),
    SYSTEM_CITY_UPDATED(202009, "城市已更新！"),
    ADMIN_UPDATED(202010, "管理员已更新！"),
    DEPT_UPDATED(202011, "部门已更新！"),
    ADMIN_JOB_UPDATED(202012, "岗位已更新！"),
    MENU_UPDATED(202013, "菜单已更新！"),
    ADMIN_ROLE_UPDATED(202014, "角色已更新！"),


    // 删除
    PRODUCT_DELETED(204001, "商品已删除！"),
    CART_DELETED(204002, "购物车移除！"),
    CART_CLEARED(204003, "购物车已清空！"),
    USER_DELETED(204004, "用户信息已删除！"),
    ORDER_DELETED(204005, "订单已删除！"),
    PRODUCT_ATTRIBUTE_DELETED(204005, "商品属性已删除！"),
    PRODUCT_ATTRIBUTE_GROUP_DELETED(204005, "商品属性已删除！"),
    PRODUCT_CATEGORY_DELETED(204006, "商品分类已删除！"),
    FILE_DELETED(204007, "文件已删除！"),
    BANNER_DELETED(204008, "轮播图已删除！"),
    SYSTEM_DICT_DELETED(204009, "数据字典已删除！"),
    SYSTEM_CITY_DELETED(204010, "城市已删除！"),
    ADMIN_DELETED(204011, "管理员已删除！"),
    DEPT_DELETED(204012, "部门已删除！"),
    ADMIN_JOB_DELETED(204013, "岗位已删除！"),
    MENU_DELETED(204014, "菜单已删除！"),
    ADMIN_ROLE_DELETED(204014, "角色已删除！"),


    // 用户认证
    UNAUTHORIZED(401001, "请先登录！"),
    AUTHORIZED_FAILED(401002, "用户名或密码错误！"),
    LOGIN_FAILED(401003, "登出失败！"),
    LOGIN_CAPTCHA_ERROR(401004, "验证码错误！"),
    LOGOUT_ERROR(401005, "注销失败！"),


    // 权限控制异常
    ACCESS_DENY(403001, "没有访问权限！"),
    OPERATION_DENY(403002, "没有操作权限！"),

    // 请求参数异常
    REQUEST_PARAM_INVALID(420001, "请求参数异常！"),
    PRODUCT_STOCK_OVER(420002, "商品库存不足！"),

    // 资源不存在
    PRODUCT_NOT_EXIST(404001, "商品不存在！"),
    CART_ITEM_NOT_EXIST(404002, "购物车信息不存在！"),
    USER_NOT_EXIST(404003, "用户信息不存在！"),
    PRODUCT_SKU_NOT_EXIST(404004, "商品规格不存在！"),
    ORDER_NOT_EXIST(404005, "订单不存在！"),
    PRODUCT_ATTRIBUTE_NOT_EXIST(404006, "商品属性不存在！"),
    PRODUCT_ATTRIBUTE_GROUP_NOT_EXIST(404006, "商品属性组不存在！"),
    PRODUCT_CATEGORY_NOT_EXIST(404007, "商品分类不存在！"),
    BANNER_NOT_EXIST(404008, "轮播图不存在！"),
    ADMIN_NOT_EXIST(404009, "管理员不存在！"),
    DEPT_NOT_EXIST(404010, "部门不存在！"),
    ADMIN_JOB_NOT_EXIST(404011, "岗位不存在！"),
    MENU_NOT_EXIST(404012, "菜单不存在！"),
    ADMIN_ROLE_NOT_EXIST(404013, "角色不存在！"),

    // 请求错误
    REQUEST_METHOD_NOT_SUPPORT(405001, "请求方法错误！"),
    RESOURCE_NOT_EXIST(405002, "请求资源不存在！"),
    RESOURCE_UNAVAILABLE(405003, "请求资源不可用！"),
    BAD_REQUEST(405004, "请求错误！"),

    // 参数异常
    PARAMS_INVALID(420001, "参数异常！"),

    // 系统异常
    FAILURE(500000, "系统异常，请稍后重试！"),
    QUERY_FAILURE(500001, "查询失败！"),
    SAVE_FAILURE(500002, "保存失败！"),
    UPDATE_FAILURE(500003, "更新失败！"),
    DELETE_FAILURE(500004, "删除失败！"),
    UPLOAD_FAILURE(500005, "上传失败！"),
    DB_OPERATION_FAILURE(500006, "数据库操作失败！"),
    ES_OPERATION_FAILURE(500007, "ES操作失败！"),
    ES_SAVE_FAILURE(500008, "ES添加失败！"),
    ES_UPDATE_FAILURE(500009, "ES更新失败！"),
    ES_DELETE_FAILURE(500010, "ES删除失败！"),
    ES_QUERY_FAILURE(500011, "ES查询失败！"),
    FILE_DELETE_FAILURE(500012, "文件删除失败！"),
    CAPTCHA_GENERATE_FAILURE(500013, "验证码生成失败！"),

    // 网关异常
    GATEWAY_TIMEOUT(504001, "网关超时！"),


    ;

    private final int code;
    private final String message;

    RespCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
