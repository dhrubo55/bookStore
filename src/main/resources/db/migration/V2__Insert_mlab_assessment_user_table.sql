--TODO: dont add users using flyway

INSERT INTO `mlab_assessment_user` (created_at,updated_at,email,full_name,password,user_name)
VALUES (CURRENT_TIMESTAMP() , current_timestamp() ,'insular55@gmail.com','Mohibul Hassan','$2a$11$Lef15NDMjV54ef6hPsI1nuU4gBlPXyI3N.gzu3qGJew0sVdR610DC', 'Mohibul'),
(CURRENT_TIMESTAMP() , current_timestamp() ,'insular55@gmail.com','Kenpachi Zaraki','$2a$11$sjEZebt/PXGen8t5tVSYgOeqVZzAlSvUx7iLzwGBP3TTpt8mF/MFC', 'kenpachi' )
