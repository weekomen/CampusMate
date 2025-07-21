package com.example.campusmate.entity;

import jakarta.persistence.*; // JPA注解包
import lombok.Data; // Lombok自动生成getter/setter等
import java.time.LocalDateTime; // Java8时间类

@Data // Lombok注解，自动生成常用方法
@Entity // JPA注解，标记为实体类
@Table(name = "user_info") // 指定表名
public class UserInfo {
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id; // 表主键

    @Column(nullable = false, unique = true) // 唯一且非空，App用户ID
    private Long userId; // App主用户ID

    @Column(nullable = false) // 非空，学号
    private String studentId; // 学号

    @Column(nullable = false) // 非空，学院
    private String college; // 学院

    @Column(nullable = false) // 非空，校区
    private String campus; // 校区

    private String gender; // 性别
    private String grade; // 年级
    private String major; // 专业
    private String avatarUrl; // 头像URL
    private String signature; // 个性签名
    private String interests; // 兴趣爱好（逗号分隔）
    private String skills; // 技能特长（逗号分隔）

    @Column(columnDefinition = "json") // JSON格式，信息可见性
    private String infoVisibility; // 信息可见性设置

    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}