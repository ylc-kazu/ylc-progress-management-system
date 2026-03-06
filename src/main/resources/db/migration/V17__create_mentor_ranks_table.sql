-- 1. ランクマスタテーブルの作成
CREATE TABLE mentor_ranks (
    id SERIAL PRIMARY KEY,
    rank_name VARCHAR(50) NOT NULL,
    training_rate INTEGER NOT NULL DEFAULT 1100,
    single_student_rate INTEGER NOT NULL,
    double_student_rate INTEGER NOT NULL,
    display_order INTEGER DEFAULT 0
);

COMMENT ON TABLE mentor_ranks IS 'メンターランク単価マスタ';
COMMENT ON COLUMN mentor_ranks.rank_name IS 'ランク名（ホワイト、ブロンズ等）';
COMMENT ON COLUMN mentor_ranks.training_rate IS '研修時の時給';
COMMENT ON COLUMN mentor_ranks.single_student_rate IS '生徒1人担当時の時給';
COMMENT ON COLUMN mentor_ranks.double_student_rate IS '生徒2人担当時の時給';

-- 2. メンターテーブルにランクIDを追加し、リレーションを張る
ALTER TABLE mentors ADD COLUMN rank_id INTEGER;
COMMENT ON COLUMN mentors.rank_id IS '所属ランクID';

-- 3. 初期データの投入（例）
INSERT INTO mentor_ranks (rank_name, training_rate, single_student_rate, double_student_rate, display_order) VALUES
                         ('ホワイト', 1100, 1300, 1500, 1),
                         ('ブロンズ', 1100, 1500, 1700, 2),
                         ('シルバー', 1100, 1600, 1800, 3),
                         ('ゴールド', 1100, 1700, 1900, 4);