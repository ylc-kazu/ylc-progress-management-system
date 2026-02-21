-- users テーブル（ログイン情報）
CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   role VARCHAR(50) NOT NULL,
   enabled BOOLEAN NOT NULL DEFAULT TRUE,
   locked BOOLEAN NOT NULL DEFAULT FALSE,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- テーブルの論理名
COMMENT ON TABLE users IS 'ユーザー（ログイン情報）';

-- カラムの論理名
COMMENT ON COLUMN users.id IS 'ユーザーID';
COMMENT ON COLUMN users.username IS 'ログインID';
COMMENT ON COLUMN users.password IS 'パスワード（暗号化）';
COMMENT ON COLUMN users.role IS 'ロール（権限）';
COMMENT ON COLUMN users.enabled IS '有効フラグ';
COMMENT ON COLUMN users.locked IS 'ロックフラグ';
COMMENT ON COLUMN users.created_at IS '作成日時';
COMMENT ON COLUMN users.updated_at IS '更新日時';
