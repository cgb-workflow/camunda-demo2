package org.hgq.mapper.base;

import org.hgq.domain.base.CollectionDerate;
import org.hgq.domain.base.CollectionDerateExample;
import org.hgq.domain.base.CollectionDerateExample.Criteria;
import org.hgq.domain.base.CollectionDerateExample.Criterion;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class CollectionDerateSqlProvider {

    public String countByExample(CollectionDerateExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("collection_derate");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(CollectionDerateExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("collection_derate");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(CollectionDerate record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("collection_derate");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUserNo() != null) {
            sql.VALUES("user_no", "#{userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=BIGINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.VALUES("product_code", "#{productCode,jdbcType=INTEGER}");
        }
        
        if (record.getDerateType() != null) {
            sql.VALUES("derate_type", "#{derateType,jdbcType=TINYINT}");
        }
        
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=TINYINT}");
        }
        
        if (record.getImages() != null) {
            sql.VALUES("images", "#{images,jdbcType=VARCHAR}");
        }
        
        if (record.getSupplementImages() != null) {
            sql.VALUES("supplement_images", "#{supplementImages,jdbcType=VARCHAR}");
        }
        
        if (record.getOperatorId() != null) {
            sql.VALUES("operator_id", "#{operatorId,jdbcType=BIGINT}");
        }
        
        if (record.getAuditTime() != null) {
            sql.VALUES("audit_time", "#{auditTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getProcessInstanceId() != null) {
            sql.VALUES("process_instance_id", "#{processInstanceId,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark() != null) {
            sql.VALUES("remark", "#{remark,jdbcType=VARCHAR}");
        }
        
        if (record.getVersion() != null) {
            sql.VALUES("version", "#{version,jdbcType=INTEGER}");
        }
        
        if (record.getDeleted() != null) {
            sql.VALUES("deleted", "#{deleted,jdbcType=TINYINT}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String selectByExample(CollectionDerateExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("user_no");
        sql.SELECT("user_id");
        sql.SELECT("product_code");
        sql.SELECT("derate_type");
        sql.SELECT("status");
        sql.SELECT("images");
        sql.SELECT("supplement_images");
        sql.SELECT("operator_id");
        sql.SELECT("audit_time");
        sql.SELECT("process_instance_id");
        sql.SELECT("remark");
        sql.SELECT("version");
        sql.SELECT("deleted");
        sql.SELECT("create_time");
        sql.SELECT("update_time");
        sql.FROM("collection_derate");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        CollectionDerate record = (CollectionDerate) parameter.get("record");
        CollectionDerateExample example = (CollectionDerateExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("collection_derate");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getUserNo() != null) {
            sql.SET("user_no = #{record.userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{record.userId,jdbcType=BIGINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.SET("product_code = #{record.productCode,jdbcType=INTEGER}");
        }
        
        if (record.getDerateType() != null) {
            sql.SET("derate_type = #{record.derateType,jdbcType=TINYINT}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{record.status,jdbcType=TINYINT}");
        }
        
        if (record.getImages() != null) {
            sql.SET("images = #{record.images,jdbcType=VARCHAR}");
        }
        
        if (record.getSupplementImages() != null) {
            sql.SET("supplement_images = #{record.supplementImages,jdbcType=VARCHAR}");
        }
        
        if (record.getOperatorId() != null) {
            sql.SET("operator_id = #{record.operatorId,jdbcType=BIGINT}");
        }
        
        if (record.getAuditTime() != null) {
            sql.SET("audit_time = #{record.auditTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getProcessInstanceId() != null) {
            sql.SET("process_instance_id = #{record.processInstanceId,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark() != null) {
            sql.SET("remark = #{record.remark,jdbcType=VARCHAR}");
        }
        
        if (record.getVersion() != null) {
            sql.SET("version = #{record.version,jdbcType=INTEGER}");
        }
        
        if (record.getDeleted() != null) {
            sql.SET("deleted = #{record.deleted,jdbcType=TINYINT}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{record.updateTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("collection_derate");
        
        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("user_no = #{record.userNo,jdbcType=VARCHAR}");
        sql.SET("user_id = #{record.userId,jdbcType=BIGINT}");
        sql.SET("product_code = #{record.productCode,jdbcType=INTEGER}");
        sql.SET("derate_type = #{record.derateType,jdbcType=TINYINT}");
        sql.SET("status = #{record.status,jdbcType=TINYINT}");
        sql.SET("images = #{record.images,jdbcType=VARCHAR}");
        sql.SET("supplement_images = #{record.supplementImages,jdbcType=VARCHAR}");
        sql.SET("operator_id = #{record.operatorId,jdbcType=BIGINT}");
        sql.SET("audit_time = #{record.auditTime,jdbcType=TIMESTAMP}");
        sql.SET("process_instance_id = #{record.processInstanceId,jdbcType=VARCHAR}");
        sql.SET("remark = #{record.remark,jdbcType=VARCHAR}");
        sql.SET("version = #{record.version,jdbcType=INTEGER}");
        sql.SET("deleted = #{record.deleted,jdbcType=TINYINT}");
        sql.SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        sql.SET("update_time = #{record.updateTime,jdbcType=TIMESTAMP}");
        
        CollectionDerateExample example = (CollectionDerateExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(CollectionDerate record) {
        SQL sql = new SQL();
        sql.UPDATE("collection_derate");
        
        if (record.getUserNo() != null) {
            sql.SET("user_no = #{userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=BIGINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.SET("product_code = #{productCode,jdbcType=INTEGER}");
        }
        
        if (record.getDerateType() != null) {
            sql.SET("derate_type = #{derateType,jdbcType=TINYINT}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=TINYINT}");
        }
        
        if (record.getImages() != null) {
            sql.SET("images = #{images,jdbcType=VARCHAR}");
        }
        
        if (record.getSupplementImages() != null) {
            sql.SET("supplement_images = #{supplementImages,jdbcType=VARCHAR}");
        }
        
        if (record.getOperatorId() != null) {
            sql.SET("operator_id = #{operatorId,jdbcType=BIGINT}");
        }
        
        if (record.getAuditTime() != null) {
            sql.SET("audit_time = #{auditTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getProcessInstanceId() != null) {
            sql.SET("process_instance_id = #{processInstanceId,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark() != null) {
            sql.SET("remark = #{remark,jdbcType=VARCHAR}");
        }
        
        if (record.getVersion() != null) {
            sql.SET("version = #{version,jdbcType=INTEGER}");
        }
        
        if (record.getDeleted() != null) {
            sql.SET("deleted = #{deleted,jdbcType=TINYINT}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, CollectionDerateExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}