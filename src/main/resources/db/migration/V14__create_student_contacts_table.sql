-- V14: 生徒連絡先テーブルの作成（student_contacts）

CREATE TABLE student_contacts (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL,
  relation VARCHAR(50),
  name VARCHAR(255),
  phone VARCHAR(255),
  email VARCHAR(255),
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE student_contacts
    ADD CONSTRAINT fk_student_contacts_student
        FOREIGN KEY (student_id) REFERENCES students(id);

-- 論理名（COMMENT ON COLUMN）
COMMENT ON TABLE student_contacts IS '生徒連絡先';

COMMENT ON COLUMN student_contacts.id IS '生徒連絡先ID';
COMMENT ON COLUMN student_contacts.student_id IS '生徒ID（students.id）';
COMMENT ON COLUMN student_contacts.relation IS '続柄（父・母・祖父母・保護者など）';
COMMENT ON COLUMN student_contacts.name IS '保護者氏名';
COMMENT ON COLUMN student_contacts.phone IS '電話番号';
COMMENT ON COLUMN student_contacts.email IS 'メールアドレス';
COMMENT ON COLUMN student_contacts.notes IS 'メモ';
COMMENT ON COLUMN student_contacts.created_at IS '作成日時';
COMMENT ON COLUMN student_contacts.updated_at IS '更新日時';
