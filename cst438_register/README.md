## CST438 Software Engineering California State University Monterey Bay
## Registration service project
### Users can view their course schedule and add/drop courses 

### REST apis  used by front end 

#### GET /schedule?year={year}&semester={semester}
- query parameters - year such as 2021,   semester such as Spring, Fall 
- result returned JSON for instance of java class ScheduleDTO.    

#### POST /schedule 
- body contains JSON data for CourseDTO.  See java class ScheduleDTO.CourseDTO

#### DELETE /schedule/{enrollment_id}  
- enrollment_id from a course enrollment  See ScheduleDTO.CourseDTO.id 

### Database Tables
- Building - id, name
- Room - id, building id, code, capacity
- Course - id, code, name, description, credits, level
- Enrollment - student id, instructor id, course id, year, semester id, room id
- Role - student/admin/instructor
- UserRole - linking table for users (many to many)
- User - for SSO, first name, last name, email, password, username
- Semester - name, dates, year, registration opens

### Rest apis used by other services
- tbd 

