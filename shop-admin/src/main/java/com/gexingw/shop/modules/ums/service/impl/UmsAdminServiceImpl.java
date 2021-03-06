package com.gexingw.shop.modules.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.ums.*;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.AdminConstant;
import com.gexingw.shop.mapper.sys.SysUploadMapper;
import com.gexingw.shop.mapper.ums.*;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminRequestParam;
import com.gexingw.shop.enums.DataScopeEnum;

import com.alibaba.fastjson.JSON;
import com.gexingw.shop.modules.ums.bo.UmsAdminDetails;
import com.gexingw.shop.modules.ums.dto.admin.UmsAdminSearchParam;
import com.gexingw.shop.modules.ums.service.UmsMenuService;
import com.gexingw.shop.modules.ums.service.*;
import com.gexingw.shop.util.AuthUtil;
import com.gexingw.shop.utils.RedisUtil;
import com.gexingw.shop.utils.RsaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    FileConfig fileConfig;

    @Autowired
    UmsAdminMapper umsAdminMapper;

    @Autowired
    UmsAdminRoleMapper umsAdminRoleMapper;

    @Autowired
    UmsRoleMenuMapper umsRoleMenuMapper;

    @Autowired
    UmsMenuMapper umsMenuMapper;

    @Autowired
    UmsAdminJobMapper umsAdminJobMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsRoleMapper umsRoleMapper;

    @Autowired
    UmsMenuService umsMenuService;

    @Autowired
    UmsRoleService umsRoleService;

    @Autowired
    UmsRoleDeptService umsRoleDeptService;

    @Autowired
    UmsDeptService umsDeptService;

    @Autowired
    private RsaUtil rsaUtil;

    @Autowired
    SysUploadMapper sysUploadMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //??????????????????
        UmsAdmin admin = getAdminDetailByAdminName(username);
        if (admin == null) {
            return null;
        }

        // ????????????????????????
        List<String> userMenus = getUserPermissions(admin.getId());

        // ??????security ??????
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (admin.isAdmin()) { // ?????????
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else {    // ????????????
            for (String menu : userMenus) {
                authorities.add(new SimpleGrantedAuthority(menu));
            }
        }

        return new UmsAdminDetails(admin, authorities);
    }

    @Override
    public UmsAdmin findByUserName(String username) {
        return umsAdminMapper.findByUserName(username);
    }

    public List<UmsRole> getUserRolesById(Long id) {
        return umsAdminMapper.getUserRolesById(id);
    }

    public List<UmsMenu> getUserMenusById(Long id) {
        // ????????????ID????????????????????????
        List<UmsAdminRole> adminRoles = umsAdminRoleMapper.getRoleByUserId(id);
        List<Long> roleIds = adminRoles.stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());

        // ??????????????????ID???????????????????????????
        List<UmsRoleMenu> roleMenus = umsRoleMenuMapper.getRoleMenusByRoleIds(roleIds);
        List<Long> menuIds = roleMenus.stream().map(UmsRoleMenu::getMenuId).collect(Collectors.toList());

        // ??????????????????ID?????????????????????
        return umsMenuMapper.getMenusByIds(menuIds);
    }

    @Override
    public List<UmsAdmin> getList() {
        return umsAdminMapper.getList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UmsAdminRequestParam umsAdminRequestParam) {
        UmsAdmin umsAdmin = new UmsAdmin();

        // ?????????????????????Bean
        BeanUtils.copyProperties(umsAdminRequestParam, umsAdmin, "gender", "id");
        umsAdmin.setEnabled(umsAdmin.isEnabled() ? 1 : 0);
        umsAdmin.setPassword(new BCryptPasswordEncoder().encode("123456")); // ????????????123456
        umsAdmin.setDeptId(umsAdminRequestParam.getDeptId());
        umsAdmin.setEmail(umsAdminRequestParam.getEmail());
        umsAdmin.setGender(umsAdminRequestParam.getGender());

        // ??????????????????
        if (umsAdminMapper.insert(umsAdmin) <= 0) {
            throw new RuntimeException("????????????????????????!");
        }

        // ??????????????????
        umsAdminRoleMapper.insertAdminRoles(umsAdmin.getId(), umsAdminRequestParam.getRoleIds());

        // ??????????????????
        umsAdminJobMapper.insertAdminJobs(umsAdmin.getId(), umsAdminRequestParam.getJobIds());

        return umsAdmin.getId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UmsAdminRequestParam umsAdminRequestParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminRequestParam, umsAdmin);
        umsAdmin.setEnabled(umsAdminRequestParam.isEnabled() ? 1 : 0);
        umsAdmin.setDeptId(umsAdminRequestParam.getDeptId());
        umsAdmin.setEmail(umsAdminRequestParam.getEmail());

        // ??????????????????
        if (umsAdminMapper.updateById(umsAdmin) < 0) {
            throw new RuntimeException("???????????????????????????");
        }

        Map<String, Object> query = new HashMap<String, Object>();
        query.put("admin_id", umsAdmin.getId().toString());

        // ????????????????????????
        umsAdminRoleMapper.deleteByMap(query);

        // ????????????????????????
        umsAdminRoleMapper.insertAdminRoles(umsAdmin.getId(), umsAdminRequestParam.getRoleIds());

        // ????????????????????????
        umsAdminJobMapper.deleteByMap(query);

        // ????????????????????????
        umsAdminJobMapper.insertAdminJobs(umsAdmin.getId(), umsAdminRequestParam.getJobIds());

        return true;
    }

    @Transactional
    public boolean delByIds(Set<Long> ids) {
        // ??????????????????
        if (umsAdminRoleMapper.delete(new QueryWrapper<UmsAdminRole>().in("admin_id", ids)) <= 0) {
            throw new RuntimeException("?????????????????????");
        }

        // ??????????????????
        if (umsAdminJobMapper.delete(new QueryWrapper<UmsAdminJob>().in("admin_id", ids)) <= 0) {
            throw new RuntimeException("?????????????????????");
        }

        // ??????????????????
        if (umsAdminMapper.deleteBatchIds(ids) <= 0) {
            throw new RuntimeException("?????????????????????");
        }

        return true;
    }

    public boolean exist(Long id) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<UmsAdmin>().eq("id", id);

        return umsAdminMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public UmsAdmin findById(Long adminId) {
        UmsAdmin adminDetail = getRedisAdminDetailByAdminId(adminId);
        if (adminDetail != null) {
            return adminDetail;
        }

        return umsAdminMapper.selectById(adminId);
    }

    @Override
    public UmsAdmin getRedisAdminDetailByAdminId(Long adminId) {
        Object redisObj = redisUtil.get(String.format(AdminConstant.REDIS_ADMIN_ID_DETAILS_FORMAT, adminId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), UmsAdmin.class);
    }

    @Override
    public boolean setRedisAdminDetailByAdminId(Long adminId, UmsAdmin umsAdmin) {
        return redisUtil.set(String.format(AdminConstant.REDIS_ADMIN_ID_DETAILS_FORMAT, adminId), umsAdmin);
    }

    @Override
    public void delRedisAdminDetailByAdminId(long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_ID_DETAILS_FORMAT, adminId));
    }

    @Override
    public List<String> getUserPermissions(Long adminId) {
        // ????????????Redis?????????
        List<String> userPermissions = umsMenuService.getAdminPermissionFromRedisByAdminId(adminId);
        if (userPermissions != null) {
            return userPermissions;
        }

        // ???DB??????
        UmsAdmin umsAdmin = umsAdminMapper.selectById(adminId);
        if (umsAdmin.isAdmin()) {
            userPermissions = Arrays.asList("admin");
        } else {
            userPermissions = getUserMenusById(adminId)
                    .stream().map(UmsMenu::getPermission)
                    .filter(Objects::nonNull).filter(string -> !string.isEmpty())
                    .collect(Collectors.toList());
        }
        // ????????????Redis
        userPermissions.forEach(permission -> {
            umsMenuService.setRedisAdminPermissionByAdminId(adminId, permission);
        });

        return userPermissions;
    }

    @Override
    public List<Long> getUserDataScope(Long adminId) {
        // ?????????Redis??????
        List<Long> dataScope = getRedisAminDataScopeByAdminId(adminId);
        if (dataScope != null) {
            return dataScope;
        }

        // ?????????????????????
        List<UmsRole> adminRoles = umsRoleService.getAdminRolesByAdminId(adminId);

        // ???????????????????????????
        List<String> roleScopes = adminRoles.stream().map(UmsRole::getDataScope).collect(Collectors.toList());
        List<Long> roleIds = adminRoles.stream().map(UmsRole::getId).collect(Collectors.toList());

        // ??????????????????
        List<Long> dataScopes = new ArrayList<>();

        // ???????????????????????????????????????????????????
        if (roleScopes.contains(DataScopeEnum.CUSTOMIZE.getValue())) {
            // ?????????????????????????????????
            List<Long> deptIds = umsRoleDeptService.getDeptIdsByRoleIds(roleIds);

            // ?????????????????????????????????????????????
            List<UmsDept> roleDepts = umsDeptService.getDeptsByDeptId(deptIds);
            roleDepts.addAll(umsDeptService.getDeptWithChildrenByPids(deptIds));

            dataScopes.addAll(roleDepts.stream().map(UmsDept::getId).collect(Collectors.toList()));
        }

        // ?????????????????????????????????????????????????????????
        if (roleScopes.contains(DataScopeEnum.DEPT.getValue())) {
            // ????????????????????????
            UmsAdmin umsAdmin = findById(adminId);
            if (umsAdmin != null) {
                dataScopes.add(umsAdmin.getDeptId());
            }
        }

        // ????????????dataScope??????Redis
        setRedisAdminDataScopeByAdminId(adminId, dataScopes);

        return dataScopes.stream().distinct().collect(Collectors.toList());
    }

    public List<Long> getRedisAminDataScopeByAdminId(Long adminId) {
        String redisKey = String.format(AdminConstant.REDIS_ADMIN_DATA_SCOPE_FORMAT, adminId);
        if (!redisUtil.hasKey(redisKey)) {
            return null;
        }

        List<Object> redisObj = redisUtil.lGet(redisKey, 0, -1);
        return redisObj.stream().map(i -> Long.valueOf(i.toString())).collect(Collectors.toList());
    }

    public boolean setRedisAdminDataScopeByAdminId(Long adminId, List<Long> dataScopes) {
        for (Long dataScope : dataScopes) {
            redisUtil.lSet(String.format(AdminConstant.REDIS_ADMIN_DATA_SCOPE_FORMAT, adminId), dataScope);
        }

        return true;
    }

    public void delRedisAdminDataScopeByAdminId(Long adminId) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_DATA_SCOPE_FORMAT, adminId));
    }

    @Override
    public UmsAdmin getAdminDetailByAdminName(String username) {
        UmsAdmin umsAdmin = getRedisAdminDetailByAdminName(username);
        // ???redis??????
        if (umsAdmin != null) {
            return umsAdmin;
        }

        // redis???????????????DB??????
        umsAdmin = umsAdminMapper.selectOne(new QueryWrapper<UmsAdmin>().eq("username", username));

        if (umsAdmin != null) {
            // ????????????redis
            setRedisAdminDetailByAdminName(username, umsAdmin);
        }

        return umsAdmin;
    }

    public UmsAdmin getAdminDetailByAdminId(Long adminId) {
        // ???redis??????
        UmsAdmin umsAdmin = getRedisAdminDetailByAdminId(adminId);
        if (umsAdmin != null) {
            return umsAdmin;
        }

        // redis???????????????DB??????
        umsAdmin = umsAdminMapper.selectById(adminId);
        if (umsAdmin != null) {
            setRedisAdminDetailByAdminId(adminId, umsAdmin);
        }

        return umsAdmin;
    }

    @Override
    public boolean updateCenter(UmsAdminRequestParam requestParam) throws Exception {
        UmsAdmin umsAdmin = getAdminDetailByAdminId(requestParam.getId());
        if (umsAdmin == null) {
            return false;
        }

        if (requestParam.getGender() != null) {
            umsAdmin.setGender(requestParam.getGender());
        }

        if (requestParam.getPhone() != null) {
            umsAdmin.setPhone(requestParam.getPhone());
        }

        if (requestParam.getNickName() != null) {
            umsAdmin.setNickName(requestParam.getNickName());
        }

        // ????????????
        if (requestParam.getNewPass() != null && requestParam.getOldPass() != null) {
            // ???????????????
            String oldPwd = rsaUtil.decryptByPrivateKey(requestParam.getOldPass());
            if (!passwordEncoder.matches(oldPwd, umsAdmin.getPassword())) {
                throw new RuntimeException("?????????????????????");
            }

            String newPwd = rsaUtil.decryptByPrivateKey(requestParam.getNewPass());
            umsAdmin.setPassword(passwordEncoder.encode(newPwd));
        }

        return umsAdminMapper.updateById(umsAdmin) > 0;
    }

    @Override
    public IPage<UmsAdmin> queryList(IPage<UmsAdmin> page, UmsAdminSearchParam requestParams) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();

        // ??????????????????
        if (requestParams.getDeptId() != 0L) {
            queryWrapper.eq("dept_id", requestParams.getDeptId());
        }

        // ????????????
        if (requestParams.getBlurry() != null) {
            queryWrapper.and(q -> q.like("username", requestParams.getBlurry()).or()
                    .like("nick_name", requestParams.getBlurry()).or()
                    .like("email", requestParams.getBlurry()).or()
                    .like("phone", requestParams.getBlurry())
            );
        }

        // ??????????????????
        if (requestParams.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", requestParams.getCreateTimeBegin());
        }

        if (requestParams.getCreateTimeEnd() != null) {
            queryWrapper.le("create_time", requestParams.getCreateTimeEnd());
        }

        return umsAdminMapper.selectPage(page, queryWrapper);
    }

    /**
     * ?????? <b>username</b> ???????????????redis??????????????????
     *
     * @param username
     * @return
     */
    @Override
    public UmsAdmin getRedisAdminDetailByAdminName(String username) {
        Object redisObj = redisUtil.get(String.format(AdminConstant.REDIS_ADMIN_NAME_DETAILS_FORMAT, username));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), UmsAdmin.class);
    }

    /**
     * ?????? <b>username</b> ????????????????????????redis???
     *
     * @param username
     * @param umsAdmin
     * @return
     */
    @Override
    public boolean setRedisAdminDetailByAdminName(String username, UmsAdmin umsAdmin) {
        return redisUtil.set(String.format(AdminConstant.REDIS_ADMIN_NAME_DETAILS_FORMAT, username), umsAdmin);
    }

    /**
     * ??????redis????????? <b>username</b> ?????????????????????
     *
     * @param username
     * @return
     */
    @Override
    public void delRedisAdminDetailByAdminName(String username) {
        redisUtil.del(String.format(AdminConstant.REDIS_ADMIN_NAME_DETAILS_FORMAT, username));
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param umsAdminRequestParam
     * @return
     */
    @Override
    public boolean checkLevel(UmsAdminRequestParam umsAdminRequestParam) {
        long adminId = AuthUtil.getAuthId();
        // ???????????????????????????Id
        QueryWrapper<UmsAdminRole> queryWrapper1 = new QueryWrapper<UmsAdminRole>().eq("admin_id", adminId);
        List<Long> roleIds = umsAdminRoleMapper.selectList(queryWrapper1).stream().map(UmsAdminRole::getRoleId).collect(Collectors.toList());

        //?????????????????????????????????
        QueryWrapper<UmsRole> queryWrapper2 = new QueryWrapper<UmsRole>().in("id", roleIds);
        List<Integer> roleLvls = umsRoleMapper.selectList(queryWrapper2).stream().map(UmsRole::getLevel).collect(Collectors.toList());

        // ???????????????
        QueryWrapper<UmsRole> queryWrapper3 = new QueryWrapper<UmsRole>().in("id", umsAdminRequestParam.getRoleIds());
        Integer optLvl = Collections.min(umsRoleMapper.selectList(queryWrapper3).stream().map(UmsRole::getLevel).collect(Collectors.toList()));

        return Collections.min(roleLvls) <= optLvl;
    }

    public String rsaDecode(String encodeStr) throws Exception {
        return rsaUtil.decryptByPrivateKey(encodeStr);
    }
}

