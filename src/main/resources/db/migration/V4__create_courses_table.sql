-- courses テーブル（コースマスタ）
CREATE TABLE courses (
     id BIGSERIAL PRIMARY KEY,
     code VARCHAR(50) NOT NULL UNIQUE,   -- BASIC, EP(A), EP(B), EP(T) など
     name VARCHAR(255) NOT NULL,         -- 日本語名
     is_regular BOOLEAN NOT NULL DEFAULT FALSE,  -- レギュラー枠かどうか
     description TEXT,                   -- 任意の説明
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルの論理名
COMMENT ON TABLE courses IS 'コースマスタ';

-- カラムの論理名
COMMENT ON COLUMN courses.id IS 'コースID';
COMMENT ON COLUMN courses.code IS 'コースコード';
COMMENT ON COLUMN courses.name IS 'コース名';
COMMENT ON COLUMN courses.is_regular IS 'レギュラー枠フラグ';
COMMENT ON COLUMN courses.description IS '説明';
COMMENT ON COLUMN courses.created_at IS '作成日時';
COMMENT ON COLUMN courses.updated_at IS '更新日時';
