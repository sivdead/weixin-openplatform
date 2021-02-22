package com.github.sivdead.wx.open.dto;

import com.github.sivdead.wx.open.constant.ValidateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaGroupDto {

    /**
     * 素材类型 {@link me.chanjar.weixin.common.api.WxConsts.MaterialType}
     */
    private String mediaType;

    /**
     * 分组名
     */
    private String groupName;

    private Long id;

}
