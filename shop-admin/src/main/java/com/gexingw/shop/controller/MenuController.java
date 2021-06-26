package com.gexingw.shop.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gexingw.shop.bean.ums.UmsAdminRole;
import com.gexingw.shop.bean.ums.UmsMenu;
import com.gexingw.shop.bean.ums.UmsRoleMenu;
import com.gexingw.shop.dto.menu.UmsMenuRequestParam;
import com.gexingw.shop.dto.menu.UmsMenuSearchParam;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.mapper.UmsMenuMapper;
import com.gexingw.shop.service.UmsAdminRoleService;
import com.gexingw.shop.service.UmsMenuService;
import com.gexingw.shop.service.UmsRoleMenuService;
import com.gexingw.shop.service.UmsRoleService;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.util.RedisUtil;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-02-16
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    UmsAdminRoleService umsAdminRoleService;

    @Autowired
    UmsRoleMenuService umsRoleMenuService;

    @Autowired
    UmsMenuService umsMenuService;

    @Autowired
    UmsMenuMapper umsMenuMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsRoleService umsRoleService;

    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public R index(UmsMenuSearchParam searchParam) {
        QueryWrapper<UmsMenu> queryWrapper = new QueryWrapper<UmsMenu>().eq("pid", searchParam.getPid());
        if (searchParam.getId() != null) {
            queryWrapper.eq("id", searchParam.getId());
        }

        if (StringUtils.hasText(searchParam.getFurry())) {
            queryWrapper.like("title", searchParam.getFurry());
        }

        // 分页信息
        IPage<UmsMenu> page = new Page<UmsMenu>(searchParam.getPage(), searchParam.getSize());

        return R.ok(PageUtil.format(umsMenuMapper.selectPage(page, queryWrapper)));
    }

    @GetMapping("/tree")
    @PreAuthorize("@el.check('menu:list')")
    public R tree() {
        // 获取当前用户的ID
        Long authId = AuthUtil.getAuthId();

        // 尝试从redis获取用户菜单
        List<Map<String, Object>> menus = umsMenuService.getAdminMenusFromRedisByAdminId(authId);
        if (menus != null) {
            return R.ok(menus);
        }

        // 获取当前用户的角色
        List<UmsAdminRole> adminRoles = umsAdminRoleService.getByUserId(authId);

        // 获取角色绑定的所有菜单Id
        List<Long> roleIds = adminRoles.stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());
        List<UmsRoleMenu> roleMenus = umsRoleMenuService.getListByRoleIds(roleIds);

        // 获取菜单详细信息
        List<Long> menuIds = roleMenus.stream().map(UmsRoleMenu::getMenuId).collect(Collectors.toList());

        menus = umsMenuService.getByPidAndIds(0, menuIds).stream().map(
                umsMenu -> umsMenuService.formatMenuForFront(umsMenu)
        ).collect(Collectors.toList());

        // 查询菜单的类型
//        Integer[] types = {0, 1};     // 方式一
        List<Integer> types = Arrays.asList(0, 1); // 方式二
