CREATE TABLE students (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  furigana VARCHAR(255),
  birthday DATE,
  gender VARCHAR(10),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE students IS '生徒マスタ';
COMMENT ON COLUMN students.id IS '生徒ID';
COMMENT ON COLUMN students.name IS '氏名';
COMMENT ON COLUMN students.furigana IS 'ふりがな';
COMMENT ON COLUMN students.birthday IS '生年月日';
COMMENT ON COLUMN students.gender IS '性別';
COMMENT ON COLUMN students.created_at IS '作成日時';
COMMENT ON COLUMN students.updated_at IS '更新日時';