package com.example.campusmate.entity;

import jakarta.persistence.*; // JPA注解包
import lombok.Data;

import java.time.LocalDateTime; // Java8时间类

@Data
@Entity // JPA注解，标记为实体类
@Table(name = "activity_applications") // 指定表名
public class ActivityApplication {
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id; // 申请ID
    @Column(nullable = false) // 非空，活动ID
    private Long activityId; // 关联活动ID
    @Column(nullable = false) // 非空，申请人ID
    private Long userId; // 申请人用户ID，统一字段名
    @Column(nullable = false, columnDefinition = "TEXT") // 非空，申请理由
    private String reason; // 申请理由
    @Column(nullable = false) // 非空，申请状态
    private String status; // 申请状态（PENDING/ACCEPTED/REJECTED）
    private LocalDateTime createdAt; // 申请时间
    private LocalDateTime updatedAt; // 状态更新时间
}