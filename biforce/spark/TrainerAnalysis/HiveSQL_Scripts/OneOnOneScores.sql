create view One_on_One_Scores (Score, Week, Subject, Batch_ID, Trainee_ID, Trainee_Name, Trainer_Name, Batch_Name)
as select distinct caliber_grade.score, caliber_assessment.week_number, caliber_category.skill_category, caliber_note.batch_id, 
caliber_note.trainee_id, caliber_trainee.trainee_name, caliber_trainer.name as Trainer_Name, caliber_batch.training_name as Batch_Name
from caliber_grade 
inner join caliber_assessment on caliber_grade.assessment_id = caliber_assessment.assessment_id 
inner join caliber_note on caliber_grade.trainee_id = caliber_note.trainee_id
inner join caliber_trainee on caliber_grade.trainee_id = caliber_trainee.trainee_id
inner join caliber_batch on caliber_note.batch_id = caliber_batch.batch_id
inner join caliber_trainer on caliber_batch.trainer_id = caliber_trainer.trainer_id
inner join caliber_category on caliber_assessment.assessment_category = caliber_category.category_id
where (is_qc_feedback = 0 and note_type = 'TRAINEE' and assessment_type = 'Verbal')
order by caliber_assessment.week_number;

insert overwrite directory 'user/hadoop/biforce/OneonOneScores' row format delimited fields terminated by '~' select * from One_on_One_Scores;
