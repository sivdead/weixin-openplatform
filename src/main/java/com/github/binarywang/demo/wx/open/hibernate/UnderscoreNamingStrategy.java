package com.github.binarywang.demo.wx.open.hibernate;

import cn.hutool.core.util.StrUtil;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.Serializable;
import java.util.Optional;

public class UnderscoreNamingStrategy implements PhysicalNamingStrategy, Serializable {

    public static final UnderscoreNamingStrategy INSTANCE = new UnderscoreNamingStrategy();

    public UnderscoreNamingStrategy() {
    }

    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return toUnderLine(name);
    }

    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return toUnderLine(name);
    }

    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return toUnderLine(name);
    }

    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return toUnderLine(name);
    }

    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return toUnderLine(name);
    }

    private Identifier toUnderLine(Identifier name) {
        return Optional.ofNullable(name)
                .map(e -> Identifier.toIdentifier(StrUtil.toUnderlineCase(e.getText())))
                .orElse(null);
    }
}
