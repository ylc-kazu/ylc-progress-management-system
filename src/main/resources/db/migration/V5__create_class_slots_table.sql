-- class_slots テーブル（教室ごとの時間割マスタ）
CREATE TABLE class_slots (
     id BIGSERIAL PRIMARY KEY,
     classroom_id BIGINT NOT NULL,       -- 教室ID
     day_of_week VARCHAR(10) NOT NULL,   -- 曜日（Mon, Tue, Wed, Thu, Fri, Sat, Sun）
     slot_number INTEGER NOT NULL,       -- 1限〜6限
     start_time TIME NOT NULL,           -- 開始時刻
     end_time TIME NOT NULL,             -- 終了時刻
     is_available BOOLEAN NOT NULL DEFAULT TRUE,  -- 生徒が入れる枠か（昼休みは FALSE）
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);

-- テーブルの論理名
COMMENT ON TABLE class_slots IS '教室ごとの時間割マスタ';

-- カラムの論理名
COMMENT ON COLUMN class_slots.id IS '時間割ID';
COMMENT ON COLUMN class_slots.classroom_id IS '教室ID';
COMMENT ON COLUMN class_slots.day_of_week IS '曜日';
COMMENT ON COLUMN class_slots.slot_number IS '限（1〜6限）';
COMMENT ON COLUMN class_slots.start_time IS '開始時刻';
COMMENT ON COLUMN class_slots.end_time IS '終了時刻';
COMMENT ON COLUMN class_slots.is_available IS '生徒が入れる枠か';
COMMENT ON COLUMN class_slots.created_at IS '作成日時';
COMMENT ON COLUMN class_slots.updated_at IS '更新日時';
