-- 1. カリキュラムマスタ
CREATE TABLE curriculums (
    id VARCHAR(20) PRIMARY KEY,
    textbook_name VARCHAR(100) NOT NULL,
    chapter VARCHAR(100),
    item_name VARCHAR(150) NOT NULL,
    page_number INTEGER,
    ai_description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE curriculums IS 'カリキュラムマスタ：教材の各ステップ詳細を管理';

-- 2. 授業ログ（既存の構成 + AI連携用カラム）
-- 一旦 DROP してから作り直す（V7の中身として）
DROP TABLE IF EXISTS lesson_logs CASCADE;

CREATE TABLE lesson_logs (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    classroom_id BIGINT NOT NULL REFERENCES classrooms(id),
    course_id BIGINT NOT NULL REFERENCES courses(id),
    class_slot_id BIGINT NOT NULL REFERENCES class_slots(id),
    contract_id BIGINT REFERENCES student_contracts(id),
    lesson_date DATE NOT NULL,

    -- 今回追加する重要カラム
    start_item_id VARCHAR(20) REFERENCES curriculums(id),
    end_item_id VARCHAR(20) REFERENCES curriculums(id),
    next_start_id VARCHAR(20) REFERENCES curriculums(id),
    next_memo TEXT,
    mentor_note TEXT, -- 指導用メモ
    generated_report TEXT, -- 保護者共有用AI文章

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE lesson_logs IS 'レッスン実施ログ（AI連携対応版）';
COMMENT ON COLUMN lesson_logs.start_item_id IS '本日の開始項目の管理ID';
COMMENT ON COLUMN lesson_logs.end_item_id IS '本日の終了項目の管理ID';
COMMENT ON COLUMN lesson_logs.next_start_id IS '次回の開始予定項目の管理ID';

-- インデックス
CREATE INDEX idx_curriculum_lookup ON curriculums (textbook_name, page_number);
CREATE INDEX idx_lesson_logs_lookup ON lesson_logs (student_id, lesson_date DESC);