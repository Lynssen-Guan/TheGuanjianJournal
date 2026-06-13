package cn.lynssen.guanjianjournal.mapper;

import cn.lynssen.guanjianjournal.dao.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Lynssen
 * @Create 2026/6/12 20:41
 */
@Mapper
public interface AuthMapper {

    UserDO selectByUsername(String username);

    int insertUser(UserDO userDO);

}
