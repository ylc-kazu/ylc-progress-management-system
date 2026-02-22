-- V11: 書き取り練習テーブルの作成（writing_practices）

CREATE TABLE writing_practices (
   id BIGSERIAL PRIMARY KEY,
   hiragana VARCHAR(10) NOT NULL,
   romaji_list TEXT NOT NULL,
   step INTEGER NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE writing_practices IS '書き取り練習データ';
COMMENT ON COLUMN writing_practices.id IS 'ID';
COMMENT ON COLUMN writing_practices.hiragana IS 'ひらがな';
COMMENT ON COLUMN writing_practices.romaji_list IS 'ローマ字（カンマ区切り）';
COMMENT ON COLUMN writing_practices.step IS '学習ステップ番号';
COMMENT ON COLUMN writing_practices.created_at IS '作成日時';
COMMENT ON COLUMN writing_practices.updated_at IS '更新日時';