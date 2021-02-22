package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "open_platform_account",
        indexes = {
                @Index(name = "idx_principal_name", unique = true, columnList = "principal_name"),
                @Index(name = "idx_app_id", columnList = "app_id")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenPlatformAccount{

    @Id
    @Column(name = "app_id")
    private String appId;

    @Column(name = "principal_name")
    private String principalName;

    @OneToMany(targetEntity = ThirdPartyAccount.class, orphanRemoval = true)
    @JoinColumn(name = "platform_account_id", referencedColumnName = "app_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<ThirdPartyAccount> thirdPartyAccounts;

}
