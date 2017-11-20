from course_parser import CourseParser
from constants import FIREBASE_URL, AUTH_PASS, AUTH_EMAIL
from datetime import datetime
from firebase.firebase import FirebaseApplication, FirebaseAuthentication

database = FirebaseApplication(FIREBASE_URL, None)
database.authentication = FirebaseAuthentication(AUTH_PASS, AUTH_EMAIL)

DEPARTMENT_SEARCH_STRING = 'subject'
DEPARTMENT_API = 'departments'

COURSE_SEARCH_STRING = 'course'
COURSE_API = 'courses/{0}'

SECTION_SEARCH_STRING = 'section'
SECTION_API = 'sections/{0}/{1}'

SEMESTER = "fall"


def post_data_to_firebase():
    year = str(datetime.today().year)
    semester = SEMESTER
    course_parser = CourseParser(year, semester)

    departments = course_parser.get_item_list(DEPARTMENT_SEARCH_STRING)
    for department in departments:
        department_id = department['id']
        database.put(DEPARTMENT_API, department['id'], department)

        courses = course_parser.get_item_list(COURSE_SEARCH_STRING, department_id)
        for course in courses:
            course_id = course['id']
            database.put(COURSE_API.format(department_id), course_id, course)

            sections = course_parser.get_item_list(SECTION_SEARCH_STRING, department_id, course_id)
            for section in sections:
                section_id = section['id']
                section_details = course_parser.get_section_details(department_id, course_id, section_id)
                database.put(SECTION_API.format(department_id, course_id), section_id, section_details)


def wipe_firebase_data():
    database.delete('/', None)


def main():
    # wipe_firebase_data()
    post_data_to_firebase()


if __name__ == '__main__':
    main()