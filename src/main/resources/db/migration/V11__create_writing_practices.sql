CREATE TABLE writing_practices (
   id BIGSERIAL PRIMARY KEY,

-- ひらがな（例：し、しゃ、ぴょ）
   hiragana VARCHAR(10) NOT NULL,

-- 正解ローマ字（複数表記をカンマ区切りで保持）
   romaji_list TEXT NOT NULL,

-- STEP番号（1〜3）
   step SMALLINT NOT NULL CHECK (step BETWEEN 1 AND 3),

-- 使用中フラグ
   is_active BOOLEAN DEFAULT TRUE,

   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルコメント
COMMENT ON TABLE writing_practices IS 'ひらがな→ローマ字書き取りのお題マスタ';

-- カラムコメント
COMMENT ON COLUMN writing_practices.id IS '主キー';
COMMENT ON COLUMN writing_practices.hiragana IS 'ひらがな（例：しゃ、ぴょ）';
COMMENT ON COLUMN writing_practices.romaji_list IS '正解ローマ字（複数表記をカンマ区切りで保持）';
COMMENT ON COLUMN writing_practices.step IS 'STEP番号（1〜3）';
COMMENT ON COLUMN writing_practices.is_active IS '使用中フラグ';
COMMENT ON COLUMN writing_practices.created_at IS '作成日時';
COMMENT ON COLUMN writing_practices.updated_at IS '更新日時';
