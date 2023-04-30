package com.board.study.entity;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass //JPA에서 엔티티 클래스 간에 공통되는 매핑 정보를 부모 클래스에서 정의하고, 자식 클래스에서는 이를 상속받아 사용할 수 있는 어노테이션
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
	
	@CreatedDate
	private LocalDateTime registerTime;
	
	@LastModifiedDate
	private LocalDateTime updateTime;

}