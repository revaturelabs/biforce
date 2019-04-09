CREATE TABLE IF NOT EXISTS caliber_address
(
  address_id DECIMAL,
  address_street VARCHAR(255),
  address_city VARCHAR(255),
  address_state VARCHAR(2),
  address_zipcode VARCHAR(15),
  address_company VARCHAR(255),
  active DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_grade
(
  grade_id DECIMAL,
  date_received VARCHAR(255)
  score DECIMAL,
  assessment_id DECIMAL,
  trainee_id DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_trainee
(
  trainee_id DECIMAL,
  trainee_email VARCHAR(255),
  trainee_name VARCHAR(255),
  training_status VARCHAR(255),
  batch_id DECIMAL,
  phone_number VARCHAR(255),
  profile_url VARCHAR(255),
  skype_id VARCHAR(255),
  resource_id VARCHAR(255),
  flag_notes VARCHAR(255),
  flag_status VARCHAR(255),
  tech_screen_score DECIMAL,
  recruiter_name VARCHAR(255),
  college VARCHAR(255),
  degree VARCHAR(255),
  major VARCHAR(255),
  tech_screener_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS red_flags_submission
(
  batch_id DECIMAL,
  borderline_grade_threshold DECIMAL,
  end_date VARCHAR(255),
  good_grade_threshold DECIMAL,
  location VARCHAR(255),
  skill_type VARCHAR(255),
  start_date VARCHAR(255),
  training_name VARCHAR(255),
  training_type VARCHAR(255),
  number_of_weeks DECIMAL,
  co_trainer_id DECIMAL,
  trainer_id DECIMAL,
  resource_id VARCHAR(255),
  addresss_id DECIMAL,
  graded_weeks DECIMAL,
  graded_weeks_exam DECIMAL,
  graded_weeks_verbal DECIMAL,
  graded_weeks_target DECIMAL,
  num_grades_missing_exam DECIMAL,
  num_grades_missing_verbal DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_batch_new
(
  batch_id DECIMAL,
  borderline_grade_threshold DECIMAL,
  end_date VARCHAR(255),
  good_grade_threshold DECIMAL,
  location VARCHAR(255),
  skill_type VARCHAR(255),
  start_date VARCHAR(255),
  training_name VARCHAR(255),
  training_type VARCHAR(255),
  number_of_weeks DECIMAL,
  co_trainer_id DECIMAL,
  trainer_id DECIMAL,
  resource_id VARCHAR(255),
  addresss_id DECIMAL,
  graded_weeks DECIMAL,
  graded_weeks_exam DECIMAL,
  graded_weeks_verbal DECIMAL,
  graded_weeks_target DECIMAL,
  num_grades_missing_exam DECIMAL,
  num_grades_missing_verbal DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_category
(
  category_id DECIMAL,
  skill_category VARCHAR(255),
  is_active DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_note
(
  note_id DECIMAL,
  note_content VARCHAR(255),
  max_visibility DECIMAL,
  is_qc_feedback DECIMAL,
  qc_status VARCHAR(255),
  note_type VARCHAR(255),
  week_number DECIMAL,
  batch_id DECIMAL,
  trainee_id DECIMAL
);

CREATE TABLE IF NOT EXISTS caliber_trainer
(
  trainer_id DECIMAL,
  email VARCHAR(255),
  name VARCHAR(255),
  tier VARCHAR(255),
  title VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS caliber_assessment
(
  assessment_id DECIMAL,
  raw_score DECIMAL,
  assessment_title VARCHAR(255),
  assessment_type VARCHAR(255),
  week_number DECIMAL,
  batch_id DECIMAL,
  assessment_category DECIMAL
);

CREATE TABLE IF NOT EXISTS normalized_one_on_ones
(
  trainer_name VARCHAR(255),
  subject VARCHAR(255),
  avg_score DECIMAL,
  std_score DECIMAL,
  normalized_score DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS percentile_range
(
  subject VARCHAR(255),
  assignment_type VARCHAR(255),
  qc_score_count DECIMAL,
  grade_avg DECIMAL,
  grade_std_pop DECIMAL,
  grade_std_samp DECIMAL
);

CREATE TABLE IF NOT EXISTS red_flags_proficiency
(
  trainer_id DECIMAL,
  trainer_name VARCHAR(255),
  subject VARCHAR(255),
  average_score DECIMAL
);


CREATE TABLE IF NOT EXISTS topic_proficiency
(
  trainer_name VARCHAR(255),
  subject VARCHAR(255),
  average_score DECIMAL
);

CREATE TABLE IF NOT EXISTS specific_weeks_submitted
(
  week_number_exam DECIMAL,
  batch_id DECIMAL,
  trainer_id DECIMAL
);