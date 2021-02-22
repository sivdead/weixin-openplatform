package com.github.sivdead.wx.open.dao.eo;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单模板
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "menu_template")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuTemplate extends AbstractPersistable<Long> {

    @OneToMany(targetEntity = MenuItem.class)
    @JoinColumn(name = "menu_template_id",referencedColumnName = "id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<MenuItem> menuItems;

    private String name;

}
