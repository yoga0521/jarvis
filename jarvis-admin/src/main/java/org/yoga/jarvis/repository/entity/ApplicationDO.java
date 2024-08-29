/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * 应用信息
 * </p>
 *
 * @author yoga
 * @since 2024-08-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("jarvis.application")
public class ApplicationDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 是否开启，0禁用 1开启 
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

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
    private Integer isDelete;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String CONTEXT_PATH = "context_path";

    public static final String ENABLED = "enabled";

    public static final String DESCRIPTION = "description";

    public static final String GMT_CREATED = "gmt_created";

    public static final String CREATOR_ID = "creator_id";

    public static final String CREATOR_NAME = "creator_name";

    public static final String GMT_MODIFY = "gmt_modify";

    public static final String MODIFIER_ID = "modifier_id";

    public static final String MEMO = "memo";

    public static final String IS_DELETE = "is_delete";
}
