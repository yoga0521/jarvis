package org.yoga.jarvis.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author yoga
 * @since 2024-08-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("jarvis.user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreated;

    /**
     * 创建人用户id
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModify;

    /**
     * 修改人用户id
     */
    private Long modifierId;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否删除，0未删除 1删除
     */
    private Integer isDel;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PASSWORD = "password";

    public static final String GMT_CREATED = "gmt_created";

    public static final String CREATOR_ID = "creator_id";

    public static final String CREATOR_NAME = "creator_name";

    public static final String GMT_MODIFY = "gmt_modify";

    public static final String MODIFIER_ID = "modifier_id";

    public static final String MEMO = "memo";

    public static final String IS_DEL = "is_del";
}
