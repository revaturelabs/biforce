create view Topic_Proficiency (QC_Score, Week, Subject, Assignment_Type, Batch_ID, Trainee_ID, Trainee_Name, Trainer_ID, Trainer_Name, Batch_Name)
as select distinct caliber_note.qc_status, caliber_assessment.week_number, caliber_category.skill_category, caliber_assessment.assessment_type, caliber_note.batch_id,
caliber_note.trainee_id, caliber_trainee.trainee_name, caliber_batch.trainer_id, caliber_trainer.name as Trainer_Name, caliber_batch.training_name as Batch_Name
from caliber_grade
inner join caliber_assessment on caliber_grade.assessment_id = caliber_assessment.assessment_id
inner join caliber_note on caliber_grade.trainee_id = caliber_note.trainee_id
inner join caliber_trainee on caliber_grade.trainee_id = caliber_trainee.trainee_id
inner join caliber_batch on caliber_note.batch_id = caliber_batch.batch_id
inner join caliber_trainer on caliber_batch.trainer_id = caliber_trainer.trainer_id
inner join caliber_category on caliber_assessment.assessment_category = caliber_category.category_id
where (is_qc_feedback = 1 and note_type = 'QC_TRAINEE' and assessment_type != 'Other' and (qc_status != null or qc_status != 'Undefined'))
order by caliber_assessment.week_number;

create table score_weights (score string, weight double) row format delimited;

insert into score_weights values ('Poor', 10), ('Average', 50), ('Good', 70), ('Superstar', 100), ('null', null);

create view weighted_qc_scores (QC_Score, Weighted_Score, Week, Subject, Assignment_Type, Trainee_ID, Trainee_Name, Trainer_ID, Trainer_Name, Batch_Name)
as select topic_proficiency.qc_score, score_weights.weight, topic_proficiency.week, topic_proficiency.subject, topic_proficiency.assignment_type,
topic_proficiency.trainee_id, topic_proficiency.trainee_name, topic_proficiency.trainer_id, topic_proficiency.trainer_name, topic_proficiency.batch_name
from topic_proficiency inner join score_weights on score_weights.score = topic_proficiency.qc_score;

insert overwrite directory 'user/hadoop/biforce/Topic_Proficiency' row format delimited fields terminated by '~' select * from weighted_qc_scores;
