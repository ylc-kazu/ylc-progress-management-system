-- =========================================================================
-- 0. 既存のテーブルを一度綺麗に削除（依存関係の逆順で安全に削除）
-- =========================================================================
DROP TABLE IF EXISTS mentor_classroom_relations CASCADE;
DROP TABLE IF EXISTS lesson_histories CASCADE;
DROP TABLE IF EXISTS student_contacts CASCADE;
DROP TABLE IF EXISTS student_profiles CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS mentors CASCADE;
DROP TABLE IF EXISTS classrooms CASCADE;

-- =========================================================================
-- 1. 校舎（教室）マスタ
-- =========================================================================
CREATE TABLE public.classrooms (
                                   classroom_code VARCHAR(50) NOT NULL,
                                   classroom_name VARCHAR(255) NOT NULL,
                                   PRIMARY KEY (classroom_code)
);

-- =========================================================================
-- 2. ユーザーマスタ（校舎のスタッフ・管理者）
-- =========================================================================
CREATE TABLE public.users (
                              username VARCHAR(255) NOT NULL,
                              classroom_code VARCHAR(50) NOT NULL,
                              password VARCHAR(255) NOT NULL,
                              full_name VARCHAR(255),
                              role VARCHAR(50) NOT NULL,            -- ADMIN, STAFF など
                              PRIMARY KEY (username),
                              FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- =========================================================================
-- 3. メンター（講師）マスタ（★既存の Mentor.java の項目を100%網羅！）
-- =========================================================================
CREATE TABLE public.mentors (
                                id SERIAL PRIMARY KEY,
                                mentor_code VARCHAR(50) UNIQUE NOT NULL,  -- メンター識別コード
                                name VARCHAR(100) NOT NULL,               -- 氏名
                                email VARCHAR(255),
                                phone_number VARCHAR(50),
                                birthday DATE,                            -- 誕生日
                                nearest_station VARCHAR(255),             -- 最寄り駅
                                transportation_fee INTEGER,               -- 交通費
                                hourly_rate INTEGER,                      -- 時給
                                completed_curriculum_id VARCHAR(255),    -- 状態・研修（修了カリキュラム）
                                active BOOLEAN DEFAULT true,              -- 稼働状態
                                note TEXT,                                -- メモ・備考
                                rank_id INTEGER                           -- メンターランク（紐付け用）
);

-- 検索高速化インデックス
CREATE INDEX IF NOT EXISTS idx_mentors_code ON public.mentors(mentor_code);

-- =========================================================================
-- 4. メンター教室所属・ヘルプ管理テーブル（中間テーブル）
-- =========================================================================
CREATE TABLE public.mentor_classroom_relations (
                                                   relation_id SERIAL PRIMARY KEY,
                                                   mentor_code VARCHAR(50) NOT NULL,
                                                   classroom_code VARCHAR(50) NOT NULL,
                                                   role_type VARCHAR(20) DEFAULT 'HELP',     -- 'MAIN'（本所属） または 'HELP'（ヘルプ）
                                                   UNIQUE(mentor_code, classroom_code),
                                                   FOREIGN KEY (mentor_code) REFERENCES public.mentors(mentor_code) ON DELETE CASCADE,
                                                   FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_mentor_classroom_search
    ON public.mentor_classroom_relations (classroom_code, role_type);

-- =========================================================================
-- 5. 生徒マスタ
-- =========================================================================
CREATE TABLE public.students (
                                 student_code VARCHAR(255) NOT NULL,
                                 classroom_code VARCHAR(50) NOT NULL,
                                 name VARCHAR(255),
                                 furigana VARCHAR(255),
                                 status VARCHAR(255),                      -- "ChatGPT NG" などもここに入る
                                 registration_source VARCHAR(255),
                                 PRIMARY KEY (student_code),
                                 FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- =========================================================================
-- 6. 生徒プロフィール（★スプレッドシートの手入力項目・進捗項目を完全統合！）
-- =========================================================================
CREATE TABLE public.student_profiles (
                                         student_code VARCHAR(255) NOT NULL,
                                         management_id VARCHAR(255),
                                         nickname VARCHAR(255),
                                         gender_text VARCHAR(255),
                                         birth_date_text VARCHAR(255),
                                         grade_text VARCHAR(255),
                                         postal_code VARCHAR(255),
                                         prefecture VARCHAR(255),
                                         address1 VARCHAR(255),
                                         address2 VARCHAR(255),
                                         school_name VARCHAR(255),
                                         phone_home VARCHAR(255),
                                         phone_mobile VARCHAR(255),
                                         email1 VARCHAR(255),
                                         email2 VARCHAR(255),
                                         parent_name VARCHAR(255),
                                         parent_name_kana VARCHAR(255),
                                         relation_text VARCHAR(255),
                                         emergency_phone VARCHAR(255),
                                         parent_email1 VARCHAR(255),
                                         parent_email2 VARCHAR(255),
                                         target_campus VARCHAR(255),
                                         staff_code VARCHAR(255),
                                         staff_name VARCHAR(255),
                                         remarks_general TEXT,
                                         typing_test VARCHAR(255),
                                         programming_test VARCHAR(255),
                                         remarks_text TEXT,

    -- 💡 以下、スプレッドシート連携・手入力用拡張カラム
                                         course_type VARCHAR(50),                  -- コース区分（4回レギュラー/2回など）
                                         lesson_day VARCHAR(20),                   -- 受講曜日（月〜日）
                                         lesson_slot VARCHAR(20),                  -- 受講時間枠（1限、2限など）
                                         current_text VARCHAR(255),                -- 現在のテキスト名
                                         current_chapter VARCHAR(50),              -- 現在の章
                                         current_page VARCHAR(50),                 -- 現在のページ
                                         progress_status VARCHAR(50),              -- 進捗状況

                                         PRIMARY KEY (student_code),
                                         FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE
);

-- =========================================================================
-- 7. 連絡先マスタ
-- =========================================================================
CREATE TABLE public.student_contacts (
                                         id BIGSERIAL NOT NULL,
                                         student_code VARCHAR(255) NOT NULL,
                                         classroom_code VARCHAR(50) NOT NULL,
                                         priority INTEGER,
                                         relation VARCHAR(255),
                                         name VARCHAR(255),
                                         phone VARCHAR(255),
                                         email VARCHAR(255),
                                         PRIMARY KEY (id),
                                         FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE,
                                         FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- =========================================================================
-- 8. 予約・割当詳細テーブル（★日付・時間を安全な型に最適化 ＆ メンター割当項目を完全統合）
-- =========================================================================
CREATE TABLE public.reservations (
                                     id BIGSERIAL NOT NULL,
                                     classroom_code VARCHAR(50) NOT NULL,
                                     student_code VARCHAR(255) NOT NULL,
                                     lesson_date DATE NOT NULL,                -- 💡 将来の検索・ソートのために DATE型 に最適化
                                     start_time TIME NOT NULL,                 -- 💡 TIME型 に最適化
                                     end_time TIME,                            -- 💡 TIME型 に最適化
                                     campus VARCHAR(255),
                                     classroom VARCHAR(255),
                                     class_code VARCHAR(255),
                                     class_name VARCHAR(255),
                                     course_code VARCHAR(255),
                                     course_name VARCHAR(255),
                                     lesson VARCHAR(255),
                                     teacher VARCHAR(255),
                                     student_name VARCHAR(255),

    -- 💡 以下、画面での割り当て・進捗入力用拡張カラム
                                     mentor_code VARCHAR(50),                  -- 割り当てられたメンターコード
                                     progress_memo TEXT,                       -- 今日の進捗メモ（割当詳細シート）
                                     homework TEXT,                            -- 宿題
                                     next_lesson_memo TEXT,                    -- 次回やること
                                     is_completed BOOLEAN DEFAULT false,       -- 授業終了フラグ

                                     PRIMARY KEY (id),
                                     CONSTRAINT unique_student_reservation_slot UNIQUE (classroom_code, student_code, lesson_date, start_time),
                                     FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code),
                                     FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE
);

-- 差分マージ超高速化のための複合インデックス
CREATE INDEX IF NOT EXISTS idx_reservations_merge_check
    ON public.reservations (lesson_date, start_time, student_code);

-- =========================================================================
-- 9. 授業割当サマリー（履歴）テーブル
-- =========================================================================
CREATE TABLE public.lesson_histories (
                                         history_id SERIAL PRIMARY KEY,
                                         lesson_date DATE NOT NULL,
                                         start_time TIME NOT NULL,
                                         student_code VARCHAR(50) NOT NULL,
                                         student_name VARCHAR(100) NOT NULL,
                                         mentor_code VARCHAR(50) NOT NULL,
                                         mentor_name VARCHAR(100) NOT NULL,
                                         course_name VARCHAR(100),
                                         text_name VARCHAR(255),
                                         chapter_page VARCHAR(100),
                                         progress_memo TEXT,
                                         homework TEXT,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE
);

-- 過去の担当被り検索を高速化するインデックス
CREATE INDEX IF NOT EXISTS idx_lesson_histories_search
    ON public.lesson_histories (student_code, lesson_date DESC);

-- =========================================================================
-- 10. 初期データ（マスタデータとテスト用ログインユーザー）
-- =========================================================================
INSERT INTO public.classrooms (classroom_code, classroom_name) VALUES ('OHASHI', '大橋校');
INSERT INTO public.classrooms (classroom_code, classroom_name) VALUES ('HAKATA', '博多校');

INSERT INTO public.users (username, classroom_code, password, full_name, role)
VALUES ('ohashi_teacher', 'OHASHI', '$2a$10$WRy3ymk0yYZSq5JrkPP2pOZB0nZeEke.UzVZYs5p8HOwtwTjvlCB.', '大橋太郎（先生）', 'STAFF');