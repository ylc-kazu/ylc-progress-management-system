-- student_classrooms テーブル（生徒と教室の紐付け）
CREATE TABLE student_classrooms (
    student_id BIGINT NOT NULL,
    classroom_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, classroom_id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);

-- テーブルの論理名
COMMENT ON TABLE student_classrooms IS '生徒と教室の紐付け（多対多）';

-- カラムの論理名
COMMENT ON COLUMN student_classrooms.student_id IS '生徒ID';
COMMENT ON COLUMN student_classrooms.classroom_id IS '教室ID';
