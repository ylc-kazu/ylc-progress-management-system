-- classrooms テーブル（教室マスタ）
CREATE TABLE classrooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルの論理名
COMMENT ON TABLE classrooms IS '教室マスタ';

-- カラムの論理名
COMMENT ON COLUMN classrooms.id IS '教室ID';
COMMENT ON COLUMN classrooms.name IS '教室名';
COMMENT ON COLUMN classrooms.address IS '住所';
COMMENT ON COLUMN classrooms.created_at IS '作成日時';
COMMENT ON COLUMN classrooms.updated_at IS '更新日時';