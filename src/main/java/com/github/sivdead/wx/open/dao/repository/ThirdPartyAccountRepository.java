package com.github.sivdead.wx.open.dao.repository;

import com.github.sivdead.wx.open.dao.eo.ThirdPartyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyAccountRepository extends JpaRepository<ThirdPartyAccount,String> {

    ThirdPartyAccount findByAppId(String appId);

}
