package com.ylc.progress_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {

    @Id // 💡 これが主キー（PK）であることを示します
    @Column(name = "username", nullable = false)
    private String username;          // 1. ユーザーID（ログイン時に打ち込む英数字）

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;     // 2. 所属する教室コード（★ここが大事！）

    @Column(name = "password", nullable = false)
    private String password;          // 3. パスワード

    @Column(name = "full_name")
    private String fullName;          // 4. 先生の氏名（大橋太郎 など）

    @Column(name = "role", nullable = false)
    private String role;              // 5. 権限（'STAFF' や 'ADMIN'）
}