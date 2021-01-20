package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "open_platform_account",
        indexes = {
                @Index(name = "idx_principal_name", unique = true, columnList = "principal_name")
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenPlatformAccount extends AbstractPersistable<Long> {


    @Column
    private String appId;

    @Column(unique = true, name = "principal_name")
    private String principalName;

    @OneToMany(targetEntity = ThirdPartyAccount.class, orphanRemoval = true)
    @JoinColumn(name = "platform_account_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<ThirdPartyAccount> thirdPartyAccounts;

}
