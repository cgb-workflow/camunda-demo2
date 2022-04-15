package org.hgq.mapper.base;


import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.hgq.domain.base.CollectionDerate;
import org.hgq.domain.base.CollectionDerateExample;

import java.util.List;

public interface CollectionDerateMapper {
    @SelectProvider(type= CollectionDerateSqlProvider.class, method="countByExample")
    long countByExample(CollectionDerateExample example);

    @DeleteProvider(type= CollectionDerateSqlProvider.class, method="deleteByExample")
    int deleteByExample(CollectionDerateExample example);

    @Delete({
        "delete from collection_derate",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into collection_derate (id, user_no, ",
        "user_id, product_code, ",
        "derate_type, status, ",
        "images, supplement_images, ",
        "operator_id, audit_time, ",
        "process_instance_id, remark, ",
        "version, deleted, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=BIGINT}, #{userNo,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=BIGINT}, #{productCode,jdbcType=INTEGER}, ",
        "#{derateType,jdbcType=TINYINT}, #{status,jdbcType=TINYINT}, ",
        "#{images,jdbcType=VARCHAR}, #{supplementImages,jdbcType=VARCHAR}, ",
        "#{operatorId,jdbcType=BIGINT}, #{auditTime,jdbcType=TIMESTAMP}, ",
        "#{processInstanceId,jdbcType=VARCHAR}, #{remark,jdbcType=VARCHAR}, ",
        "#{version,jdbcType=INTEGER}, #{deleted,jdbcType=TINYINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(CollectionDerate record);

    @InsertProvider(type= CollectionDerateSqlProvider.class, method="insertSelective")
    int insertSelective(CollectionDerate record);

    @SelectProvider(type= CollectionDerateSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_no", property="userNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="product_code", property="productCode", jdbcType=JdbcType.INTEGER),
        @Result(column="derate_type", property="derateType", jdbcType=JdbcType.TINYINT),
        @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="images", property="images", jdbcType=JdbcType.VARCHAR),
        @Result(column="supplement_images", property="supplementImages", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator_id", property="operatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="audit_time", property="auditTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="process_instance_id", property="processInstanceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="deleted", property="deleted", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CollectionDerate> selectByExample(CollectionDerateExample example);

    @Select({
        "select",
        "id, user_no, user_id, product_code, derate_type, status, images, supplement_images, ",
        "operator_id, audit_time, process_instance_id, remark, version, deleted, create_time, ",
        "update_time",
        "from collection_derate",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_no", property="userNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="product_code", property="productCode", jdbcType=JdbcType.INTEGER),
        @Result(column="derate_type", property="derateType", jdbcType=JdbcType.TINYINT),
        @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="images", property="images", jdbcType=JdbcType.VARCHAR),
        @Result(column="supplement_images", property="supplementImages", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator_id", property="operatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="audit_time", property="auditTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="process_instance_id", property="processInstanceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="deleted", property="deleted", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    CollectionDerate selectByPrimaryKey(Long id);

    @UpdateProvider(type= CollectionDerateSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") CollectionDerate record, @Param("example") CollectionDerateExample example);

    @UpdateProvider(type= CollectionDerateSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") CollectionDerate record, @Param("example") CollectionDerateExample example);

    @UpdateProvider(type= CollectionDerateSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CollectionDerate record);

    @Update({
        "update collection_derate",
        "set user_no = #{userNo,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=BIGINT},",
          "product_code = #{productCode,jdbcType=INTEGER},",
          "derate_type = #{derateType,jdbcType=TINYINT},",
          "status = #{status,jdbcType=TINYINT},",
          "images = #{images,jdbcType=VARCHAR},",
          "supplement_images = #{supplementImages,jdbcType=VARCHAR},",
          "operator_id = #{operatorId,jdbcType=BIGINT},",
          "audit_time = #{auditTime,jdbcType=TIMESTAMP},",
          "process_instance_id = #{processInstanceId,jdbcType=VARCHAR},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "version = #{version,jdbcType=INTEGER},",
          "deleted = #{deleted,jdbcType=TINYINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(CollectionDerate record);
}