from auth_constants import AUTH_KEY
from auth_constants import FIREBASE_CONFIG
from constants import COURSE_API, COURSE_SEARCH_STRING
from constants import DEPARTMENT_API, DEPARTMENT_SEARCH_STRING
from constants import SECTION_API, SECTION_SEARCH_STRING
from constants import SEMESTER
from course_parser import CourseParser
from datetime import datetime
from firebase_admin import initialize_app
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate(AUTH_KEY)
app = initialize_app(cred, FIREBASE_CONFIG)

database = db.reference('', app)


def post_data_to_firebase():
    year = str(datetime.today().year)
    semester = SEMESTER
    course_parser = CourseParser(year, semester)

    departments = course_parser.get_item_list(DEPARTMENT_SEARCH_STRING)
    for department in departments:
        department_id = department['id']
        department_ref = database.child(DEPARTMENT_API.format(department_id))
        department_ref.set(department)

        courses = course_parser.get_item_list(COURSE_SEARCH_STRING, department_id)
        for course in courses:
            course_id = course['id']
            course_ref = database.child(COURSE_API.format(department_id, course_id))
            course_ref.set(course)

            sections = course_parser.get_item_list(SECTION_SEARCH_STRING, department_id, course_id)
            for section in sections:
                section_id = section['id']
                section_ref = database.child(SECTION_API.format(department_id, course_id, section_id))
                section_details = course_parser.get_section_details(department_id, course_id, section_id)
                section_ref.set(section_details)


def wipe_firebase_data():
    database.delete()


def main():
    post_data_to_firebase()


if __name__ == '__main__':
    main()
