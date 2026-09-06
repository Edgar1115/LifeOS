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
 * <p>DB 表示与领域枚举的映射完全归属 infrastructure：TINYINT 1=ACTIVE、0=DISABLED。
 * 领域层 {@link UserStatus} 本身不持有任何数据库表示。</p>
 *
 * <p>MyBatis 默认 {@code EnumTypeHandler} 按枚举名字存字符串，与表结构冲突，
 * 因此此处显式声明自定义处理器。</p>
 */
@MappedTypes(UserStatus.class)
public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {

    /** 写：枚举 -> 数据库整数值 */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UserStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, toDbValue(parameter));
    }

    @Override
    public UserStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int dbValue = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return fromDbValue(dbValue);
    }

    @Override
    public UserStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int dbValue = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return fromDbValue(dbValue);
    }

    @Override
    public UserStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int dbValue = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return fromDbValue(dbValue);
    }

    /** 枚举 -> 数据库整数值（1=ACTIVE，0=DISABLED）。枚举穷尽由编译期保证，新增枚举值必须在这里补分支 */
    private static int toDbValue(UserStatus status) {
        return switch (status) {
            case ACTIVE -> 1;
            case DISABLED -> 0;
        };
    }

    /** 数据库整数值 -> 枚举（1=ACTIVE，0=DISABLED），非法值当作 SQLException 抛出，防止脏数据被静默吞掉 */
    private static UserStatus fromDbValue(int dbValue) throws SQLException {
        return switch (dbValue) {
            case 1 -> UserStatus.ACTIVE;
            case 0 -> UserStatus.DISABLED;
            default -> throw new SQLException("非法用户状态值: " + dbValue);
        };
    }
}
