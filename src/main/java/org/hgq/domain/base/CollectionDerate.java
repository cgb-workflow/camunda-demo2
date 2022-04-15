package org.hgq.domain.base;



import java.io.Serializable;
import java.time.Instant;

/**
 * 减免申请记录表:collection_derate
 */
public class CollectionDerate extends BaseEntity implements Serializable {
    /**
     * 主键ID:id
     */
    private Long id;

    /**
     * 客户编号:user_no
     */
    private String userNo;

    /**
     * 客户id:user_id
     */
    private Long userId;

    /**
     * 产品代码:product_code
     */
    private Integer productCode;

    /**
     * 减免类型 1-确诊新冠 2-失业 3-实体或线上销量明显减少 4-因本人健康原因发生大额医疗支出 5-本人因丧失劳动能力而失去收入来源 6-自然灾害等外力因素造成大额财产损失，例：地震、洪水、台风等 :derate_type
     */
    private Integer derateType;

    /**
     * 状态 1-初审待审核 2-初审代扣失败待审核 3-复审待审核 4-复审代扣失败待审核 5-审核通过 6-审核拒绝 99-代扣中 :status
     */
    private Integer status;

    /**
     * 图片资料:images
     */
    private String images;

    /**
     * 补充图片资料:supplement_images
     */
    private String supplementImages;

    /**
     * 审核专员编号:operator_id
     */
    private Long operatorId;

    /**
     * 审批时间:audit_time
     */
    private Instant auditTime;

    /**
     * 流程实例编号:process_instance_id
     */
    private String processInstanceId;

    /**
     * 备注:remark
     */
    private String remark;

    /**
     * 版本号:version
     */
    private Integer version;

    /**
     * 删除标记:deleted
     */
    private Boolean deleted;

    /**
     * 创建时间:create_time
     */
    private Instant createTime;

    /**
     * 更新时间:update_time
     */
    private Instant updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public Integer getDerateType() {
        return derateType;
    }

    public void setDerateType(Integer derateType) {
        this.derateType = derateType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getSupplementImages() {
        return supplementImages;
    }

    public void setSupplementImages(String supplementImages) {
        this.supplementImages = supplementImages == null ? null : supplementImages.trim();
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Instant getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Instant auditTime) {
        this.auditTime = auditTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId == null ? null : processInstanceId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }
}