CREATE TABLE writing_sessions (
  id BIGSERIAL PRIMARY KEY,

-- 生徒ID
  student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,

-- practice（練習） or test（テスト）
  session_type VARCHAR(20) NOT NULL CHECK (session_type IN ('practice', 'test')),

-- ローマ字マスターのテストかどうか（12〜10級は false）
  is_master_test BOOLEAN DEFAULT FALSE,

-- メンターのメモ
  mentor_note TEXT,

-- 実施日時
  executed_at TIMESTAMP NOT NULL,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルコメント
COMMENT ON TABLE writing_sessions IS '書き取りの実施（1回のレッスン単位）';

-- カラムコメント
COMMENT ON COLUMN writing_sessions.id IS '主キー';
COMMENT ON COLUMN writing_sessions.student_id IS '生徒ID（students.id）';
COMMENT ON COLUMN writing_sessions.session_type IS '練習 or テスト';
COMMENT ON COLUMN writing_sessions.is_master_test IS 'ローマ字マスターのテストかどうか';
COMMENT ON COLUMN writing_sessions.mentor_note IS 'メンターのメモ';
COMMENT ON COLUMN writing_sessions.executed_at IS '実施日時';
COMMENT ON COLUMN writing_sessions.created_at IS '作成日時';
COMMENT ON COLUMN writing_sessions.updated_at IS '更新日時';
