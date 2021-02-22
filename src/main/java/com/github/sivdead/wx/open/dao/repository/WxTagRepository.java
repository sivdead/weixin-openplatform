package com.github.sivdead.wx.open.dao.repository;

import com.github.sivdead.wx.open.dao.base.BaseJPA;
import com.github.sivdead.wx.open.dao.eo.Tag;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyAccount;
import com.github.sivdead.wx.open.dao.eo.WxTag;

public interface WxTagRepository extends BaseJPA<WxTag,Integer> {

    WxTag findByTagIdAndAppId(Long tagId, String appId);

}
