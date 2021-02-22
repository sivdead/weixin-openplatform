package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "media_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaGroup extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String mediaType;

    @JoinColumn(name = "group_id",referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToMany(targetEntity = Media.class)
    private List<Media> mediaList;

}
