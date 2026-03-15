-- ==========================================
-- 1. 生徒基本情報 (students)
-- ==========================================
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          student_code VARCHAR(50) UNIQUE, -- 本部コード（S26...など。上書きのキー）
                          name VARCHAR(255) NOT NULL,      -- 氏名
                          furigana VARCHAR(255),           -- ふりがな
                          status VARCHAR(20) DEFAULT 'active', -- 在籍状況
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE students IS '生徒基本マスタ';
COMMENT ON COLUMN students.student_code IS '生徒コード（本部システム連携キー）';

-- ==========================================
-- 2. 生徒詳細・CSV同期項目 (student_profiles)
-- ==========================================
CREATE TABLE student_profiles (
                                  id BIGSERIAL PRIMARY KEY,
                                  student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                                  management_id VARCHAR(50),      -- 管理ＩＤ
                                  nickname VARCHAR(100),          -- ニックネーム
                                  gender_text VARCHAR(10),        -- 性別
                                  birth_date_text VARCHAR(20),    -- 生年月日
                                  grade_text VARCHAR(50),         -- 学年
                                  postal_code VARCHAR(20),        -- 郵便番号
                                  address1 TEXT,                  -- 住所１
                                  address2 TEXT,                  -- 住所２
                                  school_name TEXT,               -- 学校名
                                  phone_mobile VARCHAR(20),       -- 携帯電話
                                  email1 VARCHAR(255),            -- メール１
                                  parent_name VARCHAR(255),       -- 保護者名
                                  emergency_phone VARCHAR(20),    -- 緊急連絡先
                                  target_campus VARCHAR(255),     -- 担当校舎
                                  remarks_general TEXT,           -- 備考
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE student_profiles IS '生徒詳細（本部CSV同期項目）';

-- ==========================================
-- 3. 手入力用：連絡先 (student_contacts)
-- ==========================================
CREATE TABLE student_contacts (
                                  id BIGSERIAL PRIMARY KEY,
                                  student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                                  relation VARCHAR(50),           -- 続柄
                                  name VARCHAR(255),              -- 名前
                                  phone VARCHAR(20),              -- 電話
                                  email VARCHAR(255),             -- メール
                                  priority INT DEFAULT 0          -- 優先順位
);
COMMENT ON TABLE student_contacts IS '生徒連絡先（複数保持用）';

-- ==========================================
-- 4. ユーザー認証 (users)
-- ==========================================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) DEFAULT 'ROLE_ADMIN',
                       enabled BOOLEAN DEFAULT TRUE
);

-- 初期管理ユーザー (パスワード: password)
INSERT INTO users (username, password) VALUES
    ('admin', '$2a$10$H/kovIUvreB.WtmzN0P7N.lkP3YXpuAxP4YYcB4P5a1NfgmQM4NZq');