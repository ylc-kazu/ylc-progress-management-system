-- V13: 生徒プロフィールテーブルの作成（student_profiles）

CREATE TABLE student_profiles (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL UNIQUE,
  birth_date DATE,
  school_name VARCHAR(255),
  grade INTEGER,
  google_drive_url TEXT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE student_profiles
    ADD CONSTRAINT fk_student_profiles_student
        FOREIGN KEY (student_id) REFERENCES students(id);

-- 論理名（COMMENT ON COLUMN）
COMMENT ON TABLE student_profiles IS '生徒プロフィール';

COMMENT ON COLUMN student_profiles.id IS '生徒プロフィールID';
COMMENT ON COLUMN student_profiles.student_id IS '生徒ID（students.id）';
COMMENT ON COLUMN student_profiles.birth_date IS '生年月日';
COMMENT ON COLUMN student_profiles.school_name IS '学校名';
COMMENT ON COLUMN student_profiles.grade IS '学年';
COMMENT ON COLUMN student_profiles.google_drive_url IS 'Googleドライブ共有URL（保護者シート）';
COMMENT ON COLUMN student_profiles.notes IS '注意事項・メモ';
COMMENT ON COLUMN student_profiles.created_at IS '作成日時';
COMMENT ON COLUMN student_profiles.updated_at IS '更新日時';
