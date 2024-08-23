package org.yoga.jarvis.repository.mapper;

import org.yoga.jarvis.repository.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author yoga
 * @since 2024-08-23
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
