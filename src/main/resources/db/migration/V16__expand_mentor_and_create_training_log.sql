-- 1. メンターテーブルに基本情報と給与・交通費関連を追加
ALTER TABLE mentors ADD COLUMN email VARCHAR(255);
ALTER TABLE mentors ADD COLUMN phone_number VARCHAR(20);
ALTER TABLE mentors ADD COLUMN birthday DATE;
ALTER TABLE mentors ADD COLUMN nearest_station VARCHAR(100);
ALTER TABLE mentors ADD COLUMN transportation_fee INTEGER;
ALTER TABLE mentors ADD COLUMN hourly_rate INTEGER DEFAULT 1100;
ALTER TABLE mentors ADD COLUMN note TEXT;

-- カラムへのコメント（論理名）追加
COMMENT ON TABLE mentors IS 'メンターマスタ';
COMMENT ON COLUMN mentors.id IS 'メンターID';
COMMENT ON COLUMN mentors.name IS '氏名';
COMMENT ON COLUMN mentors.email IS 'メールアドレス';
COMMENT ON COLUMN mentors.phone_number IS '電話番号';
COMMENT ON COLUMN mentors.birthday IS '生年月日';
COMMENT ON COLUMN mentors.nearest_station IS '最寄り駅';
COMMENT ON COLUMN mentors.transportation_fee IS '交通費(往復)';
COMMENT ON COLUMN mentors.hourly_rate IS '基本時給';
COMMENT ON COLUMN mentors.completed_curriculum_id IS '最終研修完了カリキュラムID';
COMMENT ON COLUMN mentors.active IS '有効フラグ(稼働中か)';
COMMENT ON COLUMN mentors.note IS '特記事項・相性など';

-- 2. 研修進捗を記録するログテーブルを作成
CREATE TABLE mentor_training_logs (
    id SERIAL PRIMARY KEY,
    mentor_id BIGINT NOT NULL,
    curriculum_id VARCHAR(255) NOT NULL,
    completed_date DATE NOT NULL,
    trainer_name VARCHAR(100),
    CONSTRAINT fk_mentor FOREIGN KEY(mentor_id) REFERENCES mentors(id),
    CONSTRAINT fk_curriculum FOREIGN KEY(curriculum_id) REFERENCES curriculums(id)
);

-- テーブル・カラムへのコメント（論理名）追加
COMMENT ON TABLE mentor_training_logs IS 'メンター研修進捗ログ';
COMMENT ON COLUMN mentor_training_logs.id IS 'ログID';
COMMENT ON COLUMN mentor_training_logs.mentor_id IS 'メンターID';
COMMENT ON COLUMN mentor_training_logs.curriculum_id IS 'カリキュラムID';
COMMENT ON COLUMN mentor_training_logs.completed_date IS '実施日/完了日';
COMMENT ON COLUMN mentor_training_logs.trainer_name IS '研修担当者名';