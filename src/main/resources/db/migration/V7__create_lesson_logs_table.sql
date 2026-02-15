-- lesson_logs テーブル（レッスン記録）
CREATE TABLE lesson_logs (
                             id BIGSERIAL PRIMARY KEY,
                             lesson_date DATE NOT NULL,          -- レッスン日
                             classroom_id BIGINT NOT NULL,       -- 教室ID
                             class_slot_id BIGINT NOT NULL,      -- 時間割ID（1限〜6限）
                             student_id BIGINT NOT NULL,         -- 生徒ID
                             mentor_user_id BIGINT NOT NULL,     -- メンター（users.id）
                             summary TEXT,                       -- 学習内容の概要
                             notes TEXT,                         -- メンター用メモ
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (classroom_id) REFERENCES classrooms(id),
                             FOREIGN KEY (class_slot_id) REFERENCES class_slots(id),
                             FOREIGN KEY (student_id) REFERENCES students(id),
                             FOREIGN KEY (mentor_user_id) REFERENCES users(id)
);

-- テーブルの論理名
COMMENT ON TABLE lesson_logs IS 'レッスン記録';

-- カラムの論理名
COMMENT ON COLUMN lesson_logs.id IS 'レッスン記録ID';
COMMENT ON COLUMN lesson_logs.lesson_date IS 'レッスン日';
COMMENT ON COLUMN lesson_logs.classroom_id IS '教室ID';
COMMENT ON COLUMN lesson_logs.class_slot_id IS '時間割ID';
COMMENT ON COLUMN lesson_logs.student_id IS '生徒ID';
COMMENT ON COLUMN lesson_logs.mentor_user_id IS 'メンターID';
COMMENT ON COLUMN lesson_logs.summary IS '学習内容概要';
COMMENT ON COLUMN lesson_logs.notes IS 'メンター用メモ';
COMMENT ON COLUMN lesson_logs.created_at IS '作成日時';
COMMENT ON COLUMN lesson_logs.updated_at IS '更新日時';
