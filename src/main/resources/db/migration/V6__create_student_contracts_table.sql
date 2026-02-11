-- student_contracts テーブル（生徒の契約情報）
CREATE TABLE student_contracts (
                                   id BIGSERIAL PRIMARY KEY,
                                   student_id BIGINT NOT NULL,       -- 生徒ID
                                   course_id BIGINT NOT NULL,        -- コースID
                                   class_slot_id BIGINT,             -- レギュラー枠の場合のみ（曜日＋時間帯）
                                   start_date DATE NOT NULL,         -- 契約開始日
                                   end_date DATE,                    -- 契約終了日（NULLなら継続中）
                                   is_active BOOLEAN NOT NULL DEFAULT TRUE,  -- 現在の契約かどうか
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (student_id) REFERENCES students(id),
                                   FOREIGN KEY (course_id) REFERENCES courses(id),
                                   FOREIGN KEY (class_slot_id) REFERENCES class_slots(id)
);

-- テーブルの論理名
COMMENT ON TABLE student_contracts IS '生徒の契約情報';

-- カラムの論理名
COMMENT ON COLUMN student_contracts.id IS '契約ID';
COMMENT ON COLUMN student_contracts.student_id IS '生徒ID';
COMMENT ON COLUMN student_contracts.course_id IS 'コースID';
COMMENT ON COLUMN student_contracts.class_slot_id IS '時間割ID（レギュラー枠のみ）';
COMMENT ON COLUMN student_contracts.start_date IS '契約開始日';
COMMENT ON COLUMN student_contracts.end_date IS '契約終了日';
COMMENT ON COLUMN student_contracts.is_active IS '契約が有効かどうか';
COMMENT ON COLUMN student_contracts.created_at IS '作成日時';
COMMENT ON COLUMN student_contracts.updated_at IS '更新日時';
