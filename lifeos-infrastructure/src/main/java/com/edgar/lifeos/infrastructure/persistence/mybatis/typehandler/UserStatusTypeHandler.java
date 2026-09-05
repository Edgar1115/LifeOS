package com.edgar.lifeos.infrastructure.persistence.mybatis.typehandler;

import com.edgar.lifeos.domain.identity.UserStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link UserStatus} 自定义 TypeHandler。
 *
 * <p>统一使用 {@link UserStatus#getDbValue()}（TINYINT 1/0）与数据库交互，
 * 与 V1 迁移脚本中 {@code status TINYINT} 的存储语义保持一致。</p>
 *
 * <p>MyBatis 默认 {@code EnumTypeHandler} 按枚举名字存字符串，与表结构冲突，
 * 因此此处显式声明自定义处理器。</p>
 */
@MappedTypes(UserStatus.class)
public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getDbValue());
    }

    @Override
    public UserStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int v = rs.getInt(columnName);
        return v == 0 && rs.wasNull() ? null : UserStatus.fromDbValue(v);
    }

    @Override
    public UserStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int v = rs.getInt(columnIndex);
        return v == 0 && rs.wasNull() ? null : UserStatus.fromDbValue(v);
    }

    @Override
    public UserStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int v = cs.getInt(columnIndex);
        return v == 0 && cs.wasNull() ? null : UserStatus.fromDbValue(v);
    }
}