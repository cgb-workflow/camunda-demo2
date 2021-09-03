package org.hgq.mapper.base;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.hgq.domain.base.CollectionCase;
import org.hgq.domain.base.CollectionCaseExample.Criteria;
import org.hgq.domain.base.CollectionCaseExample.Criterion;
import org.hgq.domain.base.CollectionCaseExample;

public class CollectionCaseSqlProvider {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String countByExample(CollectionCaseExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("collection_case");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String deleteByExample(CollectionCaseExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("collection_case");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String insertSelective(CollectionCase record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("collection_case");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUserNo() != null) {
            sql.VALUES("user_no", "#{userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserName() != null) {
            sql.VALUES("user_name", "#{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getIdCard() != null) {
            sql.VALUES("id_card", "#{idCard,jdbcType=VARCHAR}");
        }
        
        if (record.getMobile() != null) {
            sql.VALUES("mobile", "#{mobile,jdbcType=VARCHAR}");
        }
        
        if (record.getCaseSource() != null) {
            sql.VALUES("case_source", "#{caseSource,jdbcType=TINYINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.VALUES("product_code", "#{productCode,jdbcType=INTEGER}");
        }
        
        if (record.getOverdueDays() != null) {
            sql.VALUES("overdue_days", "#{overdueDays,jdbcType=INTEGER}");
        }
        
        if (record.getDebtAmount() != null) {
            sql.VALUES("debt_amount", "#{debtAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getDeferrableAmount() != null) {
            sql.VALUES("deferrable_amount", "#{deferrableAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getLifecycle() != null) {
            sql.VALUES("lifecycle", "#{lifecycle,jdbcType=TINYINT}");
        }
        
        if (record.getAllocationTime() != null) {
            sql.VALUES("allocation_time", "#{allocationTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getGroupNo() != null) {
            sql.VALUES("group_no", "#{groupNo,jdbcType=VARCHAR}");
        }
        
        if (record.getGroupName() != null) {
            sql.VALUES("group_name", "#{groupName,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpNo() != null) {
            sql.VALUES("emp_no", "#{empNo,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpName() != null) {
            sql.VALUES("emp_name", "#{empName,jdbcType=VARCHAR}");
        }
        
        if (record.getCollectionResult() != null) {
            sql.VALUES("collection_result", "#{collectionResult,jdbcType=TINYINT}");
        }
        
        if (record.getCollectionLogTime() != null) {
            sql.VALUES("collection_log_time", "#{collectionLogTime,jdbcType=TIMESTAMP}");
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

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String selectByExample(CollectionCaseExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("user_no");
        sql.SELECT("user_name");
        sql.SELECT("id_card");
        sql.SELECT("mobile");
        sql.SELECT("case_source");
        sql.SELECT("product_code");
        sql.SELECT("overdue_days");
        sql.SELECT("debt_amount");
        sql.SELECT("deferrable_amount");
        sql.SELECT("lifecycle");
        sql.SELECT("allocation_time");
        sql.SELECT("group_no");
        sql.SELECT("group_name");
        sql.SELECT("emp_no");
        sql.SELECT("emp_name");
        sql.SELECT("collection_result");
        sql.SELECT("collection_log_time");
        sql.SELECT("process_instance_id");
        sql.SELECT("remark");
        sql.SELECT("version");
        sql.SELECT("deleted");
        sql.SELECT("create_time");
        sql.SELECT("update_time");
        sql.FROM("collection_case");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        CollectionCase record = (CollectionCase) parameter.get("record");
        CollectionCaseExample example = (CollectionCaseExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("collection_case");
        
        if (record.getId() != null) {
            sql.SET("id = #{record.id,jdbcType=BIGINT}");
        }
        
        if (record.getUserNo() != null) {
            sql.SET("user_no = #{record.userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserName() != null) {
            sql.SET("user_name = #{record.userName,jdbcType=VARCHAR}");
        }
        
        if (record.getIdCard() != null) {
            sql.SET("id_card = #{record.idCard,jdbcType=VARCHAR}");
        }
        
        if (record.getMobile() != null) {
            sql.SET("mobile = #{record.mobile,jdbcType=VARCHAR}");
        }
        
        if (record.getCaseSource() != null) {
            sql.SET("case_source = #{record.caseSource,jdbcType=TINYINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.SET("product_code = #{record.productCode,jdbcType=INTEGER}");
        }
        
        if (record.getOverdueDays() != null) {
            sql.SET("overdue_days = #{record.overdueDays,jdbcType=INTEGER}");
        }
        
        if (record.getDebtAmount() != null) {
            sql.SET("debt_amount = #{record.debtAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getDeferrableAmount() != null) {
            sql.SET("deferrable_amount = #{record.deferrableAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getLifecycle() != null) {
            sql.SET("lifecycle = #{record.lifecycle,jdbcType=TINYINT}");
        }
        
        if (record.getAllocationTime() != null) {
            sql.SET("allocation_time = #{record.allocationTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getGroupNo() != null) {
            sql.SET("group_no = #{record.groupNo,jdbcType=VARCHAR}");
        }
        
        if (record.getGroupName() != null) {
            sql.SET("group_name = #{record.groupName,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpNo() != null) {
            sql.SET("emp_no = #{record.empNo,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpName() != null) {
            sql.SET("emp_name = #{record.empName,jdbcType=VARCHAR}");
        }
        
        if (record.getCollectionResult() != null) {
            sql.SET("collection_result = #{record.collectionResult,jdbcType=TINYINT}");
        }
        
        if (record.getCollectionLogTime() != null) {
            sql.SET("collection_log_time = #{record.collectionLogTime,jdbcType=TIMESTAMP}");
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

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("collection_case");
        
        sql.SET("id = #{record.id,jdbcType=BIGINT}");
        sql.SET("user_no = #{record.userNo,jdbcType=VARCHAR}");
        sql.SET("user_name = #{record.userName,jdbcType=VARCHAR}");
        sql.SET("id_card = #{record.idCard,jdbcType=VARCHAR}");
        sql.SET("mobile = #{record.mobile,jdbcType=VARCHAR}");
        sql.SET("case_source = #{record.caseSource,jdbcType=TINYINT}");
        sql.SET("product_code = #{record.productCode,jdbcType=INTEGER}");
        sql.SET("overdue_days = #{record.overdueDays,jdbcType=INTEGER}");
        sql.SET("debt_amount = #{record.debtAmount,jdbcType=DECIMAL}");
        sql.SET("deferrable_amount = #{record.deferrableAmount,jdbcType=DECIMAL}");
        sql.SET("lifecycle = #{record.lifecycle,jdbcType=TINYINT}");
        sql.SET("allocation_time = #{record.allocationTime,jdbcType=TIMESTAMP}");
        sql.SET("group_no = #{record.groupNo,jdbcType=VARCHAR}");
        sql.SET("group_name = #{record.groupName,jdbcType=VARCHAR}");
        sql.SET("emp_no = #{record.empNo,jdbcType=VARCHAR}");
        sql.SET("emp_name = #{record.empName,jdbcType=VARCHAR}");
        sql.SET("collection_result = #{record.collectionResult,jdbcType=TINYINT}");
        sql.SET("collection_log_time = #{record.collectionLogTime,jdbcType=TIMESTAMP}");
        sql.SET("process_instance_id = #{record.processInstanceId,jdbcType=VARCHAR}");
        sql.SET("remark = #{record.remark,jdbcType=VARCHAR}");
        sql.SET("version = #{record.version,jdbcType=INTEGER}");
        sql.SET("deleted = #{record.deleted,jdbcType=TINYINT}");
        sql.SET("create_time = #{record.createTime,jdbcType=TIMESTAMP}");
        sql.SET("update_time = #{record.updateTime,jdbcType=TIMESTAMP}");
        
        CollectionCaseExample example = (CollectionCaseExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    public String updateByPrimaryKeySelective(CollectionCase record) {
        SQL sql = new SQL();
        sql.UPDATE("collection_case");
        
        if (record.getUserNo() != null) {
            sql.SET("user_no = #{userNo,jdbcType=VARCHAR}");
        }
        
        if (record.getUserName() != null) {
            sql.SET("user_name = #{userName,jdbcType=VARCHAR}");
        }
        
        if (record.getIdCard() != null) {
            sql.SET("id_card = #{idCard,jdbcType=VARCHAR}");
        }
        
        if (record.getMobile() != null) {
            sql.SET("mobile = #{mobile,jdbcType=VARCHAR}");
        }
        
        if (record.getCaseSource() != null) {
            sql.SET("case_source = #{caseSource,jdbcType=TINYINT}");
        }
        
        if (record.getProductCode() != null) {
            sql.SET("product_code = #{productCode,jdbcType=INTEGER}");
        }
        
        if (record.getOverdueDays() != null) {
            sql.SET("overdue_days = #{overdueDays,jdbcType=INTEGER}");
        }
        
        if (record.getDebtAmount() != null) {
            sql.SET("debt_amount = #{debtAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getDeferrableAmount() != null) {
            sql.SET("deferrable_amount = #{deferrableAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getLifecycle() != null) {
            sql.SET("lifecycle = #{lifecycle,jdbcType=TINYINT}");
        }
        
        if (record.getAllocationTime() != null) {
            sql.SET("allocation_time = #{allocationTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getGroupNo() != null) {
            sql.SET("group_no = #{groupNo,jdbcType=VARCHAR}");
        }
        
        if (record.getGroupName() != null) {
            sql.SET("group_name = #{groupName,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpNo() != null) {
            sql.SET("emp_no = #{empNo,jdbcType=VARCHAR}");
        }
        
        if (record.getEmpName() != null) {
            sql.SET("emp_name = #{empName,jdbcType=VARCHAR}");
        }
        
        if (record.getCollectionResult() != null) {
            sql.SET("collection_result = #{collectionResult,jdbcType=TINYINT}");
        }
        
        if (record.getCollectionLogTime() != null) {
            sql.SET("collection_log_time = #{collectionLogTime,jdbcType=TIMESTAMP}");
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

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection_case
     *
     * @mbg.generated Thu Aug 19 11:25:54 CST 2021
     */
    protected void applyWhere(SQL sql, CollectionCaseExample example, boolean includeExamplePhrase) {
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