//        new QueryWrapper<Driver>().lambda().in("type",0,1); // 方式三

        for (Map<String, Object> menu : menus) {
            QueryWrapper<UmsMenu> queryWrapper = new QueryWrapper<UmsMenu>()
                    .in("id", menuIds)
                    .eq("pid", menu.get("id"))
//                    .and(q -> q.eq("type", 0).or().eq("type", 1));
                    .in("type", types).orderByAsc("menu_sort");

            // 查找子菜单
            List<Map<String, Object>> children = umsMenuMapper.selectList(queryWrapper).stream().map(
                    umsMenu -> umsMenuService.formatMenuForFront(umsMenu)
            ).collect(Collectors.toList());
            menu.put("children", children);
        }

        // 将查询结果写入redis
        umsMenuService.setAdminMenusFromRedisByAdminId(authId, menus);

        return R.ok(menus);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@el.check('menu:edit')")
    public R show(@PathVariable("id") long id) {
        return R.ok();
    }

    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public R save(@RequestBody UmsMenuRequestParam menuRequestParam) {
        Long id = umsMenuService.save(menuRequestParam);

        // 如果存在父级菜单，父级菜单的子菜单需要加1
        if (!umsMenuService.incrParentMenuSubCount(menuRequestParam.getPid())) {
            return R.ok(RespCode.FAILURE.getCode(), "更新父级菜单子菜单数量失败！");
        }

        // 添加角色与菜单的绑定

        long authId = AuthUtil.getAuthId();
        if (id > 0) {
            umsMenuService.delRedisAdminMenuByAdminId(authId);
            umsMenuService.delRedisAdminPermissionByAdminId(authId);
        }

        return R.ok(id);
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param ids
     * @return
     */
    @PostMapping("superior")
    @PreAuthorize("@el.check('menu:list')")
    public R superior(@RequestBody List<Long> ids) {
        List<Long> pids = umsMenuMapper.selectBatchIds(ids).stream().map(UmsMenu::getPid).collect(Collectors.toList());
        List<UmsMenu> umsMenus = new ArrayList<UmsMenu>();

        // 如果已经查询到了定级，直接返回顶级菜单
        if (pids.contains(0L)) {
            umsMenus = umsMenuMapper.selectList(new QueryWrapper<UmsMenu>().eq("pid", 0));
        } else {
            // 获取上级的Pid,查询上级的所有同级菜单
            List<Long> pPids = umsMenuMapper.selectBatchIds(pids).stream().map(UmsMenu::getPid).collect(Collectors.toList());

            // 获取当前菜单的所有同级
            umsMenus = umsMenuMapper.selectList(new QueryWrapper<UmsMenu>().in("pid", pPids));
        }

        ArrayList<Object> menus = new ArrayList<>();
        for (UmsMenu umsMenu : umsMenus) {
            HashMap<String, Object> menu = new HashMap<>();
            menu.put("cache", umsMenu.isCache());
            menu.put("component", umsMenu.getComponent());
            menu.put("componentName", umsMenu.getComponentName());
            menu.put("createTime", umsMenu.getCreateTime());
            menu.put("hasChildren", umsMenu.isHasChildren());
            menu.put("hidden", umsMenu.isHidden());
            menu.put("iFrame", umsMenu.isIframe());
            menu.put("icon", umsMenu.getIcon());
            menu.put("id", umsMenu.getId());
            menu.put("label", umsMenu.getLabel());
            menu.put("leaf", umsMenu.isLeaf());

            // 如果是本次请求的上级菜单
            if (pids.contains(umsMenu.getId())) {
                menu.put("children", umsMenuMapper.selectList(new QueryWrapper<UmsMenu>().eq("pid", umsMenu.getId())));
            }
            menus.add(menu);
        }

        return R.ok(menus);
    }

    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public R update(@RequestBody UmsMenuRequestParam menuRequestParam) {
        UmsMenu umsMenu = umsMenuService.getMenuById(menuRequestParam.getId());
        if (umsMenu == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "菜单不存在！");
        }

        if (!umsMenuService.update(menuRequestParam)) {
            return R.ok(RespCode.FAILURE.getCode(), "更新失败！");
        }

        // 如果修改了父级菜单，需要更新父级菜单的subcount
        if (!umsMenu.getPid().equals(menuRequestParam.getPid())) {

            // 旧的父级菜单 -1
            if (!umsMenuService.decrParentMenuSubCount(umsMenu.getPid())) {
                return R.ok(RespCode.DB_OPERATION_FAILURE.getCode(), "父级菜单更新失败！");
            }

            // 新的父级菜单 +1
            if (umsMenuService.incrParentMenuSubCount(menuRequestParam.getPid())) {
                return R.ok(RespCode.DB_OPERATION_FAILURE.getCode(), "父级菜单更新失败！");
            }
        }

        // 清除缓存
        long authId = AuthUtil.getAuthId();
        umsMenuService.delRedisAdminMenuByAdminId(authId);
        umsMenuService.delRedisAdminPermissionByAdminId(authId);

        return R.ok("已更新！");
    }

    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public R delete(@RequestBody List<Long> ids) {
        if (!umsMenuService.delete(ids)) {
            return R.ok(RespCode.DELETE_FAILURE.getCode(), "删除失败！");
        }

        // 更新父级菜单的 subcount
        List<UmsMenu> umsMenus = umsMenuService.getByIds(ids);
        for (UmsMenu umsMenu : umsMenus) {
            // 父级菜单subcount -1
            if (!umsMenuService.decrParentMenuSubCount(umsMenu.getPid())) {
                return R.ok(RespCode.DB_OPERATION_FAILURE, "父级菜单更新失败！");
            }
        }

        for (Long id : ids) {
            umsMenuService.delRedisAdminMenuByAdminId(id);
            umsMenuService.delRedisAdminPermissionByAdminId(id);
        }

        return R.ok("已删除！");
    }

    @GetMapping("lazy")
    @PreAuthorize("@el.check('menu:list')")
    public R lazy(@RequestParam Long pid) {
        List<UmsMenu> menuList = umsMenuMapper.selectList(new QueryWrapper<UmsMenu>().eq("pid", pid));
        return R.ok(menuList);
    }

    /**
     * 获取 所有子节点ID，包含自身节点ID
     *
     * @param id
     * @return
     */
    @GetMapping("child")
    @PreAuthorize("@el.check('menu:list')")
    public R child(@RequestParam Long id) {
        // 递归查询子菜单
        ArrayList<UmsMenu> umsMenus = new ArrayList<>();
        List<Long> result = umsMenuService.getChildren(id, umsMenus).stream().map(UmsMenu::getId).collect(Collectors.toList());

        // 添加自身节点ID
        result.add(id);

        return R.ok(result);
    }
}
