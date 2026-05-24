-- 既存のテーブルを一度綺麗に削除（依存関係順）
DROP TABLE IF EXISTS student_contacts CASCADE;
DROP TABLE IF EXISTS student_profiles CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS classrooms CASCADE;

-- 1. 校舎マスタ（★新規追加：すべての中心となるマスタ）
CREATE TABLE public.classrooms (
                                   classroom_code VARCHAR(255) NOT NULL,
                                   classroom_name VARCHAR(255) NOT NULL,
                                   PRIMARY KEY (classroom_code)
);

-- 2. ユーザーマスタ（★新規追加：所属校舎カラムを上位に配置！）
CREATE TABLE public.users (
                              username VARCHAR(255) NOT NULL,
                              classroom_code VARCHAR(255) NOT NULL, -- 💡 どの校舎のスタッフか
                              password VARCHAR(255) NOT NULL,
                              full_name VARCHAR(255),
                              role VARCHAR(50) NOT NULL,            -- ADMIN, STAFF など
                              PRIMARY KEY (username),
                              FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- 3. 生徒マスタ
CREATE TABLE public.students (
                                 student_code VARCHAR(255) NOT NULL,
                                 classroom_code VARCHAR(255) NOT NULL,
                                 name VARCHAR(255),
                                 furigana VARCHAR(255),
                                 status VARCHAR(255),
                                 registration_source VARCHAR(255),
                                 PRIMARY KEY (student_code),
                                 FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- 4. 生徒プロフィール
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
                                         PRIMARY KEY (student_code),
                                         FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE
);

-- 5. 連絡先マスタ
CREATE TABLE public.student_contacts (
                                         id BIGSERIAL NOT NULL,
                                         student_code VARCHAR(255) NOT NULL,
                                         classroom_code VARCHAR(255) NOT NULL,
                                         priority INTEGER,
                                         relation VARCHAR(255),
                                         name VARCHAR(255),
                                         phone VARCHAR(255),
                                         email VARCHAR(255),
                                         PRIMARY KEY (id),
                                         FOREIGN KEY (student_code) REFERENCES public.students(student_code) ON DELETE CASCADE,
                                         FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- 6. 予約情報
CREATE TABLE public.reservations (
                                     id BIGSERIAL NOT NULL,
                                     classroom_code VARCHAR(255) NOT NULL,
                                     student_code VARCHAR(255) NOT NULL,
                                     lesson_date VARCHAR(255) NOT NULL,
                                     start_time VARCHAR(255) NOT NULL,
                                     campus VARCHAR(255),
                                     classroom VARCHAR(255),
                                     class_code VARCHAR(255),
                                     class_name VARCHAR(255),
                                     end_time VARCHAR(255),
                                     course_code VARCHAR(255),
                                     course_name VARCHAR(255),
                                     lesson VARCHAR(255),
                                     teacher VARCHAR(255),
                                     student_name VARCHAR(255),
                                     PRIMARY KEY (id),
                                     CONSTRAINT unique_student_reservation_slot UNIQUE (classroom_code, student_code, lesson_date, start_time),
                                     FOREIGN KEY (classroom_code) REFERENCES public.classrooms(classroom_code)
);

-- 💡 初期データ（マスタデータとテスト用ログインユーザー）をここに仕込んでおきます
INSERT INTO public.classrooms (classroom_code, classroom_name) VALUES ('OHASHI', '大橋校');
INSERT INTO public.classrooms (classroom_code, classroom_name) VALUES ('HAKATA', '博多校');

INSERT INTO public.users (username, classroom_code, password, full_name, role)
VALUES ('ohashi_teacher', 'OHASHI', '$2a$10$WRy3ymk0yYZSq5JrkPP2pOZB0nZeEke.UzVZYs5p8HOwtwTjvlCB.', '大橋太郎（先生）', 'STAFF');