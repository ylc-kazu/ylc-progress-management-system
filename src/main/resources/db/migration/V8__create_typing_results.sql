CREATE TABLE typing_results (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    typing_service VARCHAR(20) NOT NULL CHECK (typing_service IN ('benesse', 'etyping')),
    grade SMALLINT NOT NULL CHECK (grade BETWEEN 1 AND 12),
    input_chars INTEGER NOT NULL,
    correct_rate NUMERIC(5,2) NOT NULL,
    miss_count INTEGER NOT NULL,
    score INTEGER,
    rank VARCHAR(10),
    clear_flag BOOLEAN DEFAULT FALSE,
    clear_count SMALLINT DEFAULT 0,
    executed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルコメント
COMMENT ON TABLE typing_results IS 'タイピング結果（12〜1級の共通履歴）';

-- カラムコメント
COMMENT ON COLUMN typing_results.id IS '主キー';
COMMENT ON COLUMN typing_results.student_id IS '生徒ID（students.id）';
COMMENT ON COLUMN typing_results.typing_service IS 'タイピングサービス区分（benesse / etyping）';
COMMENT ON COLUMN typing_results.grade IS '実施した級（12〜1）';
COMMENT ON COLUMN typing_results.input_chars IS '入力文字数（3分間）';
COMMENT ON COLUMN typing_results.correct_rate IS '正タイプ率（%）';
COMMENT ON COLUMN typing_results.miss_count IS '誤タイプ数';
COMMENT ON COLUMN typing_results.score IS 'イータイピングのスコア（12〜10級はNULL）';
COMMENT ON COLUMN typing_results.rank IS 'イータイピングのランク（A〜Eなど）';
COMMENT ON COLUMN typing_results.clear_flag IS 'ベネッセのクリア判定（9〜1級はNULL）';
COMMENT ON COLUMN typing_results.clear_count IS 'ベネッセのクリア回数（1〜3）';
COMMENT ON COLUMN typing_results.executed_at IS '実施日時';
COMMENT ON COLUMN typing_results.created_at IS '作成日時';
COMMENT ON COLUMN typing_results.updated_at IS '更新日時';
