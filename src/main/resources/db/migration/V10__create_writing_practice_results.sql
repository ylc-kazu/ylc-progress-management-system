CREATE TABLE writing_practice_results (
  id BIGSERIAL PRIMARY KEY,

-- 親テーブル（1回の書き取り実施）
  session_id BIGINT NOT NULL REFERENCES writing_sessions(id) ON DELETE CASCADE,

-- STEP番号（1〜3）
  step SMALLINT NOT NULL CHECK (step BETWEEN 1 AND 3),

-- 正答率（%）
  correct_rate NUMERIC(5,2),

-- 誤答数
  wrong_count INTEGER,

-- 誤答内容
  wrong_details TEXT,

-- テスト時のみ使用（練習は NULL）
  pass_flag BOOLEAN,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルコメント
COMMENT ON TABLE writing_practice_results IS '書き取りのSTEPごとの結果（STEP1〜3）';

-- カラムコメント
COMMENT ON COLUMN writing_practice_results.id IS '主キー';
COMMENT ON COLUMN writing_practice_results.session_id IS '書き取り実施ID（writing_sessions.id）';
COMMENT ON COLUMN writing_practice_results.step IS 'STEP番号（1〜3）';
COMMENT ON COLUMN writing_practice_results.correct_rate IS '正答率（%）';
COMMENT ON COLUMN writing_practice_results.wrong_count IS '誤答数';
COMMENT ON COLUMN writing_practice_results.wrong_details IS '誤答内容';
COMMENT ON COLUMN writing_practice_results.pass_flag IS '合否（テスト時のみ）';
COMMENT ON COLUMN writing_practice_results.created_at IS '作成日時';
COMMENT ON COLUMN writing_practice_results.updated_at IS '更新日時';
