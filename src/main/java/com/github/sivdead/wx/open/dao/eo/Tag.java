package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

/**
 * 素材
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tag")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends AbstractPersistable<Long> {

    /**
     * 标签名称
     */
    private String name;

    @JoinColumn(name = "tag_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToMany(targetEntity = WxTag.class,orphanRemoval = true)
    private List<WxTag> wxTags;


}
