package com.vitcheu.common.persistence.auditing;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.*;

import com.vitcheu.common.persistence.model.BaseEntity;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "created_date", nullable = true, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  Date createdDate;

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  String createdBy;

  @Column(name = "last_modified_date", nullable = true, updatable = true)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  Date lastModifiedDate;

  @LastModifiedBy
  @Column(name = "last_modified_by", nullable = false, updatable = true)
  String lastModifiedBy;
}
