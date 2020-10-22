package xyz.erupt.auth.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import xyz.erupt.auth.constant.MenuTypeEnum;
import xyz.erupt.auth.model.*;
import xyz.erupt.auth.model.log.EruptLoginLog;
import xyz.erupt.auth.model.log.EruptOperateLog;
import xyz.erupt.auth.util.MD5Utils;
import xyz.erupt.core.dao.EruptDao;
import xyz.erupt.core.util.ProjectUtil;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author liyuepeng
 * @date 2019-07-15.
 */
@Service
@Order(10)
@Log
public class AuthDataLoadService implements CommandLineRunner {

    @Autowired
    private EruptDao eruptDao;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        new ProjectUtil().projectStartLoaded("auth", first -> {
            if (first) {
                //用户
                EruptUser eruptUser = new EruptUser();
                eruptUser.setIsAdmin(true);
                eruptUser.setIsMd5(true);
                eruptUser.setStatus(true);
                eruptUser.setCreateTime(new Date());
                eruptUser.setAccount("erupt");
                eruptUser.setPassword(MD5Utils.digest("erupt"));
                eruptUser.setName("erupt");
                eruptDao.persistIfNotExist(EruptUser.class, eruptUser, "account", eruptUser.getAccount());

                //菜单
                String code = "code";
                String $manager = "$manager";
                Integer open = Integer.valueOf(EruptMenu.OPEN);
                EruptMenu eruptMenu = eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu($manager, "系统管理", null, null, 1, 0, "fa fa-cogs", null)
                        , code, $manager);
                eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                        EruptMenu.class.getSimpleName(), "菜单维护", MenuTypeEnum.TREE.getCode(), EruptMenu.class.getSimpleName(), open, 10, "fa fa-list-ul", eruptMenu
                ), code, EruptMenu.class.getSimpleName());
                eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                        EruptOrg.class.getSimpleName(), "组织维护", MenuTypeEnum.TREE.getCode(), EruptOrg.class.getSimpleName(), open, 20, "fa fa-users", eruptMenu
                ), code, EruptOrg.class.getSimpleName());
                eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                        EruptRole.class.getSimpleName(), "角色维护", MenuTypeEnum.TABLE.getCode(), EruptRole.class.getSimpleName(), open, 30, null, eruptMenu
                ), code, EruptRole.class.getSimpleName());
                eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                        EruptUser.class.getSimpleName(), "用户维护", MenuTypeEnum.TABLE.getCode(), EruptUser.class.getSimpleName(), open, 40, "fa fa-user", eruptMenu
                ), code, EruptUser.class.getSimpleName());
                {
                    EruptMenu eruptMenuDict = eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                            EruptDict.class.getSimpleName(), "字典维护", MenuTypeEnum.TABLE.getCode(), EruptDict.class.getSimpleName(), open, 50, null, eruptMenu
                    ), code, EruptDict.class.getSimpleName());
                    eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                            EruptDictItem.class.getSimpleName(), "字典项", MenuTypeEnum.TABLE.getCode(), EruptDictItem.class.getSimpleName(), Integer.valueOf(EruptMenu.HIDE), 10, null, eruptMenuDict
                    ), code, EruptDictItem.class.getSimpleName());
                }
                eruptDao.persistIfNotExist(EruptMenu.class, new EruptMenu(
                        EruptLoginLog.class.getSimpleName(), "登录日志", MenuTypeEnum.TABLE.getCode(), EruptLoginLog.class.getSimpleName(), open, 60, null, eruptMenu
                ), code, EruptLoginLog.class.getSimpleName());
                eruptDao.persistIfNotExist(EruptOperateLog.class, new EruptMenu(
                        EruptOperateLog.class.getSimpleName(), "操作日志", MenuTypeEnum.TABLE.getCode(), EruptOperateLog.class.getSimpleName(), open, 70, null, eruptMenu
                ), code, EruptOperateLog.class.getSimpleName());
            }
        });
    }

}
