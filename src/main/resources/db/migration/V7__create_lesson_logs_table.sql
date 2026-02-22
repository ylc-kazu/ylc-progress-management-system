CREATE TABLE lesson_logs (
 id BIGSERIAL PRIMARY KEY,

-- 生徒ID
 student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,

-- 教室ID
 classroom_id BIGINT NOT NULL REFERENCES classrooms(id),

-- コースID
 course_id BIGINT NOT NULL REFERENCES courses(id),

-- コマID（曜日・時間帯）
 class_slot_id BIGINT NOT NULL REFERENCES class_slots(id),

-- 契約ID（任意）
 contract_id BIGINT REFERENCES student_contracts(id),

-- レッスン実施日
 lesson_date DATE NOT NULL,

-- メンターのメモ
 mentor_note TEXT,

 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルコメント
COMMENT ON TABLE lesson_logs IS 'レッスン実施ログ';

-- カラムコメント
COMMENT ON COLUMN lesson_logs.id IS '主キー';
COMMENT ON COLUMN lesson_logs.student_id IS '生徒ID（students.id）';
COMMENT ON COLUMN lesson_logs.classroom_id IS '教室ID（classrooms.id）';
COMMENT ON COLUMN lesson_logs.course_id IS 'コースID（courses.id）';
COMMENT ON COLUMN lesson_logs.class_slot_id IS 'コマID（class_slots.id）';
COMMENT ON COLUMN lesson_logs.contract_id IS '契約ID（student_contracts.id）';
COMMENT ON COLUMN lesson_logs.lesson_date IS 'レッスン実施日';
COMMENT ON COLUMN lesson_logs.mentor_note IS 'メンターのメモ';
COMMENT ON COLUMN lesson_logs.created_at IS '作成日時';
COMMENT ON COLUMN lesson_logs.updated_at IS '更新日時';