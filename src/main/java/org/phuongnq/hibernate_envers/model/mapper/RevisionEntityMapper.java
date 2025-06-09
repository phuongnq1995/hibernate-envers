package org.phuongnq.hibernate_envers.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.phuongnq.hibernate_envers.config.audit.CustomRevisionEntity;
import org.springframework.jdbc.core.RowMapper;

public class RevisionEntityMapper implements RowMapper<CustomRevisionEntity> {

  @Override
  public CustomRevisionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CustomRevisionEntity entity = new CustomRevisionEntity();

    entity.setId(rs.getInt("id"));
    entity.setModule(rs.getString("module"));
    entity.setTimestamp(rs.getLong("timestamp"));
    String userId = rs.getString("user_id");
    entity.setUserId(userId == null ? null : UUID.fromString(userId));

    return entity;
  }
}
