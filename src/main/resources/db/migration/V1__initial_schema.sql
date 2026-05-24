-- ==========================================
-- 1. 生徒基本情報 (students)
-- ==========================================
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          student_code VARCHAR(50) UNIQUE, -- 本部コード（S26...など。上書きのキー）
                          name VARCHAR(255) NOT NULL,      -- 氏名
                          furigana VARCHAR(255),           -- ふりがな（生徒名カナ）
                          status VARCHAR(50) DEFAULT '生徒', -- 在籍状況（CSVのステータス「生徒」等をそのまま保持）
                          registration_source VARCHAR(100) DEFAULT '手動登録', -- 登録元
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
                                  nickname VARCHAR(100),          -- 生徒ニックネーム
                                  gender_text VARCHAR(10),        -- 性別
                                  birth_date_text VARCHAR(20),    -- 生年月日
                                  grade_text VARCHAR(50),         -- 学年
                                  postal_code VARCHAR(20),        -- 郵便番号
                                  prefecture VARCHAR(50),         -- 都道府県 ★追加
                                  address1 TEXT,                  -- 住所１
                                  address2 TEXT,                  -- 住所２
                                  school_name TEXT,               -- 勤務先／学校名
                                  phone_home VARCHAR(20),         -- 自宅電話番号 ★追加
                                  phone_mobile VARCHAR(20),       -- 携帯電話
                                  email1 VARCHAR(255),            -- 連絡先メール１：メールアドレス
                                  email2 VARCHAR(255),            -- 連絡先メール２：メールアドレス ★追加
                                  parent_name VARCHAR(255),       -- 保護者名
                                  parent_name_kana VARCHAR(255),  -- 保護者名カナ ★追加
                                  relation_text VARCHAR(50),      -- 続柄 ★追加
                                  emergency_phone VARCHAR(20),    -- 緊急連絡先電話番号
                                  parent_email1 VARCHAR(255),     -- 保護者メール１：メールアドレス ★追加
                                  parent_email2 VARCHAR(255),     -- 保護者メール２：メールアドレス ★追加
                                  target_campus VARCHAR(255),     -- 担当校舎
                                  staff_code VARCHAR(50),         -- 担当スタッフコード ★追加
                                  staff_name VARCHAR(255),        -- 担当スタッフ ★追加
                                  remarks_general TEXT,           -- 備考
                                  typing_test VARCHAR(100),       -- タイピング技能検定 ★追加
                                  programming_test VARCHAR(100),  -- プログラミング能力検定 ★追加
                                  remarks_text TEXT,              -- 備考テキスト ★追加
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
INSERT INTO users (username, password, role) VALUES
    ('admin', '$2a$10$H/kovIUvreB.WtmzN0P7N.lkP3YXpuAxP4YYcB4P5a1NfgmQM4NZq', 'ROLE_ADMIN');